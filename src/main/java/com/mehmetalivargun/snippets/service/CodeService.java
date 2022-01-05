package com.mehmetalivargun.snippets.service;

import com.mehmetalivargun.snippets.model.Code;
import com.mehmetalivargun.snippets.repoistory.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CodeService {
    private static final int LATEST_COUNT = 10;
    private CodeRepository repository;

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

    public Optional<Code> findCodeModelTest(UUID id) {
        return repository.findById(id);
    }

    public Optional<Code> findCodeModelById(UUID id) {
        Optional<Code> code = repository.findById(id);
        if (code.isPresent()) {
            Code codeModel = code.get();
            long diff = ChronoUnit.SECONDS.between(codeModel.getExpiryDate(), LocalDateTime.now());
            if ((diff >= codeModel.getTime() && codeModel.getTime() != 0)) {
                removeItem(id);
                return Optional.empty();
            }
            long views = codeModel.getViews();
            long time = codeModel.getTime();
            if (codeModel.getViews() == 1) {
                removeItem(id);
                codeModel.setViews(0);
                return code;
            }
            long val = time - diff > 0 ? time - diff : 0;
            long newView = views - 1 > 0 ? views - 1 : 0;
            codeModel.setViews(newView);
            codeModel.setTime(val);
            saveCodeModel(codeModel);
            return Optional.of(codeModel);
        }

        return Optional.empty();
    }

    public UUID saveCodeModel(Code code) {
        return repository.save(code).getId();
    }

    public void removeItem(UUID id) {
        repository.deleteById(id);
    }


}
