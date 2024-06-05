package com.cblandon.inversiones.roles.controller;

import com.cblandon.inversiones.roles.dto.AsignarPermisosDTO;
import com.cblandon.inversiones.roles.service.RolesService;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RolesController {

    private final RolesService rolesService;

    @PutMapping("/asignar-permisos")
    @PreAuthorize("hasAnyRole(@rolesService.consultarPermisoRoles(19))")
    public ResponseEntity<GenericResponseDTO> asignarPermisos(
            @RequestBody AsignarPermisosDTO asignarPermisosDTO) {

        return GenericResponseDTO.genericResponse(rolesService.asignarPermisos(asignarPermisosDTO));

    }
}
