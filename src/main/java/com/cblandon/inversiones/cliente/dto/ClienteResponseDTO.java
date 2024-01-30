package com.cblandon.inversiones.cliente.dto;

import com.cblandon.inversiones.credito.dto.CreditoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

    private Integer id;
    private String nombres;
    private String apellidos;
    private String celular;
    private String cedula;
    private String direccion;
    private String observaciones;
    private List<CreditoResponseDTO> listaCreditos;

}
