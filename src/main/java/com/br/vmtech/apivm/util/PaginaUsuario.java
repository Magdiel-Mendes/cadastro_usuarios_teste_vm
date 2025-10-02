package com.br.vmtech.apivm.util;

import com.br.vmtech.apivm.dto.UsuarioResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginaUsuario<T> {
    private List<T> usuarios;
    private int paginaAtual;
    private int totalPaginas;
    private long totalElementos;

    public PaginaUsuario(Page<T> page) {
        this.usuarios = page.getContent();
        this.paginaAtual = page.getNumber();
        this.totalPaginas = page.getTotalPages();
        this.totalElementos = page.getTotalElements();
    }
}
