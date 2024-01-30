package com.cblandon.inversiones.estado_credito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoCreditoRepository extends JpaRepository<EstadoCredito, Integer> {
}
