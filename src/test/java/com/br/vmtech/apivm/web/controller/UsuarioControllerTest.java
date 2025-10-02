package com.br.vmtech.apivm.web.controller;

import com.br.vmtech.apivm.dto.UsuarioRequest;
import com.br.vmtech.apivm.dto.UsuarioResponse;
import com.br.vmtech.apivm.dto.mapper.UsuarioMapper;
import com.br.vmtech.apivm.entity.Usuario;
import com.br.vmtech.apivm.service.UsuarioService;
import com.br.vmtech.apivm.util.PaginaUsuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

        @Mock
        private UsuarioService usuarioService;

        @Mock
        private UsuarioMapper usuarioMapper;

        @InjectMocks
        private UsuarioController usuarioController;

    @Test
    void deveRetornarUsuariosPaginadosComStatus200() {
        UsuarioResponse usuario1 = new UsuarioResponse(1L, "Bob", "bob@email.com", "123456");
        UsuarioResponse usuario2 = new UsuarioResponse(2L, "José", "jose@email.com", "654321");

        List<UsuarioResponse> lista = List.of(usuario1, usuario2);
        PaginaUsuario<UsuarioResponse> pagina = new PaginaUsuario<>(lista, 0, 1, 2);

        when(usuarioService.buscarTodos(PageRequest.of(0, 10))).thenReturn(pagina);

        ResponseEntity<PaginaUsuario<UsuarioResponse>> response = usuarioController.getAll(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getUsuarios().size());
        assertEquals("Bob", response.getBody().getUsuarios().get(0).getNome());
        assertEquals(0, response.getBody().getPaginaAtual());
        assertEquals(1, response.getBody().getTotalPaginas());
        assertEquals(2, response.getBody().getTotalElementos());
    }



    @Test
        void deveBuscarUsuarioPorIdComStatus200() {
            UsuarioResponse usuario = new UsuarioResponse(1L, "bob", "bob@email.com", "123456");

            when(usuarioService.buscarPorId(1L)).thenReturn(usuario);

            ResponseEntity<UsuarioResponse> response = usuarioController.getById(1L);

            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertEquals("bob", response.getBody().getNome());
        }

    @Test
    void deveBuscarUsuarioPorNomeComStatus200() {
        // Arrange
        UsuarioResponse maria = new UsuarioResponse(3L, "maria", "maria@email.com", "123456");
        Page<UsuarioResponse> page = new PageImpl<>(List.of(maria));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome"));

        when(usuarioService.buscarPorNome("maria", pageable)).thenReturn(page);

        ResponseEntity<PaginaUsuario> response = usuarioController.getByNome("maria", 0, 10);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody().getUsuarios().isEmpty());

        UsuarioResponse resultado = (UsuarioResponse) response.getBody().getUsuarios().get(0);
        Assertions.assertEquals("maria", resultado.getNome());
        Assertions.assertEquals("maria@email.com", resultado.getEmail());
        Assertions.assertEquals("123456", resultado.getSenha());
    }

    @Test
        void deveCriarUsuarioComStatus201() {
            UsuarioRequest request = new UsuarioRequest("ana", "ana@email.com", "senha123");
            UsuarioResponse responseDto = new UsuarioResponse(4L, "ana", "ana@email.com", "senha123");

            when(usuarioService.salvar(request)).thenReturn(responseDto);

            ResponseEntity<UsuarioResponse> response = usuarioController.create(request);

            Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertEquals("ana", response.getBody().getNome());
        }

    @Test
    void deveAtualizarUsuarioComStatus200() {
        // Arrange
        Long id = 2L;
        UsuarioRequest request = new UsuarioRequest("jose atualizado", "jose@email.com", "novaSenha");
        Usuario usuarioAtualizado = new Usuario("jose atualizado", "jose@email.com", "novaSenha");


        when(usuarioService.atualizar(id, request)).thenReturn(usuarioAtualizado);


        ResponseEntity<String> response = usuarioController.update(id, request);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Usuário atualizado com sucesso", response.getBody());
    }


    @Test
        void deveRemoverUsuarioComStatus204() {
            Mockito.doNothing().when(usuarioService).remove(3L);

            ResponseEntity<Void> response = usuarioController.delete(3L);

            Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            Assertions.assertNull(response.getBody());
        }
    }
