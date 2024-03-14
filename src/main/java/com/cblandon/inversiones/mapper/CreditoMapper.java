package com.cblandon.inversiones.mapper;

import com.cblandon.inversiones.credito.Credito;
import com.cblandon.inversiones.credito.dto.CreditoCuotasResponseDTO;
import com.cblandon.inversiones.credito.dto.CreditoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CuotaCreditoMapper.class)
public interface CreditoMapper {

    CreditoMapper mapperCredito =  Mappers.getMapper(CreditoMapper.class);

    CreditoResponseDTO creditoToCreditoResponseDTO(Credito credito);
    CreditoCuotasResponseDTO creditoToCreditoCuotasResponseDTO(Credito credito);

}
