package com.cblandon.inversiones.cliente;

import com.cblandon.inversiones.cliente.dto.ClienteAllResponseDTO;
import com.cblandon.inversiones.cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.cliente.dto.InfoClientesCuotaCreditoDTO;
import com.cblandon.inversiones.cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.mapper.Mapper;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.UtilsMetodos;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ClienteService {

    final ClienteRepository clienteRepository;

    final UtilsMetodos utilsMetodos;


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

    public List<ClienteAllResponseDTO> allClientes() {
        try {

            List<Cliente> clientes = clienteRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));


            List<ClienteAllResponseDTO> clienteResponseDTO = clientes.stream().map(
                    Mapper.mapper::clienteToClienteAllResponseDto).collect(Collectors.toList());

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
        log.info(clienteResponseDTO.toString());
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
        if (clienteRepository.findById(idCliente).isEmpty()) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value());
        }
        clienteRepository.deleteById(idCliente);

    }


    /// listqa de cuotas pendientes de la fecha actual para atras
    public List<InfoClientesCuotaCreditoDTO> infoClientesCuotasPendientes(String fechaFiltro) {
        //spring.jpa.show-sql=true
        try {


            List<Tuple> infoClienteCuotaCreditoBD = clienteRepository.infoClientesCuotasPendientes(fechaFiltro);

            List<InfoClientesCuotaCreditoDTO> listaCreditosdto = infoClienteCuotaCreditoBD.stream().map(
                    info -> InfoClientesCuotaCreditoDTO.builder()
                            .idCliente(Integer.parseInt(info.get("id_cliente").toString()))
                            .nombres(info.get("nombres").toString())
                            .apellidos(info.get("apellidos").toString())
                            .cedula(info.get("cedula").toString())
                            .fechaCredito(info.get("fecha_credito").toString())
                            .valorCredito(Double.parseDouble(info.get("valor_credito").toString()))
                            .valorInteres(Double.parseDouble(info.get("valor_interes").toString()))
                            .fechaAbono((String) info.get("fecha_abono"))
                            .fechaCuota(info.get("fecha_cuota").toString())
                            .valorCuota(Double.parseDouble(info.get("valor_cuota").toString()))
                            .idCredito(Integer.parseInt(info.get("id_credito").toString()))
                            .build()
            ).collect(Collectors.toList());
            log.info(listaCreditosdto.toString());

            return listaCreditosdto;

        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }
}
