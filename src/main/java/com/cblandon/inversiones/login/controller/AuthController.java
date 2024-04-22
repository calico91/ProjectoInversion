package com.cblandon.inversiones.login.controller;

import com.cblandon.inversiones.user.dto.LoginRequestDTO;
import com.cblandon.inversiones.user.service.UserService;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/login")
@Slf4j
public class AuthController {

    private final UserService userService;


    @PostMapping()
    public ResponseEntity<GenericResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO) {
        System.out.println("hola");
        return GenericResponseDTO.genericResponse(userService.login(loginRequestDTO));
    }

}
