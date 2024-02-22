package com.cblandon.inversiones.mapper;

import com.cblandon.inversiones.cliente.Cliente;
import com.cblandon.inversiones.cliente.dto.ClienteAllResponseDTO;
import com.cblandon.inversiones.cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.credito.Credito;
import com.cblandon.inversiones.credito.dto.CreditoAllResponseDTO;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper(uses = CreditoMapper.class)
public interface Mapper {

    Mapper mapper = Mappers.getMapper(Mapper.class);

    ClienteResponseDTO clienteToClienteResponseDto(Cliente cliente);
    RegistrarClienteDTO clienteToRegistrarClienteDTO(Cliente cliente);

    ClienteAllResponseDTO clienteToClienteAllResponseDto(Cliente cliente);

    Cliente registrarClienteDTOToCliente(RegistrarClienteDTO registrarClienteDTO);


    CreditoAllResponseDTO creditoToCreditoAllResponseDTO(Credito credito);


}
