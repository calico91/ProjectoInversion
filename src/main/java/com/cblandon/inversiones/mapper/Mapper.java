package com.cblandon.inversiones.mapper;

import com.cblandon.inversiones.cliente.entity.Cliente;
import com.cblandon.inversiones.cliente.dto.ClienteAllResponseDTO;
import com.cblandon.inversiones.cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.cliente.dto.RegistrarClienteDTO;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper( uses = CreditoMapper.class)

public interface Mapper {

    Mapper mapper = Mappers.getMapper(Mapper.class);

    ClienteResponseDTO clienteToClienteResponseDto(Cliente cliente);
    RegistrarClienteDTO clienteToRegistrarClienteDTO(Cliente cliente);

    ClienteAllResponseDTO clienteToClienteAllResponseDto(Cliente cliente);

    Cliente registrarClienteDTOToCliente(RegistrarClienteDTO registrarClienteDTO);



}
