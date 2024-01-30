package com.cblandon.inversiones.cuotacredito;


import com.cblandon.inversiones.credito.Credito;
import com.cblandon.inversiones.credito.CreditoRepository;
import com.cblandon.inversiones.cuotacredito.dto.InfoCreditoySaldo;
import com.cblandon.inversiones.cuotacredito.dto.CuotasCreditoResponseDTO;
import com.cblandon.inversiones.cuotacredito.dto.PagarCuotaRequestDTO;
import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.mapper.CuotaCreditoMapper;
import com.cblandon.inversiones.utils.Constantes;

import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    /// pagar cuota normal,solo interes o solo capital,
    public Map<String, Object> pagarCuota(
            Integer codigoCuota, PagarCuotaRequestDTO pagarCuotaRequestDTO)
            throws NoDataException {

        log.info(pagarCuotaRequestDTO.toString());

        CuotaCredito cuotaCreditoDB = cuotaCreditoRepository.findById(codigoCuota)
                .orElseThrow(() -> new NoDataException(
                        Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.NOT_FOUND.value()));

        validarEstadoCuota(cuotaCreditoDB.getFechaAbono());

        List<CuotaCredito> cuotasPagas = cuotaCreditoRepository.findByCreditoEqualsOrderByIdDesc(
                cuotaCreditoDB.getCredito());

        int cuotasPagadasSoloInteres = cuotaCreditoDB.getCuotaNumero() - 1;
        double capitalCuotaNormal = cuotaCreditoDB.getValorCredito() / cuotaCreditoDB.getNumeroCuotas();

        try {

            switch (pagarCuotaRequestDTO.getTipoAbono()) {
                case Constantes.SOLO_INTERES -> {
                    cuotaCreditoDB.setValorInteres(pagarCuotaRequestDTO.getValorAbonado());
                    cuotaCreditoDB.setValorCapital(0.0);
                }

                case Constantes.ABONO_CAPITAL -> {
                    /// si el credito se paga en su totalidad, se separa el interes del capital
                    if (pagarCuotaRequestDTO.getEstadoCredito().equals(Constantes.CREDITO_PAGADO)) {
                        cuotaCreditoDB.setValorInteres(pagarCuotaRequestDTO.getValorInteres());
                        cuotaCreditoDB.setValorCapital(
                                pagarCuotaRequestDTO.getValorAbonado() - pagarCuotaRequestDTO.getValorInteres());

                    } else {
                        cuotaCreditoDB.setValorInteres(0.0);
                        cuotaCreditoDB.setValorCapital(pagarCuotaRequestDTO.getValorAbonado());
                    }
                }

                default -> {
                    permitirPagarCuotaNormal(cuotasPagas);
                    cuotaCreditoDB.setValorCapital(capitalCuotaNormal);
                    cuotaCreditoDB.setValorInteres(
                            pagarCuotaRequestDTO.getValorAbonado() - capitalCuotaNormal);
                }

            }


            cuotaCreditoDB.setAbonoExtra(pagarCuotaRequestDTO.isAbonoExtra());
            cuotaCreditoDB.setValorAbonado(pagarCuotaRequestDTO.getValorAbonado());
            cuotaCreditoDB.setFechaAbono(pagarCuotaRequestDTO.getFechaAbono());
            cuotaCreditoDB.setTipoAbono(pagarCuotaRequestDTO.getTipoAbono());

            CuotaCredito cuotaCancelada = cuotaCreditoRepository.save(cuotaCreditoDB);

            ///si la cantidad de cuotas pagadas es mayor a las cuotas pactadas
            /// o se envia la constante C, el credito con esta cuota queda saldado
            if (pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.CUOTA_NORMAL)
                    && cuotaCancelada.getCuotaNumero() >= cuotaCancelada.getNumeroCuotas()) {
                pagarCuotaRequestDTO.setEstadoCredito(Constantes.CREDITO_PAGADO);
            }

            if (!pagarCuotaRequestDTO.getEstadoCredito().equals(Constantes.CREDITO_PAGADO)) {

                double interesCredito = calcularInteresCredito(
                        cuotaCancelada.getValorCredito(), cuotaCancelada.getInteresPorcentaje());
                CuotaCredito nuevaCuota = CuotaCredito.builder().build();

                ///si es un abono extraordinario, no cambia la fecha de la proxima cuota
                if (pagarCuotaRequestDTO.isAbonoExtra()) {
                    nuevaCuota.setFechaCuota(cuotaCreditoDB.getFechaCuota());
                } else {
                    nuevaCuota.setFechaCuota(calcularFechaProximaCuota(
                            cuotaCancelada.getFechaCuota().toString(), cuotaCreditoDB.getCredito().getModalidad()));
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
            mapRespuesta.put("estadoCuota", "Cuota cancelada correctamente");
            mapRespuesta.put("cantidadCuotas", cuotaCreditoDB.getNumeroCuotas().toString());
            mapRespuesta.put("valorAbonado", pagarCuotaRequestDTO.getValorAbonado().toString());
            mapRespuesta.put("tipoAbono", pagarCuotaRequestDTO.getTipoAbono());

            if (pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.SOLO_INTERES) ||
                    pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.ABONO_CAPITAL)) {
                mapRespuesta.put("cuotasPagadas", Integer.toString(cuotasPagadasSoloInteres));
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

            CuotasCreditoResponseDTO infoCuotaPagar = CuotaCreditoMapper.
                    mapperCuotaCredito.
                    cuotaCreditoToCuotasCreditoResponseDTO(infoCuotaCreditoClienteRes);
            double interesMora = calcularInteresMora(infoCuotaCreditoClienteRes.getFechaCuota());

            infoCuotaPagar.setInteresMora(interesMora);
            infoCuotaPagar.setValorCapital(infoCuotaPagar.getValorCredito() / infoCuotaPagar.getNumeroCuotas());
            infoCuotaPagar.setDiasMora(calcularDiasDiferenciaEntreFechas(
                    infoCuotaCreditoClienteRes.getFechaCuota(), LocalDate.now()));
            infoCuotaPagar.setValorCuota(infoCuotaPagar.getValorCuota() + interesMora);


            return infoCuotaPagar;


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
                            .modalidad(cuota.get("modalidad").toString())
                            .build()).toList();

            infoCreditoySaldo.get(0).setValorInteres(calcularInteresCredito(
                    infoCreditoySaldo.get(0).getValorCredito(), infoCreditoySaldo.get(0).getInteresPorcentaje()));

            double capitalPagado = cuotas.stream().mapToDouble(
                            valorCapital -> Double.parseDouble(valorCapital.get("valor_capital").toString()))
                    .sum();
            infoCreditoySaldo.get(0).setCapitalPagado(capitalPagado);

            double interesExtraPagado = infoCreditoySaldo.stream()
                    .filter(InfoCreditoySaldo::getAbonoExtra)
                    .mapToDouble(InfoCreditoySaldo::getValorInteres).sum();

            infoCreditoySaldo.get(0).setAbonoExtraPagado(interesExtraPagado);
            double valorCuota = infoCreditoySaldo.get(0).getValorCuota();

            Map<String, Object> datosCredito = calcularInteresActualySaldo(infoCreditoySaldo);

            infoCreditoySaldo.get(0).setInteresMora((Double) datosCredito.get(Constantes.INTERES_MORA));
            infoCreditoySaldo.get(0).setValorCuota((Double) datosCredito.get("interesMora") + valorCuota);
            infoCreditoySaldo.get(0).setInteresHoy((Double) datosCredito.get("interesActual"));
            infoCreditoySaldo.get(0).setSaldoCredito((Double) datosCredito.get("saldoCredito"));
            infoCreditoySaldo.get(0).setUltimaCuotaPagada(datosCredito.get("ultimaCuotaPagada").toString());

            log.info(infoCreditoySaldo.get(0).toString());
            return infoCreditoySaldo.get(0);
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    /// informacion del capital e interes generado segun el mes seleccionado
    public Map<String, Object> reporteInteresyCapital(String fechaInicial, String fechaFinal) {
        try {

            List<CuotaCredito> interesYcapital = cuotaCreditoRepository.reporteInteresyCapital(fechaInicial, fechaFinal);

            double capitalMes = interesYcapital.stream().mapToDouble(
                    CuotaCredito::getValorCapital).sum();

            double interesMes = interesYcapital.stream().mapToDouble(
                    CuotaCredito::getValorInteres).sum();

            mapRespuesta.put("capitalMes", Math.rint(capitalMes));
            mapRespuesta.put("interesMes", Math.rint(interesMes));

            log.info("informacion capital e interes " + mapRespuesta);

            return mapRespuesta;

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public CuotasCreditoResponseDTO modificarFechaPago(LocalDate fechaNueva, int idCredito) {

        try {
            CuotaCredito ultimaCuotaGenerada = cuotaCreditoRepository.ultimaCuotaGenerada(idCredito);

            int diasSegunModalidad = ultimaCuotaGenerada.getCredito().getModalidad().equals(Constantes.MODALIDAD_MENSUAL) ? 30 : 15;

            if (ultimaCuotaGenerada.getFechaCuota().isAfter(fechaNueva)) {
                throw new RequestException(Constantes.ERROR_FECHA_NUEVA, HttpStatus.BAD_REQUEST.value());
            }

            double interesDias = (calcularInteresCredito(
                    ultimaCuotaGenerada.getValorCredito(), ultimaCuotaGenerada.getInteresPorcentaje()) / diasSegunModalidad)
                    * calcularDiasDiferenciaEntreFechas(
                    ultimaCuotaGenerada.getFechaCuota(), fechaNueva);

            ultimaCuotaGenerada.setFechaCuota(fechaNueva);
            ultimaCuotaGenerada.setValorInteres(interesDias + ultimaCuotaGenerada.getValorInteres());
            ultimaCuotaGenerada.setValorCuota(interesDias + ultimaCuotaGenerada.getValorCuota());

            cuotaCreditoRepository.save(ultimaCuotaGenerada);

            return CuotaCreditoMapper.
                    mapperCuotaCredito.
                    cuotaCreditoToCuotasCreditoResponseDTO(cuotaCreditoRepository.save(ultimaCuotaGenerada));

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public List<CuotasCreditoResponseDTO> consultarAbonosRealizados(int idCredito) {

        try {
            List<CuotaCredito> cuotasPagas = cuotaCreditoRepository.consultarAbonosRealizados(
                    idCredito);

            cuotasPagas.remove(cuotasPagas.size() - 1);
            return cuotasPagas.stream().map(
                    CuotaCreditoMapper.
                            mapperCuotaCredito::cuotaCreditoToCuotasCreditoResponseDTO).toList();

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }


    private LocalDate calcularFechaProximaCuota(String fechaCuotaAnterior, String modalidad) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaAnterior = LocalDate.parse(fechaCuotaAnterior, dtf);
        int diasMes = fechaAnterior.lengthOfMonth();


        if (modalidad.equals(Constantes.MODALIDAD_QUINCENAL)) {
            diasMes = diasMes / 2;
            diasMes = (int) Math.ceil(Double.parseDouble(Integer.toString(diasMes)));
        }
        return fechaAnterior.plusDays(diasMes);


    }

    private double calcularInteresCredito(double valorPrestado, double interesPorcentaje) {
        double interesCredito = valorPrestado * (interesPorcentaje / 100);

        return Math.rint(interesCredito);
    }


    private Map<String, Object> calcularInteresActualySaldo(List<InfoCreditoySaldo> listaCuotas) {
        int index = 0;

        if (listaCuotas.size() != 1) {
            /// al realizar abonos extraordinarios, calcula mal el interes actual ya que no toma la
            /// fecha del ultimo pago, sino la fecha de la proxima cuota
            if (Boolean.TRUE.equals(listaCuotas.get(1).getAbonoExtra())) {
                for (int i = 1; i < listaCuotas.size(); i++) {
                    if (Boolean.FALSE.equals(listaCuotas.get(i).getAbonoExtra())) {
                        index = i;
                        break;
                    }
                }
            } else {
                index = 1;
            }

        }

        LocalDate diaCalcularInteres = index == 0
                ? listaCuotas.get(index).getFechaCredito()
                : listaCuotas.get(index).getFechaCuota();

        double interesActual = calcularInteresActual(
                diaCalcularInteres,
                listaCuotas.get(0).getValorCredito(),
                listaCuotas.get(0).getInteresPorcentaje(),
                listaCuotas.get(0).getModalidad());

        Double interesMora = calcularInteresMora(listaCuotas.get(0).getFechaCuota());
        log.info(interesMora.toString());

        interesActual = Math.max(interesActual, 0.0);

        double saldoCredito = interesMora + interesActual + (
                listaCuotas.get(0).getValorCredito() - listaCuotas.get(0).getCapitalPagado());

        String ultimaCuotaPagada = diaCalcularInteres.toString();


        mapRespuesta.put("interesActual", Math.rint(interesActual));
        mapRespuesta.put("saldoCredito", Math.rint(saldoCredito));
        mapRespuesta.put("ultimaCuotaPagada", ultimaCuotaPagada);
        mapRespuesta.put("interesMora", interesMora);

        return mapRespuesta;
    }

    ///calcula el interes al dia de hoy
    private double calcularInteresActual(
            LocalDate diaCalcularInteres, double valorCredito, double interesPorcentaje, String modalidad) {
        int diasSegunModalidad = modalidad.equals(Constantes.MODALIDAD_MENSUAL) ? 30 : 15;

        int diasDiferencia = calcularDiasDiferenciaEntreFechas(diaCalcularInteres, LocalDate.now());

        return ((valorCredito * (
                interesPorcentaje / 100) / diasSegunModalidad) * diasDiferencia);
    }

    /// calcula si el valor del saldo es mayor al valor de la cuota capital
    /// para permitir dejar hacer el abono normal
    private void permitirPagarCuotaNormal(List<CuotaCredito> cuotasPagas) {

        double capitalPagado = cuotasPagas.stream().mapToDouble(
                        CuotaCredito::getValorCapital)
                .sum();

        int index = cuotasPagas.size() != 1 ? 1 : 0;
        double interesActual = calcularInteresActual(
                cuotasPagas.get(index).getFechaCuota(),
                cuotasPagas.get(0).getValorCredito(),
                cuotasPagas.get(0).getInteresPorcentaje(),
                cuotasPagas.get(0).getCredito().getModalidad());

        double saldoCredito = interesActual + (
                cuotasPagas.get(0).getValorCredito() - capitalPagado);
        if (saldoCredito < (cuotasPagas.get(0).getValorCredito() / cuotasPagas.get(0).getNumeroCuotas())) {

            throw new RequestException(
                    Constantes.NO_PUEDE_PAGAR_CUOTA_NORMAL,
                    HttpStatus.BAD_REQUEST.value());
        }
    }

    /// por cada tres dias se genera un interes de mas por 5 mil pesos
    private Double calcularInteresMora(LocalDate fechaCuota) {
        int diasDiferencia = calcularDiasDiferenciaEntreFechas(fechaCuota, LocalDate.now());
        log.info("dias de mora:" + diasDiferencia);

        int diasCobrar = 0;

        if (diasDiferencia >= 3) {
            for (int i = 1; diasDiferencia > 0; diasDiferencia--) {
                if (i == 3) {
                    i = -1;
                    diasCobrar++;
                }
                i++;
            }
            log.info("dias cobrar" + diasCobrar);

        }
        log.info("dias a cobrar:" + diasCobrar);
        return (Double.parseDouble(Integer.toString(diasCobrar))) * 5000;
    }


    private int calcularDiasDiferenciaEntreFechas(LocalDate fechaInicial, LocalDate fechaFinal) {
        long diasDiferencia = DAYS.between(fechaInicial, fechaFinal);
        diasDiferencia = diasDiferencia <= 0 ? 0 : diasDiferencia;

        return Integer.parseInt(Long.toString(diasDiferencia));
    }

    private void validarEstadoCuota(LocalDate fechaAbono) {
        if (fechaAbono != null) {
            throw new RequestException(Constantes.CUOTA_YA_PAGADA, HttpStatus.BAD_REQUEST.value());
        }
    }


}
