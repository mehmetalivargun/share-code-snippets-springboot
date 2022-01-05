package com.mehmetalivargun.snippets.controllers;


import com.mehmetalivargun.snippets.model.Code;
import com.mehmetalivargun.snippets.service.CodeService;
import com.mehmetalivargun.snippets.util.excepitons.CodeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.UUID;

@Controller
public class ViewController {

    private CodeService service;

    public ViewController(CodeService service) {
        this.service = service;
    }

    @GetMapping("/")
    private String getIndexView() {return "index";}

    @GetMapping("/code/new")
    private String getSendingFormView(Map<String, Object> model, @CookieValue(defaultValue = "") String uuid) {
        model.put("newSnippet", new Code());
        System.out.println("Code New Here");
        return "createSnippet";
    }
    @GetMapping("/code/{uuid}")
    private String getByIdView(@PathVariable UUID uuid, Map<String, Object> model) {
        var optionalCodeSnippet = service.findCodeModelById(uuid);

        if (optionalCodeSnippet.isPresent()) {
            var codeSnippet = optionalCodeSnippet.get();

            if (codeSnippet.isAccessible()) {
                codeSnippet.increaseViewCount();
                codeSnippet = service.saveSnippet(codeSnippet);

                model.put("snippet", codeSnippet);
                return "singleSnippet";
            }

            throw new CodeNotFoundException(
                    HttpStatus.FORBIDDEN, String.format("The code snippet (%s) has expired.", uuid));
        }

        throw new CodeNotFoundException(
                HttpStatus.NOT_FOUND, String.format("This UUID (%s) is not exist.", uuid));
    }

    @GetMapping("/code/latest")
    public String getLatestPage(Map<String, Object> model){
        var list = service.getLatestSnippets();
        list.forEach(Code::increaseViewCount);
        model.put("latestTenSnippets",list);
        return  "latestSnippets";

    }
}
