package com.cblandon.inversiones.reporte.service;

import com.cblandon.inversiones.cuota_credito.dto.AbonosRealizadosResponseDTO;
import com.cblandon.inversiones.cuota_credito.repository.CuotaCreditoRepository;
import com.cblandon.inversiones.reporte.dto.ReporteInteresCapitalDTO;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ReporteService {

    private final CuotaCreditoRepository cuotaCreditoRepository;


    /**
     * informacion del capital e interes generado segun el rengo de fechas seleccionado
     */
    @Transactional(readOnly = true)
    public ReporteInteresCapitalDTO generarReporteInteresCapital(String fechaInicial, String fechaFinal) {
        try {

            ReporteInteresCapitalDTO interesYcapital = cuotaCreditoRepository
                    .generarReporteInteresyCapital(convertirFechas(fechaInicial), convertirFechas(fechaFinal));

            log.info("reporte capital e interes: {}", interesYcapital);
            return interesYcapital;

        } catch (RuntimeException ex) {
            log.error("reporte capital e interes: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * ultimos x abonos que se han realizado en general, esto para comparar con presupuesto
     */
    @Transactional(readOnly = true)
    public List<AbonosRealizadosResponseDTO> consultarUltimosAbonosRealizados(int cantidadAbonos) {

        try {

            List<Tuple> ultimosAbonosRealizados = cuotaCreditoRepository.consultarUltimosAbonosRealizados(cantidadAbonos);

            List<AbonosRealizadosResponseDTO> ultimosAbonosRealizadosDTO = ultimosAbonosRealizados.stream().map(
                    abonos -> AbonosRealizadosResponseDTO.builder()
                            .nombres(abonos.get("nombres").toString())
                            .apellidos(abonos.get("apellidos").toString())
                            .valorAbonado(Double.parseDouble(abonos.get("valor_abonado").toString()))
                            .fechaAbono(LocalDate.parse(abonos.get("fecha_abono").toString().substring(0, 10)))
                            .build()).toList();

            log.info("consultarUltimosAbonosRealizados: {}", ultimosAbonosRealizadosDTO);
            return ultimosAbonosRealizadosDTO;


        } catch (RuntimeException ex) {
            log.error("consultarUltimosAbonosRealizados: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * convierte las fechas de string a datetime ya que al realizar consulta con JPQL y enviar las fechas como string
     * genera error
     */
    private LocalDateTime convertirFechas(String fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(fecha, formatter).atStartOfDay();
    }
}
