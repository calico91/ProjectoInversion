package com.cblandon.inversiones.Cliente.dto;

import com.cblandon.inversiones.Cliente.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClienteMapper {

    ClienteMapper mapper = Mappers.getMapper(ClienteMapper.class);

    ClienteResponseDTO clienteToClienteResponseDto(Cliente cliente);

    Cliente registrarClienteDTOToCliente(RegistrarClienteDTO RegistrarClienteDTO);
}
