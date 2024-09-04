package com.cblandon.inversiones.user.service;

import com.cblandon.inversiones.user.dto.UserDTO;
import com.cblandon.inversiones.user.dto.UsuariosResponseDTO;
import com.cblandon.inversiones.excepciones.NoDataException;
import com.cblandon.inversiones.excepciones.RequestException;
import com.cblandon.inversiones.mapper.UserMapper;
import com.cblandon.inversiones.roles.entity.Roles;
import com.cblandon.inversiones.roles.repository.RolesRepository;
import com.cblandon.inversiones.security.jwt.JwtUtils;
import com.cblandon.inversiones.user.entity.UserEntity;
import com.cblandon.inversiones.user.repository.UserRepository;
import com.cblandon.inversiones.user.dto.*;
import com.cblandon.inversiones.utils.MensajesErrorEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    public UsuariosResponseDTO registrar(UserDTO userDTO) throws RequestException {
        log.info("registrarUsuario: {}", userDTO);


        String password = userDTO.password() == null ? "cambio" : userDTO.password();
        if (userRepository.findByUsername(userDTO.username()).isPresent()) {
            throw new RequestException(MensajesErrorEnum.USUARIO_REGISTRADO);
        }

        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new RequestException(MensajesErrorEnum.CORREO_REGISTRADO);
        }
        try {

            UserEntity user = UserMapper.USER.toUserEntity(userDTO);

            user.setPassword(passwordEncoder.encode(password));

            Set<Roles> roles = userDTO.roles().stream()
                    .map(role -> rolesRepository.findById(role.getId()).orElseThrow(
                            () -> new RequestException(MensajesErrorEnum.ROL_NO_ENCONTRADO)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);

            UsuariosResponseDTO usuariosResponseDTO =
                    UserMapper.USER.toUsuariosResponseDTO(userRepository.save(user));
            usuariosResponseDTO.setRoles(roles);


            return usuariosResponseDTO;

        } catch (RuntimeException ex) {
            log.error("registrarUsuario: {}", ex.getMessage());
            throw new RuntimeException(ex);
        }

    }


    @Transactional(readOnly = true)
    public List<UsuariosResponseDTO> consultarUsuarios() {
        log.info("consultarUsuarios: ");
        Set<UserEntity> usuariosConsulta = userRepository.consultarUsuarios();

        try {

            return usuariosConsulta.stream().map(
                    user -> UsuariosResponseDTO.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .lastname(user.getLastname())
                            .firstname(user.getFirstname())
                            .email(user.getEmail())
                            .active(user.isActive())
                            .build()).toList();


        } catch (RuntimeException ex) {
            log.error("consultarUsuarios: {}", ex.getMessage());

            throw new RuntimeException(ex.getMessage());
        }

    }

    @Transactional
    public UsuariosResponseDTO actualizarUsuario(UserDTO userDTO) {
        log.info("actualizarUsuario: {}", userDTO);
        UserEntity usuarioBD = userRepository.findById(userDTO.id()).orElseThrow(
                () -> new NoDataException(MensajesErrorEnum.DATOS_NO_ENCONTRADOS));

        Set<Roles> roles = userDTO.roles().stream()
                .map(role -> rolesRepository.findById(role.getId()).orElseThrow(
                        () -> new RequestException(MensajesErrorEnum.ROL_NO_ENCONTRADO)))
                .collect(Collectors.toSet());

        if (!usuarioBD.getEmail().equals(userDTO.email())) {
            userRepository.findByEmail(userDTO.email()).ifPresent(correo -> {
                throw new RequestException(MensajesErrorEnum.CORREO_REGISTRADO);
            });
        }
        if (!usuarioBD.getUsername().equals(userDTO.username())) {
            userRepository.findByUsername(userDTO.username()).ifPresent(correo -> {
                throw new RequestException(MensajesErrorEnum.USUARIO_REGISTRADO);
            });

        }

        try {

            UserEntity usuarioModificado = UserMapper.USER.toUserEntity(userDTO);
            usuarioModificado.setActive(true);
            usuarioModificado.setPassword(usuarioBD.getPassword());
            usuarioModificado.setCountry(usuarioBD.getCountry());
            usuarioModificado.setRoles(roles);

            return UserMapper.USER.toUsuariosResponseDTO(userRepository.save(usuarioModificado));

        } catch (RuntimeException ex) {
            log.error("actualizarUsuario: {}", ex.getMessage());
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

    /**
     * solo disponible para el usuario super, reinicia la contraseña de los demas usuarios
     */
    @Transactional
    public String restablecerContrasena(Integer idUsuario) {
        log.info("restablecerContrasena: {}", idUsuario.toString());

        try {
            UserEntity usuarioBD = userRepository.findById(idUsuario).orElseThrow(
                    () -> new UsernameNotFoundException("No se encontro usuario"));

            usuarioBD.setPassword(passwordEncoder.encode("cambio"));
            userRepository.save(usuarioBD);
            return "Contrasena modificada correctamente";
        } catch (RuntimeException ex) {
            log.error("restablecerContrasena: ".concat(ex.getMessage()));
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public String cambiarEstadoUsuario(Integer idUsuario) {
        log.info("cambiarEstadoUsuario: {}", idUsuario.toString());

        UserEntity usuarioBD = userRepository.findById(idUsuario).orElseThrow(
                () -> new UsernameNotFoundException("No se encontro usuario"));

        try {

            usuarioBD.setActive(!usuarioBD.isActive());
            userRepository.save(usuarioBD);
            String estado = usuarioBD.isActive() ? "activado" : "inactivado";

            return String.format("usuario %s correctamente ", estado);
        } catch (RuntimeException ex) {
            log.error("cambiarEstadoUsuario: ".concat(ex.getMessage()));
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public UsuariosResponseDTO consultarUsuario(Integer id) {
        log.error("consultarUsuario: {}", id.toString());

        UserEntity usuarioBD = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("No se encontro usuario"));
        System.out.println("--------------" + usuarioBD.getRoles());
        try {

            return UserMapper.USER.toUsuariosResponseDTO(usuarioBD);

        } catch (RuntimeException ex) {

            throw new RuntimeException(ex.getMessage());
        }


    }


    private Collection<? extends GrantedAuthority> getAuthorities(Set<Roles> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());
    }


}
