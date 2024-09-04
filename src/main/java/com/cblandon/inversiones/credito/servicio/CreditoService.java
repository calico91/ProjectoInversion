package com.cblandon.inversiones.credito.servicio;

import com.cblandon.inversiones.cliente.entity.Cliente;
import com.cblandon.inversiones.cliente.repository.ClienteRepository;
import com.cblandon.inversiones.credito.dto.*;
import com.cblandon.inversiones.credito.entity.Credito;
import com.cblandon.inversiones.credito.repository.CreditoRepository;
import com.cblandon.inversiones.cuota_credito.repository.CuotaCreditoRepository;
import com.cblandon.inversiones.cuota_credito.entity.CuotaCredito;
import com.cblandon.inversiones.estado_credito.entity.EstadoCredito;
import com.cblandon.inversiones.estado_credito.repository.EstadoCreditoRepository;
import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.mapper.CreditoMapper;
import com.cblandon.inversiones.user.entity.UserEntity;
import com.cblandon.inversiones.user.repository.UserRepository;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.MensajesErrorEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
@Slf4j
@AllArgsConstructor
public class CreditoService {
    private final UserRepository userRepository;
    private final CreditoRepository creditoRepository;
    private final ClienteRepository clienteRepository;
    private final CuotaCreditoRepository cuotaCreditoRepository;
    private final EstadoCreditoRepository estadoCreditoRepository;

    /**
     * metodo para crear y renovar creditos
     */
    @Transactional()
    public RegistrarCreditoResponseDTO registrarRenovarCredito(RegistrarCreditoRequestDTO registrarCreditoRequestDTO) {
        log.info("crearCredito: {}", registrarCreditoRequestDTO);

        Cliente clienteBD = clienteRepository.findById(registrarCreditoRequestDTO.idCliente())
                .orElseThrow(() -> new RequestException(MensajesErrorEnum.CLIENTE_NO_CREADO));


        Set<UserEntity> usuarios = userRepository.buscarUsuariosAdmin(registrarCreditoRequestDTO.usuario());

        if (registrarCreditoRequestDTO.fechaCredito().isAfter(registrarCreditoRequestDTO.fechaCuota()) ||
                registrarCreditoRequestDTO.fechaCredito().equals(registrarCreditoRequestDTO.fechaCuota())) {
            throw new RequestException(MensajesErrorEnum.ERROR_FECHAS_CREDITO);
        }

        if (registrarCreditoRequestDTO.renovacion()) {

            if (registrarCreditoRequestDTO.valorRenovacion() <= 0) {
                throw new RequestException(MensajesErrorEnum.ERROR_RENOVAR_CREDITO_POR_MONTO);
            }

            Credito pagarCredito = creditoRepository.findById(registrarCreditoRequestDTO.idCreditoActual())
                    .orElseThrow(() -> new RequestException(MensajesErrorEnum.ERROR_RENOVAR_CREDITO));

            pagarCredito.setIdEstadoCredito(new EstadoCredito(2, null));
            pagarCredito.setSaldoCredito(0.0);
            creditoRepository.save(pagarCredito);
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
                    .usuarios(usuarios)
                    .valorRenovacion(registrarCreditoRequestDTO.valorRenovacion())
                    .build();
            credito = creditoRepository.save(credito);

            // si la modalidad es quincenal el interes se divide en 2
            Double interesPorcentaje = registrarCreditoRequestDTO.modalidad().getId() == 1
                    ? registrarCreditoRequestDTO.interesPorcentaje()
                    : registrarCreditoRequestDTO.interesPorcentaje() / 2;

            Double interesPrimerCuota = calcularInteresPrimeraCuota(
                    registrarCreditoRequestDTO.valorCredito(),
                    interesPorcentaje,
                    registrarCreditoRequestDTO.fechaCuota(),
                    registrarCreditoRequestDTO.fechaCredito(),
                    credito.getModalidad().getId()
            );

            Double cuotaCapital = calcularCuotaCapital(
                    registrarCreditoRequestDTO.valorCredito(),
                    registrarCreditoRequestDTO.cantidadCuotas());

            double valorCuotas = cuotaCapital + (
                    interesPorcentaje / 100) *
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
                        .interesPorcentaje(interesPorcentaje)
                        .credito(credito)
                        .build();

                cuotaCreditoRepository.save(cuotaCredito);
            }
            return RegistrarCreditoResponseDTO.builder()
                    .cantidadCuotas(registrarCreditoRequestDTO.cantidadCuotas().toString())
                    .fechaPago(registrarCreditoRequestDTO.fechaCuota().toString())
                    .valorPrimerCuota(Double.toString(valorPrimerCuota))
                    .valorCredito(registrarCreditoRequestDTO.valorCredito().toString())
                    .valorCuotas(Double.toString(valorCuotas))
                    .build();

        } catch (RuntimeException ex) {
            log.error("crearCredito: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());

        }

    }


