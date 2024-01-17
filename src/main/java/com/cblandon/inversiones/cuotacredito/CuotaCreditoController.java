package com.cblandon.inversiones.cuotacredito;

import com.cblandon.inversiones.cuotacredito.dto.PagarCuotaRequestDTO;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.ResponseHandler;
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
    public ResponseEntity<Object> pagarCuota(
            @PathVariable Integer idCuotaCredito, @RequestBody PagarCuotaRequestDTO pagarCuotaRequestDTO) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, cuotaCreditoService.pagarCuota(
                        idCuotaCredito, pagarCuotaRequestDTO));
    }

    @GetMapping("/infoCuotaCreditoCliente/{idCliente}/{idCredito}")
    public ResponseEntity<Object> infoCuotaCreditoCliente(
            @PathVariable Integer idCliente, @PathVariable Integer idCredito) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL,
                HttpStatus.OK, cuotaCreditoService.infoCuotaCreditoCliente(idCliente, idCredito));
    }

    @GetMapping("/infoCreditoySaldo/{idCredito}")
    public ResponseEntity<Object> infoCreditoySaldo(
            @PathVariable Integer idCredito) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL,
                HttpStatus.OK, cuotaCreditoService.infoCreditoySaldo(idCredito));
    }

    @GetMapping("/reporteInteresyCapital")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> reporteInteresyCapital(
            @RequestParam String fechaInicial,
            @RequestParam String fechaFinal
    ) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL,
                HttpStatus.OK, cuotaCreditoService.reporteInteresyCapital(fechaInicial, fechaFinal));
    }

    @PutMapping("/modificarFechaPago/{fechaNueva}/{idCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> modificarFechaPago(
            @PathVariable LocalDate fechaNueva, @PathVariable int idCredito) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, cuotaCreditoService.modificarFechaPago(
                        fechaNueva, idCredito));
    }

    @GetMapping("/consultarAbonosRealizados/{idCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> consultarAbonosRealizados(
            @PathVariable int idCredito) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, cuotaCreditoService.consultarAbonosRealizados(idCredito));
    }
}
