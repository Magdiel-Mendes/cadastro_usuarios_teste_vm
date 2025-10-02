package com.br.vmtech.apivm.web.controller;

import com.br.vmtech.apivm.service.UsuarioService;
import com.br.vmtech.apivm.util.PaginaUsuario;
import com.br.vmtech.apivm.dto.UsuarioRequest;
import com.br.vmtech.apivm.dto.UsuarioResponse;
import com.br.vmtech.apivm.dto.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final UsuarioMapper mapper;

    @GetMapping
    public ResponseEntity<PaginaUsuario<UsuarioResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PaginaUsuario<UsuarioResponse> resultado = usuarioService.buscarTodos(pageable);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/BuscarPorNome")
    public ResponseEntity<PaginaUsuario> getByNome(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome"));
        Page<UsuarioResponse> resultado = usuarioService.buscarPorNome(nome, pageable);
        return ResponseEntity.ok(new PaginaUsuario(resultado));
    }

    @GetMapping("{id}")
    public ResponseEntity<UsuarioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                         @Valid @RequestBody UsuarioRequest request) {
        usuarioService.atualizar(id, request);
        return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
