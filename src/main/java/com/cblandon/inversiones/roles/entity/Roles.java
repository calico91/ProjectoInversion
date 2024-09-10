package com.cblandon.inversiones.roles.entity;


import com.cblandon.inversiones.permiso.entity.Permiso;
import com.cblandon.inversiones.roles.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "roles_permisos",
            joinColumns = @JoinColumn(
                    name = "rol_id"), inverseJoinColumns = @JoinColumn(name = "permiso_id"))
    @ToString.Exclude
    Set<Permiso> permisos;


}