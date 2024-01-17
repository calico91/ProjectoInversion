package com.cblandon.inversiones.Mapper;

import org.mapstruct.Mapper;

@Mapper(uses = {RolesMapper.class})
public interface UserMapper {


   // UsuariosResponseDTO userEntityToUsuariosDTO(UserEntity userEntity);
}
