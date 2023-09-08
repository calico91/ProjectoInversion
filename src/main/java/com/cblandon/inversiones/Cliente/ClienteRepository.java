package com.cblandon.inversiones.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Cliente findByCedula(String cedula);
    Cliente deleteByCedula(String cedula);

}
