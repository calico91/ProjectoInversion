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

        if (clienteRepository.findByCedula(registrarCreditoRequestDTO.getCedulaTitularCredito()) == null) {
            throw new RequestException(
                    Constantes.CLIENTE_NO_CREADO, "1");
        }

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
        credito.setCedulaTitularCredito(registrarCreditoRequestDTO.getCedulaTitularCredito());
        credito.setUsuarioCreador(SecurityContextHolder.getContext().getAuthentication().getName());
        credito.setCuotasCanceladas(1);
        credito.setDiaPago(registrarCreditoRequestDTO.getDiaPago());

        creditoRepository.save(credito);


        ));


        Cliente cliente = Mapper.mapper.registrarClienteDTOToCliente(registrarClienteDTO);
        cliente.setUsuariocreador(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(cliente);
        cliente.
        /// el repository devuelve un cliente y con el mapper lo convierto a dtoresponse
        return Mapper.mapper.clienteToClienteResponseDto(clienteRepository.save(cliente));


    }

    private Double calcularValorCuota(Double valorPrestado, Integer cantidadCuotas, Double interesPorcentaje) {
        Double valorCuota = (valorPrestado / cantidadCuotas) + ((interesPorcentaje / 100) + valorPrestado);
        return valorCuota;
    }

    private Double calcularCuotaCapital(Double valorPrestado, Integer cantidadCuotas) {
        Double cuotaCapital = valorPrestado / cantidadCuotas;
        return cuotaCapital;
    }

    private Double calcularInteresCredito(Double valorPrestado, Double interesPorcentaje) {
        Double interesCredito = valorPrestado * (interesPorcentaje / 100);
        return interesCredito;
    }

}
