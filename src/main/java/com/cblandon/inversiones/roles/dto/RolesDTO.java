package com.cblandon.inversiones.roles.dto;

import com.cblandon.inversiones.permiso.entity.Permiso;
import com.cblandon.inversiones.roles.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RolesDTO {

    private Integer id;
    private Role name;
    private Set<Permiso> permisos;

    public RolesDTO(Integer id, Role name) {
        this.id = id;
        this.name = name;
    }
}
