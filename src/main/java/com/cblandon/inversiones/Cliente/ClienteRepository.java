package com.cblandon.inversiones.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Cliente findByCedula(String cedula);
    Cliente deleteByCedula(String cedula);
    @Query(value = "SELECT cl.* " +
            "FROM apirest.credito cr " +
            "INNER JOIN apirest.cliente cl ON cr.id_cliente = cl.id_cliente AND cr.estado_credito='A'",nativeQuery = true)
    List<Cliente> clientesCreditosActivos();

}
