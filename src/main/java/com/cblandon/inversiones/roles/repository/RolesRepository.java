package com.cblandon.inversiones.roles.repository;

import com.cblandon.inversiones.roles.Role;
import com.cblandon.inversiones.roles.dto.RolesDTO;
import com.cblandon.inversiones.roles.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface RolesRepository extends JpaRepository<Roles, Integer> {

    Optional<Roles> findByName(Role name);

    @Query(value = "SELECT r FROM Roles r JOIN r.permisos m ON m.id = :id ")
    Set<Roles> consultarPermisos(Integer id);

    @Query(value = "SELECT new com.cblandon.inversiones.roles.dto.RolesDTO(r.id, r.name)  " +
            "FROM Roles r WHERE r.name <> 'SUPER'")
    Set<RolesDTO> consultarRoles();

    @Query(value = "SELECT r  FROM Roles r WHERE r.name <> 'SUPER' AND r.id = :id")
    Optional <Roles> consultarPermisosRol(Integer id);

}
