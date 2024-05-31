package com.cblandon.inversiones.cliente.controller;

import com.cblandon.inversiones.cliente.servicio.ClienteService;
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

    @PostMapping("/registrar-cliente")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(1))")
    public ResponseEntity<GenericResponseDTO> registrarCliente(
            @RequestBody @Valid RegistrarClienteDTO registrarClienteDTO) {

        return GenericResponseDTO.genericResponse(clienteService.registrarCliente(registrarClienteDTO));

    }

    @GetMapping("/consultar-clientes")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(2))")
    public ResponseEntity<GenericResponseDTO> consultarClientes() {
        return GenericResponseDTO.genericResponse(clienteService.allClientes());
    }

    @GetMapping("/consultar-cliente-por-cedula/{cedula}")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(3))")
    public ResponseEntity<GenericResponseDTO> consultarCliente(@PathVariable String cedula) {
        return GenericResponseDTO.genericResponse(clienteService.consultarCliente(cedula));


    }

    @GetMapping("/consultar-cuotas-por-fecha/{fechaFiltro}/{idUsuario}")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(4))")
    public ResponseEntity<GenericResponseDTO> consultarCuotasPorFechas(
            @PathVariable String fechaFiltro, @PathVariable int idUsuario) {
        return GenericResponseDTO.genericResponse(
                clienteService.consultarClientesCuotasPendientes(LocalDate.parse(fechaFiltro), idUsuario));
    }

    @PutMapping("/actualizar-cliente/{id}")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(5))")
    public ResponseEntity<GenericResponseDTO> actualizarCliente(
            @PathVariable Integer id, @Valid @RequestBody RegistrarClienteDTO registrarClienteDTO) {

        return GenericResponseDTO.genericResponse(clienteService.actualizarCliente(id, registrarClienteDTO));
    }

    @DeleteMapping("/eliminarCliente/{idCliente}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> eliminarCliente(@PathVariable int idCliente) {
        clienteService.deleteCliente(idCliente);
        return new ResponseEntity<>("Empleado eliminado exitosamente", HttpStatus.OK);
    }
}
