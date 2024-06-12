package com.cblandon.inversiones.cuota_credito.repository;

import com.cblandon.inversiones.cuota_credito.dto.AbonoPorIdDTO;
import com.cblandon.inversiones.cuota_credito.dto.IdAbonosRealizadosDTO;
import com.cblandon.inversiones.cuota_credito.entity.CuotaCredito;
import com.cblandon.inversiones.reporte.dto.ReporteInteresCapitalDTO;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CuotaCreditoRepository extends JpaRepository<CuotaCredito, Integer> {

    @Query(value = "     SELECT ccr.id_cuota_credito, ccr.valor_cuota, ccr.fecha_cuota, ccr.numero_cuotas, " +
            "            ccr.valor_capital, ccr.valor_interes,ccr.interes_porcentaje, " +
            "            ccr.couta_numero, cr.valor_credito, m.description" +
            "            FROM credito cr" +
            "            INNER JOIN cliente cl ON cr.id_cliente = cl.id_cliente " +
            "            INNER JOIN  cuota_credito ccr ON cr.id_credito= ccr.id_credito" +
            "            INNER JOIN  modalidad m ON cr.id_modalidad= m.id_modalidad" +
            "            WHERE cl.id_cliente=:idCliente AND cr.id_estado_credito=1 " +
            "            AND cr.id_credito=:idCredito ORDER BY id_cuota_credito desc limit 1",
            nativeQuery = true)
    Tuple consultarCuotaCreditoCliente(
            @Param("idCliente") Integer idCliente, @Param("idCredito") Integer idCredito);

    @Query(value = "    SELECT cr.saldo_credito,cr.valor_credito, cr.fecha_credito, m.description AS modalidad," +
            "           ccr.id_cuota_credito,ccr.couta_numero, ccr.fecha_cuota,ccr.interes_porcentaje,ccr.numero_cuotas," +
            "           ccr.valor_cuota,ccr.valor_interes,ccr.tipo_abono,ccr.abono_extra" +
            "           FROM cuota_credito ccr " +
            "           INNER JOIN credito cr ON cr.id_credito = ccr.id_credito " +
            "           INNER JOIN modalidad m ON m.id_modalidad = cr.id_modalidad" +
            "           WHERE ccr.id_credito=:idCredito ORDER BY id_cuota_credito DESC",
            nativeQuery = true)
    List<Tuple> consultarInfoCreditoySaldo(@Param("idCredito") Integer idCredito);

    @Query(value = "SELECT  new com.cblandon.inversiones.reporte.dto.ReporteInteresCapitalDTO(" +
            "sum(ccr.valorCapital),sum(ccr.valorInteres)) " +
            "    FROM CuotaCredito ccr" +
            "    where ccr.fechaAbono IS NOT NULL" +
            "    AND ccr.fechaAbono BETWEEN :fechaInicial AND :fechaFinal")
    ReporteInteresCapitalDTO generarReporteInteresyCapital(
            @Param("fechaInicial") LocalDateTime fechaInicial, @Param("fechaFinal") LocalDateTime fechaFinal);

    @Query(value = "SELECT * FROM cuota_credito " +
            "WHERE id_credito=:idCredito ORDER BY id_cuota_credito DESC LIMIT 1",
            nativeQuery = true)
    CuotaCredito consultarUltimaCuotaGenerada(@Param("idCredito") int idCredito);

    @Query(value = "SELECT ccr.valor_abonado, ccr.fecha_abono, ccr.tipo_abono, ccr.couta_numero, ccr.id_cuota_credito" +
            "       FROM cuota_credito as ccr WHERE id_credito = :idCredito ORDER BY fecha_abono DESC",
            nativeQuery = true)
    List<Tuple> consultarAbonosRealizadosPorCredito(@Param("idCredito") int idCredito);


    @Query(value = "SELECT  new com.cblandon.inversiones.cuota_credito.dto.AbonoPorIdDTO(ccr.id,ccr.valorAbonado," +
            "ccr.fechaAbono,ccr.tipoAbono,ccr.numeroCuotas, ccr.cuotaNumero )" +
            "FROM CuotaCredito ccr " +
            "WHERE ccr.id = :idCuotaCredito")
    AbonoPorIdDTO consultarAbonoPorId(@Param("idCuotaCredito") int idCuotaCredito);

    @Query(value = "SELECT cl.nombres, cl.apellidos, ccr.valor_abonado, ccr.fecha_abono" +
            "       FROM cuota_credito ccr" +
            "       INNER JOIN credito cr using(id_credito) " +
            "       INNER JOIN cliente cl using(id_cliente)" +
            "       WHERE valor_abonado IS NOT NULL " +
            "       ORDER BY fecha_abono DESC LIMIT :cantidadAbonos", nativeQuery = true)
    List<Tuple> consultarUltimosAbonosRealizados(@Param("cantidadAbonos") int cantidadAbonos);

    @Query(value = "SELECT  new com.cblandon.inversiones.cuota_credito.dto.IdAbonosRealizadosDTO(ccr.id)" +
            "FROM CuotaCredito ccr  WHERE ccr.credito.id = :idCredito ORDER BY ccr.id DESC")
    List<IdAbonosRealizadosDTO> consultarIdAbonosRealizadosPorCredito(@Param("idCredito") int idCredito);


}
