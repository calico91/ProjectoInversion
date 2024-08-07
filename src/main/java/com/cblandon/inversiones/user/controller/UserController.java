package com.cblandon.inversiones.user.controller;

import com.cblandon.inversiones.user.dto.RegisterUserRequestDTO;
import com.cblandon.inversiones.user.dto.*;
import com.cblandon.inversiones.user.service.UserService;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;


    @PostMapping(value = "registrar")
    public ResponseEntity<GenericResponseDTO> registrar(@RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        return GenericResponseDTO.genericResponse(userService.registrar(registerUserRequestDTO));
    }

    @GetMapping(value = "consultarUsuarios")
    @PreAuthorize("hasAnyRole('SUPER')")
    public ResponseEntity<GenericResponseDTO> consultarUsuarios() {
        return GenericResponseDTO.genericResponse(userService.consultarUsuarios());
    }

    @PutMapping("/actualizarUsuario/{username}")
    @PreAuthorize("hasAnyRole('SUPER')")
    public ResponseEntity<GenericResponseDTO> actualizarUsuario(
            @PathVariable String username,
            @RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        return GenericResponseDTO.genericResponse(userService.actualizarUsuario(username, registerUserRequestDTO));
    }

    @PutMapping(value = "cambiar-contrasena")
    public ResponseEntity<GenericResponseDTO> cambiarContrasena(@RequestBody CambiarContrasenaDTO cambiarContrasenaDTO) {
        return GenericResponseDTO.genericResponse(userService.cambiarContrasena(cambiarContrasenaDTO));
    }
}
