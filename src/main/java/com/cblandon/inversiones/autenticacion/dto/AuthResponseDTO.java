package com.cblandon.inversiones.autenticacion.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record AuthResponseDTO(Integer id, String username, String token,
                              Set<String> authorities) {

}
