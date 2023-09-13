package com.cblandon.inversiones.User;

import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Excepciones.NoDataException;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.Mapper;
import com.cblandon.inversiones.Roles.Role;
import com.cblandon.inversiones.Roles.Roles;
import com.cblandon.inversiones.User.dto.RegisterUserRequestDTO;
import com.cblandon.inversiones.User.dto.UsuariosResponseDTO;
import com.cblandon.inversiones.Utils.Constantes;
import com.cblandon.inversiones.Utils.GenericMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    public GenericMessageDTO register(RegisterUserRequestDTO registerUserRequestDTO) {
        Optional<UserEntity> consultarUser = userRepository.findByUsername(registerUserRequestDTO.getUsername());
        if (!consultarUser.isEmpty()) {

            throw new RequestException(Constantes.USUARIO_REGISTRADO, "2");
        }

        try {
            UserEntity user = UserEntity.builder()
                    .username(registerUserRequestDTO.getUsername())
                    .firstname(registerUserRequestDTO.getFirstname())
                    .lastname(registerUserRequestDTO.getLastname())
                    .country(registerUserRequestDTO.getCountry())
                    .build();

            user.setPassword(passwordEncoder.encode(registerUserRequestDTO.getPassword()));
            Set<Roles> authorities = registerUserRequestDTO.getRoles().stream()
                    .map(role -> Roles.builder()
                            .name(Role.valueOf(role))
                            .build())
                    .collect(Collectors.toSet());

            user.setRoles(authorities);

            userRepository.save(user);

            return GenericMessageDTO.builder()
                    .message(Constantes.USUARIO_CREADO)
                    .build();
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }


    }

    public List<UsuariosResponseDTO> consultarUsuarios() {

        List<UserEntity> usuariosConsulta = userRepository.findAll();
        if (usuariosConsulta.isEmpty()) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, "3");
        }
        try {
            List<UsuariosResponseDTO> usuariosResponseDto =
                    usuariosConsulta.stream().map(usuario -> {
                        UsuariosResponseDTO usuariosResponseDTO = UsuariosResponseDTO.builder().
                                username(usuario.getUsername())
                                .lastname(usuario.getLastname())
                                .firstname(usuario.getFirstname())
                                .country(usuario.getCountry())
                                .roles(usuario.getRoles().stream().map(
                                        roles -> roles.getName().toString()).collect(Collectors.toSet()))
                                .build();
                        return usuariosResponseDTO;
                    }).collect(Collectors.toList());

            return usuariosResponseDto;
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    public GenericMessageDTO actualizarUsuario(String username, RegisterUserRequestDTO registrarClienteDTO) {

        Optional<UserEntity> usuarioBD = userRepository.findByUsername(username);
        if (usuarioBD.isEmpty()) {
            throw new NoDataException(Constantes.DATOS_NO_ENCONTRADOS, "3");
        }
        ClienteResponseDTO clienteResponseDTO = ClienteResponseDTO.builder().build();
        try {

            UserEntity usuarioModificado = UserEntity.builder()
                    .lastname(registrarClienteDTO.getLastname())
                    .firstname(registrarClienteDTO.getFirstname())
                    .username(registrarClienteDTO.getUsername())
                    .country(registrarClienteDTO.getCountry())
                    .password(passwordEncoder.encode(registrarClienteDTO.getPassword()))
                    .roles(registrarClienteDTO.getRoles().stream()
                            .map(role -> Roles.builder()
                                    .name(Role.valueOf(role))
                                    .build())
                            .collect(Collectors.toSet()))
                    .build();

            usuarioModificado.setId(usuarioBD.get().id);
            userRepository.save(usuarioModificado);
            return GenericMessageDTO.builder()
                    .message(Constantes.USUARIO_MODIFICADO)
                    .build();


        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
