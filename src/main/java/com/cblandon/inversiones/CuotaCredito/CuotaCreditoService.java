package com.cblandon.inversiones.CuotaCredito;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.CuotaCredito.dto.PagarCuotaRequestDTO;
import com.cblandon.inversiones.Excepciones.NoDataException;
import com.cblandon.inversiones.Mapper.Mapper;
import com.cblandon.inversiones.Utils.Constantes;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CuotaCreditoService {
    private final CuotaCreditoRepository cuotaCreditoRepository;

    public CuotaCreditoService(CuotaCreditoRepository cuotaCreditoRepository) {
        this.cuotaCreditoRepository = cuotaCreditoRepository;
    }

    public CuotaCredito pagarCuota(Integer codigoCuota, PagarCuotaRequestDTO pagarCuotaRequestDTO)
            throws NoDataException {

        CuotaCredito cuotaCreditoDB = cuotaCreditoRepository.findById(codigoCuota)
                .orElseThrow(() -> new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, "3"));


        try {
            cuotaCreditoDB.setValorAbonado(pagarCuotaRequestDTO.getValorAbonado());
            cuotaCreditoDB.setFechaAbono(pagarCuotaRequestDTO.getFechaAbono());
            cuotaCreditoRepository.save(cuotaCreditoDB);

            return cuotaCreditoRepository.save(cuotaCreditoDB);

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }
}
