package com.br.vmtech.apivm.service;

import com.br.vmtech.apivm.dto.UsuarioRequest;
import com.br.vmtech.apivm.dto.UsuarioResponse;
import com.br.vmtech.apivm.dto.mapper.UsuarioMapper;
import com.br.vmtech.apivm.entity.Usuario;
import com.br.vmtech.apivm.exceptions.NomeInvalidoException;
import com.br.vmtech.apivm.exceptions.UsuarioValidoException;
import com.br.vmtech.apivm.repository.UsuarioRepository;
import com.br.vmtech.apivm.util.PaginaUsuario;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper mapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveListarUsuariosComPaginacao() {
        Usuario usuario1 = new Usuario("Maria", "maria@email.com", "senha1");
        Usuario usuario2 = new Usuario("João", "joao@email.com", "senha2");

        UsuarioResponse response1 = new UsuarioResponse(1L, "Maria", "maria@email.com", "senha1");
        UsuarioResponse response2 = new UsuarioResponse(2L, "João", "joao@email.com", "senha2");

        Page<Usuario> paginaUsuarios = new PageImpl<>(List.of(usuario1, usuario2), PageRequest.of(0, 2), 2);
        Pageable pageable = PageRequest.of(0, 2);

        when(usuarioRepository.findAll(pageable)).thenReturn(paginaUsuarios);
        when(mapper.toResponse(usuario1)).thenReturn(response1);
        when(mapper.toResponse(usuario2)).thenReturn(response2);

        PaginaUsuario<UsuarioResponse> resultado = usuarioService.buscarTodos(pageable);

        assertEquals(2, resultado.getUsuarios().size());
        assertEquals("Maria", resultado.getUsuarios().get(0).getNome());
        assertEquals("senha1", resultado.getUsuarios().get(0).getSenha());
        assertEquals("João", resultado.getUsuarios().get(1).getNome());
        assertEquals("senha2", resultado.getUsuarios().get(1).getSenha());
        assertEquals(0, resultado.getPaginaAtual());
        assertEquals(1, resultado.getTotalPaginas());
        assertEquals(2, resultado.getTotalElementos());
    }



    @Test
    void deveBuscarUsuarioPorId() {
        Usuario usuario = new Usuario("Maria", "maria@email.com", "senha");
        UsuarioResponse response = new UsuarioResponse(2L, "Maria", "maria@email.com", "senha");

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));
        when(mapper.toResponse(usuario)).thenReturn(response);

        UsuarioResponse resultado = usuarioService.buscarPorId(2L);

        assertEquals("Maria", resultado.getNome());
        assertEquals("senha", resultado.getSenha());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.buscarPorId(99L);
        });

        assertTrue(ex.getMessage().contains("Usuário id = 99 não encontrado"));
    }

    @Test
    void deveBuscarUsuariosPorNome() {
        Pageable pageable = PageRequest.of(0, 10);
        Usuario usuario = new Usuario("Carlos", "carlos@email.com", "abc");
        UsuarioResponse response = new UsuarioResponse(3L, "Carlos", "carlos@email.com", "abc");

        Page<Usuario> page = new PageImpl<>(List.of(usuario));
        when(usuarioRepository.buscarPorNome("Carlos", pageable)).thenReturn(page);
        when(mapper.toResponse(usuario)).thenReturn(response);

        Page<UsuarioResponse> resultado = usuarioService.buscarPorNome("Carlos", pageable);

        assertEquals(1, resultado.getTotalElements());
        assertEquals("Carlos", resultado.getContent().get(0).getNome());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorNomeVazio() {
        NomeInvalidoException ex = assertThrows(NomeInvalidoException.class, () -> {
            usuarioService.buscarPorNome(" ", PageRequest.of(0, 10));
        });

        assertEquals("O nome para busca não pode estar vazio", ex.getMessage());
    }

    @Test
    void deveSalvarUsuarioComSucesso() {
        UsuarioRequest request = new UsuarioRequest("Ana", "ana@email.com", "senha");
        Usuario usuario = new Usuario("Ana", "ana@email.com", "senha");
        UsuarioResponse response = new UsuarioResponse(4L, "Ana", "ana@email.com", "senha");

        when(usuarioRepository.existsByEmail("ana@email.com")).thenReturn(false);
        when(mapper.toUsuario(request)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(mapper.toResponse(usuario)).thenReturn(response);

        UsuarioResponse resultado = usuarioService.salvar(request);

        assertEquals("Ana", resultado.getNome());
        assertEquals("senha", resultado.getSenha());
        verify(emailService).enviarEmailCadastro(usuario);
    }

    @Test
    void deveLancarExcecaoAoSalvarEmailDuplicado() {
        UsuarioRequest request = new UsuarioRequest("Ana", "ana@email.com", "senha");

        when(usuarioRepository.existsByEmail("ana@email.com")).thenReturn(true);

        UsuarioValidoException ex = assertThrows(UsuarioValidoException.class, () -> {
            usuarioService.salvar(request);
        });

        assertTrue(ex.getMessage().contains("já está cadastrado"));
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        Long id = 5L;
        Usuario usuario = new Usuario("Pedro", "pedro@email.com", "123");
        UsuarioRequest request = new UsuarioRequest("Pedro Silva", "pedro@email.com", "456");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario atualizado = usuarioService.atualizar(id, request);

        assertEquals("Pedro Silva", atualizado.getNome());
        assertEquals("456", atualizado.getSenha());
        verify(emailService).enviarEmailAtualizacao(usuario);
    }

    @Test
    void deveLancarExcecaoAoAtualizarUsuarioInexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.atualizar(99L, new UsuarioRequest("X", "x@email.com", "x"));
        });

        assertEquals("Usuário não encontrado", ex.getMessage());
    }

    @Test
    void deveRemoverUsuarioComSucesso() {
        Usuario usuario = new Usuario("Lucas", "lucas@email.com", "abc");

        when(usuarioRepository.findById(6L)).thenReturn(Optional.of(usuario));

        usuarioService.remove(6L);

        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void deveLancarExcecaoAoRemoverUsuarioInexistente() {
        when(usuarioRepository.findById(100L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.remove(100L);
        });
        assertTrue(ex.getMessage().contains("Usuário id = 100 não encontrado"));
    }
}

