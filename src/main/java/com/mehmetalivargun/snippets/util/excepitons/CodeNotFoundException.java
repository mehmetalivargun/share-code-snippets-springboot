package com.mehmetalivargun.snippets.util.excepitons;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "The snippet has expired.")
public class CodeNotFoundException extends ResponseStatusException {

    public CodeNotFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
