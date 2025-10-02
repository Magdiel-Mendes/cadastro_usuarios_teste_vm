package com.br.vmtech.apivm.web.controller;

import com.br.vmtech.apivm.dto.UsuarioRequest;
import com.br.vmtech.apivm.dto.UsuarioResponse;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuario/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuario/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioControllerIT {

    @Autowired
    WebTestClient testClient;

    @Test
    void deveListarTodosUsuarios() {
        testClient.get()
                .uri("/api/v1/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UsuarioResponse.class)
                .hasSize(3);
    }

    @Test
    void deveBuscarUsuarioPorId() {
        testClient.get()
                .uri("/api/v1/usuarios/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nome").isEqualTo("bob")
                .jsonPath("$.email").isEqualTo("bob@email.com");
    }

    @Test
    void deveBuscarUsuarioPorNome() {
        testClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/usuarios/BuscarPorNome")
                        .queryParam("nome", "maria")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content[0].nome").isEqualTo("ria")
                .jsonPath("$.content[0].email").isEqualTo("maria@email.com");
    }

    @Test
    void deveCriarNovoUsuario() {
        UsuarioRequest novo = new UsuarioRequest("Ana", "ana@email.com", "senha123");

        testClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(novo)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.nome").isEqualTo("Ana")
                .jsonPath("$.email").isEqualTo("ana@email.com");
    }

    @Test
    void deveAtualizarUsuario() {
        UsuarioRequest atualizacao = new UsuarioRequest("José Silva", "jose@email.com", "novaSenha");

        testClient.put()
                .uri("/api/v1/usuarios/101")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(atualizacao)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Usuário atualizado com sucesso");
    }

    @Test
    void deveRemoverUsuario() {
        testClient.delete()
                .uri("/api/v1/usuarios/102")
                .exchange()
                .expectStatus().isNoContent();
    }
}
