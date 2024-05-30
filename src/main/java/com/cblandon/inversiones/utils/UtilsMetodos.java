package com.cblandon.inversiones.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class UtilsMetodos {

    public static String obtenerUsuarioLogueado() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName()).orElse("test");
    }

    public static int calcularCuotasPagadas(double valorCredito, double saldoCredito, int numeroCuotas) {
        double cuotasPagadas = ((valorCredito - saldoCredito) / (valorCredito / numeroCuotas));
        return (int) Math.floor(cuotasPagadas);
    }


}
