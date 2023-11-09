package com.cblandon.inversiones.CuotaCredito;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CuotaCreditoRepository extends JpaRepository<CuotaCredito, Integer> {

    @Query(value = "     SELECT ccr.*" +
            "            FROM apirest.credito cr" +
            "            INNER JOIN apirest.cliente cl ON cr.id_cliente = cl.id_cliente " +
            "            INNER JOIN   apirest.cuota_credito ccr ON cr.id_credito= ccr.id_credito" +
            "            WHERE cl.id_cliente=:idCliente AND cr.estado_credito='A' " +
            "            AND cr.id_credito=:idCredito ORDER BY id_cuota_credito desc limit 1;",
            nativeQuery = true)
    CuotaCredito infoCuotaCreditoCliente(@Param("idCliente") Integer idCliente, @Param("idCredito") Integer idCredito);
}
