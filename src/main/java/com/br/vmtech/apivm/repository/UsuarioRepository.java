package com.br.vmtech.apivm.repository;

import com.br.vmtech.apivm.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(
            value = "SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))",
            countQuery = "SELECT COUNT(u) FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))"
    )
    Page<Usuario> buscarPorNome(@Param("nome") String nome, Pageable pageable);
    boolean existsByEmail(String email);
}
