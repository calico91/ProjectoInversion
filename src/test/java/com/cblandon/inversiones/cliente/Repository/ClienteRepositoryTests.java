package com.cblandon.inversiones.cliente.Repository;

import static org.assertj.core.api.Assertions.assertThat;


import com.cblandon.inversiones.cliente.Cliente;
import com.cblandon.inversiones.cliente.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClienteRepositoryTests {

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente cliente;

    @BeforeEach
    void setup() {
        cliente = Cliente.builder()
                .nombres("Christian")
                .apellidos("Ramirez")
                .email("c1@gmail.com")
                .celular("3104657100")
                .pais("colombia")
                .cedula("11414")
                .direccion("cali")
                .build();
    }

    @DisplayName("Test para guardar un cliente")
    @Test
    void testGuardarCliente() {
        //given
        Cliente cliente2 = Cliente.builder()
                .nombres("Christian")
                .apellidos("Ramirez")
                .email("ce@gmail.com")
                .celular("3104657100")
                .pais("colombia")
                .cedula("1141400")
                .direccion("cali")
                .build();

        //when - acción o el comportamiento que vamos a probar
        Cliente clienteGuardado = clienteRepository.save(cliente2);

        //then - verificar la salida
        assertThat(clienteGuardado).isNotNull();
        assertThat(clienteGuardado.getId()).isPositive();
    }

    @DisplayName("Test para listar todos los clientes")
    @Test
    void testListarClientes() {
        Cliente cliente2 = Cliente.builder()
                .nombres("Christian")
                .apellidos("Ramirez")
                .email("ce@gmail.com")
                .celular("3104657100")
                .pais("colombia")
                .cedula("1141400")
                .direccion("cali")
                .build();
        clienteRepository.save(cliente2);
        clienteRepository.save(cliente);

        List<Cliente> listaClientes = clienteRepository.findAll();

        assertThat(listaClientes).isNotNull();
    }

    @DisplayName("Test para obtener un cliente por ID")
    @Test
    void testObtenerClientePorId() {
        clienteRepository.save(cliente);

        Cliente clienteBD = clienteRepository.findById(cliente.getId()).get();

        assertThat(clienteBD).isNotNull();
    }

    @DisplayName("Test para actualizar un cliente")
    @Test
    void testActualizarCliente() {
        clienteRepository.save(cliente);

        Cliente clienteGuardado = clienteRepository.findById(cliente.getId()).get();
        clienteGuardado.setEmail("donblan@gmail.com");
        clienteGuardado.setNombres("prueba cliente");
        clienteGuardado.setApellidos("prueba cliente");
        Cliente clienteActualizado = clienteRepository.save(clienteGuardado);

        assertThat(clienteActualizado.getEmail()).isEqualTo("donblan@gmail.com");
        assertThat(clienteActualizado.getNombres()).isEqualTo("prueba cliente");
    }

    @DisplayName("Test para eliminar un cliente")
    @Test
    void testEliminarCliente() {
        clienteRepository.save(cliente);

        clienteRepository.deleteById(cliente.getId());
        Optional<Cliente> clienteOptional = clienteRepository.findById(cliente.getId());

        assertThat(clienteOptional).isEmpty();
    }

}
