package com.br.vmtech.apivm.web.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@Data
public class ErrorMessage {

    private int status;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public ErrorMessage(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }

    public ErrorMessage(HttpStatus status, String message, BindingResult result) {
        this.status = status.value();
        this.message = message;
        addErrors(result);
    }

    private void addErrors(BindingResult result) {
        errors = new HashMap<>();
        result.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
    }
}
