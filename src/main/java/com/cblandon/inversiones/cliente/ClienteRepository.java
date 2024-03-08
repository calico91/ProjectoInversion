package com.cblandon.inversiones.cliente;

import com.cblandon.inversiones.cliente.dto.ClientesCuotaCreditoDTO;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByCedula(String cedula);


    @Query(value = "SELECT new com.cblandon.inversiones.cliente.dto.ClientesCuotaCreditoDTO(" +
            "c.id, c.nombres, c.apellidos, c.cedula, cr.fechaCredito, cr.valorCredito, ccr.fechaAbono, " +
            "ccr.fechaCuota, ccr.valorCuota, ccr.valorInteres,cr.id) " +
            "FROM Cliente c " +
            "JOIN  c.listaCreditos cr " +
            "JOIN cr.listaCuotasCredito ccr " +
            "WHERE cr.idEstadoCredito.id = 1 AND ccr.fechaAbono IS NULL " +
            "AND ccr.fechaCuota <= :fechaFiltro ORDER BY ccr.fechaCuota ASC")
    List<ClientesCuotaCreditoDTO> consultarClientesCuotasPendientes(@Param("fechaFiltro") LocalDate fechaFiltro);

}
