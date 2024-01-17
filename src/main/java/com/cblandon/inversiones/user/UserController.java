package com.cblandon.inversiones.user;

import com.cblandon.inversiones.user.dto.RegisterUserRequestDTO;
import com.cblandon.inversiones.user.dto.UsuariosResponseDTO;
import com.cblandon.inversiones.utils.GenericMessageDTO;
import com.cblandon.inversiones.utils.ResponseHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;


    @PostMapping(value = "register")
    public ResponseEntity<GenericMessageDTO> register(@RequestBody RegisterUserRequestDTO request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("pruebaConexion")
    public ResponseEntity<String> pruebaConexion() {
        return ResponseEntity.ok("conexion establecida");
    }

    @GetMapping(value = "consultarUsuarios")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<UsuariosResponseDTO>> consultarUsuarios() {

        return ResponseEntity.ok(userService.consultarUsuarios());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/actualizarUsuario/{username}")
    public ResponseEntity<GenericMessageDTO> actualizarUsuario(@PathVariable String username,
                                                               @RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        return ResponseEntity.ok().body(userService.actualizarUsuario(username, registerUserRequestDTO));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("getUser")
    public ResponseEntity<Object> getUser(@RequestHeader("Authorization") final String token) {
        return new ResponseHandler().generateResponse("successful", HttpStatus.OK, userService.getUserDetails(token));
    }
}
