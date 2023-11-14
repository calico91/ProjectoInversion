package com.cblandon.inversiones.CuotaCredito;


import com.cblandon.inversiones.CuotaCredito.dto.InfoCreditoySaldo;
import com.cblandon.inversiones.CuotaCredito.dto.CuotasCreditoResponseDTO;
import com.cblandon.inversiones.CuotaCredito.dto.PagarCuotaRequestDTO;
import com.cblandon.inversiones.Excepciones.NoDataException;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.CuotaCreditoMapper;
import com.cblandon.inversiones.Utils.Constantes;

import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Slf4j
public class CuotaCreditoService {
    private final CuotaCreditoRepository cuotaCreditoRepository;

    public CuotaCreditoService(CuotaCreditoRepository cuotaCreditoRepository) {
        this.cuotaCreditoRepository = cuotaCreditoRepository;
    }

    Map<String, Object> mapRespuesta = new HashMap<>();

    /// pagar cuota normal o solo interes
    public Map<String, Object> pagarCuota(
            Integer codigoCuota, PagarCuotaRequestDTO pagarCuotaRequestDTO)
            throws NoDataException {
        log.info(pagarCuotaRequestDTO.toString());
        CuotaCredito cuotaCreditoDB = cuotaCreditoRepository.findById(codigoCuota)
                .orElseThrow(() -> new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.NOT_FOUND.value()));

        if (cuotaCreditoDB.getFechaAbono() != null) {
            throw new RequestException(Constantes.CUOTA_YA_PAGADA, HttpStatus.BAD_REQUEST.value());
        }


