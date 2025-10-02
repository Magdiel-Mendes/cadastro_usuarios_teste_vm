package com.br.vmtech.apivm.service;

import com.br.vmtech.apivm.entity.Usuario;
import org.springframework.stereotype.Service;

@Service
public class EmailService{

        public void enviarEmailCadastro(Usuario usuario) {
            System.out.printf("Cadastro realizado para %s (%s)%n", usuario.getNome(), usuario.getEmail());
        }

        public void enviarEmailAtualizacao(Usuario usuario) {
            System.out.printf("Dados atualizados para %s (%s)%n", usuario.getNome(), usuario.getEmail());
        }
}
