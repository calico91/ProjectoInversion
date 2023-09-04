package com.cblandon.inversiones.Cliente;

import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public void createCliente(RegistrarClienteDTO registrarClienteDTO) {

        Cliente cliente = Cliente.builder()
                .nombres(registrarClienteDTO.getNombres())
                .apellidos(registrarClienteDTO.getApellidos())
                .celular(registrarClienteDTO.getCelular())
                .email(registrarClienteDTO.getEmail())
                .pais(registrarClienteDTO.getPais())
                .build();

        clienteRepository.save(cliente);
    }

    public List<Cliente> allClientes() {

        System.out.println(clienteRepository.findAll());
        return clienteRepository.findAll();

    }

    public void deleteCliente(Integer id) {
        clienteRepository.deleteById(id);
    }


}
