package com.cblandon.inversiones.CuotaCredito;

import com.cblandon.inversiones.Cliente.ClienteService;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.CuotaCredito.dto.PagarCuotaRequestDTO;
import com.cblandon.inversiones.Utils.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cuotaCredito")
public class CuotaCreditoController {


    private final CuotaCreditoService cuotaCreditoService;


    @PutMapping("/pagarCuota/{idCuotaCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> pagarCuota(
            @PathVariable Integer idCuotaCredito, @RequestBody PagarCuotaRequestDTO pagarCuotaRequestDTO) {
        return new ResponseHandler().generateResponse(
                "successful", HttpStatus.OK, cuotaCreditoService.pagarCuota(
                        idCuotaCredito, pagarCuotaRequestDTO));
    }

    @GetMapping("/infoCuotaCreditoCliente/{idCliente}/{idCredito}")
    public ResponseEntity<?> infoCuotaCreditoCliente(
            @PathVariable Integer idCliente, @PathVariable Integer idCredito) {
        return new ResponseHandler().generateResponse(
                "successful",
                HttpStatus.OK, cuotaCreditoService.infoCuotaCreditoCliente(idCliente, idCredito));
    }

    @GetMapping("/infoCreditoySaldo/{idCredito}")
    public ResponseEntity<?> infoCreditoySaldo(
            @PathVariable Integer idCredito) {
        return new ResponseHandler().generateResponse(
                "successful",
                HttpStatus.OK, cuotaCreditoService.infoCreditoySaldo(idCredito));
    }

    @GetMapping("/reporteInteresyCapital")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> reporteInteresyCapital(
            @RequestParam String fechaInicial,
            @RequestParam String fechaFinal
    ) {
        return new ResponseHandler().generateResponse(
                "successful",
                HttpStatus.OK, cuotaCreditoService.reporteInteresyCapital(fechaInicial, fechaFinal));
    }

    @PutMapping("/modificarFechaPago/{fechaNueva}/{idCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> modificarFechaPago(
            @PathVariable LocalDate fechaNueva, @PathVariable int idCredito) {
        return new ResponseHandler().generateResponse(
                "successful", HttpStatus.OK, cuotaCreditoService.modificarFechaPago(
                        fechaNueva, idCredito));
    }
}
