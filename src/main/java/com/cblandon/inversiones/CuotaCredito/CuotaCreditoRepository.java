package com.cblandon.inversiones.CuotaCredito;

import com.cblandon.inversiones.Credito.Credito;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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

    @Query(value = "     SELECT cr.fecha_credito,cr.modalidad, ccr.* " +
            "   FROM apirest.cuota_credito ccr " +
            "   INNER JOIN apirest.credito cr ON cr.id_credito = ccr.id_credito " +
            "   WHERE ccr.id_credito=:idCredito ORDER BY id_cuota_credito desc",
            nativeQuery = true)
    List<Tuple> infoCuotasPagadas(@Param("idCredito") Integer idCredito);

    List<CuotaCredito> findByCreditoEqualsOrderByIdDesc(Credito idCredito);

    @Query(value = "SELECT  ccr.*" +
            "    FROM apirest.cuota_credito ccr" +
            "    where ccr.fecha_abono IS NOT NULL" +
            "    AND ccr.fecha_abono BETWEEN :fechaInicial AND :fechaFinal",
            nativeQuery = true)
    List<CuotaCredito> reporteInteresyCapital(@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal);

    @Query(value = "SELECT * FROM apirest.cuota_credito " +
            "WHERE id_credito=:idCredito ORDER BY id_cuota_credito DESC LIMIT 1",
            nativeQuery = true)
    CuotaCredito ultimaCuotaGenerada(@Param("idCredito") int idCredito);
}
