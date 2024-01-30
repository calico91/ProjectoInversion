package com.cblandon.inversiones.credito;

import java.time.LocalDate;
import java.util.List;

import com.cblandon.inversiones.cliente.Cliente;
import com.cblandon.inversiones.cuotacredito.CuotaCredito;
import com.cblandon.inversiones.estado_credito.EstadoCredito;
import com.cblandon.inversiones.modalidad.Modalidad;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
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

    @Column(nullable = false, name = "usuario_creador_credito", length = 20)
    private String usuarioCreador;


    @Column(nullable = false,length = 10)
    private Double valorCredito;


    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_credito", nullable = false, length = 10)
    private LocalDate fechaCredito;


    @Column(name = "saldo_credito", nullable = false, length = 10)
    private Double saldoCredito;

    @ManyToOne(targetEntity = Cliente.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(targetEntity = CuotaCredito.class, fetch = FetchType.LAZY, mappedBy = "credito")
    private List<CuotaCredito> listaCuotasCredito;

    @ManyToOne
    @JoinColumn(name = "id_modalidad")
    private Modalidad modalidad;

    @ManyToOne
    @JoinColumn(name = "id_estado_credito")
    private EstadoCredito idEstadoCredito;

    @PrePersist
    public void prePersit() {
        this.fechaCredito = this.fechaCredito == null ? LocalDate.now() : this.fechaCredito;
    }
}
