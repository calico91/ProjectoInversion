package com.cblandon.inversiones.Mapper;

import com.cblandon.inversiones.Roles.Roles;
import com.cblandon.inversiones.Roles.dto.RolesDTO;
import org.mapstruct.Mapper;

@Mapper
public interface RolesMapper {

    Roles rolesDTOToRoles(RolesDTO dto);
    RolesDTO rolesDTOToRoles(Roles roles);
}
