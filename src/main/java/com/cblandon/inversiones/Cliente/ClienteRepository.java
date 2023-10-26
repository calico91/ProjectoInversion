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

    @Query(value = "SELECT cl.nombres,cl.apellidos,cl.cedula ,cr.fecha_credito, ccr.*" +
            "            FROM apirest.credito cr" +
            "            INNER JOIN apirest.cliente cl ON cr.id_cliente = cl.id_cliente " +
            "            INNER JOIN   apirest.cuota_credito ccr ON cr.id_credito= ccr.id_credito" +
            "            WHERE cl.id_cliente=:id AND cr.estado_credito='A' order by id_cuota_credito desc limit 1;",
            nativeQuery = true)
    Tuple infoClienteCuotaCredito(@Param("id") Integer id);


}
