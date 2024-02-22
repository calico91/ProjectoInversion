package com.cblandon.inversiones.roles;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByName(Role name);
}
