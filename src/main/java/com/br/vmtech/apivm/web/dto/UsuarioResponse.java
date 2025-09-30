package com.br.vmtech.apivm.web.dto;

import lombok.Data;

@Data
public class UsuarioResponse {

    private String nome;
    private String email;
    private String senha;
}
