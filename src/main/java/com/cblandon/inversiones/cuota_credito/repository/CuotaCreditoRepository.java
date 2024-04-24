package com.cblandon.inversiones.cuota_credito.repository;

import com.cblandon.inversiones.cuota_credito.dto.AbonoPorIdDTO;
import com.cblandon.inversiones.cuota_credito.entity.CuotaCredito;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CuotaCreditoRepository extends JpaRepository<CuotaCredito, Integer> {

    @Query(value = "     SELECT ccr.id_cuota_credito, ccr.valor_cuota, ccr.fecha_cuota, ccr.numero_cuotas, " +
            "            ccr.valor_capital, ccr.valor_interes,ccr.interes_porcentaje, " +
            "            ccr.couta_numero, cr.valor_credito" +
            "            FROM apirest.credito cr" +
            "            INNER JOIN apirest.cliente cl ON cr.id_cliente = cl.id_cliente " +
            "            INNER JOIN   apirest.cuota_credito ccr ON cr.id_credito= ccr.id_credito" +
            "            WHERE cl.id_cliente=:idCliente AND cr.id_estado_credito=1 " +
            "            AND cr.id_credito=:idCredito ORDER BY id_cuota_credito desc limit 1",
            nativeQuery = true)
    Tuple consultarCuotaCreditoCliente(
            @Param("idCliente") Integer idCliente, @Param("idCredito") Integer idCredito);

    @Query(value = "    SELECT cr.saldo_credito,cr.valor_credito, cr.fecha_credito, m.description AS modalidad," +
            "           ccr.id_cuota_credito,ccr.couta_numero, ccr.fecha_cuota,ccr.interes_porcentaje,ccr.numero_cuotas," +
            "           ccr.valor_cuota,ccr.valor_interes,ccr.tipo_abono,ccr.abono_extra" +
            "           FROM apirest.cuota_credito ccr " +
            "           INNER JOIN apirest.credito cr ON cr.id_credito = ccr.id_credito " +
            "           INNER JOIN apirest.modalidad m ON m.id_modalidad = cr.id_modalidad" +
            "           WHERE ccr.id_credito=:idCredito ORDER BY id_cuota_credito DESC",
            nativeQuery = true)
    List<Tuple> consultarInfoCreditoySaldo(@Param("idCredito") Integer idCredito);

    @Query(value = "SELECT  sum(valor_capital) as valorCapital, sum(valor_interes) as valorInteres " +
            "    FROM apirest.cuota_credito ccr" +
            "    where ccr.fecha_abono IS NOT NULL" +
            "    AND ccr.fecha_abono BETWEEN :fechaInicial AND :fechaFinal",
            nativeQuery = true)
    Tuple generarReporteInteresyCapital(@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal);

    @Query(value = "SELECT * FROM apirest.cuota_credito " +
            "WHERE id_credito=:idCredito ORDER BY id_cuota_credito DESC LIMIT 1",
            nativeQuery = true)
    CuotaCredito consultarUltimaCuotaGenerada(@Param("idCredito") int idCredito);

    @Query(value = "SELECT ccr.valor_abonado, ccr.fecha_abono, ccr.tipo_abono, ccr.couta_numero, ccr.id_cuota_credito" +
            "       FROM apirest.cuota_credito as ccr WHERE id_credito = :idCredito ORDER BY fecha_abono DESC",
            nativeQuery = true)
    List<Tuple> consultarAbonosRealizadosPorCredito(@Param("idCredito") int idCredito);

    @Query(value = "SELECT cl.nombres, cl.apellidos, ccr.valor_abonado, ccr.fecha_abono" +
            "       FROM apirest.cuota_credito ccr" +
            "       INNER JOIN apirest.credito cr using(id_credito) " +
            "       INNER JOIN apirest.cliente cl using(id_cliente)" +
            "       WHERE valor_abonado IS NOT NULL " +
            "       ORDER BY fecha_abono DESC LIMIT :cantidadAbonos", nativeQuery = true)
    List<Tuple> consultarUltimosAbonosRealizados(@Param("cantidadAbonos") int cantidadAbonos);

    @Query(value = "SELECT  new com.cblandon.inversiones.cuota_credito.dto.AbonoPorIdDTO(ccr.id,ccr.valorAbonado," +
            "ccr.fechaAbono,ccr.tipoAbono,ccr.numeroCuotas, ccr.cuotaNumero )" +
            "FROM CuotaCredito ccr " +
            "WHERE ccr.id = :idCuotaCredito")
    AbonoPorIdDTO consultarAbonoPorId(@Param("idCuotaCredito") int idCuotaCredito);

}
