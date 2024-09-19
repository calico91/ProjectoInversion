package com.cblandon.inversiones.permiso.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
