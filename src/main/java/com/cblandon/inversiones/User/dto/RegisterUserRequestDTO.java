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
public class RegisterUserRequestDTO {
    String username;
    String password;
    String firstname;
    String lastname;
    String country;
    String email;
    Set<String> roles = new HashSet<>();
}
