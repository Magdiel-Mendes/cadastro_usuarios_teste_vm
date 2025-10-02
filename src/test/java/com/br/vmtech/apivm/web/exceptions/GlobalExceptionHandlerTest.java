package com.br.vmtech.apivm.web.exceptions;

import com.br.vmtech.apivm.exceptions.NomeInvalidoException;
import com.br.vmtech.apivm.exceptions.UsuarioValidoException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Test
    void deveRetornarErroInternoParaExceptionGenerica() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception ex = new RuntimeException("Falha inesperada");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/teste");

        ResponseEntity<ErrorMessage> response = handler.handleGenericException(ex, request);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro interno do servidor", response.getBody().getMensagem());
        Assertions.assertEquals("/teste", response.getBody().getPath());
    }

    @Test
    void deveRetornarBadRequestParaParametroAusente() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("id", "Long");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/usuarios");

        ResponseEntity<ErrorMessage> response = handler.handleMissingParam(ex, request);


        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Parâmetro obrigatório ausente: id", response.getBody().getMensagem());
        Assertions.assertEquals("/usuarios", response.getBody().getPath());
    }

    @Test
    void deveRetornarNotFoundParaEntityNotFoundException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        EntityNotFoundException ex = new EntityNotFoundException("Usuário não encontrado");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/usuarios/99");

        ResponseEntity<ErrorMessage> response = handler.handleEntityNotFound(ex, request);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Usuário não encontrado", response.getBody().getMensagem());
        Assertions.assertEquals("/usuarios/99", response.getBody().getPath());
    }

    @Test
    void deveRetornarBadRequestParaUsuarioValidoException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        UsuarioValidoException ex = new UsuarioValidoException("Email já cadastrado");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/usuarios");

        ResponseEntity<ErrorMessage> response = handler.handleUsuarioValido(ex, request);


        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Email já cadastrado", response.getBody().getMensagem());
        Assertions.assertEquals("/usuarios", response.getBody().getPath());
    }

    @Test
    void deveRetornarBadRequestParaNomeInvalidoException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        NomeInvalidoException ex = new NomeInvalidoException("Nome não pode conter números");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/usuarios");

        ResponseEntity<ErrorMessage> response = handler.handleNomeInvalido(ex, request);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Nome não pode conter números", response.getBody().getMensagem());
        Assertions.assertEquals("/usuarios", response.getBody().getPath());
    }

    @Test
    void deveRetornarUnprocessableEntityParaValidacao() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MethodArgumentNotValidException ex = Mockito.mock(MethodArgumentNotValidException.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/usuarios");

        ResponseEntity<ErrorMessage> response = handler.handleValidationException(ex, request);

        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        Assertions.assertEquals("Campo(s) inválido(s)", response.getBody().getMensagem());
        Assertions.assertEquals("/usuarios", response.getBody().getPath());
    }
}