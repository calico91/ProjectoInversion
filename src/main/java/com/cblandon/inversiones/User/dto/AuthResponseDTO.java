package com.cblandon.inversiones.user.dto;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Builder
public record AuthResponseDTO(String username, String token, Collection<? extends GrantedAuthority> authorities) {

}
