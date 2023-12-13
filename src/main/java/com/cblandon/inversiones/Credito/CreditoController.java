package com.cblandon.inversiones.Credito;


import com.cblandon.inversiones.Credito.dto.RegistrarCreditoRequestDTO;
import com.cblandon.inversiones.Utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> crearCredito(@RequestBody RegistrarCreditoRequestDTO registrarCreditoRequestDTO) {
        return new ResponseHandler().generateResponse(
                "successful", HttpStatus.OK, creditoService.crearCredito(registrarCreditoRequestDTO));
    }

    @GetMapping("/consultarCreditos")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> consultarCreditos() {
        return ResponseEntity.ok().body(creditoService.allCreditos());
    }

    @GetMapping("/consultarCredito/{idCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> consultarCredito(@PathVariable Integer idCredito) {
        return ResponseEntity.ok().body(creditoService.consultarCredito(idCredito));

    }

    @GetMapping("/infoCreditosActivos")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> infoCreditosActivos() {
        return new ResponseHandler().generateResponse(
                "successful", HttpStatus.OK, creditoService.infoCreditosActivos());
    }

}
