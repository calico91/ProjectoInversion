package com.cblandon.inversiones.credito;


import com.cblandon.inversiones.credito.dto.RegistrarCreditoRequestDTO;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> crearCredito(@RequestBody @Valid RegistrarCreditoRequestDTO registrarCreditoRequestDTO) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, creditoService.crearCredito(registrarCreditoRequestDTO));
    }


    @GetMapping("/consultarCredito/{idCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> consultarCredito(@PathVariable Integer idCredito) {
        return ResponseEntity.ok().body(creditoService.consultarCredito(idCredito));

    }

    @GetMapping("/infoCreditosActivos")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> infoCreditosActivos() {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, creditoService.consultarCreditosActivos());
    }

    @PutMapping("/modificarEstadoCredito/{idCredito}/{idEstadoCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> modificarEstadoCredito(@PathVariable int idCredito,
                                                         @PathVariable int idEstadoCredito) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, creditoService.modificarEstadoCredito(idCredito, idEstadoCredito));
    }


}
