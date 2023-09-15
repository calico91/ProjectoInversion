package com.cblandon.inversiones.Cliente.dto;

import com.cblandon.inversiones.Credito.Credito;
import com.cblandon.inversiones.Credito.dto.CreditoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;


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
    private List<CreditoResponseDTO> listaCreditos;

}
