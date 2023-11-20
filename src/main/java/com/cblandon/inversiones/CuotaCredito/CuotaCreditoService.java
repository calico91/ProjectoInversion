package com.cblandon.inversiones.CuotaCredito;


import com.cblandon.inversiones.Credito.Credito;
import com.cblandon.inversiones.Credito.CreditoRepository;
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
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Slf4j
public class CuotaCreditoService {
    private final CreditoRepository creditoRepository;
    private final CuotaCreditoRepository cuotaCreditoRepository;

    public CuotaCreditoService(CuotaCreditoRepository cuotaCreditoRepository,
                               CreditoRepository creditoRepository) {
        this.cuotaCreditoRepository = cuotaCreditoRepository;
        this.creditoRepository = creditoRepository;
    }

    Map<String, Object> mapRespuesta = new HashMap<>();

    /// pagar cuota normal,solo interes o solo capital, dependiendo el tipo de abono
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
                /// si el credito se paga en su totalidad, se separa el interes del capital
                if (pagarCuotaRequestDTO.getEstadoCredito().equals(Constantes.CREDITO_PAGADO)) {
                    cuotaCreditoDB.setValorInteres(pagarCuotaRequestDTO.getValorInteres());
                } else {
                    cuotaCreditoDB.setValorInteres(0.0);
                }

