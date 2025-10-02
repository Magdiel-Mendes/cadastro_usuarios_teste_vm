package com.br.vmtech.apivm.dto.mapper;

import com.br.vmtech.apivm.dto.UsuarioRequest;
import com.br.vmtech.apivm.dto.UsuarioResponse;
import com.br.vmtech.apivm.entity.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class UsuarioMapperTest {

    @Test
    void deveMapearUsuarioRequestParaUsuario() {

        UsuarioRequest request = new UsuarioRequest("joao", "joao@email.com", "senha123");
        ModelMapper modelMapper = new ModelMapper();
        UsuarioMapper mapper = new UsuarioMapper(modelMapper);

        Usuario usuario = mapper.toUsuario(request);

        Assertions.assertNotNull(usuario);
        Assertions.assertEquals("joao", usuario.getNome());
        Assertions.assertEquals("joao@email.com", usuario.getEmail());
        Assertions.assertEquals("senha123", usuario.getSenha());
    }

    @Test
    void deveMapearUsuarioParaUsuarioResponse() {

        Usuario usuario = new Usuario("ana", "ana@email.com", "senha456");
        usuario.setId(1L);
        ModelMapper modelMapper = new ModelMapper();
        UsuarioMapper mapper = new UsuarioMapper(modelMapper);

        UsuarioResponse response = mapper.toResponse(usuario);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("ana", response.getNome());
        Assertions.assertEquals("ana@email.com", response.getEmail());
        Assertions.assertEquals("senha456", response.getSenha());
    }

    @Test
    void deveMapearListaDeUsuariosParaListaDeUsuarioResponse() {
        // Arrange
        Usuario u1 = new Usuario("maria", "maria@email.com", "123");
        u1.setId(1L);
        Usuario u2 = new Usuario("carlos", "carlos@email.com", "456");
        u2.setId(2L);

        List<Usuario> usuarios = List.of(u1, u2);
        ModelMapper modelMapper = new ModelMapper();
        UsuarioMapper mapper = new UsuarioMapper(modelMapper);

        List<UsuarioResponse> responses = mapper.toResponseList(usuarios);

        Assertions.assertNotNull(responses);
        Assertions.assertEquals(2, responses.size());

        Assertions.assertEquals("maria", responses.get(0).getNome());
        Assertions.assertEquals("carlos", responses.get(1).getNome());
    }

}