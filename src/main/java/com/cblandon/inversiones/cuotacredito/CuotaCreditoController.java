package com.cblandon.inversiones.cuotacredito;

import com.cblandon.inversiones.cuotacredito.dto.PagarCuotaRequestDTO;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.ResponseHandler;
import jakarta.validation.Valid;
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
            @PathVariable Integer idCuotaCredito, @RequestBody @Valid PagarCuotaRequestDTO pagarCuotaRequestDTO) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, cuotaCreditoService.pagarCuota(
                        idCuotaCredito, pagarCuotaRequestDTO));
    }

    @GetMapping("/infoCuotaCreditoCliente/{idCliente}/{idCredito}")
    public ResponseEntity<Object> infoCuotaCreditoCliente(
            @PathVariable Integer idCliente, @PathVariable Integer idCredito) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL,
                HttpStatus.OK, cuotaCreditoService.consultarInfoCuotaCreditoCliente(idCliente, idCredito));
    }

    @GetMapping("/infoCreditoySaldo/{idCredito}")
    public ResponseEntity<Object> infoCreditoySaldo(
            @PathVariable Integer idCredito) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL,
                HttpStatus.OK, cuotaCreditoService.consultarInfoCreditoySaldo(idCredito));
    }

    @GetMapping("/reporteInteresyCapital")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> reporteInteresyCapital(
            @RequestParam String fechaInicial,
            @RequestParam String fechaFinal
    ) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL,
                HttpStatus.OK, cuotaCreditoService.generarReporteInteresyCapital(fechaInicial, fechaFinal));
    }

    @PutMapping("/modificarFechaPago/{fechaNueva}/{idCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> modificarFechaPago(@PathVariable LocalDate fechaNueva, @PathVariable int idCredito) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, cuotaCreditoService.modificarFechaPago(
                        fechaNueva, idCredito));
    }

    @GetMapping("/consultarAbonosRealizados/{idCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> consultarAbonosRealizados(@PathVariable int idCredito) {
        return new ResponseHandler().generateResponse(
                Constantes.SUCCESSFUL, HttpStatus.OK, cuotaCreditoService.consultarAbonosRealizados(idCredito));
    }

    @GetMapping("/consultarUltimosAbonosRealizados/{cantidadAbonos}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> consultarUltimosAbonosRealizados(@PathVariable int cantidadAbonos) {

        try {
            return new ResponseHandler().generateResponse(
                    Constantes.SUCCESSFUL,
                    HttpStatus.OK, cuotaCreditoService.consultarUltimosAbonosRealizados(cantidadAbonos));

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    @GetMapping("/consultarAbonoPorId/{idCuotaCredito}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> consultarAbonoPorId(@PathVariable int idCuotaCredito) {

        try {
            return new ResponseHandler().generateResponse(
                    Constantes.SUCCESSFUL,
                    HttpStatus.OK, cuotaCreditoService.consultarAbonoPorId(idCuotaCredito));

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
