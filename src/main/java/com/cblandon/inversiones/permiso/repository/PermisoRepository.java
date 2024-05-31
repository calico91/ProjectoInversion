package com.cblandon.inversiones.permiso.repository;

import com.cblandon.inversiones.permiso.entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {

}
