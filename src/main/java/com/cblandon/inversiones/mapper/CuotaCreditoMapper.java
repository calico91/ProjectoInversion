package com.cblandon.inversiones.mapper;

import com.cblandon.inversiones.cuotacredito.CuotaCredito;
import com.cblandon.inversiones.cuotacredito.dto.AbonosRealizadosResponseDTO;
import com.cblandon.inversiones.cuotacredito.dto.CuotasCreditoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface CuotaCreditoMapper {

    CuotaCreditoMapper mapperCuotaCredito = Mappers.getMapper(CuotaCreditoMapper.class);

    CuotasCreditoResponseDTO cuotaCreditoToCuotasCreditoResponseDTO(CuotaCredito cuotaCredito);

}
