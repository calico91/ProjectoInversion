package com.cblandon.inversiones.Credito;


import com.cblandon.inversiones.Credito.dto.RegistrarCreditoRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credito")
public class CreditoController {

    @Autowired
    private CreditoService creditoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> crearCredito(@RequestBody RegistrarCreditoRequestDTO registrarCreditoRequestDTO) {
        return ResponseEntity.ok().body(creditoService.crearCredito(registrarCreditoRequestDTO));
    }

    @GetMapping("/consultarCreditos")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> consultarCreditos() {
        return ResponseEntity.ok().body(creditoService.allCreditos());
    }

    @GetMapping("/consultarCredito/{idCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> consultarCredito(@PathVariable Long idCredito) {
        return ResponseEntity.ok().body(creditoService.consultarCredito(idCredito));

    }

}