package com.cblandon.inversiones.roles.service;

import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.mapper.RolesMapper;
import com.cblandon.inversiones.permiso.entity.Permiso;
import com.cblandon.inversiones.permiso.repository.PermisoRepository;
import com.cblandon.inversiones.roles.Role;
import com.cblandon.inversiones.roles.dto.AsignarPermisosDTO;
import com.cblandon.inversiones.roles.dto.RolesDTO;
import com.cblandon.inversiones.roles.entity.Roles;
import com.cblandon.inversiones.roles.repository.RolesRepository;
import com.cblandon.inversiones.utils.MensajesErrorEnum;
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
        asignarPermisosSuperUsuario();
    }

    private void asignarPermisosSuperUsuario() {
        Set<Permiso> permisos = Set.copyOf(permisoRepository.findAll());
        Roles rolAdmin = rolesRepository.findById(1).orElse(new Roles());

        rolAdmin.setPermisos(permisos);
        rolesRepository.save(rolAdmin);
    }

    /**
     * metodo que se utiliza en cada peticion para saber si el usuario tiene permisos a un endpoint
     */
    @Transactional(readOnly = true)
    public Set<String> consultarPermisoRoles(int id) {
        try {
            return rolesRepository.consultarPermisos(id).
                    stream().map(rol -> rol.getName().toString()).collect(Collectors.toSet());


        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public String asignarPermisos(AsignarPermisosDTO asignarPermisosDTO) {

        Roles rol = rolesRepository.findByName(Role.valueOf(asignarPermisosDTO.rol().toUpperCase())).orElse(new Roles());

        Set<Permiso> permisos = asignarPermisosDTO.permisos().stream().map(
                        permiso -> permisoRepository.findById(permiso)
                                .orElseThrow(() -> new RequestException(MensajesErrorEnum.PERMISO_NO_EXISTE)))
                .collect(Collectors.toSet());

        try {
            rol.setPermisos(permisos);
            rolesRepository.save(rol);
            return "Permisos asignados correctamente al rol ".concat(asignarPermisosDTO.rol());
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Set<RolesDTO> consultarRoles() {

        try {
            return rolesRepository.consultarRoles();

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public RolesDTO consultarPermisosRol(Integer id) {

        return RolesMapper.ROLES.toRolesDTO(
                rolesRepository.consultarPermisosRol(id).orElseThrow(
                        () -> new NoDataException(MensajesErrorEnum.DATOS_NO_ENCONTRADOS)));

    }


}
