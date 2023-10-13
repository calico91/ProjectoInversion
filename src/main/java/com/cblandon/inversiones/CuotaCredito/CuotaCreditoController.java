package com.cblandon.inversiones.CuotaCredito;

import com.cblandon.inversiones.Cliente.ClienteService;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.CuotaCredito.dto.PagarCuotaRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cuotaCredito")
public class CuotaCreditoController {


    private final CuotaCreditoService cuotaCreditoService;


    @PutMapping("/pagarCuota/{idCuotaCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> actualizarCliente(@PathVariable Integer idCuotaCredito, @RequestBody PagarCuotaRequestDTO pagarCuotaRequestDTO) {
        return ResponseEntity.ok().body(cuotaCreditoService.pagarCuota(idCuotaCredito, pagarCuotaRequestDTO));
    }


}