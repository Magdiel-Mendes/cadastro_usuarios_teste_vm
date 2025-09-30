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

    @GetMapping("{id}")
    public ResponseEntity<UsuarioResponse> getById(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(mapper.toResponse(usuario));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponse>> buscarPorNome(@RequestParam String nome) {
        List<Usuario> usuarios = usuarioService.filtrarPorNome(nome);
        List<UsuarioResponse> resposta = usuarios.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@RequestBody UsuarioRequest request) {
        Usuario salvo = usuarioService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Long id, @RequestBody UsuarioRequest request) {
        usuarioService.atualizar(id, request);
        return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
