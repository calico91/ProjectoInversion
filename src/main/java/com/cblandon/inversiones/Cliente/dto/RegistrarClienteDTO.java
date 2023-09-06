package com.cblandon.inversiones.Cliente.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
}
