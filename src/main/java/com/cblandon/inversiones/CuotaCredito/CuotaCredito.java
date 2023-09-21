package com.cblandon.inversiones.CuotaCredito;

import com.cblandon.inversiones.Credito.Credito;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

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
    @Column(name = "cantidad_abonado")
    private Double cantidadAbonado;

    @Column(name = "valor_capital", nullable = false)
    private Double valorCapital;

    @Column(name = "valor_interes", nullable = false)
    private Double valorInteres;

    @Column(name = "fecha_abono")
    private Date fechaAbono;

    @ManyToOne(targetEntity = Credito.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_credito")
    private Credito credito;

    /*@PreUpdate
    public void preUpdate() {
        this.fechamodificacion = new Date();
    }*/


}