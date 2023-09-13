package com.cblandon.inversiones.Cliente;

import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> consultarClientes() {
        return ResponseEntity.ok().body(clienteService.allClientes());
    }

    @GetMapping("/consultarClientePorCedula/{cedula}")
    public ResponseEntity<?> consultarCliente(@PathVariable String cedula) {
        return ResponseEntity.ok().body(clienteService.consultarCliente(cedula));

    }

    @PutMapping("/actualizarCliente/{cedula}")
    public ResponseEntity<?> actualizarCliente(@PathVariable String cedula, @RequestBody RegistrarClienteDTO registrarClienteDTO) {
        return ResponseEntity.ok().body(clienteService.actualizarCliente(cedula, registrarClienteDTO));
    }

    @DeleteMapping("/eliminarCliente/{cedula}")
    public ResponseEntity<?> eliminarCliente(@PathVariable String cedula) {
        return ResponseEntity.ok().body(clienteService.deleteCliente(cedula));
    }
}
