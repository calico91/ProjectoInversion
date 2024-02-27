package com.cblandon.inversiones.user;

import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.imagenes.ImagenesService;
import com.cblandon.inversiones.roles.Role;
import com.cblandon.inversiones.roles.Roles;
import com.cblandon.inversiones.roles.RolesRepository;
import com.cblandon.inversiones.security.jwt.JwtUtils;
import com.cblandon.inversiones.user.dto.RegisterUserRequestDTO;
import com.cblandon.inversiones.user.dto.UsuariosResponseDTO;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.GenericMessageDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final RolesRepository rolesRepository;
    private final JwtUtils jwtUtils;
    private final ImagenesService imagenesService;


    public GenericMessageDTO register(RegisterUserRequestDTO registerUserRequestDTO, MultipartFile archivo) throws RequestException {

        log.info("registerUserRequestDTO: ".concat(registerUserRequestDTO.toString()));

        Optional<UserEntity> consultarUser = userRepository.findByUsername(registerUserRequestDTO.getUsername());

        if (consultarUser.isPresent()) {
            throw new RequestException(Constantes.USUARIO_REGISTRADO, HttpStatus.BAD_REQUEST.value());
        }

        Map<String, String> mensaje = new HashMap<>();

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
                    .map(role -> rolesRepository.findByName(Role.valueOf(role)).orElseThrow(
                            () -> new RequestException(Constantes.ROL_NO_ENCONTRADO, HttpStatus.BAD_REQUEST.value())))
                    .collect(Collectors.toSet());

            user.setRoles(authorities);
            if (archivo != null) {
                user.setImagen(imagenesService.guardarArchivo(archivo));
            }

            userRepository.save(user);
            mensaje.put("message", Constantes.USUARIO_CREADO);
            return GenericMessageDTO.builder()
                    .message(mensaje)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public List<UsuariosResponseDTO> consultarUsuarios() {

        List<UserEntity> usuariosConsulta = userRepository.findAll();
        if (usuariosConsulta.isEmpty()) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value());
        }
        try {
            return
                    usuariosConsulta.stream().map(usuario -> UsuariosResponseDTO.builder().
                            username(usuario.getUsername())
                            .lastname(usuario.getLastname())
                            .firstname(usuario.getFirstname())
                            .country(usuario.getCountry())
                            .email(usuario.getEmail())
                            .imagen(imagenesService.cargarArchivo(usuario.getImagen()))
                            .roles(usuario.getRoles().stream().map(
                                    roles -> roles.getName().toString()).collect(Collectors.toSet()))
                            .build()
                    ).toList();


        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public GenericMessageDTO actualizarUsuario(String username, RegisterUserRequestDTO registrarClienteDTO) {

        Optional<UserEntity> usuarioBD = userRepository.findByUsername(username);
        Map<String, String> mensaje = new HashMap<>();
        if (usuarioBD.isEmpty()) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value());
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

        usuarioModificado.setId(usuarioBD.get().getId());
        userRepository.save(usuarioModificado);
        mensaje.put("message", Constantes.USUARIO_MODIFICADO);
        return GenericMessageDTO.builder()
                .message(mensaje)
                .build();

    }

    public UserDetails getUserDetails(String token) {
        Map<String, Object> logInfo = new HashMap<>();
        String username = jwtUtils.getUsernameFromToken(token.substring(7));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        logInfo.put("userService", "getUserDetails");
        logInfo.put("userdetails", userDetails);
        log.info(logInfo.toString());
        return userDetails;

    }

    public String cargarImagen(MultipartFile imagen, int idUsuario) {

        UserEntity usuario = userRepository.findById(idUsuario).orElseThrow(
                () -> new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, HttpStatus.BAD_REQUEST.value()));

        if (!imagen.isEmpty()) {
            String nombreArchivo = imagen.getOriginalFilename();
            Path rutaArchivo = Paths.get("imagenes").resolve(nombreArchivo).toAbsolutePath();

            try {
                Files.copy(imagen.getInputStream(), rutaArchivo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            usuario.setImagen(nombreArchivo);

            userRepository.save(usuario);
        }


        return "userDetails";
    }


}
