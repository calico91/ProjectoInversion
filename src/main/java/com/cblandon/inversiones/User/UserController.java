package com.cblandon.inversiones.User;

import com.cblandon.inversiones.Cliente.dto.RegistrarClienteDTO;
import com.cblandon.inversiones.Security.jwt.JwtUtils;
import com.cblandon.inversiones.User.dto.RegisterUserRequestDTO;
import com.cblandon.inversiones.Utils.ResponseHandler;
import com.cblandon.inversiones.Utils.UtilsMetodos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    private UserDetailsService userDetailsService;

    private UtilsMetodos utilsMetodos;


    @PostMapping(value = "register")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequestDTO request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping(value = "consultarUsuarios")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> consultarUsuarios() {

        return ResponseEntity.ok(userService.consultarUsuarios());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/actualizarUsuario/{username}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable String username,
                                               @RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        return ResponseEntity.ok().body(userService.actualizarUsuario(username, registerUserRequestDTO));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("getUser")
    public ResponseEntity<Object> getUser() {
        return new  ResponseHandler().generateResponse("successful",HttpStatus.OK,userService.getUserDetails());
    }
}
