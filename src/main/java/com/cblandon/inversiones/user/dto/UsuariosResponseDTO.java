package com.cblandon.inversiones.user.dto;

import com.cblandon.inversiones.roles.entity.Roles;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuariosResponseDTO {

    Integer id;
    String username;
    String firstname;
    String lastname;
    String email;
    String country;
    Set<Roles> roles;
    Boolean active;

}
