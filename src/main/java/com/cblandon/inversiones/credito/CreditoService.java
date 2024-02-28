package com.cblandon.inversiones.credito;

import com.cblandon.inversiones.cliente.Cliente;
import com.cblandon.inversiones.cliente.ClienteRepository;
import com.cblandon.inversiones.credito.dto.*;
import com.cblandon.inversiones.cuotacredito.CuotaCreditoRepository;
import com.cblandon.inversiones.cuotacredito.CuotaCredito;
import com.cblandon.inversiones.estado_credito.EstadoCredito;
import com.cblandon.inversiones.estado_credito.EstadoCreditoRepository;
import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.mapper.CreditoMapper;
import com.cblandon.inversiones.utils.Constantes;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
@Slf4j
@AllArgsConstructor
public class CreditoService {
    final CreditoRepository creditoRepository;
    final ClienteRepository clienteRepository;
    final CuotaCreditoRepository cuotaCreditoRepository;
    final EstadoCreditoRepository estadoCreditoRepository;

    @Transactional()
    public RegistrarCreditoResponseDTO crearCredito(RegistrarCreditoRequestDTO registrarCreditoRequestDTO) {
        log.info("crearCredito peticion " + registrarCreditoRequestDTO);

        Cliente clienteBD = clienteRepository.findByCedula(registrarCreditoRequestDTO.getCedulaTitularCredito())
                .orElseThrow(() -> new RequestException(Constantes.CLIENTE_NO_CREADO, HttpStatus.BAD_REQUEST.value()));

        if (registrarCreditoRequestDTO.getFechaCredito().isAfter(registrarCreditoRequestDTO.getFechaCuota()) ||
                registrarCreditoRequestDTO.getFechaCredito().equals(registrarCreditoRequestDTO.getFechaCuota())) {
            throw new RequestException(Constantes.ERROR_FECHAS_CREDITO, HttpStatus.BAD_REQUEST.value());
        }

        try {

            Credito credito = Credito.builder()
                    .fechaCredito(registrarCreditoRequestDTO.getFechaCredito())
                    .valorCredito(registrarCreditoRequestDTO.getValorCredito())
                    .usuarioCreador(SecurityContextHolder.getContext().getAuthentication().getName())
                    .saldoCredito(registrarCreditoRequestDTO.getValorCredito())
                    .cliente(clienteBD)
                    .modalidad(registrarCreditoRequestDTO.getModalidad())
                    .idEstadoCredito(new EstadoCredito(Constantes.ID_CREDITO_ACTIVO, null))
                    .build();

            credito = creditoRepository.save(credito);
            Double interesPrimerCuota = calcularInteresPrimeraCuota(
                    registrarCreditoRequestDTO.getValorCredito(),
                    registrarCreditoRequestDTO.getInteresPorcentaje(),
                    registrarCreditoRequestDTO.getFechaCuota(),
                    registrarCreditoRequestDTO.getFechaCredito(),
                    credito.getModalidad().getId()
            );

            Double cuotaCapital = calcularCuotaCapital(
                    registrarCreditoRequestDTO.getValorCredito(),
                    registrarCreditoRequestDTO.getCantidadCuotas());

            double valorCuotas = cuotaCapital + (
                    registrarCreditoRequestDTO.getInteresPorcentaje() / 100) *
                    registrarCreditoRequestDTO.getValorCredito();

            double valorPrimerCuota = cuotaCapital + interesPrimerCuota;


            /*
             * cuando se registra un credito, se crea la primer cuota
             */
            if (credito.getId() != null) {

                CuotaCredito cuotaCredito = CuotaCredito.builder()
                        .fechaCuota(registrarCreditoRequestDTO.getFechaCuota())
                        .cuotaNumero(1)
                        .numeroCuotas(registrarCreditoRequestDTO.getCantidadCuotas())
                        .valorCuota(cuotaCapital + interesPrimerCuota)
                        .valorCapital(0.0)
                        .valorInteres(interesPrimerCuota)
                        .interesPorcentaje(registrarCreditoRequestDTO.getInteresPorcentaje())
                        .credito(credito)
                        .build();

                cuotaCreditoRepository.save(cuotaCredito);
            }
            RegistrarCreditoResponseDTO registrarCreditoResponseDTO = RegistrarCreditoResponseDTO.builder()
                    .cantidadCuotas(registrarCreditoRequestDTO.getCantidadCuotas().toString())
                    .fechaPago(registrarCreditoRequestDTO.getFechaCuota().toString())
                    .valorPrimerCuota(Double.toString(valorPrimerCuota))
                    .valorCredito(registrarCreditoRequestDTO.getValorCredito().toString())
                    .valorCuotas(Double.toString(valorCuotas))
                    .build();

            log.info("crearCredito ".concat(registrarCreditoResponseDTO.toString()));

            return registrarCreditoResponseDTO;
        } catch (RuntimeException ex) {
            log.error("crearCredito ".concat(ex.getMessage()));
            throw new RuntimeException(ex.getMessage());

        }

    }


