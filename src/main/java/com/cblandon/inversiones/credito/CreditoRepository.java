package com.cblandon.inversiones.credito;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditoRepository extends JpaRepository<Credito, Integer> {

    List<Credito> findByEstadoCreditoEquals(String estadoCredito);

    @Query(value = "SELECT cl.id_cliente,cl.nombres,cl.apellidos,cl.cedula,cr.id_credito, " +
            "cr.fecha_credito,cr.valor_credito " +
            "FROM apirest.credito cr " +
            "INNER JOIN apirest.cliente cl ON cr.id_cliente = cl.id_cliente " +
            "AND cr.estado_credito='A' ORDER BY cr.id_credito DESC ;", nativeQuery = true)
    List<Tuple> infoClientesConCreditosActivos();

}
