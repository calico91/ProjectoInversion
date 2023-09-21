package com.cblandon.inversiones.Credito;

import java.util.Date;
import java.util.List;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.CuotaCredito.CuotaCredito;
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
    @Column(name = "id_credito")
    private Integer id;
    @Column(nullable = false, name = "cantidad_prestada")
    private Double cantidadPrestada;

    @Column(nullable = false, name = "interes_porcentaje")
    private Double interesPorcentaje;

    @Column(nullable = false, name = "cantidad_cuotas")
    private Integer cantidadCuotas;

    private Double saldo;

    @Column(nullable = false, name = "usuario_creador_credito")
    private String usuarioCreador;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_credito", nullable = false)
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
