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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
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
            credito.setCliente(clienteBD);

            credito = creditoRepository.save(credito);

            if (credito.getId() != null) {
                CuotaCredito cuotaCredito = CuotaCredito.builder()
                        .fechaCuota(registrarCreditoRequestDTO.getFechaCuota())
                        .cuotaNumero(1)
                        .numeroCuotas(registrarCreditoRequestDTO.getCantidadCuotas())
                        .valorCuota(calcularValorCuota(
                                registrarCreditoRequestDTO.getCantidadPrestada(),
                                registrarCreditoRequestDTO.getCantidadCuotas(),
                                registrarCreditoRequestDTO.getInteresPorcentaje()))
                        .valorCapital(calcularCuotaCapital(
                                registrarCreditoRequestDTO.getCantidadPrestada(),
                                registrarCreditoRequestDTO.getCantidadCuotas()))
                        .valorInteres(calcularInteresCredito(
                                registrarCreditoRequestDTO.getCantidadPrestada(),
                                registrarCreditoRequestDTO.getInteresPorcentaje()))
                        .credito(credito)
                        .build();

                cuotaCreditoRepository.save(cuotaCredito);
            }


            return Mapper.mapper.creditoToRegistrarCreditoResponseDTO(credito);
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());


        }


    }

    private Double calcularValorCuota(Double valorPrestado, Integer cantidadCuotas, Double interesPorcentaje) {
        Double valorCuota = (valorPrestado / cantidadCuotas) + ((interesPorcentaje / 100) * valorPrestado);
        return Math.rint(valorCuota);
    }

    private Double calcularCuotaCapital(Double valorPrestado, Integer cantidadCuotas) {
        Double cuotaCapital = valorPrestado / cantidadCuotas;
        return Math.rint(cuotaCapital);
    }

    private Double calcularInteresCredito(Double valorPrestado, Double interesPorcentaje) {
        Double interesCredito = valorPrestado * (interesPorcentaje / 100);
        return Math.rint(interesCredito);
    }

    /*private LocalDate calcularFechaCuota() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ld = LocalDate.parse(new Date().toString(), dtf);
        int monthDays = ld.lengthOfMonth();
        int yearDays = ld.lengthOfYear();
        int year = ld.getYear();
        int month = ld.getMonthValue();
        System.out.printf("Mes % 4d de %d tiene %d días%nAño %d tiene %d días",
                month, year, monthDays,
                year, yearDays);
    }*/

}
