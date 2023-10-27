package com.cblandon.inversiones.Cliente;

import com.cblandon.inversiones.Cliente.dto.ClienteAllResponseDTO;
import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.InfoClientesCuotaCreditoDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.Excepciones.NoDataException;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.Mapper;
import com.cblandon.inversiones.Utils.Constantes;
import com.cblandon.inversiones.Utils.UtilsMetodos;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            List<Cliente> clientes;
            System.out.println(clientesCreditosActivos);
            if (clientesCreditosActivos.contains(Constantes.TRUE)) {
                clientes = clienteRepository.clientesCreditosActivos();
            } else {
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


    public List<InfoClientesCuotaCreditoDTO> infoClientesCuotasPendientes() {
        try {

            List<Tuple> infoClienteCuotaCreditoBD = clienteRepository.infoClientesCuotasPendientes();

            List<InfoClientesCuotaCreditoDTO> listaCreditosdto = infoClienteCuotaCreditoBD.stream().map(
                    info -> InfoClientesCuotaCreditoDTO.builder()
                            .idCliente(Integer.parseInt(info.get("id_cliente").toString()))
                            .nombres(info.get("nombres").toString())
                            .apellidos(info.get("apellidos").toString())
                            .cedula(info.get("cedula").toString())
                            .fechaCredito(info.get("fecha_credito").toString())
                            .valorCredito(Double.parseDouble(info.get("valor_credito").toString()))
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
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }

    }
}
