package com.cblandon.inversiones.Credito;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Cliente.ClienteRepository;
import com.cblandon.inversiones.Credito.dto.*;
import com.cblandon.inversiones.CuotaCredito.CuotaCreditoRepository;
import com.cblandon.inversiones.CuotaCredito.CuotaCredito;
import com.cblandon.inversiones.Excepciones.NoDataException;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.CreditoMapper;
import com.cblandon.inversiones.Mapper.Mapper;
import com.cblandon.inversiones.Utils.Constantes;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
@Slf4j
public class CreditoService {
    @Autowired
    CreditoRepository creditoRepository;

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    CuotaCreditoRepository cuotaCreditoRepository;


    public RegistrarCreditoResponseDTO crearCredito(RegistrarCreditoRequestDTO registrarCreditoRequestDTO) {
        Cliente clienteBD = clienteRepository.findByCedula(registrarCreditoRequestDTO.getCedulaTitularCredito());

        if (clienteBD == null) {
            log.error(Constantes.CLIENTE_NO_CREADO);
            throw new RequestException(Constantes.CLIENTE_NO_CREADO, HttpStatus.BAD_REQUEST.value());
        }
        if (registrarCreditoRequestDTO.getFechaCredito().isAfter(registrarCreditoRequestDTO.getFechaCuota()) ||
                registrarCreditoRequestDTO.getFechaCredito().equals(registrarCreditoRequestDTO.getFechaCuota())) {

            log.error(Constantes.ERROR_FECHAS_CREDITO);
            throw new RequestException(Constantes.ERROR_FECHAS_CREDITO, HttpStatus.BAD_REQUEST.value());
        }

        try {

            Credito credito = Credito.builder()
                    .modalidad(registrarCreditoRequestDTO.getModalidad())
                    .fechaCredito(registrarCreditoRequestDTO.getFechaCredito())
                    .valorCredito(registrarCreditoRequestDTO.getValorCredito())
                    .usuarioCreador(SecurityContextHolder.getContext().getAuthentication().getName())
                    .estadoCredito(Constantes.CREDITO_ACTIVO)
                    .cliente(clienteBD)
                    .build();

            credito = creditoRepository.save(credito);

            Double interesPrimerCuota = calcularInteresPrimeraCuota(
                    registrarCreditoRequestDTO.getValorCredito(),
                    registrarCreditoRequestDTO.getInteresPorcentaje(),
                    registrarCreditoRequestDTO.getFechaCuota(),
                    registrarCreditoRequestDTO.getFechaCredito(),
                    registrarCreditoRequestDTO.getModalidad()
            );

            Double cuotaCapital = calcularCuotaCapital(
                    registrarCreditoRequestDTO.getValorCredito(),
                    registrarCreditoRequestDTO.getCantidadCuotas());

            double valorCuotas = cuotaCapital + (
                    registrarCreditoRequestDTO.getInteresPorcentaje() / 100) *
                    registrarCreditoRequestDTO.getValorCredito();

            double valorPrimerCuota = cuotaCapital + interesPrimerCuota;


            /// cuando se registra un credito, se crea la primer cuota
            if (credito.getId() != null) {

                CuotaCredito cuotaCredito = CuotaCredito.builder()
                        .fechaCuota(registrarCreditoRequestDTO.getFechaCuota())
                        .cuotaNumero(1)
                        .numeroCuotas(registrarCreditoRequestDTO.getCantidadCuotas())
                        .valorCuota(cuotaCapital + interesPrimerCuota)
                        .valorCapital(0.0)
                        .valorCredito(registrarCreditoRequestDTO.getValorCredito())
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

            log.info(registrarCreditoResponseDTO.toString());

            return registrarCreditoResponseDTO;
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());

        }

    }

    public List<CreditoAllResponseDTO> allCreditos() {

        try {
            List<Credito> creditos = creditoRepository.findByEstadoCreditoEquals("A");

            return creditos.stream().map(
                    Mapper.mapper::creditoToCreditoAllResponseDTO).collect(Collectors.toList());
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    /// consulta credito y sus cuotas generadas
    @Transactional(readOnly = true)
    public CreditoCuotasResponseDTO consultarCredito(Integer idCredito) throws NoDataException {

        Credito credito = creditoRepository.findById(idCredito)
                .orElseThrow(() -> new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value()));

        /*mapeo manual sin utilizar Mapper
        List<Credito> listaCreditos = creditoRepository.listaCreditosCliente(clienteBD.getId());

        List<CreditoResponseDTO> listaCreditosdto = listaCreditos.stream().map(
                credito -> CreditoResponseDTO.builder()
                        .idCredito(credito.getId())
                        .cantidadPrestada(credito.getCantidadPrestada())
                        .valorCuota(credito.getValorCuota())
                        .cantidadCuotas(credito.getCantidadCuotas())
                        .build()
        ).collect(Collectors.toList());*/

        return CreditoMapper.mapperCredito.creditoToCreditoCuotasResponseDTO(credito);

    }

    /// consulta informacion de los creditos activos y algunos datos del cliente
    public List<InfoCreditosActivosDTO> infoCreditosActivos() {
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
            ).collect(Collectors.toList());
            log.info(listaClientes.toString());

            return listaClientes;

        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }

    private double calcularCuotaCapital(Double valorPrestado, Integer cantidadCuotas) {
        double cuotaCapital = valorPrestado / cantidadCuotas;

        return Math.rint(cuotaCapital);
    }


    private double calcularInteresPrimeraCuota(
            double valorPrestado, Double interesPorcentaje, LocalDate fechaCuota, LocalDate fechaCredito, String modalidad) {
        int diasSegunModalidad = modalidad.equals(Constantes.MODALIDAD_MENSUAL) ? 30 : 15;
        fechaCredito = fechaCredito == null ? LocalDate.now() : fechaCredito;
        long diasDiferencia = DAYS.between(fechaCredito, fechaCuota);
        diasDiferencia = diasDiferencia == 31 ? 30 : diasDiferencia;
        double interesCredito = ((valorPrestado * (interesPorcentaje / 100) / diasSegunModalidad) * diasDiferencia);

        return Math.rint(interesCredito);
    }

}
