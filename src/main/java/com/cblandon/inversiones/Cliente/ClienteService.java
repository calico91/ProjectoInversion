package com.cblandon.inversiones.Cliente;

import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.Excepciones.NoDataException;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Jwt.JwtService;
import com.cblandon.inversiones.Mapper.Mapper;
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

    public ClienteResponseDTO createCliente(RegistrarClienteDTO registrarClienteDTO) {

        try {

            if (clienteRepository.findByCedula(registrarClienteDTO.getCedula()) == null) {
                throw new RequestException(
                        "el cliente con cedula " + registrarClienteDTO.getCedula() + " ya se encuentra registrado", "1");
            }

            Cliente cliente = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);
            cliente.setUsuariocreador(SecurityContextHolder.getContext().getAuthentication().getName());

            /// el repository devuelve un cliente y con el mapper lo convierto a dtoresponse
            return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.save(cliente));
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());

        }


    }

    public List<ClienteResponseDTO> allClientes() {
        try {

            List<Cliente> clientes = clienteRepository.findAll();

            List<ClienteResponseDTO> clienteResponseDTOS = clientes.stream().map(
                    cliente -> Mapper.mapper.clienteToClienteResponseDto(cliente)).collect(Collectors.toList());

            return clienteResponseDTOS;
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public ClienteResponseDTO consultarCliente(String cedula) {
        try {
            if (clienteRepository.findByCedula(cedula) == null) {
                throw new NoDataException("no se encontraron resultados", "3");
            }

            return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.findByCedula(cedula));

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public void deleteCliente(Integer id) {
        clienteRepository.deleteById(id);
    }


}
