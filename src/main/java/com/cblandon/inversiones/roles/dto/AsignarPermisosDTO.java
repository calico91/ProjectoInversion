package com.cblandon.inversiones.roles.dto;

import com.cblandon.inversiones.permiso.entity.Permiso;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record AsignarPermisosDTO(
        @JsonProperty("id_rol")
        Integer idRol,
        Set<Permiso> permisos) {
}
