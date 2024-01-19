package com.cblandon.inversiones.cuotacredito;

import com.cblandon.inversiones.credito.Credito;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CuotaCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ("id_cuota_credito"))
    private Integer id;
    @Column(name = "valor_cuota", nullable = false)
    private Double valorCuota;
    @Column(name = "fecha_cuota", nullable = false)
    private LocalDate fechaCuota;
    @Column(name = "numero_cuotas")
    private Integer numeroCuotas;
    @Column(name = "couta_numero")
    private Integer cuotaNumero;
    @Column(name = "valor_abonado")
    private Double valorAbonado;

    @Column(name = "valor_capital", nullable = false)
    private Double valorCapital;

    @Column(name = "valor_interes", nullable = false)
    private Double valorInteres;

    @Column(name = "valor_credito", nullable = false)
    private Double valorCredito;

    @Column(nullable = false, name = "interes_porcentaje")
    private Double interesPorcentaje;

    @Column(name = "fecha_abono")
    private LocalDate fechaAbono;

    @Column(name = "tipo_abono")
    private String tipoAbono;

    @Column(name = "abono_extra")
    private Boolean abonoExtra;

    @ManyToOne(targetEntity = Credito.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_credito")
    private Credito credito;


}