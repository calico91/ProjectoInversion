package com.cblandon.inversiones.Mapper;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.Credito.Credito;
import com.cblandon.inversiones.Credito.dto.RegistrarCreditoRequestDTO;
import com.cblandon.inversiones.Credito.dto.RegistrarCreditoResponseDTO;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper()
public interface Mapper {

    Mapper mapper = Mappers.getMapper(Mapper.class);

    ClienteResponseDTO clienteToClienteResponseDto(Cliente cliente);

    Cliente registrarClienteDTOToCliente(RegistrarClienteDTO RegistrarClienteDTO);

    Credito registrarCreditoRequestDTOToCredito(RegistrarCreditoRequestDTO registrarCreditoRequestDTO);

    RegistrarCreditoResponseDTO creditoToRegistrarCreditoResponseDTO(Credito credito);
}
