package com.mehmetalivargun.snippets.controllers;


import com.mehmetalivargun.snippets.model.Code;
import com.mehmetalivargun.snippets.model.NewCodeResponse;
import com.mehmetalivargun.snippets.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class RestController {


    private CodeService service;

    @Autowired
    public RestController(CodeService service) {
        this.service = service;
    }

    @GetMapping("/api/latest")
    public List<Code> latestCodes() {
        return service.getLatestSnippets();
    }

    @PostMapping(value = "/api/code/new")
    public ResponseEntity<Map<String, String>> addNewCode(@RequestBody Code code) {
        var saved = service.saveCodeModel(code);
        System.out.println("New code added!");
        return ResponseEntity.ok(Map.of("id", String.valueOf(saved)));

    }
}
