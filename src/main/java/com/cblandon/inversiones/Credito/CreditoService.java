package com.cblandon.inversiones.Credito;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Cliente.ClienteRepository;
import com.cblandon.inversiones.Credito.dto.RegistrarCreditoRequestDTO;
import com.cblandon.inversiones.Credito.dto.RegistrarCreditoResponseDTO;
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


    public RegistrarCreditoResponseDTO crearCredito(RegistrarCreditoRequestDTO registrarCreditoRequestDTO) {
        Cliente clienteBD = clienteRepository.findByCedula(registrarCreditoRequestDTO.getCedulaTitularCredito());
        if (clienteBD == null) {
            throw new RequestException(
                    Constantes.CLIENTE_NO_CREADO, "1");
        }
        try {
            Credito credito = Mapper.mapper.registrarCreditoRequestDTOToCredito(registrarCreditoRequestDTO);


            credito.setValorCuota(calcularValorCuota(
                    registrarCreditoRequestDTO.getCantidadPrestada(),
                    registrarCreditoRequestDTO.getCantidadCuotas(),
                    registrarCreditoRequestDTO.getInteresPorcentaje()));
            credito.setCuotaCapital(calcularCuotaCapital(registrarCreditoRequestDTO.getCantidadPrestada(),
                    registrarCreditoRequestDTO.getCantidadCuotas()));
            credito.setInteresCredito(calcularInteresCredito(registrarCreditoRequestDTO.getCantidadPrestada(),
                    registrarCreditoRequestDTO.getInteresPorcentaje()));
            credito.setSaldo(1.0);
            credito.setUsuarioCreador(SecurityContextHolder.getContext().getAuthentication().getName());
            credito.setCliente(clienteBD);
            return Mapper.mapper.creditoToRegistrarCreditoResponseDTO(creditoRepository.save(credito));
        }catch (RuntimeException ex){
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

}
