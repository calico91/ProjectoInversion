
package com.cblandon.inversiones.cliente.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


import com.cblandon.inversiones.cliente.Cliente;
import com.cblandon.inversiones.cliente.ClienteRepository;
import com.cblandon.inversiones.cliente.ClienteService;
import com.cblandon.inversiones.cliente.dto.ClienteAllResponseDTO;
import com.cblandon.inversiones.cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.cliente.dto.InfoClientesCuotaCreditoDTO;
import com.cblandon.inversiones.cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.mapper.Mapper;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {


    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private Authentication authenticationMock;


    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    private InfoClientesCuotaCreditoDTO infoClientesCuotaCreditoDTO;


    @BeforeEach
    void setup() {


        cliente = Cliente.builder()
                .id(1)
                .nombres("Christian")
                .apellidos("Ramirez")
                .email("c1@gmail.com")
                .pais("colombia")
                .cedula("1")
                .celular("310125121")
                .usuariocreador("blandon")
                .build();


    }

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("Test para guardar un cliente")
    @Test
    void testCreateCliente() {
        //given
        given(clienteRepository.findByCedula(cliente.getCedula()))
                .willReturn(Optional.empty());

        SecurityContextHolder.getContext().setAuthentication(authenticationMock);
        given(authenticationMock.getName()).willReturn("mockuser");

        RegistrarClienteDTO clienteDTO = Mapper.mapper.clienteToRegistrarClienteDTO(cliente);
        given(clienteRepository.save(any())).willReturn(cliente);

        //when
        ClienteResponseDTO clienteGuardado = clienteService.createCliente(clienteDTO);

        //then
        assertThat(clienteGuardado).isNotNull();
        assertThat(clienteGuardado.getCedula()).isEqualTo(cliente.getCedula());

    }


    @DisplayName("Test para guardar un cliente con Throw Exception")
    @Test
    void testGuardarClienteConThrowException() {
        //given
        given(clienteRepository.findByCedula(cliente.getCedula())).willReturn(Optional.of(cliente));
        RegistrarClienteDTO registrarClienteDto = Mapper.mapper.clienteToRegistrarClienteDTO(cliente);

        //when
        assertThrows(RequestException.class, () ->
                clienteService.createCliente(registrarClienteDto));


        //then
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @DisplayName("Test para listar los clientes")
    @Test
    void testListarClientes() {
        //given
        Cliente cliente2 = Cliente.builder()
                .id(2)
                .nombres("maelo")
                .apellidos("maelito")
                .email("j2@gmail.com")
                .build();
        given(clienteRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))).willReturn(List.of(cliente, cliente2));

        //when
        List<ClienteAllResponseDTO> clientes = clienteService.allClientes();

        //then
        assertThat(clientes)
                .hasSize(2)
                .isNotNull();
    }

    @DisplayName("Test para obtener un cliente por cedula")
    @Test
    void testObtenerClientePorCedula() {
        //given
        given(clienteRepository.findByCedula("1")).willReturn(Optional.of(cliente));

        //when
        ClienteResponseDTO clienteGuardado = clienteService.consultarCliente(cliente.getCedula());

        //then
        assertThat(clienteGuardado).isNotNull();
    }

    @DisplayName("Test para obtener un cliente por cedula con throw")
    @Test
    void testObtenerClientePorCedulaThrow() {

        given(clienteRepository.findByCedula("2")).willReturn(Optional.empty());

        assertThrows(NoDataException.class, () -> clienteService.consultarCliente("2"));

        //then
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @DisplayName("Test para actualizar un cliente")
    @Test
    void testActualizarCliente() {

        //given
        given(clienteRepository.findById(1)).willReturn(Optional.ofNullable(cliente));
        cliente.setApellidos("blandito");
        cliente.setNombres("Maelito");
        SecurityContextHolder.getContext().setAuthentication(authenticationMock);
        given(authenticationMock.getName()).willReturn("mockuser");
        given(clienteRepository.save(any())).willReturn(cliente);
        RegistrarClienteDTO registrarClienteDTO = Mapper.mapper.clienteToRegistrarClienteDTO(cliente);

        //when
        ClienteResponseDTO clienteActualizado = clienteService.actualizarCliente(1, registrarClienteDTO);

        //then
        assertThat(clienteActualizado.getApellidos()).isEqualTo("blandito");
        assertThat(clienteActualizado.getNombres()).isEqualTo("Maelito");
    }

    @DisplayName("Test para actualizar un cliente con throw")
    @Test
    void testActualizarClienteThrow() {

        //given
        given(clienteRepository.findById(5)).willReturn(Optional.empty());
        RegistrarClienteDTO registrarClienteDTO = Mapper.mapper.clienteToRegistrarClienteDTO(cliente);

        //when
        assertThrows(NoDataException.class, () -> clienteService.actualizarCliente(5, registrarClienteDTO));

        //then
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @DisplayName("Test para listar de cuotas pendientes de la fecha actual para atras")
    @Test
    void infoClientesCuotasPendientesTes() {
        Tuple mockedTuple = mock(Tuple.class);
        String fechaFiltro = "2022-04-15";

        List<Tuple> listTuple = new ArrayList<>();
        listTuple.add(mockedTuple);

        //given(clienteRepository.infoClientesCuotasPendientes(fechaFiltro)).willReturn(listTuple);

        //when
        clienteService.infoClientesCuotasPendientes(fechaFiltro);

        //then
        verify(clienteRepository, times(1)).infoClientesCuotasPendientes(fechaFiltro);
    }



}

