package com.cblandon.inversiones.credito;


import com.cblandon.inversiones.credito.dto.RegistrarCreditoRequestDTO;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.ResponseHandler;
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
    public ResponseEntity<Object> crearCredito(@RequestBody RegistrarCreditoRequestDTO registrarCreditoRequestDTO) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, creditoService.crearCredito(registrarCreditoRequestDTO));
    }

    @GetMapping("/consultarCreditos")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> consultarCreditos() {
        return ResponseEntity.ok().body(creditoService.allCreditos());
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
                Constantes.SUCCESSFUL, HttpStatus.OK, creditoService.consultarInfoCreditosActivos());
    }

    @PutMapping("/modificarEstadoCredito/{idCredito}/{estadoCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> modificarEstadoCredito(@PathVariable Integer idCredito,
                                                    @PathVariable String estadoCredito) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, creditoService.modificarEstadoCredito(idCredito, estadoCredito));
    }


}
