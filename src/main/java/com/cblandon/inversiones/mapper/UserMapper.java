package com.cblandon.inversiones.mapper;

import com.cblandon.inversiones.user.dto.RegisterUserRequestDTO;
import com.cblandon.inversiones.user.dto.UsuariosResponseDTO;
import com.cblandon.inversiones.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper()
public interface UserMapper {

    UserMapper USER = Mappers.getMapper(UserMapper.class);
    @Mapping(target = "roles", ignore = true)
    UsuariosResponseDTO toUsuariosResponseDTO(UserEntity user);

    @Mapping(target = "roles", ignore = true)
    UserEntity toUserEntity(RegisterUserRequestDTO registerUserRequestDTO);


}
