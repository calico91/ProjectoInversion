package com.cblandon.inversiones.Cliente;

import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createCliente(@RequestBody RegistrarClienteDTO registrarClienteDTO) {
        return new ResponseHandler().generateResponse("successful", HttpStatus.OK, clienteService.createCliente(registrarClienteDTO));

    }

    @GetMapping("/consultarClientes")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> consultarClientes() {
        return new ResponseHandler().generateResponse("successful", HttpStatus.OK, clienteService.allClientes());
    }

    @GetMapping("/consultarClientePorCedula/{cedula}")
    public ResponseEntity<?> consultarCliente(@PathVariable String cedula) {
        return new ResponseHandler().generateResponse(
                "successful", HttpStatus.OK, clienteService.consultarCliente(cedula));


    }

    @GetMapping("/infoClientesCuotaCredito/{fechaFiltro}")
    public ResponseEntity<?> infoClientesCuotaCredito(@PathVariable String fechaFiltro) {
        return new ResponseHandler().generateResponse(
                "successful", HttpStatus.OK, clienteService.infoClientesCuotasPendientes(fechaFiltro));
    }

    @PutMapping("/actualizarCliente/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> actualizarCliente(@PathVariable Integer id, @RequestBody RegistrarClienteDTO registrarClienteDTO) {
        return new ResponseHandler().generateResponse(
                "successful", HttpStatus.OK, clienteService.actualizarCliente(id, registrarClienteDTO));
    }

    @DeleteMapping("/eliminarCliente/{idCliente}")
    public ResponseEntity<?> eliminarCliente(@PathVariable int idCliente) {
        clienteService.deleteCliente(idCliente);
        return new ResponseEntity<String>("Empleado eliminado exitosamente", HttpStatus.OK);
    }
}
