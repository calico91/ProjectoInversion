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
                new Permiso(1, "registrar-cliente"),
                new Permiso(2, "consultar-clientes"),
                new Permiso(3, "consultar-cliente-por-cedula"),
                new Permiso(4, "consultar-cuotas-por-fecha"),
                new Permiso(5, "actualizar-cliente"),
                new Permiso(6, "registrar-credito"),
                new Permiso(7, "consultar-credito"),
                new Permiso(8, "consultar-creditos-activos"),
                new Permiso(9, "modificar-estado-credito"),
                new Permiso(10, "pagar-cuota"),
                new Permiso(11, "consultar-cuota-cliente"),
                new Permiso(12, "consultar-credito-saldo"),
                new Permiso(13, "modificar-fecha-pago"),
                new Permiso(14, "generar-reporte-interes-capital"),
                new Permiso(15, "consultar-abonos-realizados"),
                new Permiso(16, "generar-reporte-ultimos-abonos-realizados"),
                new Permiso(17, "consultar-abono-por-id"),
                new Permiso(18, "anular-ultimo-abono"),
                new Permiso(19, "asignar-permisos")

        ));

        List<Permiso> permisosActuales = permisoRepository.findAll();

        if (permisosActuales.size() != permisos.size()) {
            permisoRepository.saveAll(permisos);

        }
    }
}
