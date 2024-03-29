package com.cblandon.inversiones.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

public class UtilsMetodos {

    public String obtenerUsuarioLogueado() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName()).orElse("test");
    }


}
