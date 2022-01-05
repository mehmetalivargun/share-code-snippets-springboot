package com.mehmetalivargun.snippets.service;

import com.mehmetalivargun.snippets.model.Code;
import com.mehmetalivargun.snippets.repoistory.CodeRepository;
import com.mehmetalivargun.snippets.util.excepitons.CodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CodeService {
    private final CodeRepository repository;

    @Autowired
    public CodeService(CodeRepository repository ) {
        this.repository = repository;
    }

    public Code saveSnippet(Code code) {
        return repository.save(code);
    }

    public List<Code> getLatestSnippets() {
        return repository.findFirst10ByTimeAndViewsOrderByDateDesc(0, 0);
    }

    public Code findCodeModelById(UUID id) {
        var optional= repository.findById(id);
        if(optional.isPresent()){
            var code =optional.get();
            if(code.isAccessible()){
                code.increaseViewCount();
                code = repository.save(code);
                return code;
            }
        }
        throw  new CodeNotFoundException(HttpStatus.NOT_FOUND,String.format("This UUID (%s) is not exist.", id));
    }

    public UUID saveCodeModel(Code code) {
        return repository.save(code).getId();
    }

    public void removeItem(UUID id) {
        repository.deleteById(id);
    }


}
