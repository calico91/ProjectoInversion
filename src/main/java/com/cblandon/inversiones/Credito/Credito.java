package com.cblandon.inversiones.Credito;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.CuotasCredito.CuotaCredito;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credito")
public class Credito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcredito")
    private Integer id;
    @Column(nullable = false, name = "cantidadprestada")
    private Double cantidadPrestada;

    @Column(nullable = false, name = "interesporcentaje")
    private Double interesPorcentaje;

    @Column(nullable = false, name = "valorcuota")
    private Double valorCuota;

    @Column(nullable = false, name = "cantidadcuotas")
    private Integer cantidadCuotas;

    @Column(nullable = false)
    private Double saldo;

    @Column(nullable = false, name = "cuotacapital")
    private Double cuotaCapital;

    @Column(nullable = false, name = "interescredito")
    private Double interesCredito;

    @Column(nullable = false, name = "usuariocreadorCredito")
    private String usuarioCreador;

    @Temporal(TemporalType.DATE)
    @Column(name = "fechacredito", nullable = false)
    private Date fechaCredito;

    @ManyToOne(targetEntity = Cliente.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(targetEntity = CuotaCredito.class, fetch = FetchType.LAZY, mappedBy = "credito")
    private List<CuotaCredito> listaCuotasCredito;

    @PrePersist
    public void prePersit() {
        this.fechaCredito = new Date();
    }
}
