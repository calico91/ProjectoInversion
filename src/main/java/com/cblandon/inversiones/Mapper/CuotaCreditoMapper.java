package com.cblandon.inversiones.Mapper;

import com.cblandon.inversiones.Credito.Credito;
import com.cblandon.inversiones.Credito.dto.CreditoResponseDTO;
import com.cblandon.inversiones.cuotacredito.CuotaCredito;
import com.cblandon.inversiones.cuotacredito.dto.CuotasCreditoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface CuotaCreditoMapper {

    CuotaCreditoMapper mapperCuotaCredito =  Mappers.getMapper(CuotaCreditoMapper.class);
    CuotasCreditoResponseDTO cuotaCreditoToCuotasCreditoResponseDTO(CuotaCredito cuotaCredito);

    Credito creditoResponseDTOToCredito(CreditoResponseDTO creditoResponseDTO);
}
