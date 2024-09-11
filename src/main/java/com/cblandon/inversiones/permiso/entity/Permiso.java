package com.cblandon.inversiones.permiso.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "permisos")
@Entity
public class Permiso {
    @Id
    private Integer id;

    @Column(length = 50)
    private String descripcion;
    @Column(length = 50)
    private String endpoint;
}
