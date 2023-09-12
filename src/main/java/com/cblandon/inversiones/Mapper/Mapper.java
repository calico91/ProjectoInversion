package com.cblandon.inversiones.Mapper;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper(uses = {RolesMapper.class})
public interface Mapper {

    Mapper mapper = Mappers.getMapper(Mapper.class);

    ClienteResponseDTO clienteToClienteResponseDto(Cliente cliente);

    Cliente registrarClienteDTOToCliente(RegistrarClienteDTO RegistrarClienteDTO);

    //UsuariosDTO userEntityToUsuariosDTO(UserEntity userEntity);
}
