package com.mehmetalivargun.snippets.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class NewCodeResponse {
    private UUID id;
    public NewCodeResponse(UUID id) {
        this.id = id;
    }
}
