package com.cblandon.inversiones.Credito;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Cliente.ClienteRepository;
import com.cblandon.inversiones.Credito.dto.RegistrarCreditoRequestDTO;
import com.cblandon.inversiones.Credito.dto.RegistrarCreditoResponseDTO;
import com.cblandon.inversiones.CuotaCredito.CuotaCreditoRepository;
import com.cblandon.inversiones.CuotaCredito.CuotaCredito;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.Mapper;
import com.cblandon.inversiones.Utils.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
            throw new RequestException(Constantes.CLIENTE_NO_CREADO, "1");
        }
        try {

            Credito credito = Mapper.mapper.registrarCreditoRequestDTOToCredito(registrarCreditoRequestDTO);
            credito.setSaldo((calcularInteresCredito(
                    registrarCreditoRequestDTO.getCantidadPrestada(),
                    registrarCreditoRequestDTO.getInteresPorcentaje()))
                    + registrarCreditoRequestDTO.getCantidadPrestada());
            credito.setUsuarioCreador(SecurityContextHolder.getContext().getAuthentication().getName());
            credito.setEstadoCredito(Constantes.CREDITO_ACTIVO);
            credito.setCliente(clienteBD);

            credito = creditoRepository.save(credito);
            if (credito.getId() != null) {

                Double interesPrimerCuota = calcularInteresPrimeraCuota(
                        registrarCreditoRequestDTO.getCantidadPrestada(),
                        registrarCreditoRequestDTO.getInteresPorcentaje(),
                        registrarCreditoRequestDTO.getFechaCuota());

                CuotaCredito cuotaCredito = CuotaCredito.builder()
                        .fechaCuota(registrarCreditoRequestDTO.getFechaCuota())
                        .cuotaNumero(1)
                        .numeroCuotas(registrarCreditoRequestDTO.getCantidadCuotas())
                        .valorCuota(calcularValorPrimeraCuota(
                                registrarCreditoRequestDTO.getCantidadPrestada(),
                                registrarCreditoRequestDTO.getCantidadCuotas()) + interesPrimerCuota)
                        .valorCapital(calcularCuotaCapital(
                                registrarCreditoRequestDTO.getCantidadPrestada(),
                                registrarCreditoRequestDTO.getCantidadCuotas()))
                        .valorInteres(interesPrimerCuota)
                        .credito(credito)
                        .build();

                cuotaCreditoRepository.save(cuotaCredito);
            }

            return Mapper.mapper.creditoToRegistrarCreditoResponseDTO(credito);
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());

        }

    }


    private Double calcularCuotaCapital(Double valorPrestado, Integer cantidadCuotas) {
        Double cuotaCapital = valorPrestado / cantidadCuotas;

        return Math.rint(cuotaCapital);
    }

    private Double calcularInteresCredito(Double valorPrestado, Double interesPorcentaje) {
        Double interesCredito = valorPrestado * (interesPorcentaje / 100);

        return Math.rint(interesCredito);
    }

    private Double calcularInteresPrimeraCuota(Double valorPrestado, Double interesPorcentaje, LocalDate fechaCuota) {
        Long diasDiferencia = DAYS.between(LocalDate.now(), fechaCuota);
        Double interesCredito = ((valorPrestado * (interesPorcentaje / 100) / 30) * diasDiferencia);

        return Math.rint(interesCredito);
    }

    private Double calcularValorPrimeraCuota(Double valorPrestado, Integer cantidadCuotas) {
        Double valorCuota = (valorPrestado / cantidadCuotas);

        return Math.rint(valorCuota);
    }

}
