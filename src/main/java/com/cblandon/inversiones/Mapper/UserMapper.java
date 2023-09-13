package com.cblandon.inversiones.Mapper;

import com.cblandon.inversiones.User.UserEntity;
import com.cblandon.inversiones.User.dto.UsuariosResponseDTO;
import org.mapstruct.Mapper;

@Mapper(uses = {RolesMapper.class})
public interface UserMapper {


   // UsuariosResponseDTO userEntityToUsuariosDTO(UserEntity userEntity);
}