        Integer cuotasPagadasSoloInteres = cuotaCreditoDB.getCuotaNumero() - 1;
        try {
            if (pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.SOLO_INTERES)) {
                cuotaCreditoDB.setValorInteres(pagarCuotaRequestDTO.getValorAbonado());
                cuotaCreditoDB.setValorCapital(0.0);
            } else if (pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.ABONO_CAPITAL)) {
                cuotaCreditoDB.setValorInteres(0.0);
                cuotaCreditoDB.setValorCapital(pagarCuotaRequestDTO.getValorAbonado());

            } else {
                cuotaCreditoDB.setValorCapital(cuotaCreditoDB.getValorCredito() / cuotaCreditoDB.getNumeroCuotas());
            }

            cuotaCreditoDB.setValorAbonado(pagarCuotaRequestDTO.getValorAbonado());
            cuotaCreditoDB.setFechaAbono(pagarCuotaRequestDTO.getFechaAbono());
            cuotaCreditoDB.setTipoAbono(pagarCuotaRequestDTO.getTipoAbono());
            CuotaCredito cuotaCancelada = cuotaCreditoRepository.save(cuotaCreditoDB);

            if (cuotaCancelada.getValorAbonado() != null &&
                    cuotaCancelada.getCuotaNumero() <= cuotaCancelada.getNumeroCuotas()) {

                Double interesCredito = calcularInteresCredito(
                        cuotaCancelada.getValorCredito(), cuotaCancelada.getInteresPorcentaje());
                CuotaCredito nuevaCuota = CuotaCredito.builder().build();

                nuevaCuota.setFechaCuota(calcularFechaProximaCuota(cuotaCancelada.getFechaCuota().toString()));
                if (pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.SOLO_INTERES) ||
                        pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.ABONO_CAPITAL)) {
                    nuevaCuota.setCuotaNumero(cuotaCancelada.getCuotaNumero());
                } else {
                    nuevaCuota.setCuotaNumero(cuotaCancelada.getCuotaNumero() + 1);
                }
                nuevaCuota.setValorCuota(interesCredito + (cuotaCancelada.getValorCredito() / cuotaCreditoDB.getNumeroCuotas()));
                nuevaCuota.setCredito(cuotaCancelada.getCredito());
                nuevaCuota.setNumeroCuotas(cuotaCancelada.getNumeroCuotas());
                nuevaCuota.setValorCredito(cuotaCancelada.getValorCredito());
                nuevaCuota.setInteresPorcentaje(cuotaCancelada.getInteresPorcentaje());
                nuevaCuota.setValorCapital(0.0);
                nuevaCuota.setValorInteres(interesCredito);

                cuotaCreditoRepository.save(nuevaCuota);

            } else {
                mapRespuesta.put("estadoCredito", "credito cancelado");
            }
            mapRespuesta.put("estadoCuota", "cuota cancelada correctamente");
            mapRespuesta.put("cantidadCuotas", cuotaCreditoDB.getNumeroCuotas().toString());
            if (pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.SOLO_INTERES)) {
                mapRespuesta.put("cuotasPagadas", cuotasPagadasSoloInteres.toString());
            } else {
                mapRespuesta.put("cuotasPagadas", cuotaCreditoDB.getCuotaNumero().toString());
            }
            return mapRespuesta;
        } catch (
                RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    /// abonar capital solo a capital
    public Map<String, Object> pagarCapital(
            Integer codigoCuota, PagarCuotaRequestDTO pagarCuotaRequestDTO)
            throws NoDataException {
        CuotaCredito cuotaCreditoDB = cuotaCreditoRepository.findById(codigoCuota)
                .orElseThrow(() -> new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.NOT_FOUND.value()));

        if (cuotaCreditoDB.getFechaAbono() != null) {
            throw new RequestException(Constantes.CUOTA_YA_PAGADA, HttpStatus.BAD_REQUEST.value());
        }
        if (pagarCuotaRequestDTO.getValorAbonado() < cuotaCreditoDB.getValorCuota()) {
            throw new RequestException(Constantes.ERROR_CUOTA, HttpStatus.BAD_REQUEST.value());
        }

        try {

            cuotaCreditoDB.setValorInteres(0.0);
            cuotaCreditoDB.setValorAbonado(pagarCuotaRequestDTO.getValorAbonado());
            cuotaCreditoDB.setValorCapital(pagarCuotaRequestDTO.getValorAbonado());

            cuotaCreditoDB.setFechaAbono(pagarCuotaRequestDTO.getFechaAbono());
            cuotaCreditoDB.setTipoAbono(pagarCuotaRequestDTO.getTipoAbono());
            CuotaCredito cuotaCancelada = cuotaCreditoRepository.save(cuotaCreditoDB);

            if (cuotaCancelada.getValorAbonado() != null &&
                    cuotaCancelada.getCuotaNumero() <= cuotaCancelada.getNumeroCuotas()) {

                Double interesCredito = calcularInteresCredito(
                        cuotaCancelada.getValorCredito(), cuotaCancelada.getInteresPorcentaje());
                CuotaCredito nuevaCuota = CuotaCredito.builder().build();

                nuevaCuota.setFechaCuota(calcularFechaProximaCuota(cuotaCancelada.getFechaCuota().toString()));
                nuevaCuota.setCuotaNumero(cuotaCancelada.getCuotaNumero());

                nuevaCuota.setValorCuota(interesCredito + (cuotaCancelada.getValorCredito() / cuotaCreditoDB.getNumeroCuotas()));
                nuevaCuota.setCredito(cuotaCancelada.getCredito());
                nuevaCuota.setNumeroCuotas(cuotaCancelada.getNumeroCuotas());
                nuevaCuota.setValorCredito(cuotaCancelada.getValorCredito());
                nuevaCuota.setInteresPorcentaje(cuotaCancelada.getInteresPorcentaje());
                nuevaCuota.setValorCapital(0.0);
                nuevaCuota.setValorInteres(interesCredito);

                cuotaCreditoRepository.save(nuevaCuota);

            } else {
                mapRespuesta.put("estadoCredito", "credito cancelado");
            }

            return mapRespuesta;
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    /// cuotas que no se han pagado hasta la fecha
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


    ///saldo del credito hasta la fecha y otros del credito
    public InfoCreditoySaldo infoCreditoySaldo(Integer idCredito) {
        try {
            List<Tuple> cuotas = cuotaCreditoRepository.infoCuotasPagadas(idCredito);

            List<InfoCreditoySaldo> infoCreditoySaldo = cuotas.stream().map(
                    cuota -> InfoCreditoySaldo.builder()
                            .fechaCredito(LocalDate.parse(cuota.get("fecha_credito").toString()))
                            .id(Integer.parseInt(cuota.get("id_cuota_credito").toString()))
                            .cuotaNumero(Integer.parseInt(cuota.get("couta_numero").toString()))
                            .fechaCuota(LocalDate.parse(cuota.get("fecha_cuota").toString()))
                            .interesPorcentaje(Double.parseDouble(cuota.get("interes_porcentaje").toString()))
                            .numeroCuotas(Integer.parseInt(cuota.get("numero_cuotas").toString()))
                            .valorCredito(Double.parseDouble(cuota.get("valor_credito").toString()))
                            .valorInteres(Double.parseDouble(cuota.get("valor_interes").toString()))
                            .valorCuota(Double.parseDouble(cuota.get("valor_cuota").toString()))
                            .build()).collect(Collectors.toList());

            double capitalPagado = cuotas.stream().mapToDouble(
                    valorCapital -> Double.parseDouble(valorCapital.get("valor_capital").toString())).sum();
            System.out.println(capitalPagado);
            infoCreditoySaldo.get(0).setCapitalPagado(capitalPagado);

            Map<String, Object> datosCredito = calcularInteresActualySaldo(infoCreditoySaldo);
            infoCreditoySaldo.get(0).setInteresHoy((Double) datosCredito.get("interesActual"));
            infoCreditoySaldo.get(0).setSaldoCredito((Double) datosCredito.get("saldoCredito"));
            infoCreditoySaldo.get(0).setUltimaCuotaPagada(datosCredito.get("ultimaCuotaPagada").toString());


            return infoCreditoySaldo.get(0);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
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


    private Map<String, Object> calcularInteresActualySaldo(List<InfoCreditoySaldo> listaCuotas) {
        Long diasDiferencia = 1L;
        Double interesActual = 0.0;
        Double saldoCredito = 0.0;
        String ultimaCuotaPagada = "";

        if (listaCuotas.size() == 1) {
            diasDiferencia = DAYS.between(listaCuotas.get(0).getFechaCredito(), LocalDate.now());
            interesActual = ((listaCuotas.get(0).getValorCredito() * (listaCuotas.get(0).getInteresPorcentaje() / 100) / 30) * diasDiferencia);
            saldoCredito = interesActual + (
                    listaCuotas.get(0).getValorCredito() - listaCuotas.get(0).getCapitalPagado());
            ultimaCuotaPagada = listaCuotas.get(0).getFechaCredito().toString();
        } else {

            diasDiferencia = DAYS.between(listaCuotas.get(1).getFechaCuota(), LocalDate.now());
            interesActual = ((listaCuotas.get(1).getValorCredito() * (listaCuotas.get(1).getInteresPorcentaje() / 100) / 30) * diasDiferencia);
            saldoCredito = interesActual + (
                    listaCuotas.get(1).getValorCredito() - listaCuotas.get(0).getCapitalPagado());
            ultimaCuotaPagada = listaCuotas.get(1).getFechaCuota().toString();
        }

        mapRespuesta.put("interesActual", Math.rint(interesActual));
        mapRespuesta.put("saldoCredito", Math.rint(saldoCredito));
        mapRespuesta.put("ultimaCuotaPagada", ultimaCuotaPagada);
        System.out.println(mapRespuesta.toString());

        return mapRespuesta;
    }

}
