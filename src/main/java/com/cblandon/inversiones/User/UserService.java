package com.cblandon.inversiones.User;

import com.cblandon.inversiones.Excepciones.NoDataException;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Roles.Role;
import com.cblandon.inversiones.Roles.Roles;
import com.cblandon.inversiones.Security.jwt.JwtUtils;
import com.cblandon.inversiones.User.dto.RegisterUserRequestDTO;
import com.cblandon.inversiones.User.dto.UsuariosResponseDTO;
import com.cblandon.inversiones.Utils.Constantes;
import com.cblandon.inversiones.Utils.GenericMessageDTO;
import com.cblandon.inversiones.Utils.ResponseHandler;
import com.cblandon.inversiones.Utils.UtilsMetodos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtUtils jwtUtils;
    Map<String, Object> logInfo = new HashMap();

    public GenericMessageDTO register(RegisterUserRequestDTO registerUserRequestDTO) {
        Optional<UserEntity> consultarUser = userRepository.findByUsername(registerUserRequestDTO.getUsername());
        Map<String, String> mensaje = new HashMap();
        if (!consultarUser.isEmpty()) {

            throw new RequestException(Constantes.USUARIO_REGISTRADO, "2");
        }

        try {
            UserEntity user = UserEntity.builder()
                    .username(registerUserRequestDTO.getUsername())
                    .firstname(registerUserRequestDTO.getFirstname())
                    .lastname(registerUserRequestDTO.getLastname())
                    .country(registerUserRequestDTO.getCountry())
                    .email(registerUserRequestDTO.getEmail())
                    .build();

            user.setPassword(passwordEncoder.encode(registerUserRequestDTO.getPassword()));
            Set<Roles> authorities = registerUserRequestDTO.getRoles().stream()
                    .map(role -> Roles.builder()
                            .name(Role.valueOf(role))
                            .build())
                    .collect(Collectors.toSet());

            user.setRoles(authorities);

            userRepository.save(user);
            mensaje.put("message", Constantes.USUARIO_CREADO);
            return GenericMessageDTO.builder()
                    .message(mensaje)
                    .build();
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex);
        }


    }

    public List<UsuariosResponseDTO> consultarUsuarios() {

        List<UserEntity> usuariosConsulta = userRepository.findAll();
        if (usuariosConsulta.isEmpty()) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, "3");
        }
        try {
            List<UsuariosResponseDTO> usuariosResponseDto =
                    usuariosConsulta.stream().map(usuario -> UsuariosResponseDTO.builder().
                            username(usuario.getUsername())
                            .lastname(usuario.getLastname())
                            .firstname(usuario.getFirstname())
                            .country(usuario.getCountry())
                            .email(usuario.getEmail())
                            .roles(usuario.getRoles().stream().map(
                                    roles -> roles.getName().toString()).collect(Collectors.toSet()))
                            .build()
                    ).collect(Collectors.toList());

            return usuariosResponseDto;
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public GenericMessageDTO actualizarUsuario(String username, RegisterUserRequestDTO registrarClienteDTO) {

        Optional<UserEntity> usuarioBD = userRepository.findByUsername(username);
        Map<String, String> mensaje = new HashMap();
        if (usuarioBD.isEmpty()) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, "3");
        }
        UserEntity usuarioModificado = UserEntity.builder()
                .lastname(registrarClienteDTO.getLastname())
                .firstname(registrarClienteDTO.getFirstname())
                .username(registrarClienteDTO.getUsername())
                .country(registrarClienteDTO.getCountry())
                .email(registrarClienteDTO.getEmail())
                .password(passwordEncoder.encode(registrarClienteDTO.getPassword()))
                .roles(registrarClienteDTO.getRoles().stream()
                        .map(role -> Roles.builder()
                                .name(Role.valueOf(role))
                                .build())
                        .collect(Collectors.toSet()))
                .build();

        usuarioModificado.setId(usuarioBD.get().id);
        userRepository.save(usuarioModificado);
        mensaje.put("message", Constantes.USUARIO_MODIFICADO);
        return GenericMessageDTO.builder()
                .message(mensaje)
                .build();

    }

    public UserDetails getUserDetails(String token) {
        String username = jwtUtils.getUsernameFromToken(token.substring(7));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        logInfo.put("userdetails", userDetails);
        logInfo.put("userService", "getUserDetails");
        log.info(logInfo.toString());
        return userDetails;

    }


}
