package com.cblandon.inversiones.cliente.dto;

import com.cblandon.inversiones.excepciones.validation.anotation.ValidDocument;
import com.cblandon.inversiones.excepciones.validation.anotation.ValidNullBlank;
import com.cblandon.inversiones.utils.Constantes;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrarClienteDTO {

    Integer id;

    @ValidNullBlank
    String nombres;

    @ValidNullBlank
    String apellidos;

    @Email(regexp = Constantes.REGEX_EMAIL)
    String email;

    @Size(min = 10, max = 10, message = Constantes.VALIDAR_CELULAR)
    String celular;

    String pais;

    @ValidDocument
    @ValidNullBlank
    String cedula;

    String observaciones;

    String direccion;
}
