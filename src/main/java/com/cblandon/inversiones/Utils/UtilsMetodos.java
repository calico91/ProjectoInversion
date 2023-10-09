package com.cblandon.inversiones.Utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UtilsMetodos {

    public String obtenerUsuarioLogueado() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName()).orElse("test");
    }
}
