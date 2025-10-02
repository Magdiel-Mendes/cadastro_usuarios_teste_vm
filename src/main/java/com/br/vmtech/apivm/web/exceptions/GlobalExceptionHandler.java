package com.br.vmtech.apivm.web.exceptions;

import com.br.vmtech.apivm.exceptions.NomeInvalidoException;
import com.br.vmtech.apivm.exceptions.UsuarioValidoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

        private ResponseEntity<ErrorMessage> buildResponse(HttpStatus status, String mensagem, HttpServletRequest request) {
            ErrorMessage error = new ErrorMessage(status, mensagem, request.getRequestURI());
            return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(error);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorMessage> handleGenericException(Exception ex, HttpServletRequest request) {
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", request);
        }

        @ExceptionHandler(MissingServletRequestParameterException.class)
        public ResponseEntity<ErrorMessage> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
            String mensagem = "Parâmetro obrigatório ausente: " + ex.getParameterName();
            return buildResponse(HttpStatus.BAD_REQUEST, mensagem, request);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorMessage> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
            return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        }

        @ExceptionHandler(UsuarioValidoException.class)
        public ResponseEntity<ErrorMessage> handleUsuarioValido(UsuarioValidoException ex, HttpServletRequest request) {
            return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        }

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<ErrorMessage> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
            String mensagem = "Recurso não encontrado: " + request.getRequestURI();
            return buildResponse(HttpStatus.NOT_FOUND, mensagem, request);
        }

        @ExceptionHandler(NomeInvalidoException.class)
        public ResponseEntity<ErrorMessage> handleNomeInvalido(NomeInvalidoException ex, HttpServletRequest request) {
            return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
            return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) inválido(s)", request);
        }

}
