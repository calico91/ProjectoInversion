package com.cblandon.inversiones.credito.dto;

import com.cblandon.inversiones.cuotacredito.dto.CuotasCreditoResponseDTO;




import java.util.Date;
import java.util.List;



public record CreditoCuotasResponseDTO(Integer id, String usuarioCreador, String estadoCredito, Date fechaCredito,
                                       List<CuotasCreditoResponseDTO> listaCuotasCredito) {


}
