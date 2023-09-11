package com.cblandon.inversiones.Mapper;

import com.cblandon.inversiones.Auth.dto.RegisterRequestDTO;
import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.User.User;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface Mapper {

    Mapper mapper = Mappers.getMapper(Mapper.class);

    ClienteResponseDTO clienteToClienteResponseDto(Cliente cliente);

    Cliente registrarClienteDTOToCliente(RegistrarClienteDTO RegistrarClienteDTO);

    //User registerRequestDTOToUser(RegisterRequestDTO registerRequestDTO);
}
