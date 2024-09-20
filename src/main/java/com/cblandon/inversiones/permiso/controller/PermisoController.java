package com.cblandon.inversiones.permiso.controller;

import com.cblandon.inversiones.permiso.service.PermisoService;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permisos")
public class PermisoController {

    private final PermisoService permisoService;

    @GetMapping("/consultar-todos")
    @PreAuthorize("hasAnyRole('SUPER')")
    public ResponseEntity<GenericResponseDTO> consultarTodos() {

        return GenericResponseDTO.genericResponse(permisoService.consultarTodos());

    }
}