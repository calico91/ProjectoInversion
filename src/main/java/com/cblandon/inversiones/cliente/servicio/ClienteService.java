package com.cblandon.inversiones.cliente.servicio;

import com.cblandon.inversiones.cliente.dto.ClienteAllResponseDTO;
import com.cblandon.inversiones.cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.cliente.dto.ClientesCuotaCreditoDTO;
import com.cblandon.inversiones.cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.cliente.entity.Cliente;
import com.cblandon.inversiones.cliente.repository.ClienteRepository;
import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.mapper.Mapper;
import com.cblandon.inversiones.utils.MensajesErrorEnum;
import com.cblandon.inversiones.utils.UtilsMetodos;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ClienteService {

    final ClienteRepository clienteRepository;


    @Transactional
    public ClienteResponseDTO registrarCliente(RegistrarClienteDTO registrarClienteDTO) {
        log.info("createCliente registrarClienteDTO: {}", registrarClienteDTO.toString());

        if (clienteRepository.findByCedula(registrarClienteDTO.cedula()).isPresent()) {
            throw new RequestException(
                    MensajesErrorEnum.DOCUMENTO_DUPLICADO);
        }
        try {
            Cliente cliente = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);
            cliente.setUsuariocreador(UtilsMetodos.obtenerUsuarioLogueado());

            return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.save(cliente));

        } catch (RuntimeException ex) {
            log.error("createCliente: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public List<ClienteAllResponseDTO> allClientes() {
        try {

            List<Cliente> clientes = clienteRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

            List<ClienteAllResponseDTO> clienteResponseDTO = clientes.stream().map(
                    Mapper.mapper::clienteToClienteAllResponseDto).toList();

            log.info("allClientes: {}", clienteResponseDTO);

            return clienteResponseDTO;

        } catch (RuntimeException ex) {
            log.error("allClientes: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO consultarCliente(String cedula) {

        Cliente clienteBD = clienteRepository.findByCedula(cedula).orElseThrow(
                () -> new NoDataException(MensajesErrorEnum.DATOS_NO_ENCONTRADOS));

        try {

            ClienteResponseDTO clienteResponseDTO = Mapper.mapper.clienteToClienteResponseDto(clienteBD);
            log.info("consultarCliente: {}", clienteResponseDTO.toString());
            return clienteResponseDTO;

        } catch (RuntimeException ex) {

            log.error("consultarCliente: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }


    }

    @Transactional
    public ClienteResponseDTO actualizarCliente(Integer id, RegistrarClienteDTO registrarClienteDTO) {

        log.info("actualizarCliente: {}", registrarClienteDTO.toString());
        Cliente clienteBD = clienteRepository.findById(id).orElseThrow(
                () -> new NoDataException(MensajesErrorEnum.DATOS_NO_ENCONTRADOS));

        try {

            Cliente clienteModificado = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);
            clienteModificado.setId(clienteBD.getId());
            clienteModificado.setUsuariomodificador(UtilsMetodos.obtenerUsuarioLogueado());
            clienteModificado.setUsuariocreador(clienteBD.getUsuariocreador());
            clienteModificado.setFechacreacion(clienteBD.getFechacreacion());

            return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.save(clienteModificado));

        } catch (RuntimeException ex) {
            log.error("actualizarCliente: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }


    }

    @Transactional
    public void deleteCliente(int idCliente) {
        if (clienteRepository.findById(idCliente).isEmpty()) {
            throw new NoDataException(MensajesErrorEnum.DATOS_NO_ENCONTRADOS);
        }
        clienteRepository.deleteById(idCliente);

    }


    /**
     * lista de cuotas pendientes de la fecha seleccionada para atras
     */
    @Transactional(readOnly = true)
    public List<ClientesCuotaCreditoDTO> consultarClientesCuotasPendientes(LocalDate fechaFiltro, int idUsuario) {

        try {
            List<ClientesCuotaCreditoDTO> listaCreditosdto = clienteRepository.consultarClientesCuotasPendientes(
                    fechaFiltro, idUsuario);

            log.info("infoClientesCuotasPendientes: {} ", listaCreditosdto.toString());
            return listaCreditosdto;

        } catch (RuntimeException ex) {
            log.error("infoClientesCuotasPendientes: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }
}
