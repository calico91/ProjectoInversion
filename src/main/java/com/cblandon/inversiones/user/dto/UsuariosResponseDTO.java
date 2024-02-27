package com.cblandon.inversiones.user.dto;

import org.springframework.core.io.Resource;
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
public class UsuariosResponseDTO {

    String username;
    String firstname;
    String lastname;
    String email;
    String country;
    String imagen;
    Set<String> roles = new HashSet<>();
}
