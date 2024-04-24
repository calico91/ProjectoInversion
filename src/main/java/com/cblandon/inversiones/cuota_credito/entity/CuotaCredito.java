package com.cblandon.inversiones.cuota_credito.entity;

import com.cblandon.inversiones.credito.entity.Credito;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = @Index(name = "multi_index", columnList = "fecha_cuota,fecha_abono" ))
public class CuotaCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ("id_cuota_credito"))
    private Integer id;
    @Column(name = "valor_cuota", nullable = false, length = 10)
    private Double valorCuota;
    @Column(name = "fecha_cuota", nullable = false, length = 10)
    private LocalDate fechaCuota;
    @Column(name = "numero_cuotas", length = 3)
    private Integer numeroCuotas;
    @Column(name = "couta_numero", length = 3)
    private Integer cuotaNumero;
    @Column(name = "valor_abonado", length = 10)
    private Double valorAbonado;

    @Column(name = "valor_capital", nullable = false, length = 10)
    private Double valorCapital;

    @Column(name = "valor_interes", nullable = false, length = 10)
    private Double valorInteres;

    @Column(nullable = false, name = "interes_porcentaje", length = 3)
    private Double interesPorcentaje;

    @Column(name = "fecha_abono", length = 10)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaAbono;

    @Column(name = "tipo_abono", length = 20)
    private String tipoAbono;

    @Column(name = "abono_extra", length = 2)
    private Boolean abonoExtra;

    @ManyToOne(targetEntity = Credito.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_credito")
    private Credito credito;


}