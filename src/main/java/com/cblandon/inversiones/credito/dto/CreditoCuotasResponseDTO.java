package com.cblandon.inversiones.credito.dto;

import com.cblandon.inversiones.cuotacredito.dto.CuotasCreditoResponseDTO;

import lombok.Builder;


import java.util.Date;
import java.util.List;


@Builder
public record CreditoCuotasResponseDTO(Integer id, String usuarioCreador, String estadoCredito, Date fechaCredito,
                                       List<CuotasCreditoResponseDTO> listaCuotasCredito) {


}
