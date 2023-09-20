package com.cblandon.inversiones.CuotasCredito;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Credito.Credito;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CuotaCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ("idcuotacredito"))
    private Integer id;
    @Column(name = "valorcuota", nullable = false)
    private Double valorCuota;

    @Column(name = "fechacuota", nullable = false)
    private Date fechaCuota;
    @Column(name = "numerocuota")
    private Integer numeroCuota;
    @Column(name = "cantidadabonado")
    private Double cantidadAbonado;

    @Column(name = "valorcapital")
    private Double valorCapital;

    @Column(name = "valorinteres")
    private Double valorInteres;

    @Column(name = "fechaabono")
    private Date fechaAbono;

    @ManyToOne(targetEntity = Credito.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "idcredito")
    private Credito credito;

    @PrePersist
    public void prePersit() {
        this.fechaCuota = new Date();
    }

    /*@PreUpdate
    public void preUpdate() {
        this.fechamodificacion = new Date();
    }*/


}