package com.cblandon.inversiones.cliente.dto;

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
    private String nombres;
    private String apellidos;
    private String email;
    private String celular;
    private String pais;
    private String cedula;
    private String observaciones;
    private String direccion;
}
