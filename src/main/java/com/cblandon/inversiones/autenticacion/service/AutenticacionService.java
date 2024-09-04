package com.cblandon.inversiones.autenticacion.service;

import com.cblandon.inversiones.excepciones.UsernameNotFoundExceptionCustom;
import com.cblandon.inversiones.security.jwt.JwtUtils;
import com.cblandon.inversiones.autenticacion.dto.AuthBiometriaRequestDTO;
import com.cblandon.inversiones.autenticacion.dto.AuthResponseDTO;
import com.cblandon.inversiones.autenticacion.dto.LoginRequestDTO;
import com.cblandon.inversiones.autenticacion.dto.RegistrarDispositivoDTO;
import com.cblandon.inversiones.user.entity.UserEntity;
import com.cblandon.inversiones.user.repository.UserRepository;
import com.cblandon.inversiones.utils.MensajesErrorEnum;
import com.cblandon.inversiones.utils.UtilsMetodos;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@AllArgsConstructor
public class AutenticacionService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    final JwtUtils jwtUtils;


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
                    .authorities(UtilsMetodos.getAuthoritiesString(usuario.getRoles()))
                    .build();


        } catch (RuntimeException ex) {
            log.error("login: {}", ex.getMessage());
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
                    .authorities(UtilsMetodos.getAuthoritiesString(usuario.getRoles()))
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

    private String generarToken(UserEntity usuario) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                usuario.getUsername(), null, UtilsMetodos.getAuthorities(usuario.getRoles()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtUtils.createToken(authentication);
    }
}
