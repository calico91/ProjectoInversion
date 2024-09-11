package com.cblandon.inversiones.permiso.service;

import com.cblandon.inversiones.permiso.entity.Permiso;
import com.cblandon.inversiones.permiso.repository.PermisoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PermisoService {


    private final PermisoRepository permisoRepository;


    public PermisoService(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    @PostConstruct
    public void init() {
        crearPermisos();
    }

    private void crearPermisos() {
        List<Permiso> permisos = new ArrayList<>(Arrays.asList(
                new Permiso(101, "Clientes/registrar", "registrar-cliente"),
                new Permiso(102, "Clientes/consultar todos los clientes", "consultar-clientes"),
                new Permiso(103, "Clientes/consultar clientes por cedula", "consultar-cliente-por-cedula"),
                new Permiso(104, "Clientes/actualizar clientes", "actualizar-cliente"),

                new Permiso(201, "Creditos/crear o renovar credito", "registrar-credito"),
                new Permiso(202, "Creditos/consultar creditos", "consultar-creditos-activos"),
                new Permiso(203, "Creditos/modificar estado de credito", "modificar-estado-credito"),
                new Permiso(204, "Credito/modificar fecha pago", "modificar-fecha-pago"),
                new Permiso(205, "Credito/consultar informacion de credito", "consultar-credito-saldo"),
                new Permiso(206, "Credito/consultar abonos realizados", "consultar-abonos-realizados"),
                new Permiso(206, "Credito/anular ultimo abono", "anular-ultimo-abono"),
                new Permiso(207, "Credito/saldarCredito", "saldar-credito"),
                new Permiso(208, "Credito/abonar interes, capital o pagar cuota", "pagar-cuota"),
                new Permiso(209, "Credito/consultar abono realizado", "consultar-abono-por-id"),

                new Permiso(301, "Inicio/consultar cuota cliente", "consultar-cuota-cliente"),
                new Permiso(302, "Inicio/consultar cuotas pendientes", "consultar-cuotas-por-fecha")
        ));

        List<Permiso> permisosActuales = permisoRepository.findAll();

        if (permisosActuales.size() != permisos.size()) {
            permisoRepository.saveAll(permisos);

        }
    }
}
