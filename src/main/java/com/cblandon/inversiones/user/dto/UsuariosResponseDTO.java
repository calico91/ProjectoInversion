package com.cblandon.inversiones.user.dto;

import com.cblandon.inversiones.roles.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosResponseDTO {

    String username;
    String firstname;
    String lastname;
    String email;
    String country;
    Set<Roles> roles;
}
