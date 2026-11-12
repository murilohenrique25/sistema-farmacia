package br.com.starkstecnologia.control_api.web.dto.mapper;

import br.com.starkstecnologia.control_api.entity.Usuario;
import br.com.starkstecnologia.control_api.web.dto.UsuarioResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {

    public static UsuarioResponseDto toDto(Usuario usuario){
        String role = usuario.getRole().getDescricao();

        PropertyMap<Usuario, UsuarioResponseDto> props = new PropertyMap<Usuario, UsuarioResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
                map().setIdMatriz(source.getMatriz().getIdMatriz());
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(usuario, UsuarioResponseDto.class);
    }

    public static List<UsuarioResponseDto> toListDto(List<Usuario> usuarioList){
        return usuarioList.stream().map(UsuarioMapper::toDto).collect(Collectors.toList());
    }

}
