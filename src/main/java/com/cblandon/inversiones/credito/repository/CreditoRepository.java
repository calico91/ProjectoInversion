package com.cblandon.inversiones.credito.repository;

import com.cblandon.inversiones.credito.dto.CreditosActivosDTO;
import com.cblandon.inversiones.credito.entity.Credito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditoRepository extends JpaRepository<Credito, Integer> {

    @Query(value = "SELECT  new com.cblandon.inversiones.credito.dto.CreditosActivosDTO(cl.id,cl.nombres," +
            "cl.apellidos,cl.cedula,cr.id, cr.fechaCredito,cr.valorCredito )" +
            "FROM Credito cr " +
            "JOIN cr.cliente cl " +
            "JOIN cr.usuarios uc on uc.id = :idUsuario " +
            "WHERE cr.idEstadoCredito.id=1 ORDER BY cr.id DESC ")
    List<CreditosActivosDTO> consultarClientesConCreditosActivos(Integer idUsuario);

}
