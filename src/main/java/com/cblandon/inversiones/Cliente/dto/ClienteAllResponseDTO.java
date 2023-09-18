package com.cblandon.inversiones.Cliente.dto;

import com.cblandon.inversiones.Credito.dto.CreditoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteAllResponseDTO {

    private String nombres;
    private String apellidos;
    private String email;
    private String celular;
    private String pais;
    private String cedula;

}
