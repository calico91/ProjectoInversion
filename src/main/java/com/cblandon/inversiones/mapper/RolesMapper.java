package com.cblandon.inversiones.mapper;

import com.cblandon.inversiones.roles.dto.RolesDTO;
import com.cblandon.inversiones.roles.entity.Roles;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = PermisosMapper.class)
public interface RolesMapper {

    RolesMapper ROLES = Mappers.getMapper(RolesMapper.class);

    RolesDTO toRolesDTO(Roles roles);




}
