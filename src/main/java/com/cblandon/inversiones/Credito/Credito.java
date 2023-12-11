package com.cblandon.inversiones.Credito;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.CuotaCredito.CuotaCredito;
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

    @Column(nullable = false, name = "usuario_creador_credito")
    private String usuarioCreador;

    @Column(nullable = false)
    private String estadoCredito;

    @Column(nullable = false)
    private Double valorCredito;


    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_credito", nullable = false)
    private LocalDate fechaCredito;

    @Column(nullable = false)
    private String modalidad;

    @ManyToOne(targetEntity = Cliente.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(targetEntity = CuotaCredito.class, fetch = FetchType.LAZY, mappedBy = "credito")
    private List<CuotaCredito> listaCuotasCredito;

    @PrePersist
    public void prePersit() {
        this.fechaCredito = this.fechaCredito == null ? LocalDate.now() : this.fechaCredito;
    }
}
