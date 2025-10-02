package com.br.vmtech.apivm.web.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {

    private int status;
    private String mensagem;
    private String path;
    private LocalDateTime timestamp;
    private Map<String, String> errors;

    public ErrorMessage(HttpStatus status, String mensagem, String path) {
        this.status = status.value();
        this.mensagem = mensagem;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorMessage(HttpStatus status, String mensagem, String path, BindingResult result) {
        this(status, mensagem, path);
        this.errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> Objects.requireNonNullElse(error.getDefaultMessage(), "Mensagem não disponível")
                ));
    }
}

