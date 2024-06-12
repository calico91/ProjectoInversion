package com.cblandon.inversiones.cuota_credito.servicio;


import com.cblandon.inversiones.credito.entity.Credito;
import com.cblandon.inversiones.credito.repository.CreditoRepository;
import com.cblandon.inversiones.cuota_credito.dto.*;
import com.cblandon.inversiones.cuota_credito.entity.CuotaCredito;
import com.cblandon.inversiones.cuota_credito.repository.CuotaCreditoRepository;
import com.cblandon.inversiones.estado_credito.entity.EstadoCredito;
import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.mapper.CuotaCreditoMapper;
import com.cblandon.inversiones.utils.Constantes;

import com.cblandon.inversiones.utils.MensajesErrorEnum;
import com.cblandon.inversiones.utils.UtilsMetodos;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.transaction.annotation.Transactional;

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

    private final Map<String, Object> mapRespuesta = new HashMap<>();
    private LocalDate fechaProximaMora;

    /// pagar cuota normal,solo interes o solo capital
    @Transactional
    public Map<String, Object> pagarCuota(
            Integer codigoCuota, PagarCuotaRequestDTO pagarCuotaRequestDTO)
            throws NoDataException {

        log.info("pagarCuota: {}", pagarCuotaRequestDTO.toString());

        CuotaCredito cuotaCreditoDB = cuotaCreditoRepository.findById(codigoCuota)
                .orElseThrow(() -> new NoDataException(MensajesErrorEnum.DATOS_NO_ENCONTRADOS));

        validarEstadoCuotaYCredito(cuotaCreditoDB.getValorAbonado(),
                cuotaCreditoDB.getCredito().getIdEstadoCredito().getId());


        double capitalCuotaNormal = Math.rint(
                cuotaCreditoDB.getCredito().getValorCredito() / cuotaCreditoDB.getNumeroCuotas());

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
                        cuotaCreditoDB.getCredito().setSaldoCredito(0.0);

                    } else {
                        cuotaCreditoDB.setValorInteres(0.0);
                        cuotaCreditoDB.setValorCapital(pagarCuotaRequestDTO.getValorAbonado());
                        cuotaCreditoDB.getCredito().setSaldoCredito(
                                Math.rint(
                                        cuotaCreditoDB.getCredito().getSaldoCredito()
                                                - pagarCuotaRequestDTO.getValorAbonado()));
                    }
                }

                default -> {
                    permitirPagarCuotaNormal(cuotaCreditoDB.getCredito().getSaldoCredito(),
                            cuotaCreditoDB.getFechaCuota(), cuotaCreditoDB.getCredito().getValorCredito(),
                            cuotaCreditoDB.getInteresPorcentaje(), cuotaCreditoDB.getCredito().getModalidad().getDescription(),
                            cuotaCreditoDB.getNumeroCuotas());

                    cuotaCreditoDB.setValorCapital(capitalCuotaNormal);
                    cuotaCreditoDB.setValorInteres(
                            Math.rint(pagarCuotaRequestDTO.getValorAbonado() - capitalCuotaNormal));
                    cuotaCreditoDB.getCredito().setSaldoCredito(
                            Math.rint(cuotaCreditoDB.getCredito().getSaldoCredito() - capitalCuotaNormal));
                }

            }


            cuotaCreditoDB.setAbonoExtra(pagarCuotaRequestDTO.isAbonoExtra());
            cuotaCreditoDB.setValorAbonado(pagarCuotaRequestDTO.getValorAbonado());
            cuotaCreditoDB.setFechaAbono(LocalDateTime.now());
            cuotaCreditoDB.setTipoAbono(pagarCuotaRequestDTO.getTipoAbono());

            CuotaCredito cuotaCancelada = cuotaCreditoRepository.save(cuotaCreditoDB);

            /*
              si la cantidad de cuotas pagadas es mayor a las cuotas pactadas
              o se envia la constante C, el credito con esta cuota queda saldado
             */
            if (pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.CUOTA_NORMAL)
                    && cuotaCancelada.getCuotaNumero() >= cuotaCancelada.getNumeroCuotas()) {
                pagarCuotaRequestDTO.setEstadoCredito(Constantes.CREDITO_PAGADO);
            }

            if (!pagarCuotaRequestDTO.getEstadoCredito().equals(Constantes.CREDITO_PAGADO)) {

                double interesCredito = calcularInteresCredito(
                        cuotaCancelada.getCredito().getValorCredito(), cuotaCancelada.getInteresPorcentaje());
                CuotaCredito nuevaCuota = CuotaCredito.builder().build();

                /*
                 * si es un abono extraordinario, no cambia la fecha de la proxima cuota
                 * */
                if (pagarCuotaRequestDTO.isAbonoExtra()) {
                    nuevaCuota.setFechaCuota(cuotaCreditoDB.getFechaCuota());
                } else {
                    nuevaCuota.setFechaCuota(calcularFechaProximaCuota(
                            cuotaCancelada.getFechaCuota().toString(),
                            cuotaCreditoDB.getCredito().getModalidad().getDescription()));
                }

                if (pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.SOLO_INTERES) ||
                        pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.ABONO_CAPITAL)) {
                    nuevaCuota.setCuotaNumero(cuotaCreditoDB.getCuotaNumero());
                } else {
                    nuevaCuota.setCuotaNumero(cuotaCancelada.getCuotaNumero() + 1);
                }
                nuevaCuota.setValorCuota(Math.rint(interesCredito + (
                        cuotaCancelada.getCredito().getValorCredito() / cuotaCreditoDB.getNumeroCuotas())));
                nuevaCuota.setCredito(cuotaCancelada.getCredito());
                nuevaCuota.setNumeroCuotas(cuotaCancelada.getNumeroCuotas());
                nuevaCuota.setInteresPorcentaje(cuotaCancelada.getInteresPorcentaje());
                nuevaCuota.setValorCapital(0.0);
                nuevaCuota.setValorInteres(interesCredito);

                cuotaCreditoRepository.save(nuevaCuota);

            } else {
                log.info("entre a saldar credito");
                Credito credito = creditoRepository.findById(cuotaCancelada.getCredito().getId())
                        .orElseThrow(() -> new NoDataException(
                                MensajesErrorEnum.DATOS_NO_ENCONTRADOS));

                credito.setIdEstadoCredito(new EstadoCredito(Constantes.ID_CREDITO_PAGADO, null));
                creditoRepository.save(credito);

                mapRespuesta.put("estadoCredito", "Credito pagado en su totalidad");
            }
            mapRespuesta.put("estadoCuota", "Cuota cancelada correctamente");
            mapRespuesta.put("cantidadCuotas", cuotaCreditoDB.getNumeroCuotas());
            mapRespuesta.put("valorAbonado", pagarCuotaRequestDTO.getValorAbonado());
            mapRespuesta.put("tipoAbono", pagarCuotaRequestDTO.getTipoAbono());

            if (pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.SOLO_INTERES) ||
                    pagarCuotaRequestDTO.getTipoAbono().equals(Constantes.ABONO_CAPITAL)) {
                mapRespuesta.put("cuotasPagadas", cuotaCreditoDB.getCuotaNumero() - 1);
            } else {
                mapRespuesta.put("cuotasPagadas", cuotaCreditoDB.getCuotaNumero());
            }

            log.info("pagarCuota: {}", mapRespuesta);
            return mapRespuesta;
        } catch (RuntimeException ex) {
            log.error("pagarCuota: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public CuotasCreditoResponseDTO consultarCuotaCliente(Integer idCliente, Integer idCredito) {
        try {
            Tuple infoConsulta = cuotaCreditoRepository.consultarCuotaCreditoCliente(idCliente, idCredito);

            CuotasCreditoResponseDTO infoCuotaPagar = CuotasCreditoResponseDTO.builder()
                    .id(Integer.parseInt(infoConsulta.get("id_cuota_credito").toString()))
                    .valorCuota(Double.parseDouble(infoConsulta.get("valor_cuota").toString()))
                    .fechaCuota(LocalDate.parse(infoConsulta.get("fecha_cuota").toString()))
                    .numeroCuotas(Integer.parseInt(infoConsulta.get("numero_cuotas").toString()))
                    .valorCapital(Double.parseDouble(infoConsulta.get("valor_capital").toString()))
                    .valorInteres(Double.parseDouble(infoConsulta.get("valor_interes").toString()))
                    .interesPorcentaje(Double.parseDouble(infoConsulta.get("interes_porcentaje").toString()))
                    .cuotaNumero(Integer.parseInt(infoConsulta.get("couta_numero").toString()))
                    .valorCredito(Double.parseDouble(infoConsulta.get("valor_credito").toString()))
                    .modalidad(infoConsulta.get("description").toString())
                    .build();


            double interesMora = calcularInteresMora(infoCuotaPagar.getFechaCuota());

            infoCuotaPagar.setInteresMora(interesMora);
            infoCuotaPagar.setValorInteres(infoCuotaPagar.getValorInteres() + interesMora);

            infoCuotaPagar.setValorCapital(
                    infoCuotaPagar.getValorCredito() / infoCuotaPagar.getNumeroCuotas());

            infoCuotaPagar.setDiasMora(calcularDiasDiferenciaEntreFechas(
                    infoCuotaPagar.getFechaCuota(), LocalDate.now()));

            infoCuotaPagar.setValorCuota(infoCuotaPagar.getValorCuota() + interesMora);
            infoCuotaPagar.setValorCredito(infoCuotaPagar.getValorCredito());
            infoCuotaPagar.setFechaProximaMora(fechaProximaMora);
            log.info("consultarCuotaCreditoCliente: {}", infoCuotaPagar);

            return infoCuotaPagar;


        } catch (RuntimeException ex) {
            log.error("consultarInfoCuotaCreditoCliente: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }


    /**
     * saldo del credito hasta la fecha y otros datos del credito
     */
    @Transactional(readOnly = true)
    public InfoCreditoySaldoResponseDTO consultarCreditoSaldo(Integer idCredito) {
        try {
            List<Tuple> cuotas = cuotaCreditoRepository.consultarInfoCreditoySaldo(idCredito);


            List<InfoCreditoySaldoResponseDTO> infoCreditoySaldo = cuotas.stream().map(
                    cuota -> InfoCreditoySaldoResponseDTO.builder()
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
                            .saldoCredito(Double.parseDouble(cuota.get("saldo_credito").toString()))
                            .build()).toList();


            infoCreditoySaldo.get(0).setValorInteres(calcularInteresCredito(
                    infoCreditoySaldo.get(0).getValorCredito(), infoCreditoySaldo.get(0).getInteresPorcentaje()));

            infoCreditoySaldo.get(0).setCuotaNumero(UtilsMetodos.calcularCuotasPagadas(infoCreditoySaldo.get(0).getValorCredito(),
                    infoCreditoySaldo.get(0).getSaldoCredito(), infoCreditoySaldo.get(0).getNumeroCuotas()));

            infoCreditoySaldo.get(0).setCapitalPagado(
                    infoCreditoySaldo.get(0).getValorCredito() - infoCreditoySaldo.get(0).getSaldoCredito());

            Map<String, Object> datosCredito = calcularInteresActualySaldo(infoCreditoySaldo);

            infoCreditoySaldo.get(0).setInteresMora((Double) datosCredito.get(Constantes.INTERES_MORA));
            infoCreditoySaldo.get(0).setValorCuota(
                    (Double) datosCredito.get("interesMora") + infoCreditoySaldo.get(0).getValorCuota());
            infoCreditoySaldo.get(0).setInteresHoy((Double) datosCredito.get("interesActual"));
            infoCreditoySaldo.get(0).setSaldoCredito((Double) datosCredito.get("saldoCredito"));
            infoCreditoySaldo.get(0).setUltimaCuotaPagada(datosCredito.get("ultimaCuotaPagada").toString());

            log.info("consultarInfoCreditoySaldo: {}", infoCreditoySaldo.get(0).toString());
            return infoCreditoySaldo.get(0);
        } catch (RuntimeException ex) {
            log.error("consultarInfoCreditoySaldo: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Transactional
    public CuotasCreditoResponseDTO modificarFechaPago(LocalDate fechaNueva, int idCredito) {

        try {
            CuotaCredito ultimaCuotaGenerada = cuotaCreditoRepository.consultarUltimaCuotaGenerada(idCredito);

            int diasSegunModalidad = ultimaCuotaGenerada.getCredito().getModalidad().getDescription().equals(
                    Constantes.MODALIDAD_MENSUAL) ? 30 : 15;

            if (ultimaCuotaGenerada.getFechaCuota().isAfter(fechaNueva)) {
                throw new RequestException(MensajesErrorEnum.ERROR_FECHA_NUEVA);
            }

            double interesDias = (calcularInteresCredito(
                    ultimaCuotaGenerada.getCredito().getValorCredito(), ultimaCuotaGenerada.getInteresPorcentaje()) / diasSegunModalidad)
                    * calcularDiasDiferenciaEntreFechas(
                    ultimaCuotaGenerada.getFechaCuota(), fechaNueva);

            ultimaCuotaGenerada.setFechaCuota(fechaNueva);
            ultimaCuotaGenerada.setValorInteres(interesDias + ultimaCuotaGenerada.getValorInteres());
            ultimaCuotaGenerada.setValorCuota(interesDias + ultimaCuotaGenerada.getValorCuota());

            CuotaCredito cuotaGenerada = cuotaCreditoRepository.save(ultimaCuotaGenerada);

            CuotasCreditoResponseDTO cuotasCreditoResponseDTO = CuotaCreditoMapper.
                    mapperCuotaCredito.
                    cuotaCreditoToCuotasCreditoResponseDTO(cuotaGenerada);

            log.info("modificarFechaPago: {}", cuotasCreditoResponseDTO.toString());

            return cuotasCreditoResponseDTO;

        } catch (RuntimeException ex) {

            log.error("modificarFechaPago: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * abonos que se han realizado a un credito
     */
    @Transactional(readOnly = true)
    public List<AbonosRealizadosResponseDTO> consultarAbonosRealizados(int idCredito) {

        try {
            List<Tuple> cuotasPagas = cuotaCreditoRepository.consultarAbonosRealizadosPorCredito(
                    idCredito);

            cuotasPagas.remove(cuotasPagas.size() - 1);

            log.info("consultarAbonosRealizados: {}", cuotasPagas.size());

            return cuotasPagas.stream().map(
                    cuota -> AbonosRealizadosResponseDTO.builder()
                            .id(Integer.parseInt(cuota.get("id_cuota_credito").toString()))
                            .valorAbonado(Double.parseDouble(cuota.get("valor_abonado").toString()))
                            .fechaAbono(LocalDate.parse(cuota.get("fecha_abono").toString().substring(0, 10)))
                            .tipoAbono((String) cuota.get("tipo_abono"))
                            .cuotaNumero(Integer.parseInt(cuota.get("couta_numero").toString()))
                            .build()).toList();

        } catch (RuntimeException ex) {
            log.error("consultarAbonosRealizados: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * consulta informacion de un abono, por si se necesita compartir nuevamente el comprobante
     */
    @Transactional(readOnly = true)
    public AbonoPorIdDTO consultarAbonoPorId(int idCuotaCredito) {

        try {

            AbonoPorIdDTO abonoPorId = cuotaCreditoRepository.consultarAbonoPorId(idCuotaCredito);

            log.info("consultarAbonoPorId: {}", abonoPorId.toString());
            return abonoPorId;


        } catch (RuntimeException ex) {
            log.error("consultarAbonoPorId: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public String anularUltimoAbono(Integer idAbono, Integer idCredito) {
        log.info("anularAbono: {}", idAbono);
        List<IdAbonosRealizadosDTO> abonosRealizados = cuotaCreditoRepository.consultarIdAbonosRealizadosPorCredito(
                idCredito);
        int ultimaCuotaGenerada = abonosRealizados.get(0).idAbono();

        abonosRealizados.remove(0);


        ///valida si el abono a realizar es el ultimo
        if (abonosRealizados.stream().anyMatch(idAbonos -> idAbonos.idAbono() > idAbono)) {
            throw new RequestException(MensajesErrorEnum.ERROR_ANULAR_ABONO);
        }

        CuotaCredito abono = cuotaCreditoRepository.findById(idAbono).orElseThrow(
                () -> new RequestException(MensajesErrorEnum.ID_ABONO_NO_EXISTE));
        try {

            abono.setFechaAbono(null);
            abono.setValorAbonado(null);
            abono.setTipoAbono(null);
            abono.setAbonoExtra(null);
            abono.setValorCapital(0.0);
            abono.setValorInteres((abono.getCredito().getValorCredito() * abono.getInteresPorcentaje()) / 100);

            cuotaCreditoRepository.save(abono);
            cuotaCreditoRepository.delete(new CuotaCredito(ultimaCuotaGenerada));

            return "Abono anulado correctamente.";

        } catch (RuntimeException e) {
            log.error("anularAbono: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    private LocalDate calcularFechaProximaCuota(String fechaCuotaAnterior, String modalidad) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaAnterior = LocalDate.parse(fechaCuotaAnterior, dtf);
        int diasMes = fechaAnterior.lengthOfMonth();

        /* se realiza validacion para que los creditos quincenales siempre queden para las mismas fechas */
        if (modalidad.equals(Constantes.MODALIDAD_QUINCENAL)) {
            int diaPago = Integer.parseInt(fechaAnterior.toString().substring(8));
            if (diaPago >= 15 && diasMes == 31) {
                /*cuota del 15-30 y mes 31*/
                diasMes = 16;

            } else if (diaPago >= 15 && diasMes == 30) {
                /*cuota del 15-30 y mes 30*/
                diasMes = 15;

            } else if (diaPago >= 15 && diasMes < 30) {
                /*cuota del 15-30 del mes de febrero y si es año bisiesto*/
                diasMes = diasMes == 29 ? 14 : 13;

            } else {
                /*cuota del 1-15*/
                diasMes = 15;
            }
            diasMes = (int) Math.ceil(Double.parseDouble(Integer.toString(diasMes)));
        }
        return fechaAnterior.plusDays(diasMes);


    }

    private double calcularInteresCredito(double valorPrestado, double interesPorcentaje) {
        double interesCredito = valorPrestado * (interesPorcentaje / 100);

        return Math.rint(interesCredito);
    }


    private Map<String, Object> calcularInteresActualySaldo(List<InfoCreditoySaldoResponseDTO> listaCuotas) {
        int index = 0;

        if (listaCuotas.size() != 1) {
            /* al realizar abonos extraordinarios, calcula mal el interes actual ya que no toma la
             fecha del ultimo pago, sino la fecha de la proxima cuota*/
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

        double saldoCredito = interesMora + interesActual + listaCuotas.get(0).getSaldoCredito();

        String ultimaCuotaPagada = diaCalcularInteres.toString();


        mapRespuesta.put("interesActual", Math.rint(interesActual));
        mapRespuesta.put("saldoCredito", Math.rint(saldoCredito));
        mapRespuesta.put("ultimaCuotaPagada", ultimaCuotaPagada);
        mapRespuesta.put("interesMora", interesMora);

        return mapRespuesta;
    }

    /**
     * calcula el interes al dia de hoy
     */
    private double calcularInteresActual(
            LocalDate diaCalcularInteres, double valorCredito, double interesPorcentaje, String modalidad) {
        int diasSegunModalidad = modalidad.equals(Constantes.MODALIDAD_MENSUAL) ? 30 : 15;

        int diasDiferencia = calcularDiasDiferenciaEntreFechas(diaCalcularInteres, LocalDate.now());

        return ((valorCredito * (
                interesPorcentaje / 100) / diasSegunModalidad) * diasDiferencia);
    }

    /**
     * calcula si el valor del saldo es mayor al valor de la cuota capital
     * para permitir dejar hacer el abono normal
     */
    private void permitirPagarCuotaNormal(Double saldoCredito, LocalDate fechaCuota, double valorCredito,
                                          double interesPorcentaje, String modalidad, int numeroCuotas) {

        double interesActual = calcularInteresActual(
                fechaCuota,
                valorCredito,
                interesPorcentaje,
                modalidad);

        if ((saldoCredito + interesActual) < (valorCredito / numeroCuotas)) {

            throw new RequestException(MensajesErrorEnum.NO_PUEDE_PAGAR_CUOTA_NORMAL);
        }
    }

    /**
     * por cada tres dias de mora genera un interes de 5 mil pesos,
     * despues de la primer mora, suma cada 4 dias 5k de mora
     */
    private Double calcularInteresMora(LocalDate fechaCuota) {
        int diasDiferencia = calcularDiasDiferenciaEntreFechas(fechaCuota, LocalDate.now());
        log.info("dias de mora:" + diasDiferencia);
        fechaProximaMora = fechaCuota.plusDays(4);

        int diasCobrar = 0;

        for (int i = 1; diasDiferencia > 0; diasDiferencia--) {
            if (i % 4 == 0) {
                fechaProximaMora = fechaProximaMora.plusDays(4);
                diasCobrar++;
            }
            i++;
        }
        log.info("fecha proxima mora".concat(fechaProximaMora.toString()));
        log.info("dias a cobrar:" + diasCobrar);
        double valorMora = Double.parseDouble(Integer.toString(diasCobrar)) * 5000;
        log.info("valorMora:" + valorMora);

        return valorMora;
    }


    private int calcularDiasDiferenciaEntreFechas(LocalDate fechaInicial, LocalDate fechaFinal) {
        long diasDiferencia = DAYS.between(fechaInicial, fechaFinal);
        diasDiferencia = diasDiferencia <= 0 ? 0 : diasDiferencia;

        return Integer.parseInt(Long.toString(diasDiferencia));
    }

    //estado cuota credito
    private void validarEstadoCuotaYCredito(Double valorAbono, int codigoEstadoCredito) {
        if (valorAbono != null) {
            throw new RequestException(MensajesErrorEnum.CUOTA_YA_PAGADA);
        }

        if (codigoEstadoCredito != 1) {
            throw new RequestException(MensajesErrorEnum.ESTADO_NO_ACTIVO);
        }
    }


}
