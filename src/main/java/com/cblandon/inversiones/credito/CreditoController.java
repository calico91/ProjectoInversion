package com.cblandon.inversiones.credito;


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

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<GenericResponseDTO> crearCredito(
            @RequestBody @Valid RegistrarCreditoRequestDTO registrarCreditoRequestDTO) {
        return GenericResponseDTO.genericResponse(creditoService.crearCredito(registrarCreditoRequestDTO));
    }


    @GetMapping("/consultarCredito/{idCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<GenericResponseDTO> consultarCredito(@PathVariable Integer idCredito) {
        return GenericResponseDTO.genericResponse(creditoService.consultarCredito(idCredito));

    }

    @GetMapping("/infoCreditosActivos")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<GenericResponseDTO> infoCreditosActivos() {
        return GenericResponseDTO.genericResponse(creditoService.consultarCreditosActivos());
    }

    @PutMapping("/modificarEstadoCredito/{idCredito}/{idEstadoCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<GenericResponseDTO> modificarEstadoCredito(
            @PathVariable int idCredito,
            @PathVariable int idEstadoCredito) {

        return GenericResponseDTO.genericResponse(creditoService.modificarEstadoCredito(idCredito, idEstadoCredito));
    }


}
