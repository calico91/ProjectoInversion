package com.cblandon.inversiones.Cliente;

import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public void createCliente(@RequestBody RegistrarClienteDTO registrarClienteDTO) {
        clienteService.createCliente(registrarClienteDTO);

    }

    @GetMapping("/consultarClientes")
    public List<Cliente> consultarClientes() {
        return clienteService.allClientes();
    }
}
