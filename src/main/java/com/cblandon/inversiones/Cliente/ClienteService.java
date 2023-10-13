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
                    Constantes.DOCUMENTO_DUPLICADO, "1");
        }

        Cliente cliente = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);
        cliente.setUsuariocreador(utilsMetodos.obtenerUsuarioLogueado());

        /// el repository devuelve un cliente y con el mapper lo convierto a dtoresponse
        return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.save(cliente));

    }

    public List<ClienteAllResponseDTO> allClientes() {
        try {

            List<Cliente> clientes = clienteRepository.findAll();

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
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, "3");
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

    public ClienteResponseDTO actualizarCliente(String cedula, RegistrarClienteDTO registrarClienteDTO) {
        Cliente clienteBD = clienteRepository.findByCedula(cedula);
        if (clienteBD == null) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, "3");
        }

        Cliente clienteModificado = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);

        clienteModificado.setId(clienteBD.getId());
        clienteModificado.setUsuariomodificador(utilsMetodos.obtenerUsuarioLogueado());
        clienteModificado.setUsuariocreador(clienteBD.getUsuariocreador());
        clienteModificado.setFechacreacion(clienteBD.getFechacreacion());

        return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.save(clienteModificado));

    }

    public void deleteCliente(int idCliente) {
        if (clienteRepository.findById(idCliente) == null) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, "3");
        }
        clienteRepository.deleteById(idCliente);

    }
}
