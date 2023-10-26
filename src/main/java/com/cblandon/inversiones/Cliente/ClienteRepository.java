package com.cblandon.inversiones.Cliente;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Cliente findByCedula(String cedula);

    Cliente deleteByCedula(String cedula);

    @Query(value = "SELECT cl.* " +
            "FROM apirest.credito cr " +
            "INNER JOIN apirest.cliente cl ON cr.id_cliente = cl.id_cliente AND cr.estado_credito='A'", nativeQuery = true)
    List<Cliente> clientesCreditosActivos();


    @Query(value = "SELECT cl.id_cliente, cl.nombres,cl.apellidos,cl.cedula ,cr.fecha_credito,ccr.valor_credito, " +
            "            ccr.fecha_abono, ccr.fecha_cuota,cr.id_credito" +
            "            FROM apirest.credito cr" +
            "            INNER JOIN apirest.cliente cl ON cr.id_cliente = cl.id_cliente " +
            "            INNER JOIN   apirest.cuota_credito ccr ON cr.id_credito= ccr.id_credito" +
            "            WHERE cr.estado_credito='A' AND ccr.fecha_abono IS NULL " +
            "            AND fecha_cuota <= CURRENT_DATE() ORDER BY fecha_cuota ASC",
            nativeQuery = true)
    List<Tuple> infoClientesCuotasPendientes();


}
