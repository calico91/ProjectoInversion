package com.cblandon.inversiones.user.controller;

import com.cblandon.inversiones.user.dto.RegisterUserRequestDTO;
import com.cblandon.inversiones.user.dto.*;
import com.cblandon.inversiones.user.service.UserService;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @PostMapping(value = "registrar")
    @PreAuthorize("hasAnyRole('SUPER')")
    public ResponseEntity<GenericResponseDTO> registrar(@RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        return GenericResponseDTO.genericResponse(userService.registrar(registerUserRequestDTO));
    }

    @GetMapping(value = "consultar-usuarios")
    @PreAuthorize("hasAnyRole('SUPER')")
    public ResponseEntity<GenericResponseDTO> consultarUsuarios() {
        return GenericResponseDTO.genericResponse(userService.consultarUsuarios());

    }

    @PutMapping("/actualizar")
    @PreAuthorize("hasAnyRole('SUPER')")
    public ResponseEntity<GenericResponseDTO> actualizarUsuario(
            @RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        return GenericResponseDTO.genericResponse(userService.actualizarUsuario(registerUserRequestDTO));
    }

    @PutMapping(value = "cambiar-contrasena")
    public ResponseEntity<GenericResponseDTO> cambiarContrasena(@RequestBody CambiarContrasenaDTO cambiarContrasenaDTO) {
        return GenericResponseDTO.genericResponse(userService.cambiarContrasena(cambiarContrasenaDTO));
    }

    @PutMapping(value = "reiniciar-contrasena/{idUsuario}")
    @PreAuthorize("hasAnyRole('SUPER')")
    public ResponseEntity<GenericResponseDTO> reiniciarContrasena(@PathVariable Integer idUsuario) {
        return GenericResponseDTO.genericResponse(userService.restablecerContrasena(idUsuario));
    }

    @PutMapping(value = "cambiar-estado/{idUsuario}")
    @PreAuthorize("hasAnyRole('SUPER')")
    public ResponseEntity<GenericResponseDTO> cambiarEstado(@PathVariable Integer idUsuario) {
        return GenericResponseDTO.genericResponse(userService.cambiarEstadoUsuario(idUsuario));
    }
}
