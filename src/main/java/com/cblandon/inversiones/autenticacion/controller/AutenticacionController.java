package com.cblandon.inversiones.autenticacion.controller;

import com.cblandon.inversiones.autenticacion.service.AutenticacionService;
import com.cblandon.inversiones.autenticacion.dto.AuthBiometriaRequestDTO;
import com.cblandon.inversiones.autenticacion.dto.LoginRequestDTO;
import com.cblandon.inversiones.autenticacion.dto.RegistrarDispositivoDTO;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/autenticacion")
@Slf4j
public class AutenticacionController {

    private final AutenticacionService autenticacionService;

    @PostMapping(value = "login")
    public ResponseEntity<GenericResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO) {
        return GenericResponseDTO.genericResponse(autenticacionService.login(loginRequestDTO));
    }

    @PostMapping("auth-biometrica")
    public ResponseEntity<GenericResponseDTO> authBiometrica(
            @RequestBody AuthBiometriaRequestDTO authBiometriaRequestDTO) {
        return GenericResponseDTO.genericResponse(autenticacionService.authBiometrica(authBiometriaRequestDTO));
    }

    @PostMapping("vincular-dispositivo")
    public ResponseEntity<GenericResponseDTO> vincularDispositivo(
            @RequestBody RegistrarDispositivoDTO registrarDispositivoDTO) {
        return GenericResponseDTO.genericResponse(autenticacionService.vincularDispositivo(registrarDispositivoDTO));

    }
}
