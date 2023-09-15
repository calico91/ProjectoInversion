package com.cblandon.inversiones.Mapper;

import com.cblandon.inversiones.Credito.Credito;
import com.cblandon.inversiones.Credito.dto.CreditoResponseDTO;
import com.cblandon.inversiones.Roles.Roles;
import com.cblandon.inversiones.Roles.dto.RolesDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CreditoMapper {

    CreditoResponseDTO creditoToCreditoResponseDTO(Credito credito);

    Credito creditoResponseDTOToCredito(CreditoResponseDTO creditoResponseDTO);
}
