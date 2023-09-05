package com.cblandon.inversiones.Auth;

import com.cblandon.inversiones.Auth.dto.AuthResponseDTO;

import com.cblandon.inversiones.Auth.dto.LoginRequestDTO;
import com.cblandon.inversiones.Auth.dto.RegisterRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping(value = "login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) throws Exception {
        return ResponseEntity.ok(authService.register(request));
    }
}