    @Transactional(readOnly = true)
    public CreditoCuotasResponseDTO consultarCredito(Integer idCredito) throws NoDataException {
        try {
            Credito credito = creditoRepository.findById(idCredito)
                    .orElseThrow(() -> new NoDataException(MensajesErrorEnum.DATOS_NO_ENCONTRADOS));

            log.info("consultarCredito: {}", credito.toString());

            return CreditoMapper.mapperCredito.creditoToCreditoCuotasResponseDTO(credito);
        } catch (RuntimeException ex) {
            log.error("consultarCredito: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }

    /**
     * consulta credito y sus cuotas generadas
     */
    @Transactional(readOnly = true)
    public List<CreditosActivosDTO> consultarCreditosActivos() {
        try {

            List<CreditosActivosDTO> listaClientes = creditoRepository.consultarClientesConCreditosActivos();

            log.info("consultarInfoCreditosActivos: {}", listaClientes.toString());

            return listaClientes;

        } catch (RuntimeException ex) {
            log.error("consultarInfoCreditosActivos: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Transactional()
    public String modificarEstadoCredito(int idCredito, int idstadoCredito) throws NoDataException {
        log.error("modificarEstadoCredito: {}", idCredito);
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
            log.error("modificarEstadoCredito: {}", ex.getMessage());
            throw new RuntimeException("Estado de credito ".concat(ex.getMessage()));
        }
    }

    public SaldarCreditoResponseDTO saldar(SaldarCreditoDTO saldarCreditoDTO) throws NoDataException {
        log.error("saldarCredito: {}", saldarCreditoDTO);

        Credito creditoConsultado = creditoRepository.findById(saldarCreditoDTO.idCredito())
                .orElseThrow(() -> new NoDataException(
                        MensajesErrorEnum.DATOS_NO_ENCONTRADOS));

        if (creditoConsultado.getSaldoCredito() > saldarCreditoDTO.valorPagado()) {
            throw new RequestException(MensajesErrorEnum.ERROR_SALDAR_CREDITO);
        }

        try {
            CuotaCredito ultimaCuota = creditoConsultado.getListaCuotasCredito().get(
                    creditoConsultado.getListaCuotasCredito().size() - 1);

            ultimaCuota.setValorInteres(saldarCreditoDTO.valorPagado() - creditoConsultado.getSaldoCredito());
            ultimaCuota.setValorCapital(creditoConsultado.getSaldoCredito());
            ultimaCuota.setValorAbonado(saldarCreditoDTO.valorPagado());
            ultimaCuota.setFechaAbono(LocalDateTime.now());
            ultimaCuota.setTipoAbono("AC");
            ultimaCuota.setAbonoExtra(true);


            creditoConsultado.setIdEstadoCredito(new EstadoCredito(2));
            creditoConsultado.setSaldoCredito(0.0);
            creditoConsultado.setListaCuotasCredito(new ArrayList<>(List.of(ultimaCuota)));
            creditoRepository.save(creditoConsultado);


            return new SaldarCreditoResponseDTO(
                    saldarCreditoDTO.idCredito(),
                    saldarCreditoDTO.valorPagado(),
                    creditoConsultado.getCliente().getNombres() + " " + creditoConsultado.getCliente().getApellidos()
            );

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
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
