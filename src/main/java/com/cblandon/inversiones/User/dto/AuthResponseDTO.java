package com.cblandon.inversiones.user.dto;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Builder
public record AuthResponseDTO(Integer id, String username, String token,
                              Collection<? extends GrantedAuthority> authorities) {

}
