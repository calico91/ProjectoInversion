package com.cblandon.inversiones.Credito.dto;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.CuotaCredito.dto.CuotasCreditoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditoCuotasResponseDTO {

    private Integer id;

    private String usuarioCreador;

    private String estadoCredito;

    private Date fechaCredito;

    private List<CuotasCreditoResponseDTO> listaCuotasCredito;


}
