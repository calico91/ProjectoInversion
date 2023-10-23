package com.cblandon.inversiones.Cliente;

import com.cblandon.inversiones.Cliente.dto.ClienteAllResponseDTO;
import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.Excepciones.NoDataException;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.Mapper;
import com.cblandon.inversiones.Utils.Constantes;
import com.cblandon.inversiones.Utils.UtilsMetodos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    UtilsMetodos utilsMetodos;


    public ClienteResponseDTO createCliente(RegistrarClienteDTO registrarClienteDTO) {

        if (clienteRepository.findByCedula(registrarClienteDTO.getCedula()) != null) {
            throw new RequestException(
                    Constantes.DOCUMENTO_DUPLICADO, HttpStatus.BAD_REQUEST.value());
        }

        Cliente cliente = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);
        cliente.setUsuariocreador(utilsMetodos.obtenerUsuarioLogueado());

        /// el repository devuelve un cliente y con el mapper lo convierto a dtoresponse
        return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.save(cliente));

    }

    public List<ClienteAllResponseDTO> allClientes(String clientesCreditosActivos) {
        try {
            List<Cliente> clientes = new ArrayList<>();

            if (clientesCreditosActivos.contains(Constantes.TRUE)) {
                clientes = clienteRepository.clientesCreditosActivos();
            } else {
                System.out.println("no entre");
                clientes = clienteRepository.findAll();

            }

            List<ClienteAllResponseDTO> clienteResponseDTO = clientes.stream().map(
                    cliente -> Mapper.mapper.clienteToClienteAllResponseDto(cliente)).collect(Collectors.toList());

            log.info(clienteResponseDTO.toString());

            return clienteResponseDTO;

        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO consultarCliente(String cedula) {

        Cliente clienteBD = clienteRepository.findByCedula(cedula);
        if (clienteBD == null) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value());
        }
        /*mapeo manual sin utilizar Mapper
        List<Credito> listaCreditos = creditoRepository.listaCreditosCliente(clienteBD.getId());

        List<CreditoResponseDTO> listaCreditosdto = listaCreditos.stream().map(
                credito -> CreditoResponseDTO.builder()
                        .idCredito(credito.getId())
                        .cantidadPrestada(credito.getCantidadPrestada())
                        .valorCuota(credito.getValorCuota())
                        .cantidadCuotas(credito.getCantidadCuotas())
                        .build()
        ).collect(Collectors.toList());*/

        ClienteResponseDTO clienteResponseDTO = Mapper.mapper.clienteToClienteResponseDto(clienteBD);
        return clienteResponseDTO;

    }

    public ClienteResponseDTO actualizarCliente(Integer id, RegistrarClienteDTO registrarClienteDTO) {
        Cliente clienteBD = clienteRepository.findById(id).orElseThrow(
                () -> new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value()));

        Cliente clienteModificado = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);

        clienteModificado.setId(clienteBD.getId());
        clienteModificado.setUsuariomodificador(utilsMetodos.obtenerUsuarioLogueado());
        clienteModificado.setUsuariocreador(clienteBD.getUsuariocreador());
        clienteModificado.setFechacreacion(clienteBD.getFechacreacion());

        return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.save(clienteModificado));

    }

    public void deleteCliente(int idCliente) {
        if (clienteRepository.findById(idCliente) == null) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value());
        }
        clienteRepository.deleteById(idCliente);

    }

    public List<ClienteAllResponseDTO> clientesCreditosActivos() {
        try {

            List<Cliente> clientes = clienteRepository.clientesCreditosActivos();

            List<ClienteAllResponseDTO> clienteResponseDTO = clientes.stream().map(
                    cliente -> Mapper.mapper.clienteToClienteAllResponseDto(cliente)).collect(Collectors.toList());

            log.info(clienteResponseDTO.toString());

            return clienteResponseDTO;

        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }
}
