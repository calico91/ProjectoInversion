package com.cblandon.inversiones.Mapper;

import com.cblandon.inversiones.Credito.Credito;
import com.cblandon.inversiones.Credito.dto.CreditoResponseDTO;
import com.cblandon.inversiones.CuotaCredito.CuotaCredito;
import com.cblandon.inversiones.CuotaCredito.dto.CuotasCreditoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface CuotaCreditoMapper {

    CuotaCreditoMapper mapperCuotaCredito =  Mappers.getMapper(CuotaCreditoMapper.class);
    CuotasCreditoResponseDTO cuotaCreditoToCuotasCreditoResponseDTO(CuotaCredito cuotaCredito);

    Credito creditoResponseDTOToCredito(CreditoResponseDTO creditoResponseDTO);
}
