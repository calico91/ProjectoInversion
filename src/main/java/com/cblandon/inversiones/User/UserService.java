package com.cblandon.inversiones.User;

import com.cblandon.inversiones.Cliente.Cliente;
import com.cblandon.inversiones.Cliente.dto.ClienteResponseDTO;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.Mapper;
import com.cblandon.inversiones.Roles.Role;
import com.cblandon.inversiones.Roles.Roles;
import com.cblandon.inversiones.User.dto.RegisterUserRequestDTO;
import com.cblandon.inversiones.User.dto.UsuariosDTO;
import com.cblandon.inversiones.Utils.Constantes;
import com.cblandon.inversiones.Utils.GenericMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<UserEntity> consultarUsuarios() {
        try {
            List<UserEntity> usuarios = userRepository.findAll();

            /*List<UsuariosDTO> usuariosDTO = usuarios.stream().map(
                    usuario -> Mapper.mapper.userEntityToUsuariosDTO(usuario)).collect(Collectors.toList());
            usuariosDTO*/
            return usuarios;
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }
}
