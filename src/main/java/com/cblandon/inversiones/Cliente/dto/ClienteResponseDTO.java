package com.cblandon.inversiones.Cliente.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

    private String nombres;
    private String apellidos;
    private String email;
    private String celular;
    private String pais;
    private String cedula;

}
