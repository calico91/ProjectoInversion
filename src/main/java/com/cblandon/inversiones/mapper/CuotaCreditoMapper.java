package com.cblandon.inversiones.mapper;

import com.cblandon.inversiones.cuota_credito.entity.CuotaCredito;
import com.cblandon.inversiones.cuota_credito.dto.CuotasCreditoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CuotaCreditoMapper {

    CuotaCreditoMapper mapperCuotaCredito = Mappers.getMapper(CuotaCreditoMapper.class);

    CuotasCreditoResponseDTO cuotaCreditoToCuotasCreditoResponseDTO(CuotaCredito cuotaCredito);

}
