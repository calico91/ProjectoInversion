package com.cblandon.inversiones.user.dto;

import com.cblandon.inversiones.roles.entity.Roles;

import java.util.Set;

public record UsuarioModificadoDTO(
        Integer id,
        String username,
        String firstname,
        String lastname,
        String country,
        String email,
        Set<Roles> roles,
        Boolean active
) {
}