                cuotaCreditoDB.setValorCapital(pagarCuotaRequestDTO.getValorAbonado());

            } else {
                cuotaCreditoDB.setValorCapital(cuotaCreditoDB.getValorCredito() / cuotaCreditoDB.getNumeroCuotas());
            }

            cuotaCreditoDB.setAbonoExtra(pagarCuotaRequestDTO.isAbonoExtra());
            cuotaCreditoDB.setValorAbonado(pagarCuotaRequestDTO.getValorAbonado());
            cuotaCreditoDB.setFechaAbono(pagarCuotaRequestDTO.getFechaAbono());
            cuotaCreditoDB.setTipoAbono(pagarCuotaRequestDTO.getTipoAbono());

            CuotaCredito cuotaCancelada = cuotaCreditoRepository.save(cuotaCreditoDB);

            ///si la cantidad de cuotas pagadas es mayor o igual a la cuotas pactadas
            /// o se envia la constante C, el credito con esta cuota queda saldado
            if (cuotaCancelada.getCuotaNumero() > cuotaCancelada.getNumeroCuotas()) {
                pagarCuotaRequestDTO.setEstadoCredito(Constantes.CREDITO_PAGADO);
            }

            if (!pagarCuotaRequestDTO.getEstadoCredito().equals(Constantes.CREDITO_PAGADO)) {

                Double interesCredito = calcularInteresCredito(
                        cuotaCancelada.getValorCredito(), cuotaCancelada.getInteresPorcentaje());
                CuotaCredito nuevaCuota = CuotaCredito.builder().build();

                ///si es un abono extraordinario, no cambia la fecha de la proxima cuota
                if (pagarCuotaRequestDTO.isAbonoExtra()) {
                    nuevaCuota.setFechaCuota(cuotaCreditoDB.getFechaCuota());
                } else {
                    nuevaCuota.setFechaCuota(calcularFechaProximaCuota(cuotaCancelada.getFechaCuota().toString()));

                }
                if (pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.SOLO_INTERES) ||
                        pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.ABONO_CAPITAL)) {
                    nuevaCuota.setCuotaNumero(cuotaCreditoDB.getCuotaNumero());
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
                log.info("entre a saldar credito");
                Credito credito = creditoRepository.findById(cuotaCancelada.getCredito().getId())
                        .orElseThrow(() -> new NoDataException(
                                Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.NOT_FOUND.value()));

                credito.setEstadoCredito(Constantes.CREDITO_PAGADO);
                creditoRepository.save(credito);

                mapRespuesta.put("estadoCredito", "Credito pagado en su totalidad");
            }
            mapRespuesta.put("estadoCuota", "cuota pagada correctamente");
            mapRespuesta.put("cantidadCuotas", cuotaCreditoDB.getNumeroCuotas().toString());

            if (pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.SOLO_INTERES) ||
                    pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.ABONO_CAPITAL)) {
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


    ///saldo del credito hasta la fecha y otros datos del credito
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
                            .valorCuota(Double.parseDouble(cuota.get("valor_cuota").toString()))
                            .valorInteres(Double.parseDouble(cuota.get("valor_interes").toString()))
                            .tipoAbono(Optional.ofNullable((String) cuota.get("tipo_abono")).orElse("CP"))
                            .abonoExtra(Optional.ofNullable((Boolean) cuota.get("abono_extra")).orElse(false))
                            .build()).collect(Collectors.toList());

            infoCreditoySaldo.get(0).setValorInteres(calcularInteresCredito(
                    infoCreditoySaldo.get(0).getValorCredito(), infoCreditoySaldo.get(0).getInteresPorcentaje()));

            double capitalPagado = cuotas.stream().mapToDouble(
                            valorCapital -> Double.parseDouble(valorCapital.get("valor_capital").toString()))
                    .sum();
            infoCreditoySaldo.get(0).setCapitalPagado(capitalPagado);

            double interesExtraPagado = infoCreditoySaldo.stream()
                    .filter(abonoExtra -> abonoExtra.getAbonoExtra())
                    .mapToDouble(abonoExtra -> abonoExtra.getValorInteres()).sum();

            infoCreditoySaldo.get(0).setAbonoExtraPagado(interesExtraPagado);


            Map<String, Object> datosCredito = calcularInteresActualySaldo(infoCreditoySaldo);
            infoCreditoySaldo.get(0).setInteresHoy((Double) datosCredito.get("interesActual"));
            infoCreditoySaldo.get(0).setSaldoCredito((Double) datosCredito.get("saldoCredito"));
            infoCreditoySaldo.get(0).setUltimaCuotaPagada(datosCredito.get("ultimaCuotaPagada").toString());

            log.info(infoCreditoySaldo.get(0).toString());
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
        int index = 0;

        if (listaCuotas.size() != 1) {
            /// al realizar abonos extraordinarios, calcula mal el interes actual ya que no toma la
            /// fecha del ultimo pago, sino la fecha de la proxima cuota
            if (listaCuotas.get(1).getTipoAbono().equals(Constantes.ABONO_CAPITAL)) {
                for (int i = 0; i < listaCuotas.size(); i++) {
                    if (listaCuotas.get(i).getTipoAbono().equals(Constantes.CUOTA_NORMAL)
                            || listaCuotas.get(i).getTipoAbono().equals(Constantes.SOLO_INTERES)) {
                        index = i;
                        break;
                    }
                }
            } else {
                index = 1;
            }

        }

        System.out.println(index);
        LocalDate diaCalcularInteres = index == 0
                ? listaCuotas.get(index).getFechaCredito()
                : listaCuotas.get(index).getFechaCuota();

        diasDiferencia = DAYS.between(diaCalcularInteres, LocalDate.now());

        interesActual = ((listaCuotas.get(0).getValorCredito() * (
                listaCuotas.get(0).getInteresPorcentaje() / 100) / 30) * diasDiferencia)
                - listaCuotas.get(0).getAbonoExtraPagado();

        interesActual = interesActual <= 0.0 ? 0.0 : interesActual;
        saldoCredito = interesActual + (
                listaCuotas.get(0).getValorCredito() - listaCuotas.get(0).getCapitalPagado());

        ultimaCuotaPagada = diaCalcularInteres.toString();


        mapRespuesta.put("interesActual", Math.rint(interesActual));
        mapRespuesta.put("saldoCredito", Math.rint(saldoCredito));
        mapRespuesta.put("ultimaCuotaPagada", ultimaCuotaPagada);

        return mapRespuesta;
    }

}
