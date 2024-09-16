package com.cblandon.inversiones.reporte.controller;

import com.cblandon.inversiones.reporte.service.ReporteService;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reporte")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/generar-reporte-interes-capital")
    @PreAuthorize("hasAnyRole('SUPER')")
    public ResponseEntity<GenericResponseDTO> generarReporteInteresCapital(
            @RequestParam String fechaInicial,
            @RequestParam String fechaFinal
    ) {
        return GenericResponseDTO.genericResponse(reporteService.generarReporteInteresCapital(fechaInicial, fechaFinal));
    }

    @GetMapping("/generar-reporte-ultimos-abonos-realizados/{cantidadAbonos}")
    @PreAuthorize("hasAnyRole('SUPER')")
    public ResponseEntity<GenericResponseDTO> consultarUltimosAbonosRealizados(@PathVariable int cantidadAbonos) {
        return GenericResponseDTO.genericResponse(
                reporteService.consultarUltimosAbonosRealizados(cantidadAbonos));
    }
}
