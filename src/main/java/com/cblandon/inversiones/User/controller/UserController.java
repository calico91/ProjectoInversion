package com.cblandon.inversiones.user.controller;

import com.cblandon.inversiones.user.dto.AuthBiometriaRequestDTO;
import com.cblandon.inversiones.user.dto.LoginRequestDTO;
import com.cblandon.inversiones.user.dto.RegisterUserRequestDTO;
import com.cblandon.inversiones.user.dto.RegistrarDispositivoDTO;
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


    @PostMapping(value = "register")
    public ResponseEntity<GenericResponseDTO> register(@RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        return GenericResponseDTO.genericResponse(userService.register(registerUserRequestDTO));
    }

    @PostMapping("login")
    public ResponseEntity<GenericResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO) {
        System.out.println("hola");
        return GenericResponseDTO.genericResponse(userService.login(loginRequestDTO));
    }

    @GetMapping("pruebaConexion")
    public ResponseEntity<String> pruebaConexion() {
        return ResponseEntity.ok("conexion establecida");
    }

    @GetMapping(value = "consultarUsuarios")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<GenericResponseDTO> consultarUsuarios() {

        return GenericResponseDTO.genericResponse(userService.consultarUsuarios());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/actualizarUsuario/{username}")
    public ResponseEntity<GenericResponseDTO> actualizarUsuario(
            @PathVariable String username,
            @RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        return GenericResponseDTO.genericResponse(userService.actualizarUsuario(username, registerUserRequestDTO));
    }

    @GetMapping("getUser")
    public ResponseEntity<GenericResponseDTO> getUser(@RequestHeader("Authorization") final String token) {
        return GenericResponseDTO.genericResponse(userService.getUserDetails(token));
    }

    @PostMapping("auth-biometrica")
    public ResponseEntity<GenericResponseDTO> authBiometrica(
            @RequestBody AuthBiometriaRequestDTO authBiometriaRequestDTO) {
        System.out.println("print");
        return GenericResponseDTO.genericResponse(userService.authBiometrica(authBiometriaRequestDTO));
    }

    @PostMapping("vincular-dispositivo")
    public ResponseEntity<GenericResponseDTO> vincularDispositivo(
            @RequestBody RegistrarDispositivoDTO registrarDispositivoDTO) {
        return GenericResponseDTO.genericResponse(userService.vincularDispositivo(registrarDispositivoDTO));

    }
}
