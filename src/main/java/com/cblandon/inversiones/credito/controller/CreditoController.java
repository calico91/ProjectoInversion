package com.cblandon.inversiones.credito.controller;


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

    @PostMapping("/registrar-credito")
    @PreAuthorize("hasAnyRole('ADMIN','COBRADOR')")
    public ResponseEntity<GenericResponseDTO> registrarCredito(
            @RequestBody @Valid RegistrarCreditoRequestDTO registrarCreditoRequestDTO) {
        return GenericResponseDTO.genericResponse(creditoService.registrarCredito(registrarCreditoRequestDTO));
    }


    @GetMapping("/consultar-credito/{idCredito}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> consultarCredito(@PathVariable Integer idCredito) {
        return GenericResponseDTO.genericResponse(creditoService.consultarCredito(idCredito));

    }

    @GetMapping("/consultar-creditos-activos")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> consultarCreditosActivos() {
        return GenericResponseDTO.genericResponse(creditoService.consultarCreditosActivos());
    }

    @PutMapping("/modificar-estado-credito/{idCredito}/{idEstadoCredito}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> modificarEstadoCredito(
            @PathVariable int idCredito,
            @PathVariable int idEstadoCredito) {

        return GenericResponseDTO.genericResponse(creditoService.modificarEstadoCredito(idCredito, idEstadoCredito));
    }




}
