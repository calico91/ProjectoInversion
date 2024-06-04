package com.cblandon.inversiones.roles.service;

import com.cblandon.inversiones.permiso.entity.Permiso;
import com.cblandon.inversiones.permiso.repository.PermisoRepository;
import com.cblandon.inversiones.roles.Role;
import com.cblandon.inversiones.roles.entity.Roles;
import com.cblandon.inversiones.roles.repository.RolesRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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

        rolAdmin.setPermisos(permisos);
        rolesRepository.save(rolAdmin);
    }

    @Transactional(readOnly = true)
    public Set<String> consultarPermisoRoles(int id) {
        try {
            Set<String> roles = rolesRepository.consultarPermisos(id).
                    stream().map(
                            rol -> rol.getName().toString()).collect(Collectors.toSet());

            log.info("consultarPermisoRoles: {}", roles);
            return roles;

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
