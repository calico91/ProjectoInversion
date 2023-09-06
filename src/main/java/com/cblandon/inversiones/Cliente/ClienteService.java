package com.cblandon.inversiones.Cliente;

import com.cblandon.inversiones.Mapper.Mapper;
import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public void createCliente(RegistrarClienteDTO registrarClienteDTO) {

        Cliente clientemaper = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);

        clienteRepository.save(clientemaper);
    }

    public List<ClienteResponseDTO> allClientes() {

        List<Cliente> clientes = clienteRepository.findAll();

        List<ClienteResponseDTO> clienteResponseDTOS = clientes.stream().map(
                cliente -> Mapper.mapper.clienteToClienteResponseDto(cliente)).collect(Collectors.toList());

        return clienteResponseDTOS;
    }

    public void deleteCliente(Integer id) {
        clienteRepository.deleteById(id);
    }


}
