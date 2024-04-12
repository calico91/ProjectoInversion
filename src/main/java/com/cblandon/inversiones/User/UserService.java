package com.cblandon.inversiones.user;

import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.roles.Role;
import com.cblandon.inversiones.roles.Roles;
import com.cblandon.inversiones.roles.RolesRepository;
import com.cblandon.inversiones.security.jwt.JwtUtils;
import com.cblandon.inversiones.user.dto.*;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.MensajesErrorEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    final JwtUtils jwtUtils;


    public UserEntity register(RegisterUserRequestDTO registerUserRequestDTO) throws RequestException {

        Optional<UserEntity> consultarUser = userRepository.findByUsername(registerUserRequestDTO.getUsername());

        if (consultarUser.isPresent()) {
            throw new RequestException(MensajesErrorEnum.USUARIO_REGISTRADO);
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
                    .map(role -> rolesRepository.findByName(Role.valueOf(role)).orElseThrow(
                            () -> new RequestException(MensajesErrorEnum.ROL_NO_ENCONTRADO)))
                    .collect(Collectors.toSet());

            user.setRoles(authorities);

            return userRepository.save(user);

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex);
        }


    }

    public List<UsuariosResponseDTO> consultarUsuarios() {


        List<UserEntity> usuariosConsulta = userRepository.findAll();
        if (usuariosConsulta.isEmpty()) {
            throw new NoDataException(MensajesErrorEnum.DATOS_NO_ENCONTRADOS);
        }
        try {
            return
                    usuariosConsulta.stream().map(usuario -> UsuariosResponseDTO.builder().
                            username(usuario.getUsername())
                            .lastname(usuario.getLastname())
                            .firstname(usuario.getFirstname())
                            .country(usuario.getCountry())
                            .email(usuario.getEmail())
                            .roles(usuario.getRoles().stream().map(
                                    roles -> roles.getName().toString()).collect(Collectors.toSet()))
                            .build()
                    ).toList();


        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public UserEntity actualizarUsuario(String username, RegisterUserRequestDTO registrarClienteDTO) {

        UserEntity usuarioBD = userRepository.findByUsername(username).orElseThrow(
                () -> new NoDataException(MensajesErrorEnum.DATOS_NO_ENCONTRADOS));
        try {

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

            usuarioModificado.setId(usuarioBD.getId());
            return userRepository.save(usuarioModificado);

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }


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


    public AuthResponseDTO authBiometrica(AuthBiometriaRequestDTO authBiometriaRequestDTO) {

        UserEntity user = userRepository.findByUsername(
                authBiometriaRequestDTO.username()).orElseThrow(() ->
                new UsernameNotFoundException(Constantes.AUTH_BIOMETRICA_FALLIDA));

        if (!passwordEncoder.matches(authBiometriaRequestDTO.idMovil(), user.getIdMovil())) {
            throw new UsernameNotFoundException(Constantes.AUTH_BIOMETRICA_FALLIDA);
        }
        String token = jwtUtils.generateAccesToken(user.getUsername());
        User userDetail = (User) userDetailsService.loadUserByUsername(authBiometriaRequestDTO.username());
        userDetail.eraseCredentials();

        return AuthResponseDTO.builder()
                .username(userDetail.getUsername())
                .token(token)
                .authorities(userDetail.getAuthorities())
                .build();

    }

    /**
     * se registra el id del dispositivo con el cual se desea hacer auth biometrica
     */
    public String vincularDispositivo(RegistrarDispositivoDTO registrarDispositivoDTO) {
        try {

            UserEntity user = userRepository.findByUsername(
                    registrarDispositivoDTO.username()).orElseThrow(() ->
                    new UsernameNotFoundException("No se encontro usuario"));

            user.setIdMovil(passwordEncoder.encode(registrarDispositivoDTO.idDispositivo()));
            userRepository.save(user);

            return "Dispositivo vinculado correctamente";
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }
}
