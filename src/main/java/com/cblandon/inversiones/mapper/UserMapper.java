package com.cblandon.inversiones.mapper;

import com.cblandon.inversiones.user.dto.RegisterUserRequestDTO;
import com.cblandon.inversiones.user.dto.UsuariosResponseDTO;
import com.cblandon.inversiones.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {RolesMapper.class})
public interface UserMapper {

    UserMapper USER = Mappers.getMapper(UserMapper.class);

    UsuariosResponseDTO toUsuariosResponseDTO(UserEntity user);
    UserEntity toUserEntity(RegisterUserRequestDTO registerUserRequestDTO);
}
