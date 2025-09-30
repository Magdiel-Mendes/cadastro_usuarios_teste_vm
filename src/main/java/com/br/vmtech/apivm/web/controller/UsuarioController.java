package com.br.vmtech.apivm.web.controller;

import com.br.vmtech.apivm.entity.Usuario;
import com.br.vmtech.apivm.service.UsuarioService;
import com.br.vmtech.apivm.web.dto.UsuarioRequest;
import com.br.vmtech.apivm.web.dto.UsuarioResponse;
import com.br.vmtech.apivm.web.dto.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMapper mapper;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> getAll() {
        List<Usuario> usuarios = usuarioService.buscarTodos();
        return ResponseEntity.ok(mapper.toResponseList(usuarios));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario  = usuarioService.salvar(mapper.toUsuario(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(usuario));
    }
}
