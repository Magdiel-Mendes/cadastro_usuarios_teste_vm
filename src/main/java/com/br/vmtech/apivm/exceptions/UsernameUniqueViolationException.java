package com.br.vmtech.apivm.exceptions;

public class UsernameUniqueViolationException extends RuntimeException{

    public UsernameUniqueViolationException(String message) {
        super(message);
    }
}
