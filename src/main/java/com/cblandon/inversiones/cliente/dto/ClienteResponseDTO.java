package com.cblandon.inversiones.cliente.dto;

import com.cblandon.inversiones.credito.dto.CreditoResponseDTO;


import java.util.List;


public record ClienteResponseDTO(Integer id, String nombres, String apellidos, String celular, String cedula,
                                 String direccion, String observaciones, List<CreditoResponseDTO> listaCreditos) {

}
