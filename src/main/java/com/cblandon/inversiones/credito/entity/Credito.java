package com.cblandon.inversiones.credito.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.cblandon.inversiones.cliente.entity.Cliente;
import com.cblandon.inversiones.cuota_credito.entity.CuotaCredito;
import com.cblandon.inversiones.estado_credito.entity.EstadoCredito;
import com.cblandon.inversiones.modalidad.entity.Modalidad;
import com.cblandon.inversiones.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "credito")
public class Credito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_credito")
    private Integer id;

    @Column(nullable = false, name = "usuario_creador_credito", length = 20)
    private String usuarioCreador;


    @Column(nullable = false, length = 10)
    private Double valorCredito;


    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_credito", nullable = false, length = 10)
    private LocalDate fechaCredito;


    @Column(name = "saldo_credito", nullable = false, length = 10)
    private Double saldoCredito;

    @Column(length = 10)
    private Double valorRenovacion;

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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "usuario_creditos",
            joinColumns = @JoinColumn(
                    name = "credito_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    Set<UserEntity> usuarios;

    @PrePersist
    public void prePersit() {
        this.fechaCredito = this.fechaCredito == null ? LocalDate.now() : this.fechaCredito;
    }
}
