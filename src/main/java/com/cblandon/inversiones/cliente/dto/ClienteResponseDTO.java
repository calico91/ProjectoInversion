package com.cblandon.inversiones.cliente.dto;

import com.cblandon.inversiones.credito.dto.CreditoResponseDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Builder
public record ClienteResponseDTO(Integer id, String nombres, String apellidos, String celular, String cedula,
                                 String direccion, String observaciones, List<CreditoResponseDTO> listaCreditos) {

}
