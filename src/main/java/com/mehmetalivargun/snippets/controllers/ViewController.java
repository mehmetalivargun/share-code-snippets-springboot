package com.mehmetalivargun.snippets.controllers;


import com.mehmetalivargun.snippets.model.Code;
import com.mehmetalivargun.snippets.service.CodeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.UUID;

@Controller
public class ViewController {

    private final CodeService service;
    public ViewController(CodeService service) {
        this.service = service;
    }

    @GetMapping("/")
    private String getIndexView() {
        return "index";
    }

    @GetMapping("/code/new")
    private String getSendingFormView(Map<String, Object> model) {
        model.put("newSnippet", new Code());
        System.out.println("Code New Here");
        return "createSnippet";
    }

    @GetMapping("/code/{uuid}")
    private String getByIdView(@PathVariable UUID uuid, Map<String, Object> model) {
        var optionalCodeSnippet = service.findCodeModelById(uuid);
        model.put("snippet", optionalCodeSnippet);
        return "singleSnippet";
    }


    @GetMapping("/code/latest")
    public String getLatestPage(Map<String, Object> model) {
        var list = service.getLatestSnippets();
        list.forEach(Code::increaseViewCount);
        model.put("latestTenSnippets", list);
        return "latestSnippets";

    }
}
