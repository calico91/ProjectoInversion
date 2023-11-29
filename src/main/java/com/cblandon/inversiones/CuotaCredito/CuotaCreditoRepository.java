package com.cblandon.inversiones.CuotaCredito;

import com.cblandon.inversiones.Credito.Credito;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CuotaCreditoRepository extends JpaRepository<CuotaCredito, Integer> {

    @Query(value = "     SELECT ccr.*" +
            "            FROM apirest.credito cr" +
            "            INNER JOIN apirest.cliente cl ON cr.id_cliente = cl.id_cliente " +
            "            INNER JOIN   apirest.cuota_credito ccr ON cr.id_credito= ccr.id_credito" +
            "            WHERE cl.id_cliente=:idCliente AND cr.estado_credito='A' " +
            "            AND cr.id_credito=:idCredito ORDER BY id_cuota_credito desc limit 1",
            nativeQuery = true)
    CuotaCredito infoCuotaCreditoCliente(
            @Param("idCliente") Integer idCliente, @Param("idCredito") Integer idCredito);

    @Query(value = "     SELECT cr.fecha_credito, ccr.* " +
            "   FROM apirest.cuota_credito ccr " +
            "   INNER JOIN apirest.credito cr ON cr.id_credito = ccr.id_credito " +
            "   WHERE ccr.id_credito=:idCredito ORDER BY id_cuota_credito desc",
            nativeQuery = true)
    List<Tuple> infoCuotasPagadas(@Param("idCredito") Integer idCredito);

    List<CuotaCredito> findByCreditoEqualsOrderByIdDesc(Credito idCredito);


  /*  SELECT  ccr.*
    FROM apirest.cuota_credito ccr
    where ccr.fecha_abono IS NOT NULL
    AND ccr.fecha_cuota BETWEEN '2023-11-01' AND '2023-11-30'
    cambiar por esta consulta
    */

    @Query(value = "SELECT  ccr.* " +
            "FROM apirest.cuota_credito ccr " +
            "WHERE ccr.fecha_abono IS NOT NULL AND MONTH (ccr.fecha_cuota)=:mes",
            nativeQuery = true)
    List<CuotaCredito> infoInteresYCapitalMes(@Param("mes") Integer mes);
}
