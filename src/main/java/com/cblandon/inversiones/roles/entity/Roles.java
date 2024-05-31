package com.cblandon.inversiones.roles.entity;


import com.cblandon.inversiones.permiso.entity.Permiso;
import com.cblandon.inversiones.roles.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class   Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private Role name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "roles_permisos",
            joinColumns = @JoinColumn(
                    name = "rol_id"), inverseJoinColumns = @JoinColumn(name = "permiso_id"))
    Set<Permiso> permisos;
}