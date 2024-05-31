package com.cblandon.inversiones.cuota_credito.controller;

import com.cblandon.inversiones.cuota_credito.servicio.CuotaCreditoService;
import com.cblandon.inversiones.cuota_credito.dto.PagarCuotaRequestDTO;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cuotaCredito")
public class CuotaCreditoController {


    private final CuotaCreditoService cuotaCreditoService;


    @PutMapping("/pagar-cuota/{idCuotaCredito}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COBRADOR')")
    public ResponseEntity<GenericResponseDTO> pagarCuota(
            @PathVariable Integer idCuotaCredito, @RequestBody @Valid PagarCuotaRequestDTO pagarCuotaRequestDTO) {
        return GenericResponseDTO.genericResponse(cuotaCreditoService.pagarCuota(idCuotaCredito, pagarCuotaRequestDTO));
    }

    @GetMapping("/consultar-cuota-cliente/{idCliente}/{idCredito}")
    @PreAuthorize("hasAnyRole('ADMIN','COBRADOR')")
    public ResponseEntity<GenericResponseDTO> consultarCuotaCliente(
            @PathVariable Integer idCliente, @PathVariable Integer idCredito) {
        return GenericResponseDTO.genericResponse(
                cuotaCreditoService.consultarCuotaCliente(idCliente, idCredito));
    }

    @GetMapping("/consultar-credito-saldo/{idCredito}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> consultarCreditoSaldo(
            @PathVariable Integer idCredito) {
        return GenericResponseDTO.genericResponse(cuotaCreditoService.consultarCreditoSaldo(idCredito));
    }

    @GetMapping("/generar-reporte-interes-capital")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> generarReporteInteresCapital(
            @RequestParam String fechaInicial,
            @RequestParam String fechaFinal
    ) {
        return GenericResponseDTO.genericResponse(cuotaCreditoService.generarReporteInteresCapital(fechaInicial, fechaFinal));
    }

    @PutMapping("/modificar-fecha-pago/{fechaNueva}/{idCredito}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> modificarFechaPago(@PathVariable LocalDate fechaNueva, @PathVariable int idCredito) {
        return GenericResponseDTO.genericResponse(cuotaCreditoService.modificarFechaPago(fechaNueva, idCredito));
    }

    @GetMapping("/consultar-abonos-realizados/{idCredito}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> consultarAbonosRealizados(@PathVariable int idCredito) {
        return GenericResponseDTO.genericResponse(cuotaCreditoService.consultarAbonosRealizados(idCredito));
    }

    @GetMapping("/consultar-ultimos-abonos-realizados/{cantidadAbonos}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> consultarUltimosAbonosRealizados(@PathVariable int cantidadAbonos) {

        try {
            return GenericResponseDTO.genericResponse(
                    cuotaCreditoService.consultarUltimosAbonosRealizados(cantidadAbonos));

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }
    @GetMapping("/consultar-abono-por-id/{idCuotaCredito}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<GenericResponseDTO> consultarAbonoPorId(@PathVariable int idCuotaCredito) {

        try {
            return GenericResponseDTO.genericResponse(cuotaCreditoService.consultarAbonoPorId(idCuotaCredito));

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
