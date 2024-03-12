package com.cblandon.inversiones.cliente;

import com.cblandon.inversiones.cliente.dto.ClienteAllResponseDTO;
import com.cblandon.inversiones.cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.cliente.dto.ClientesCuotaCreditoDTO;
import com.cblandon.inversiones.cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.mapper.Mapper;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.UtilsMetodos;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ClienteService {

    final ClienteRepository clienteRepository;

    final UtilsMetodos utilsMetodos = new UtilsMetodos();

    @Transactional
    public ClienteResponseDTO createCliente(RegistrarClienteDTO registrarClienteDTO) {
        log.info("createCliente registrarClienteDTO: ".concat(registrarClienteDTO.toString()));

        if (clienteRepository.findByCedula(registrarClienteDTO.cedula()).isPresent()) {
            throw new RequestException(
                    Constantes.DOCUMENTO_DUPLICADO, HttpStatus.BAD_REQUEST.value());
        }
        try {
            Cliente cliente = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);
            cliente.setUsuariocreador(utilsMetodos.obtenerUsuarioLogueado());

            return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.save(cliente));

        } catch (RuntimeException ex) {
            log.error("createCliente ".concat(ex.getMessage()));
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public List<ClienteAllResponseDTO> allClientes() {
        try {

            List<Cliente> clientes = clienteRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

            List<ClienteAllResponseDTO> clienteResponseDTO = clientes.stream().map(
                    Mapper.mapper::clienteToClienteAllResponseDto).toList();

            log.info("allClientes ".concat(clienteResponseDTO.toString()));

            return clienteResponseDTO;

        } catch (RuntimeException ex) {
            log.error("allClientes ".concat(ex.getMessage()));
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO consultarCliente(String cedula) {

        Cliente clienteBD = clienteRepository.findByCedula(cedula).orElseThrow(
                () -> new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value()));

        try {

            ClienteResponseDTO clienteResponseDTO = Mapper.mapper.clienteToClienteResponseDto(clienteBD);
            log.info("consultarCliente ".concat(clienteResponseDTO.toString()));
            return clienteResponseDTO;

        } catch (RuntimeException ex) {

            log.error("consultarCliente ".concat(ex.getMessage()));
            throw new RuntimeException(ex.getMessage());
        }


    }

    @Transactional
    public ClienteResponseDTO actualizarCliente(Integer id, RegistrarClienteDTO registrarClienteDTO) {

        log.info("actualizarCliente ".concat(registrarClienteDTO.toString()));
        Cliente clienteBD = clienteRepository.findById(id).orElseThrow(
                () -> new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value()));

        try {

            Cliente clienteModificado = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);
            clienteModificado.setId(clienteBD.getId());
            clienteModificado.setUsuariomodificador(utilsMetodos.obtenerUsuarioLogueado());
            clienteModificado.setUsuariocreador(clienteBD.getUsuariocreador());
            clienteModificado.setFechacreacion(clienteBD.getFechacreacion());

            return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.save(clienteModificado));

        } catch (RuntimeException ex) {
            log.error("actualizarCliente " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }


    }

    @Transactional
    public void deleteCliente(int idCliente) {
        if (clienteRepository.findById(idCliente).isEmpty()) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value());
        }
        clienteRepository.deleteById(idCliente);

    }


    /**
     * lista de cuotas pendientes de la fecha actual para atras
     */
    @Transactional(readOnly = true)
    public List<ClientesCuotaCreditoDTO> consultarClientesCuotasPendientes(LocalDate fechaFiltro) {

        try {
            List<ClientesCuotaCreditoDTO> listaCreditosdto = clienteRepository.consultarClientesCuotasPendientes(fechaFiltro);

            log.info("infoClientesCuotasPendientes ".concat(listaCreditosdto.toString()));
            return listaCreditosdto;

        } catch (RuntimeException ex) {
            log.error("infoClientesCuotasPendientes ".concat(ex.getMessage()));
            throw new RuntimeException(ex.getMessage());
        }

    }
}
