package com.cblandon.inversiones.reporte.dto;

public record ReporteInteresCapital(Double valorCapital, Double valorInteres) {

    public ReporteInteresCapital {
        valorCapital = valorCapital == null ? 0 : valorCapital;
        valorInteres = valorInteres == null ? 0 : valorInteres;
    }
}
