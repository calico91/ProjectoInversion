package com.cblandon.inversiones.roles.dto;

import com.cblandon.inversiones.permiso.entity.Permiso;
import com.cblandon.inversiones.roles.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
public class RolesDTO {

    private Integer id;
    private Role name;
    private Set<Permiso> permisos;

}
