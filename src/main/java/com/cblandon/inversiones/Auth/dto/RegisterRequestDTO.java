package com.cblandon.inversiones.Auth.dto;

import com.cblandon.inversiones.Roles.Role;
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
public class RegisterRequestDTO {
    String username;
    String password;
    String firstname;
    String lastname;
    String country;
    Set<String> roles = new HashSet<>();
}
