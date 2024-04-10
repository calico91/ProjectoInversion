package com.cblandon.inversiones.cliente;

import com.cblandon.inversiones.cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
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
    public ResponseEntity<GenericResponseDTO> createCliente(
            @RequestBody @Valid RegistrarClienteDTO registrarClienteDTO) {

        return GenericResponseDTO.genericResponse(clienteService.createCliente(registrarClienteDTO));

    }

    @GetMapping("/consultarClientes")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<GenericResponseDTO> consultarClientes() {
        return GenericResponseDTO.genericResponse(clienteService.allClientes());
    }

    @GetMapping("/consultarClientePorCedula/{cedula}")
    public ResponseEntity<GenericResponseDTO> consultarCliente(@PathVariable String cedula) {
        return GenericResponseDTO.genericResponse(clienteService.consultarCliente(cedula));


    }

    @GetMapping("/infoClientesCuotaCredito/{fechaFiltro}")
    public ResponseEntity<GenericResponseDTO> infoClientesCuotaCredito(@PathVariable String fechaFiltro) {
        return GenericResponseDTO.genericResponse(
                clienteService.consultarClientesCuotasPendientes(LocalDate.parse(fechaFiltro)));
    }

    @PutMapping("/actualizarCliente/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<GenericResponseDTO> actualizarCliente(
            @PathVariable Integer id, @Valid @RequestBody RegistrarClienteDTO registrarClienteDTO) {

        return GenericResponseDTO.genericResponse(clienteService.actualizarCliente(id, registrarClienteDTO));
    }

    @DeleteMapping("/eliminarCliente/{idCliente}")
    public ResponseEntity<String> eliminarCliente(@PathVariable int idCliente) {
        clienteService.deleteCliente(idCliente);
        return new ResponseEntity<>("Empleado eliminado exitosamente", HttpStatus.OK);
    }
}
