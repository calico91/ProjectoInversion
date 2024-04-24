package com.cblandon.inversiones.user.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record RegisterUserRequestDTO(String username,
                                     String password,
                                     String firstname,
                                     String lastname,
                                     String country,
                                     String email,
                                     Set<String> roles) {

}
