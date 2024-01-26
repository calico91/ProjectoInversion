package com.cblandon.inversiones.cliente.dto;

import com.cblandon.inversiones.utils.Constantes;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarClienteDTO {

    private Integer id;

    @NotNull(message = Constantes.NOT_NULL)
    @NotBlank(message = Constantes.NOT_BLANK)
    private String nombres;

    @NotNull(message = Constantes.NOT_NULL)
    @NotBlank(message = Constantes.NOT_BLANK)
    private String apellidos;

    @Email(regexp = Constantes.REGEX_EMAIL)
    private String email;

    @Size(min = 10, max = 10, message = Constantes.VALIDAR_CELULAR)
    private String celular;

    private String pais;

    @NotNull(message = Constantes.NOT_NULL)
    @NotBlank(message = Constantes.NOT_BLANK)
    private String cedula;

    private String observaciones;

    private String direccion;
}
