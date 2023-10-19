package com.cblandon.inversiones.Credito;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Cliente.ClienteRepository;
import com.cblandon.inversiones.Cliente.dto.ClienteAllResponseDTO;
import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Credito.dto.CreditoAllResponseDTO;
import com.cblandon.inversiones.Credito.dto.CreditoCuotasResponseDTO;
import com.cblandon.inversiones.Credito.dto.RegistrarCreditoRequestDTO;
import com.cblandon.inversiones.Credito.dto.RegistrarCreditoResponseDTO;
import com.cblandon.inversiones.CuotaCredito.CuotaCreditoRepository;
import com.cblandon.inversiones.CuotaCredito.CuotaCredito;
import com.cblandon.inversiones.Excepciones.NoDataException;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.CreditoMapper;
import com.cblandon.inversiones.Mapper.Mapper;
import com.cblandon.inversiones.Utils.Constantes;
import com.cblandon.inversiones.Utils.GenericMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            throw new RequestException(Constantes.CLIENTE_NO_CREADO, "1");
        }
        try {

            Credito credito = Mapper.mapper.registrarCreditoRequestDTOToCredito(registrarCreditoRequestDTO);

            Double interesPrimerCuota = calcularInteresPrimeraCuota(
                    registrarCreditoRequestDTO.getCantidadPrestada(),
                    registrarCreditoRequestDTO.getInteresPorcentaje(),
                    registrarCreditoRequestDTO.getFechaCuota(),
                    registrarCreditoRequestDTO.getFechaCredito()
            );

            Double cuotaCapital = calcularCuotaCapital(
                    registrarCreditoRequestDTO.getCantidadPrestada(),
                    registrarCreditoRequestDTO.getCantidadCuotas());

            credito.setUsuarioCreador(SecurityContextHolder.getContext().getAuthentication().getName());
            credito.setEstadoCredito(Constantes.CREDITO_ACTIVO);
            credito.setCliente(clienteBD);

            credito = creditoRepository.save(credito);

            /// cuando se registra un credito, se crea la primer cuota
            if (credito.getId() != null) {


                CuotaCredito cuotaCredito = CuotaCredito.builder()
                        .fechaCuota(registrarCreditoRequestDTO.getFechaCuota())
                        .cuotaNumero(1)
                        .numeroCuotas(registrarCreditoRequestDTO.getCantidadCuotas())
                        .valorCuota(cuotaCapital + interesPrimerCuota)
                        .valorCapital(cuotaCapital)
                        .valorCredito(registrarCreditoRequestDTO.getCantidadPrestada())
                        .valorInteres(interesPrimerCuota)
                        .interesPorcentaje(registrarCreditoRequestDTO.getInteresPorcentaje())
                        .credito(credito)
                        .build();

                cuotaCreditoRepository.save(cuotaCredito);
            }

            return Mapper.mapper.creditoToRegistrarCreditoResponseDTO(credito);
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());

        }

    }

    public List<CreditoAllResponseDTO> allCreditos() {

        try {
            List<Credito> creditos = creditoRepository.findAll();
            List<CreditoAllResponseDTO> CreditoAllResponseDTO = creditos.stream().map(
                    credito -> Mapper.mapper.creditoToCreditoAllResponseDTO(credito)).collect(Collectors.toList());

            return CreditoAllResponseDTO;
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    /// consulta credito y sus cuotas generadas
    @Transactional(readOnly = true)
    public CreditoCuotasResponseDTO consultarCredito(Long idCredito) throws NoDataException {

        Credito credito = creditoRepository.findById(idCredito)
                .orElseThrow(() -> new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, "3"));

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

    private Double calcularCuotaCapital(Double valorPrestado, Integer cantidadCuotas) {
        Double cuotaCapital = valorPrestado / cantidadCuotas;

        return Math.rint(cuotaCapital);
    }

    private Double calcularInteresPrimeraCuota(
            Double valorPrestado, Double interesPorcentaje, LocalDate fechaCuota, LocalDate fechaCredito) {
        fechaCredito = fechaCredito == null ? LocalDate.now() : fechaCredito;
        Long diasDiferencia = DAYS.between(fechaCredito, fechaCuota);
        Double interesCredito = ((valorPrestado * (interesPorcentaje / 100) / 30) * diasDiferencia);

        return Math.rint(interesCredito);
    }

}
