package com.cblandon.inversiones.Cliente;

import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.Excepciones.NoDataException;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.Mapper;
import com.cblandon.inversiones.Security.jwt.JwtUtils;
import com.cblandon.inversiones.Utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;


    public ClienteResponseDTO createCliente(RegistrarClienteDTO registrarClienteDTO) {

        if (clienteRepository.findByCedula(registrarClienteDTO.getCedula()) != null) {
            throw new RequestException(
                    Constantes.DOCUMENTO_DUPLICADO, "1");
        }

        try {

            Cliente cliente = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);
            cliente.setUsuariocreador(SecurityContextHolder.getContext().getAuthentication().getName());
            System.out.println(cliente);
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

        if (clienteRepository.findByCedula(cedula) == null) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, "3");
        }

        try {
            return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.findByCedula(cedula));
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public ClienteResponseDTO actualizarCliente(String cedula, RegistrarClienteDTO registrarClienteDTO) {

        Cliente clienteBD = clienteRepository.findByCedula(cedula);
        if (clienteBD == null) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, "3");
        }

        try {

            Cliente clienteModificado = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);

            clienteModificado.setId(clienteBD.getId());
            clienteModificado.setUsuariomodificador(SecurityContextHolder.getContext().getAuthentication().getName());
            clienteModificado.setUsuariocreador(clienteBD.getUsuariocreador());
            clienteModificado.setFechacreacion(clienteBD.getFechacreacion());
            System.out.println(clienteModificado);


            return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.save(clienteModificado));

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public ClienteResponseDTO deleteCliente(String cedula) {
        if (clienteRepository.findByCedula(cedula) == null) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, "3");
        }
        try {
            return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.deleteByCedula(cedula));

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
