package com.cblandon.inversiones.user.service;

import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.excepciones.UsernameNotFoundExceptionCustom;
import com.cblandon.inversiones.mapper.UserMapper;
import com.cblandon.inversiones.roles.Role;
import com.cblandon.inversiones.roles.entity.Roles;
import com.cblandon.inversiones.roles.repository.RolesRepository;
import com.cblandon.inversiones.security.jwt.JwtUtils;
import com.cblandon.inversiones.user.entity.UserEntity;
import com.cblandon.inversiones.user.repository.UserRepository;
import com.cblandon.inversiones.user.dto.*;
import com.cblandon.inversiones.utils.MensajesErrorEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    final JwtUtils jwtUtils;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));


        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                getAuthorities(userEntity.getRoles()));
    }

    @Transactional
    public UsuariosResponseDTO register(RegisterUserRequestDTO registerUserRequestDTO) throws RequestException {
        log.info("registrarUsuario: {}", registerUserRequestDTO);
        Optional<UserEntity> consultarUser = userRepository.findByUsername(registerUserRequestDTO.username());

        if (consultarUser.isPresent()) {
            throw new RequestException(MensajesErrorEnum.USUARIO_REGISTRADO);
        }


        try {

            UserEntity user = UserMapper.USER.toUserEntity(registerUserRequestDTO);

            user.setPassword(passwordEncoder.encode(registerUserRequestDTO.password()));

            Set<Roles> authorities = registerUserRequestDTO.roles().stream()
                    .map(role -> rolesRepository.findByName(Role.valueOf(role)).orElseThrow(
                            () -> new RequestException(MensajesErrorEnum.ROL_NO_ENCONTRADO)))
                    .collect(Collectors.toSet());
            user.setRoles(authorities);

            UsuariosResponseDTO usuariosResponseDTO =
                    UserMapper.USER.toUsuariosResponseDTO(userRepository.save(user));
            usuariosResponseDTO.setRoles(getAuthoritiesString(authorities));

            return usuariosResponseDTO;

        } catch (RuntimeException ex) {
            log.error("registrarUsuario: {}", ex.getMessage());
            throw new RuntimeException(ex);
        }

    }

    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        log.info("login: {}", loginRequestDTO);

        UserEntity usuario = userRepository.findByUsername(loginRequestDTO.username())
                .orElseThrow(() -> new UsernameNotFoundExceptionCustom(
                        null, MensajesErrorEnum.ERROR_AUTENTICACION));

        if (!passwordEncoder.matches(loginRequestDTO.password(), usuario.getPassword())) {
            throw new UsernameNotFoundExceptionCustom(
                    null, MensajesErrorEnum.ERROR_AUTENTICACION);
        }
        try {
            return AuthResponseDTO.builder()
                    .id(usuario.getId())
                    .username(usuario.getUsername())
                    .token(generarToken(usuario))
                    .authorities(getAuthoritiesString(usuario.getRoles()))
                    .build();


        } catch (RuntimeException ex) {
            log.error("login: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public List<UsuariosResponseDTO> consultarUsuarios() {
        log.info("consultarUsuarios: ");
        List<UserEntity> usuariosConsulta = userRepository.findAll();
        if (usuariosConsulta.isEmpty()) {
            throw new NoDataException(MensajesErrorEnum.DATOS_NO_ENCONTRADOS);
        }
        try {

            return usuariosConsulta.stream().map(
                    user -> UsuariosResponseDTO.builder()
                            .username(user.getUsername())
                            .lastname(user.getLastname())
                            .firstname(user.getFirstname())
                            .email(user.getEmail())
                            .country(user.getCountry())
                            .roles(getAuthoritiesString(user.getRoles()))
                            .build()
            ).toList();


        } catch (RuntimeException ex) {
            log.error("consultarUsuarios: {}", ex.getMessage());

            throw new RuntimeException(ex.getMessage());
        }

    }

    @Transactional
    public UserEntity actualizarUsuario(String username, RegisterUserRequestDTO registrarClienteDTO) {
        log.info("actualizarUsuario: {}", registrarClienteDTO);
        UserEntity usuarioBD = userRepository.findByUsername(username).orElseThrow(
                () -> new NoDataException(MensajesErrorEnum.DATOS_NO_ENCONTRADOS));
        try {

            UserEntity usuarioModificado = UserEntity.builder()
                    .lastname(registrarClienteDTO.lastname())
                    .firstname(registrarClienteDTO.firstname())
                    .username(registrarClienteDTO.username())
                    .country(registrarClienteDTO.country())
                    .email(registrarClienteDTO.email())
                    .password(passwordEncoder.encode(registrarClienteDTO.password()))
                    .roles(registrarClienteDTO.roles().stream()
                            .map(role -> Roles.builder()
                                    .name(Role.valueOf(role))
                                    .build())
                            .collect(Collectors.toSet()))
                    .build();

            usuarioModificado.setId(usuarioBD.getId());
            return userRepository.save(usuarioModificado);

        } catch (RuntimeException ex) {
            log.error("actualizarUsuario: {}", ex.getMessage());

            throw new RuntimeException(ex.getMessage());
        }


    }

    @Transactional(readOnly = true)
    public AuthResponseDTO authBiometrica(AuthBiometriaRequestDTO authBiometriaRequestDTO) {
        log.info("authBiometrica: {}", authBiometriaRequestDTO);
        UserEntity usuario = userRepository.findByUsername(
                authBiometriaRequestDTO.username()).orElseThrow(() ->
                new UsernameNotFoundExceptionCustom(null, MensajesErrorEnum.AUTENTICACION_BIOMETRICA_FALLIDA));

        if (!passwordEncoder.matches(authBiometriaRequestDTO.idMovil(), usuario.getIdMovil())) {
            throw new UsernameNotFoundExceptionCustom(null, MensajesErrorEnum.AUTENTICACION_BIOMETRICA_FALLIDA);
        }

        try {

            return AuthResponseDTO.builder()
                    .id(usuario.getId())
                    .username(usuario.getUsername())
                    .token(generarToken(usuario))
                    .authorities(getAuthoritiesString(usuario.getRoles()))
                    .build();

        } catch (RuntimeException ex) {
            log.error("authBiometrica: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }


    /**
     * se registra el id del dispositivo con el cual se desea hacer auth biometrica
     */
    @Transactional
    public String vincularDispositivo(RegistrarDispositivoDTO registrarDispositivoDTO) {
        log.info("vincularDispositivo: {}", registrarDispositivoDTO);
        UserEntity user = userRepository.findByUsername(
                registrarDispositivoDTO.username()).orElseThrow(() ->
                new UsernameNotFoundException("No se encontro usuario"));
        try {

            user.setIdMovil(passwordEncoder.encode(registrarDispositivoDTO.idDispositivo()));
            userRepository.save(user);

            return "Dispositivo vinculado correctamente";
        } catch (RuntimeException ex) {
            log.error("vincularDispositivo: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Transactional
    public String cambiarContrasena(CambiarContrasenaDTO cambiarContrasenaDTO) {
        log.info("cambiarContrasena: {}", cambiarContrasenaDTO.toString());
        UserEntity usuarioBD = userRepository.findById(cambiarContrasenaDTO.idUsuario()).orElseThrow(
                () -> new UsernameNotFoundException("No se encontro usuario"));

        if (!passwordEncoder.matches(cambiarContrasenaDTO.contrasenaActual(), usuarioBD.getPassword())) {
            throw new RequestException(MensajesErrorEnum.CONTRASENA_ANTIGUA_INCORRECTA);
        }
        try {
            usuarioBD.setPassword(passwordEncoder.encode(cambiarContrasenaDTO.contrasenaNueva()));
            userRepository.save(usuarioBD);
            return "Contrasena modificada correctamente";
        } catch (RuntimeException ex) {
            log.error("cambiarContrasena: ".concat(ex.getMessage()));
            throw new RuntimeException(ex.getMessage());
        }
    }




    private Collection<? extends GrantedAuthority> getAuthorities(Set<Roles> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());
    }


    private Set<String> getAuthoritiesString(Set<Roles> roles) {
        return roles.stream().map(role -> role.getName().name()).collect(Collectors.toSet());
    }

    private String generarToken(UserEntity usuario) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                usuario.getUsername(), null, getAuthorities(usuario.getRoles()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtUtils.createToken(authentication);
    }
}
