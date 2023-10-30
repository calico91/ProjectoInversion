package com.cblandon.inversiones.CuotaCredito;


import com.cblandon.inversiones.CuotaCredito.dto.CuotasCreditoResponseDTO;
import com.cblandon.inversiones.CuotaCredito.dto.PagarCuotaRequestDTO;
import com.cblandon.inversiones.Excepciones.NoDataException;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.CuotaCreditoMapper;
import com.cblandon.inversiones.Mapper.Mapper;
import com.cblandon.inversiones.Utils.Constantes;
import com.cblandon.inversiones.Utils.GenericMessageDTO;

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

    GenericMessageDTO mensajeRespuesta = GenericMessageDTO.builder().build();
    Map<String, String> mapRespuesta = new HashMap<>();

    public GenericMessageDTO pagarCuota(Integer codigoCuota, PagarCuotaRequestDTO pagarCuotaRequestDTO)
            throws NoDataException {

        CuotaCredito cuotaCreditoDB = cuotaCreditoRepository.findById(codigoCuota)
                .orElseThrow(() -> new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value()));

        if (cuotaCreditoDB.getFechaAbono() != null) {
            throw new RequestException(Constantes.CUOTA_YA_PAGADA, HttpStatus.BAD_REQUEST.value());
        }

        try {

            cuotaCreditoDB.setValorAbonado(pagarCuotaRequestDTO.getValorAbonado());
            cuotaCreditoDB.setFechaAbono(pagarCuotaRequestDTO.getFechaAbono());
            CuotaCredito cuotaCancelada = cuotaCreditoRepository.save(cuotaCreditoDB);
            mapRespuesta.put("estado", "cuota cancelada correctamente");

            if (cuotaCancelada.getValorAbonado() != null &&
                    cuotaCancelada.getCuotaNumero() < cuotaCancelada.getNumeroCuotas()) {

                Double interesCredito = calcularInteresCredito(
                        cuotaCancelada.getValorCredito(), cuotaCancelada.getInteresPorcentaje());
                CuotaCredito nuevaCuota = CuotaCredito.builder().build();

                nuevaCuota.setFechaCuota(calcularFechaProximaCuota(cuotaCancelada.getFechaCuota().toString()));
                nuevaCuota.setCuotaNumero(cuotaCancelada.getCuotaNumero() + 1);
                nuevaCuota.setValorCuota(interesCredito + cuotaCancelada.getValorCapital());
                nuevaCuota.setCredito(cuotaCancelada.getCredito());
                nuevaCuota.setNumeroCuotas(cuotaCancelada.getNumeroCuotas());
                nuevaCuota.setValorCredito(cuotaCancelada.getValorCredito());
                nuevaCuota.setInteresPorcentaje(cuotaCancelada.getInteresPorcentaje());
                nuevaCuota.setValorCapital(cuotaCancelada.getValorCapital());
                nuevaCuota.setValorInteres(interesCredito);

                cuotaCreditoRepository.save(nuevaCuota);

            } else {
                mapRespuesta.put("estado credito", "credito cancelado");
            }
            mensajeRespuesta.setMessage(mapRespuesta);
            return mensajeRespuesta;
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public GenericMessageDTO pagarInteresCuota(Integer codigoCuota, PagarCuotaRequestDTO pagarCuotaRequestDTO)
            throws NoDataException {

        CuotaCredito cuotaCreditoDB = cuotaCreditoRepository.findById(codigoCuota)
                .orElseThrow(() -> new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value()));
        if (cuotaCreditoDB.getFechaAbono() != null) {
            throw new RequestException(Constantes.CUOTA_YA_PAGADA, HttpStatus.BAD_REQUEST.value());
        }

        try {

            cuotaCreditoDB.setValorAbonado(pagarCuotaRequestDTO.getInteresAbonado());
            cuotaCreditoDB.setValorInteres(pagarCuotaRequestDTO.getInteresAbonado());
            cuotaCreditoDB.setValorCapital(0.0);
            cuotaCreditoDB.setFechaAbono(pagarCuotaRequestDTO.getFechaAbono());
            CuotaCredito cuotaCancelada = cuotaCreditoRepository.save(cuotaCreditoDB);

            if (cuotaCancelada.getValorAbonado() != null &&
                    cuotaCancelada.getCuotaNumero() < cuotaCancelada.getNumeroCuotas()) {

                Double interesCredito = calcularInteresCredito(
                        cuotaCancelada.getValorCredito(), cuotaCancelada.getInteresPorcentaje());
                CuotaCredito nuevaCuota = CuotaCredito.builder().build();

                nuevaCuota.setFechaCuota(calcularFechaProximaCuota(cuotaCancelada.getFechaCuota().toString()));
                nuevaCuota.setCuotaNumero(cuotaCancelada.getCuotaNumero());
                nuevaCuota.setValorCuota(interesCredito + cuotaCancelada.getValorCapital());
                nuevaCuota.setCredito(cuotaCancelada.getCredito());
                nuevaCuota.setNumeroCuotas(cuotaCancelada.getNumeroCuotas());
                nuevaCuota.setValorCredito(cuotaCancelada.getValorCredito());
                nuevaCuota.setInteresPorcentaje(cuotaCancelada.getInteresPorcentaje());
                nuevaCuota.setValorCapital(cuotaCancelada.getValorCapital());
                nuevaCuota.setValorInteres(interesCredito);

                cuotaCreditoRepository.save(nuevaCuota);

            } else {
                mapRespuesta.put("estado credito", "credito cancelado");
            }
            mapRespuesta.put("estado cuota", "cuota cancelada correctamente");
            mapRespuesta.put("cantidad cuotas", cuotaCreditoDB.getNumeroCuotas().toString());
            mapRespuesta.put("cuotas pagadas", cuotaCreditoDB.getCuotaNumero().toString());

            mensajeRespuesta.setMessage(mapRespuesta);
            return mensajeRespuesta;
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public CuotasCreditoResponseDTO infoCuotaCreditoCliente(Integer idCliente) {
        try {

            CuotaCredito infoCuotaCreditoCliente = cuotaCreditoRepository.infoCuotaCreditoCliente(idCliente);

            return CuotaCreditoMapper.
                    mapperCuotaCredito.
                    cuotaCreditoToCuotasCreditoResponseDTO(infoCuotaCreditoCliente);


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
