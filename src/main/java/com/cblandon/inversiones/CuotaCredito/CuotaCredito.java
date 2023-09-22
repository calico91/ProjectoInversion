package com.cblandon.inversiones.CuotaCredito;

import com.cblandon.inversiones.Credito.Credito;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

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
    @Column(name = "valor_abonado")
    private Double valorAbonado;

    @Column(name = "valor_capital", nullable = false)
    private Double valorCapital;

    @Column(name = "valor_interes", nullable = false)
    private Double valorInteres;

    @Column(name = "fecha_abono")
    private LocalDate fechaAbono;

    @ManyToOne(targetEntity = Credito.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_credito")
    private Credito credito;

    /*@PreUpdate
    public void preUpdate() {
        this.fechamodificacion = new Date();
    }*/


}