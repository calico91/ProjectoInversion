package com.cblandon.inversiones.Mapper;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Cliente.ClienteService;
import com.cblandon.inversiones.Cliente.dto.ClienteAllResponseDTO;
import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.Credito.Credito;
import com.cblandon.inversiones.Credito.dto.CreditoAllResponseDTO;
import com.cblandon.inversiones.Credito.dto.RegistrarCreditoRequestDTO;
import com.cblandon.inversiones.Credito.dto.RegistrarCreditoResponseDTO;
import com.cblandon.inversiones.CuotaCredito.CuotaCredito;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper(uses = CreditoMapper.class)
public interface Mapper {

    Mapper mapper = Mappers.getMapper(Mapper.class);

    ClienteResponseDTO clienteToClienteResponseDto(Cliente cliente);
    RegistrarClienteDTO clienteToRegistrarClienteDto(Cliente cliente);

    ClienteAllResponseDTO clienteToClienteAllResponseDto(Cliente cliente);

    Cliente registrarClienteDTOToCliente(RegistrarClienteDTO RegistrarClienteDTO);

    Credito registrarCreditoRequestDTOToCredito(RegistrarCreditoRequestDTO registrarCreditoRequestDTO);

    RegistrarCreditoResponseDTO creditoToRegistrarCreditoResponseDTO(Credito credito);

    CreditoAllResponseDTO creditoToCreditoAllResponseDTO(Credito credito);

    Credito registrarCreditoResponseDTOToCredito(CreditoAllResponseDTO creditoAllResponseDTO);

}
