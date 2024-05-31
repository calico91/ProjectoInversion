package com.cblandon.inversiones.roles.repository;

import com.cblandon.inversiones.roles.Role;
import com.cblandon.inversiones.roles.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Integer> {

    Optional<Roles> findByName(Role name);
}
