package com.cblandon.inversiones.Mapper;

import com.cblandon.inversiones.Credito.Credito;
import com.cblandon.inversiones.Credito.dto.CreditoResponseDTO;
import com.cblandon.inversiones.CuotaCredito.CuotaCredito;
import com.cblandon.inversiones.CuotaCredito.dto.CuotasCreditoResponseDTO;
import org.mapstruct.Mapper;

@Mapper()
public interface CuotaCreditoMapper {

    CuotasCreditoResponseDTO creditoToCreditoResponseDTO(CuotaCredito cuotaCredito);

    Credito creditoResponseDTOToCredito(CreditoResponseDTO creditoResponseDTO);
}
