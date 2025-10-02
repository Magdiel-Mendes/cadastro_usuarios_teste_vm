package com.br.vmtech.apivm.dto.mapper;

import com.br.vmtech.apivm.entity.Usuario;
import com.br.vmtech.apivm.dto.UsuarioRequest;
import com.br.vmtech.apivm.dto.UsuarioResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class UsuarioMapper {


    private final ModelMapper mapper;

    public Usuario toUsuario(UsuarioRequest request){
        return mapper.map(request, Usuario.class);
    }

    public UsuarioResponse toResponse(Usuario usuario){
        return mapper.map(usuario, UsuarioResponse.class);
    }

    public List<UsuarioResponse> toResponseList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
