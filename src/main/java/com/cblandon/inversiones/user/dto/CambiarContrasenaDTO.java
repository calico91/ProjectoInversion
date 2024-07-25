package com.cblandon.inversiones.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CambiarContrasenaDTO(
        @JsonProperty("id_usuario")
        Integer idUsuario,
        @JsonProperty("contrasena_actual")
        String contrasenaActual,
        @JsonProperty("contrasena_nueva")
        String contrasenaNueva) {
}
