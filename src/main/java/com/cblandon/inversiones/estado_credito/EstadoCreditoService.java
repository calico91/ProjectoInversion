package com.cblandon.inversiones.estado_credito;

import com.cblandon.inversiones.utils.Constantes;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EstadoCreditoService {

    private final EstadoCreditoRepository estadoCreditoRepository;

    @PostConstruct
    public void init() {
        insertarDatos();
    }

    private void insertarDatos() {
        List<EstadoCredito> estadoCreditoBD = estadoCreditoRepository.findAll();

        if (estadoCreditoBD.isEmpty()) {

            List<EstadoCredito> estadoCreditoList = new ArrayList<>();
            estadoCreditoList.add(new EstadoCredito(1, Constantes.CREDITO_ACTIVO_INSERT));
            estadoCreditoList.add(new EstadoCredito(2, Constantes.CREDITO_PAGADO_INSERT));
            estadoCreditoList.add(new EstadoCredito(3, Constantes.CREDITO_ANULADO_INSERT));

            estadoCreditoRepository.saveAll(estadoCreditoList);

        }
    }
}
