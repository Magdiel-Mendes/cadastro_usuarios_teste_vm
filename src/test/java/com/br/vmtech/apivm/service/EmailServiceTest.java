package com.br.vmtech.apivm.service;

import com.br.vmtech.apivm.dto.UsuarioRequest;
import com.br.vmtech.apivm.dto.mapper.UsuarioMapper;
import com.br.vmtech.apivm.entity.Usuario;
import com.br.vmtech.apivm.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private UsuarioMapper mapper;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveEnviarEmailCadastroAoSalvarUsuario() {
        UsuarioRequest request = new UsuarioRequest("João", "joao@email.com", "123456");
        Usuario usuario = new Usuario("João", "joao@email.com", "123456");

        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(mapper.toUsuario(request)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        usuarioService.salvar(request);

        verify(emailService).enviarEmailCadastro(usuario);
    }

    @Test
    void deveEnviarEmailAtualizacaoAoAtualizarUsuario() {
        Usuario usuario = new Usuario("Maria", "maria@email.com", "senha123");
        Long id = 1L;

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any())).thenReturn(usuario);

        UsuarioRequest request = new UsuarioRequest("Maria", "maria@email.com", "senha123");
        usuarioService.atualizar(id, request);

        verify(emailService).enviarEmailAtualizacao(usuario);
    }
}