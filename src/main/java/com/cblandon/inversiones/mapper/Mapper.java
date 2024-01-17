package com.cblandon.inversiones.mapper;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Cliente.dto.ClienteAllResponseDTO;
import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.Credito.Credito;
import com.cblandon.inversiones.Credito.dto.CreditoAllResponseDTO;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper(uses = CreditoMapper.class)
public interface Mapper {

    Mapper mapper = Mappers.getMapper(Mapper.class);

    ClienteResponseDTO clienteToClienteResponseDto(Cliente cliente);

    ClienteAllResponseDTO clienteToClienteAllResponseDto(Cliente cliente);

    Cliente registrarClienteDTOToCliente(RegistrarClienteDTO registrarClienteDTO);


    CreditoAllResponseDTO creditoToCreditoAllResponseDTO(Credito credito);


}
