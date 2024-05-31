package com.cblandon.inversiones.roles.service;

import com.cblandon.inversiones.permiso.entity.Permiso;
import com.cblandon.inversiones.permiso.repository.PermisoRepository;
import com.cblandon.inversiones.roles.Role;
import com.cblandon.inversiones.roles.entity.Roles;
import com.cblandon.inversiones.roles.repository.RolesRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RolesService {

    private final PermisoRepository permisoRepository;
    private final RolesRepository rolesRepository;


    public RolesService(PermisoRepository permisoRepository, RolesRepository rolesRepository) {
        this.permisoRepository = permisoRepository;
        this.rolesRepository = rolesRepository;
    }

    @PostConstruct
    public void init() {
        asignarPermisosUsuarioAdmin();
    }

    private void asignarPermisosUsuarioAdmin() {
        Set<Permiso> permisos = Set.copyOf(permisoRepository.findAll());
        Roles rolAdmin = rolesRepository.findByName(Role.ADMIN).orElse(new Roles());


        if (rolAdmin.getPermisos().isEmpty()) {

            rolAdmin.setPermisos(permisos);
            rolesRepository.save(rolAdmin);
        }
    }
}
