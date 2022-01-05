package com.mehmetalivargun.snippets.controllers;


import com.mehmetalivargun.snippets.model.Code;
import com.mehmetalivargun.snippets.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/code")
public class RestController {


    private final CodeService service;

    @Autowired
    public RestController(CodeService service) {
        this.service = service;
    }

    @GetMapping("/latest")
    public List<Code> latestCodes() {
        return service.getLatestSnippets();
    }

    @PostMapping(value = "/new")
    public ResponseEntity<Map<String, String>> addNewCode(@RequestBody Code code) {
        var saved = service.saveCodeModel(code);
        return ResponseEntity.ok(Map.of("id", String.valueOf(saved)));

    }
    @GetMapping("/{id}")
    public  Code getCodeByID(@PathVariable UUID id){
        return service.findCodeModelById(id);
    }
}
