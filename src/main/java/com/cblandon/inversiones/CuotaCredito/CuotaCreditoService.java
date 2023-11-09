package com.cblandon.inversiones.CuotaCredito;


import com.cblandon.inversiones.CuotaCredito.dto.CuotasCreditoResponseDTO;
import com.cblandon.inversiones.CuotaCredito.dto.PagarCuotaRequestDTO;
import com.cblandon.inversiones.Excepciones.NoDataException;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.CuotaCreditoMapper;
import com.cblandon.inversiones.Utils.Constantes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CuotaCreditoService {
    private final CuotaCreditoRepository cuotaCreditoRepository;

    public CuotaCreditoService(CuotaCreditoRepository cuotaCreditoRepository) {
        this.cuotaCreditoRepository = cuotaCreditoRepository;
    }

    Map<String, String> mapRespuesta = new HashMap<>();

    public Map<String, String> pagarCuota(
            Integer codigoCuota, PagarCuotaRequestDTO pagarCuotaRequestDTO, boolean soloInteres)
            throws NoDataException {
        log.info(pagarCuotaRequestDTO.toString());
        CuotaCredito cuotaCreditoDB = cuotaCreditoRepository.findById(codigoCuota)
                .orElseThrow(() -> new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.NOT_FOUND.value()));

        if (cuotaCreditoDB.getFechaAbono() != null) {
            throw new RequestException(Constantes.CUOTA_YA_PAGADA, HttpStatus.BAD_REQUEST.value());
        }
        if (!soloInteres && pagarCuotaRequestDTO.getValorAbonado() < cuotaCreditoDB.getValorCuota()) {
            throw new RequestException(Constantes.ERROR_CUOTA, HttpStatus.BAD_REQUEST.value());
        }

        Integer cuotasPagadasSoloInteres = cuotaCreditoDB.getCuotaNumero() - 1;
        try {
            if (soloInteres) {
                cuotaCreditoDB.setValorInteres(pagarCuotaRequestDTO.getValorAbonado());
                cuotaCreditoDB.setValorAbonado(pagarCuotaRequestDTO.getValorAbonado());
                cuotaCreditoDB.setValorCapital(0.0);
            } else {
                cuotaCreditoDB.setValorAbonado(pagarCuotaRequestDTO.getValorAbonado());

            }

            cuotaCreditoDB.setFechaAbono(pagarCuotaRequestDTO.getFechaAbono());
            cuotaCreditoDB.setTipoAbono(pagarCuotaRequestDTO.getTipoAbono());
            CuotaCredito cuotaCancelada = cuotaCreditoRepository.save(cuotaCreditoDB);

            if (cuotaCancelada.getValorAbonado() != null &&
                    cuotaCancelada.getCuotaNumero() <= cuotaCancelada.getNumeroCuotas()) {

                Double interesCredito = calcularInteresCredito(
                        cuotaCancelada.getValorCredito(), cuotaCancelada.getInteresPorcentaje());
                CuotaCredito nuevaCuota = CuotaCredito.builder().build();

                nuevaCuota.setFechaCuota(calcularFechaProximaCuota(cuotaCancelada.getFechaCuota().toString()));
                if (soloInteres) {
                    nuevaCuota.setCuotaNumero(cuotaCancelada.getCuotaNumero());
                } else {
                    nuevaCuota.setCuotaNumero(cuotaCancelada.getCuotaNumero() + 1);
                }
                nuevaCuota.setValorCuota(interesCredito + (cuotaCancelada.getValorCredito() / cuotaCreditoDB.getNumeroCuotas()));
                nuevaCuota.setCredito(cuotaCancelada.getCredito());
                nuevaCuota.setNumeroCuotas(cuotaCancelada.getNumeroCuotas());
                nuevaCuota.setValorCredito(cuotaCancelada.getValorCredito());
                nuevaCuota.setInteresPorcentaje(cuotaCancelada.getInteresPorcentaje());
                nuevaCuota.setValorCapital(cuotaCancelada.getValorCredito() / cuotaCreditoDB.getNumeroCuotas());
                nuevaCuota.setValorInteres(interesCredito);

                cuotaCreditoRepository.save(nuevaCuota);

            } else {
                mapRespuesta.put("estadoCredito", "credito cancelado");
            }
            mapRespuesta.put("estadoCuota", "cuota cancelada correctamente");
            mapRespuesta.put("cantidadCuotas", cuotaCreditoDB.getNumeroCuotas().toString());
            if (soloInteres) {
                mapRespuesta.put("cuotasPagadas", cuotasPagadasSoloInteres.toString());
            } else {
                mapRespuesta.put("cuotasPagadas", cuotaCreditoDB.getCuotaNumero().toString());
            }
            return mapRespuesta;
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public CuotasCreditoResponseDTO infoCuotaCreditoCliente(Integer idCliente, Integer idCredito) {
        try {
            CuotaCredito infoCuotaCreditoClienteRes = cuotaCreditoRepository.infoCuotaCreditoCliente(idCliente, idCredito);
            log.info(infoCuotaCreditoClienteRes.toString());

            return CuotaCreditoMapper.
                    mapperCuotaCredito.
                    cuotaCreditoToCuotasCreditoResponseDTO(infoCuotaCreditoClienteRes);


        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }

    private LocalDate calcularFechaProximaCuota(String fechaCuotaAnterior) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaAnterior = LocalDate.parse(fechaCuotaAnterior, dtf);
        int diasMes = fechaAnterior.lengthOfMonth();
        fechaAnterior = fechaAnterior.plusDays(diasMes);

        return fechaAnterior;
    }

    private Double calcularInteresCredito(Double valorPrestado, Double interesPorcentaje) {
        Double interesCredito = valorPrestado * (interesPorcentaje / 100);

        return Math.rint(interesCredito);
    }

}
