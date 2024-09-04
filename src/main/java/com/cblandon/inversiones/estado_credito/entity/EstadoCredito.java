package com.cblandon.inversiones.estado_credito.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class EstadoCredito {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_estado_credito")
    private Integer id;
    @Column(length = 20)
    private String descripcion;

    public EstadoCredito(Integer id) {
        this.id = id;
    }
}