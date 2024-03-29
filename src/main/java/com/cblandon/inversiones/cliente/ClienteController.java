package com.cblandon.inversiones.cliente;

import com.cblandon.inversiones.cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createCliente(@RequestBody @Valid RegistrarClienteDTO registrarClienteDTO) {
        return new ResponseHandler().generateResponse(Constantes.SUCCESSFUL, HttpStatus.OK, clienteService.createCliente(registrarClienteDTO));

    }

    @GetMapping("/consultarClientes")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> consultarClientes() {
        return new ResponseHandler().generateResponse(Constantes.SUCCESSFUL, HttpStatus.OK, clienteService.allClientes());
    }

    @GetMapping("/consultarClientePorCedula/{cedula}")
    public ResponseEntity<Object> consultarCliente(@PathVariable String cedula) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, clienteService.consultarCliente(cedula));


    }

    @GetMapping("/infoClientesCuotaCredito/{fechaFiltro}")
    public ResponseEntity<Object> infoClientesCuotaCredito(@PathVariable String fechaFiltro) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, clienteService.consultarClientesCuotasPendientes(LocalDate.parse(fechaFiltro)));
    }

    @PutMapping("/actualizarCliente/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> actualizarCliente(@PathVariable Integer id, @Valid @RequestBody RegistrarClienteDTO registrarClienteDTO) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, clienteService.actualizarCliente(id, registrarClienteDTO));
    }

    @DeleteMapping("/eliminarCliente/{idCliente}")
    public ResponseEntity<String> eliminarCliente(@PathVariable int idCliente) {
        clienteService.deleteCliente(idCliente);
        return new ResponseEntity<>("Empleado eliminado exitosamente", HttpStatus.OK);
    }
}
