package com.cblandon.inversiones.user.dto;

import com.cblandon.inversiones.roles.entity.Roles;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserDTO(
        Integer id,
        String username,
        String password,
        String firstname,
        String lastname,
        String country,
        String email,
        Set<Roles> roles) {

}
