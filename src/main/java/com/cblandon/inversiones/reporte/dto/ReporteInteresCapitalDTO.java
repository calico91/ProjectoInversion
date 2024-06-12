package com.cblandon.inversiones.reporte.dto;

public record ReporteInteresCapitalDTO(Double valorCapital, Double valorInteres) {

    public ReporteInteresCapitalDTO {
        valorCapital = valorCapital == null ? 0 : valorCapital;
        valorInteres = valorInteres == null ? 0 : valorInteres;
    }
}

