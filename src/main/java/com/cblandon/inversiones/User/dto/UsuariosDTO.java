package com.cblandon.inversiones.User.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosDTO {

    String username;
    String firstname;
    String lastname;
    String country;
    Set<String> roles = new HashSet<>();
}
