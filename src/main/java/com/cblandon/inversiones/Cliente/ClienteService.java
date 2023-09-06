package com.cblandon.inversiones.Cliente;

import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Jwt.JwtService;
import com.cblandon.inversiones.Mapper.Mapper;
import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;


    @Autowired
    JwtService jwtService;

    public void createCliente(RegistrarClienteDTO registrarClienteDTO) {

        Optional consultarCliente = clienteRepository.findByCedula(registrarClienteDTO.getCedula());

        if (!consultarCliente.isEmpty()) {
            throw new RequestException(
                    "el cliente con cedula " + registrarClienteDTO.getCedula() + " ya se encuentra registrado", "1");
        }

        try {
            Cliente cliente = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);
            cliente.setUsuariocreador(SecurityContextHolder.getContext().getAuthentication().getName());
            clienteRepository.save(cliente);
        } catch (RuntimeException ex) {
            System.out.println("entre");
            throw new RuntimeException(ex.getMessage());

        }


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
