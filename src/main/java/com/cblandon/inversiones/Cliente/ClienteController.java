package com.cblandon.inversiones.Cliente;

import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody RegistrarClienteDTO registrarClienteDTO) {
        return ResponseEntity.ok().body(clienteService.createCliente(registrarClienteDTO));

    }

    @GetMapping("/consultarClientes")
    public ResponseEntity<?> consultarClientes() {
        return ResponseEntity.ok().body(clienteService.allClientes());
    }
}
