package com.br.vmtech.apivm.service;

import com.br.vmtech.apivm.entity.Usuario;
import com.br.vmtech.apivm.exceptions.NomeInvalidoException;
import com.br.vmtech.apivm.exceptions.UsuarioValidoException;
import com.br.vmtech.apivm.repository.UsuarioRepository;
import com.br.vmtech.apivm.dto.UsuarioRequest;
import com.br.vmtech.apivm.dto.UsuarioResponse;
import com.br.vmtech.apivm.dto.mapper.UsuarioMapper;
import com.br.vmtech.apivm.util.PaginaUsuario;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioMapper mapper;
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public PaginaUsuario<UsuarioResponse> buscarTodos(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        Page<UsuarioResponse> paginaResponse = usuarios.map(mapper::toResponse);
        return new PaginaUsuario<>(paginaResponse);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {

      Usuario usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário id = %s não encontrado", id))
        );
       return mapper.toResponse(usuario);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> buscarPorNome(String nome, Pageable pageable) {
        if (nome == null || nome.isBlank()) {
            throw new NomeInvalidoException("O nome para busca não pode estar vazio");
        }

        return usuarioRepository.buscarPorNome(nome, pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public UsuarioResponse salvar(UsuarioRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new UsuarioValidoException(
                    String.format("E-mail '%s' já está cadastrado", request.getEmail())
            );
        }
            Usuario usuario = mapper.toUsuario(request);
            Usuario novoUsuario = usuarioRepository.save(usuario);
            emailService.enviarEmailCadastro(usuario);
            return mapper.toResponse(novoUsuario);
    }

    @Transactional
    public Usuario atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (request.getNome() != null) { usuario.setNome(request.getNome()); }
        if (request.getEmail() != null) { usuario.setEmail(request.getEmail()); }
        if (request.getSenha() != null) { usuario.setSenha(request.getSenha()); }

        emailService.enviarEmailAtualizacao(usuario);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void remove(Long id) {

            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format("Usuário id = %s não encontrado", id)
                    ));
            usuarioRepository.delete(usuario);
    }
}
