package com.cblandon.inversiones.credito.controller;


import com.cblandon.inversiones.credito.dto.SaldarCreditoDTO;
import com.cblandon.inversiones.credito.servicio.CreditoService;
import com.cblandon.inversiones.credito.dto.RegistrarCreditoRequestDTO;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credito")
public class CreditoController {

    private final CreditoService creditoService;

    public CreditoController(CreditoService creditoService) {
        this.creditoService = creditoService;
    }

    @PostMapping("/registrar-renovar-credito")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(201))")
    public ResponseEntity<GenericResponseDTO> registrarRenovarCredito(
            @RequestBody @Valid RegistrarCreditoRequestDTO registrarCreditoRequestDTO) {
        return GenericResponseDTO.genericResponse(creditoService.registrarRenovarCredito(registrarCreditoRequestDTO));
    }


    @GetMapping("/consultar-credito/{idCredito}")
    @PreAuthorize("hasAnyRole('SUPER')")
    public ResponseEntity<GenericResponseDTO> consultarCredito(@PathVariable Integer idCredito) {
        return GenericResponseDTO.genericResponse(creditoService.consultarCredito(idCredito));

    }

    @GetMapping("/consultar-creditos-activos/{idUsuario}")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(202))")
    public ResponseEntity<GenericResponseDTO> consultarCreditosActivos(@PathVariable Integer idUsuario) {
        return GenericResponseDTO.genericResponse(creditoService.consultarCreditosActivos(idUsuario));
    }

    @PutMapping("/modificar-estado-credito/{idCredito}/{idEstadoCredito}")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(203))")
    public ResponseEntity<GenericResponseDTO> modificarEstadoCredito(
            @PathVariable int idCredito,
            @PathVariable int idEstadoCredito) {

        return GenericResponseDTO.genericResponse(creditoService.modificarEstadoCredito(idCredito, idEstadoCredito));
    }

    @PutMapping("/saldar")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(207))")
    public ResponseEntity<GenericResponseDTO> saldar(
            @RequestBody SaldarCreditoDTO saldarCreditoDTO) {

        return GenericResponseDTO.genericResponse(creditoService.saldar(saldarCreditoDTO));
    }


}
