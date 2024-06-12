package com.cblandon.inversiones.reporte.controller;

import com.cblandon.inversiones.reporte.service.ReporteService;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reporte")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/generar-reporte-interes-capital")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(14))")
    public ResponseEntity<GenericResponseDTO> generarReporteInteresCapital(
            @RequestParam String fechaInicial,
            @RequestParam String fechaFinal
    ) {
        return GenericResponseDTO.genericResponse(reporteService.generarReporteInteresCapital(fechaInicial, fechaFinal));
    }
}
