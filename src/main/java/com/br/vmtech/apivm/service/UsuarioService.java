package com.br.vmtech.apivm.service;

import com.br.vmtech.apivm.entity.Usuario;
import com.br.vmtech.apivm.exceptions.UsernameUniqueViolationException;
import com.br.vmtech.apivm.repository.UsuarioRepository;
import com.br.vmtech.apivm.web.dto.UsuarioRequest;
import com.br.vmtech.apivm.web.dto.mapper.UsuarioMapper;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    public UsuarioMapper mapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário id=%s não encontrado", id))
        );
    }

    @Transactional(readOnly = true)
    public List<Usuario> filtrarPorNome(String nome) {
        return usuarioRepository.buscarPorNome(nome);
    }

    @Transactional
    public Usuario salvar(UsuarioRequest request) {
        Usuario usuario = mapper.toUsuario(request);
        try {
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(
                    String.format("Usuário '%s' já cadastrado", usuario.getNome())
            );
        }
    }

    @Transactional
    public Usuario atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (request.getNome() != null) { usuario.setNome(request.getNome()); }
        if (request.getEmail() != null) { usuario.setEmail(request.getEmail()); }
        if (request.getSenha() != null) { usuario.setSenha(request.getSenha()); }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void remove(Long id) {

            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format("Usuário id=%s não encontrado", id)
                    ));

            usuarioRepository.delete(usuario);
    }
}
