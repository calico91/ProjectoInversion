package com.cblandon.inversiones.credito.servicio;

import com.cblandon.inversiones.cliente.entity.Cliente;
import com.cblandon.inversiones.cliente.repository.ClienteRepository;
import com.cblandon.inversiones.credito.dto.*;
import com.cblandon.inversiones.credito.entity.Credito;
import com.cblandon.inversiones.credito.repository.CreditoRepository;
import com.cblandon.inversiones.cuotacredito.repository.CuotaCreditoRepository;
import com.cblandon.inversiones.cuotacredito.entity.CuotaCredito;
import com.cblandon.inversiones.estado_credito.entity.EstadoCredito;
import com.cblandon.inversiones.estado_credito.repository.EstadoCreditoRepository;
import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.mapper.CreditoMapper;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.MensajesErrorEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        Cliente clienteBD = clienteRepository.findByCedula(registrarCreditoRequestDTO.cedulaTitularCredito())
                .orElseThrow(() -> new RequestException(MensajesErrorEnum.CLIENTE_NO_CREADO));

        if (registrarCreditoRequestDTO.fechaCredito().isAfter(registrarCreditoRequestDTO.fechaCuota()) ||
                registrarCreditoRequestDTO.fechaCredito().equals(registrarCreditoRequestDTO.fechaCuota())) {
            throw new RequestException(MensajesErrorEnum.ERROR_FECHAS_CREDITO);
        }

        try {

            Credito credito = Credito.builder()
                    .fechaCredito(registrarCreditoRequestDTO.fechaCredito())
                    .valorCredito(registrarCreditoRequestDTO.valorCredito())
                    .usuarioCreador(SecurityContextHolder.getContext().getAuthentication().getName())
                    .saldoCredito(registrarCreditoRequestDTO.valorCredito())
                    .cliente(clienteBD)
                    .modalidad(registrarCreditoRequestDTO.modalidad())
                    .idEstadoCredito(new EstadoCredito(Constantes.ID_CREDITO_ACTIVO, null))
                    .build();

            credito = creditoRepository.save(credito);
            Double interesPrimerCuota = calcularInteresPrimeraCuota(
                    registrarCreditoRequestDTO.valorCredito(),
                    registrarCreditoRequestDTO.interesPorcentaje(),
                    registrarCreditoRequestDTO.fechaCuota(),
                    registrarCreditoRequestDTO.fechaCredito(),
                    credito.getModalidad().getId()
            );

            Double cuotaCapital = calcularCuotaCapital(
                    registrarCreditoRequestDTO.valorCredito(),
                    registrarCreditoRequestDTO.cantidadCuotas());

            double valorCuotas = cuotaCapital + (
                    registrarCreditoRequestDTO.interesPorcentaje() / 100) *
                    registrarCreditoRequestDTO.valorCredito();

            double valorPrimerCuota = cuotaCapital + interesPrimerCuota;


            /*
             * cuando se registra un credito, se crea la primer cuota
             */
            if (credito.getId() != null) {

                CuotaCredito cuotaCredito = CuotaCredito.builder()
                        .fechaCuota(registrarCreditoRequestDTO.fechaCuota())
                        .cuotaNumero(1)
                        .numeroCuotas(registrarCreditoRequestDTO.cantidadCuotas())
                        .valorCuota(cuotaCapital + interesPrimerCuota)
                        .valorCapital(0.0)
                        .valorInteres(interesPrimerCuota)
                        .interesPorcentaje(registrarCreditoRequestDTO.interesPorcentaje())
                        .credito(credito)
                        .build();

                cuotaCreditoRepository.save(cuotaCredito);
            }
            RegistrarCreditoResponseDTO registrarCreditoResponseDTO = RegistrarCreditoResponseDTO.builder()
                    .cantidadCuotas(registrarCreditoRequestDTO.cantidadCuotas().toString())
                    .fechaPago(registrarCreditoRequestDTO.fechaCuota().toString())
                    .valorPrimerCuota(Double.toString(valorPrimerCuota))
                    .valorCredito(registrarCreditoRequestDTO.valorCredito().toString())
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
                .orElseThrow(() -> new NoDataException(MensajesErrorEnum.DATOS_NO_ENCONTRADOS));

        log.info("consultarCredito ".concat(credito.toString()));

        return CreditoMapper.mapperCredito.creditoToCreditoCuotasResponseDTO(credito);
    }

    /**
     * consulta credito y sus cuotas generadas
     */
    @Transactional(readOnly = true)
    public List<CreditosActivosDTO> consultarCreditosActivos() {
        try {

            List<CreditosActivosDTO> listaClientes = creditoRepository.consultarClientesConCreditosActivos();

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
                        MensajesErrorEnum.DATOS_NO_ENCONTRADOS));

        EstadoCredito estadoCredito = estadoCreditoRepository.findById(idstadoCredito).orElseThrow(
                () -> new NoDataException(
                        MensajesErrorEnum.DATOS_NO_ENCONTRADOS));

        try {
            creditoConsultado.setIdEstadoCredito(estadoCredito);
            creditoRepository.save(creditoConsultado);

            return "Estado de credito ".concat(estadoCredito.getDescripcion());

        } catch (RuntimeException ex) {
            log.error("modificarEstadoCredito");
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

        return Math.rint(((valorPrestado * (interesPorcentaje / 100) / diasSegunModalidad) * diasDiferencia));
    }

}
