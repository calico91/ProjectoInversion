package com.cblandon.inversiones.Mapper;

import com.cblandon.inversiones.roles.Roles;
import com.cblandon.inversiones.roles.dto.RolesDTO;
import org.mapstruct.Mapper;

@Mapper
public interface RolesMapper {

    Roles rolesDTOToRoles(RolesDTO dto);
    RolesDTO rolesDTOToRoles(Roles roles);
}
