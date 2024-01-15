/*package com.cblandon.inversiones.Cliente.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;


import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Cliente.ClienteRepository;
import com.cblandon.inversiones.Cliente.ClienteService;
import com.cblandon.inversiones.Cliente.dto.ClienteAllResponseDTO;
import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {


    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;


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
                .build();


    }

    @DisplayName("Test para guardar un cliente")
    @Test
    void testGuardarCliente() {
        //given
        given(clienteRepository.findByCedula(cliente.getCedula()))
                .willReturn(null);
        given(clienteRepository.save(cliente)).willReturn(cliente);
        RegistrarClienteDTO clienteDTO = Mapper.mapper.clienteToRegistrarClienteDto(cliente);

        //when
        ClienteResponseDTO clienteGuardado = clienteService.createCliente(clienteDTO);

        //then
        assertThat(clienteGuardado).isNotNull();
    }


    @DisplayName("Test para guardar un cliente con Throw Exception")
    @Test
    void testGuardarClienteConThrowException() {
        //given
        given(clienteRepository.findByCedula(cliente.getCedula())).willReturn(cliente);
        RegistrarClienteDTO registrarClienteDto = Mapper.mapper.clienteToRegistrarClienteDto(cliente);

        //when
        assertThrows(RequestException.class, () -> {
            clienteService.createCliente(registrarClienteDto);
        });

        //then
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

  *//*  @DisplayName("Test para listar los clientes")
    @Test
    void testListarClientes() {
        //given
        Cliente cliente2 = Cliente.builder()
                .id(2)
                .nombres("maelo")
                .apellidos("maelito")
                .email("j2@gmail.com")
                .build();
        given(clienteRepository.findAll()).willReturn(List.of(cliente, cliente2));

        //when
        List<ClienteAllResponseDTO> clientes = clienteService.allClientes();

        //then
        assertThat(clientes).isNotNull();
        assertThat(clientes.size()).isEqualTo(2);
    }*//*

    @DisplayName("Test para obtener un cliente por cedula")
    @Test
    void testObtenerClientePorCedula() {
        //given
        given(clienteRepository.findByCedula("1")).willReturn(cliente);

        //when
        ClienteResponseDTO clienteGuardado = clienteService.consultarCliente(cliente.getCedula());

        //then
        assertThat(clienteGuardado).isNotNull();
    }

    @DisplayName("Test para actualizar un cliente")
    @Test
    void testActualizarCliente() {

        //given
        given(clienteRepository.findById(1)).willReturn(Optional.ofNullable(cliente));
        cliente.setApellidos("blandito");
        cliente.setNombres("Maelito");
        given(clienteRepository.save(cliente)).willReturn(cliente);
        RegistrarClienteDTO registrarClienteDTO = Mapper.mapper.clienteToRegistrarClienteDto(cliente);

        //when
        ClienteResponseDTO clienteActualizado = clienteService.actualizarCliente(1, registrarClienteDTO);

        //then
        assertThat(clienteActualizado.getApellidos()).isEqualTo("blandito");
        assertThat(clienteActualizado.getNombres()).isEqualTo("Maelito");
    }

    @DisplayName("Test para eliminar un cliente")
    @Test
    void testEliminarCliente() {
        //given
        int idCliente = 1;
        willDoNothing().given(clienteRepository).deleteById(idCliente);

        //when
        clienteService.deleteCliente(idCliente);

        //then
        verify(clienteRepository, times(1)).deleteById(idCliente);
    }

}*/
