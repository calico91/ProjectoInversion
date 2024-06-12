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
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(10))")
    public ResponseEntity<GenericResponseDTO> pagarCuota(
            @PathVariable Integer idCuotaCredito, @RequestBody @Valid PagarCuotaRequestDTO pagarCuotaRequestDTO) {
        return GenericResponseDTO.genericResponse(cuotaCreditoService.pagarCuota(idCuotaCredito, pagarCuotaRequestDTO));
    }

    @GetMapping("/consultar-cuota-cliente/{idCliente}/{idCredito}")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(11))")
    public ResponseEntity<GenericResponseDTO> consultarCuotaCliente(
            @PathVariable Integer idCliente, @PathVariable Integer idCredito) {
        return GenericResponseDTO.genericResponse(
                cuotaCreditoService.consultarCuotaCliente(idCliente, idCredito));
    }

    @GetMapping("/consultar-credito-saldo/{idCredito}")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(12))")
    public ResponseEntity<GenericResponseDTO> consultarCreditoSaldo(
            @PathVariable Integer idCredito) {
        return GenericResponseDTO.genericResponse(cuotaCreditoService.consultarCreditoSaldo(idCredito));
    }

    @PutMapping("/modificar-fecha-pago/{fechaNueva}/{idCredito}")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(13))")
    public ResponseEntity<GenericResponseDTO> modificarFechaPago(@PathVariable LocalDate fechaNueva, @PathVariable int idCredito) {
        return GenericResponseDTO.genericResponse(cuotaCreditoService.modificarFechaPago(fechaNueva, idCredito));
    }

    @GetMapping("/consultar-abonos-realizados/{idCredito}")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(15))")
    public ResponseEntity<GenericResponseDTO> consultarAbonosRealizados(@PathVariable int idCredito) {
        return GenericResponseDTO.genericResponse(cuotaCreditoService.consultarAbonosRealizados(idCredito));
    }

    @GetMapping("/consultar-ultimos-abonos-realizados/{cantidadAbonos}")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(16))")
    public ResponseEntity<GenericResponseDTO> consultarUltimosAbonosRealizados(@PathVariable int cantidadAbonos) {
        return GenericResponseDTO.genericResponse(
                cuotaCreditoService.consultarUltimosAbonosRealizados(cantidadAbonos));
    }

    @GetMapping("/consultar-abono-por-id/{idCuotaCredito}")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(17))")
    public ResponseEntity<GenericResponseDTO> consultarAbonoPorId(@PathVariable int idCuotaCredito) {
        return GenericResponseDTO.genericResponse(cuotaCreditoService.consultarAbonoPorId(idCuotaCredito));
    }

    @PutMapping("/anular-ultimo-abono/{idAbono}/{idCredito}")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(18))")
    public ResponseEntity<GenericResponseDTO> anularUltimoAbono(
            @PathVariable int idAbono, @PathVariable int idCredito) {
        return GenericResponseDTO.genericResponse(cuotaCreditoService.anularUltimoAbono(idAbono, idCredito));
    }

}
