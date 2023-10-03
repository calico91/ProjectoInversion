package com.cblandon.inversiones.Cliente.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Cliente.ClienteRepository;
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
public class ClienteRepositoryTests {

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
                .build();
    }

    @DisplayName("Test para guardar un cliente")
    @Test
    void testGuardarCliente() {
        //given - dado o condición previa o configuración
        Cliente cliente2 = Cliente.builder()
                .nombres("Christian")
                .apellidos("Ramirez")
                .email("ce@gmail.com")
                .celular("3104657100")
                .pais("colombia")
                .cedula("1141400")
                .build();

        //when - acción o el comportamiento que vamos a probar
        Cliente clienteGuardado = clienteRepository.save(cliente2);

        //then - verificar la salida
        assertThat(clienteGuardado).isNotNull();
        assertThat(clienteGuardado.getId()).isGreaterThan(0);
    }

    @DisplayName("Test para listar todos los clientes")
    @Test
    void testListarClientes() {
        //given
        Cliente cliente2 = Cliente.builder()
                .nombres("Christian")
                .apellidos("Ramirez")
                .email("ce@gmail.com")
                .celular("3104657100")
                .pais("colombia")
                .cedula("1141400")
                .build();
        clienteRepository.save(cliente2);
        clienteRepository.save(cliente);

        //when
        List<Cliente> listaClientes = clienteRepository.findAll();

        //then
        assertThat(listaClientes).isNotNull();
    }

    @DisplayName("Test para obtener un cliente por ID")
    @Test
    void testObtenerClientePorId() {
        clienteRepository.save(cliente);

        //when - comportamiento o accion que vamos a probar
        Cliente clienteBD = clienteRepository.findById(cliente.getId()).get();

        //then
        assertThat(clienteBD).isNotNull();
    }

    @DisplayName("Test para actualizar un cliente")
    @Test
    void testActualizarCliente() {
        clienteRepository.save(cliente);

        //when
        Cliente clienteGuardado = clienteRepository.findById(cliente.getId()).get();
        clienteGuardado.setEmail("donblan@gmail.com");
        clienteGuardado.setNombres("prueba cliente");
        clienteGuardado.setApellidos("prueba cliente");
        Cliente clienteActualizado = clienteRepository.save(clienteGuardado);

        //then
        assertThat(clienteActualizado.getEmail()).isEqualTo("donblan@gmail.com");
        assertThat(clienteActualizado.getNombres()).isEqualTo("prueba cliente");
    }

    @DisplayName("Test para eliminar un cliente")
    @Test
    void testEliminarCliente() {
        clienteRepository.save(cliente);

        //when
        clienteRepository.deleteById(cliente.getId());
        Optional<Cliente> clienteOptional = clienteRepository.findById(cliente.getId());

        //then
        assertThat(clienteOptional).isEmpty();
    }

}