    @Transactional(readOnly = true)
    public CreditoCuotasResponseDTO consultarCredito(Integer idCredito) throws NoDataException {

        Credito credito = creditoRepository.findById(idCredito)
                .orElseThrow(() -> new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value()));

        log.info("consultarCredito ".concat(credito.toString()));

        return CreditoMapper.mapperCredito.creditoToCreditoCuotasResponseDTO(credito);
    }

    /**
     * consulta credito y sus cuotas generadas
     */

    @Transactional(readOnly = true)
    public List<InfoCreditosActivosDTO> consultarInfoCreditosActivos() {
        try {

            List<Tuple> resultadoBD = creditoRepository.infoClientesConCreditosActivos();

            List<InfoCreditosActivosDTO> listaClientes = resultadoBD.stream().map(
                    info -> InfoCreditosActivosDTO.builder()
                            .idCliente(Integer.parseInt(info.get("id_cliente").toString()))
                            .idCredito(Integer.parseInt(info.get("id_credito").toString()))
                            .nombres(info.get("nombres").toString())
                            .apellidos(info.get("apellidos").toString())
                            .cedula(info.get("cedula").toString())
                            .fechaCredito(info.get("fecha_credito").toString())
                            .valorCredito(Double.parseDouble(info.get("valor_credito").toString()))
                            .build()
            ).toList();

            log.info("consultarInfoCreditosActivos ".concat(listaClientes.toString()));

            return listaClientes;

        } catch (RuntimeException ex) {
            log.error("consultarInfoCreditosActivos ".concat(ex.getMessage()));
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Transactional()
    public String modificarEstadoCredito(int idCredito, int idstadoCredito) throws NoDataException {

        Credito creditoConsultado = creditoRepository.findById(idCredito)
                .orElseThrow(() -> new NoDataException(
                        Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.NOT_FOUND.value()));

        EstadoCredito estadoCredito = estadoCreditoRepository.findById(idstadoCredito).orElseThrow(
                () -> new NoDataException(
                        Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.NOT_FOUND.value()));

        try {
            creditoConsultado.setIdEstadoCredito(estadoCredito);
            creditoRepository.save(creditoConsultado);


            return "Estado de credito ".concat(estadoCredito.getDescripcion());

        } catch (RuntimeException ex) {
            throw new RuntimeException("Estado de credito ".concat(ex.getMessage()));
        }
    }

    private double calcularCuotaCapital(Double valorPrestado, Integer cantidadCuotas) {
        double cuotaCapital = valorPrestado / cantidadCuotas;

        return Math.rint(cuotaCapital);
    }


    private double calcularInteresPrimeraCuota(
            double valorPrestado, Double interesPorcentaje,
            LocalDate fechaCuota, LocalDate fechaCredito, int codigoModalidad) {
        int diasSegunModalidad = codigoModalidad == Constantes.CODIGO_MODALIDAD_MENSUAL ? 30 : 15;
        fechaCredito = fechaCredito == null ? LocalDate.now() : fechaCredito;
        long diasDiferencia = DAYS.between(fechaCredito, fechaCuota);
        diasDiferencia = diasDiferencia == 31 ? 30 : diasDiferencia;
        double interesCredito = ((valorPrestado * (interesPorcentaje / 100) / diasSegunModalidad) * diasDiferencia);

        return Math.rint(interesCredito);
    }

}
