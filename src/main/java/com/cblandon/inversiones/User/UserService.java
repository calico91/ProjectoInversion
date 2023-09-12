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
            return usuarios;

            List<UsuariosDTO> usuariosDto =
                    usuarios.stream().map(usuario -> {
                        UsuariosDTO usuariosDTO = UsuariosDTO.builder().
                                username(usuario.getUsername())
                                .lastname(usuario.getLastname())
                                .firstname(usuario.getFirstname())
                                .country(usuario.getCountry())
                                .roles(usuario.getRoles())
                                .
                                .itemMovimientos(e.getItemMovimientos())
                                .vendedor(UsuarioResponseDTO.builder().nombre(e.getVendedor().getNombre())
                                        .documento(e.getVendedor().getDocumento())
                                        .telefono(e.getVendedor().getTelefono())
                                        .direccion(e.getVendedor().getDireccion())
                                        .ciudad(e.getVendedor().getCiudad())
                                        .estado(e.getVendedor().getEstado())
                                        .build())
                                .cliente(UsuarioResponseDTO.builder().nombre(e.getCliente().getNombre())
                                        .documento(e.getCliente().getDocumento())
                                        .telefono(e.getCliente().getTelefono())
                                        .direccion(e.getCliente().getDireccion())
                                        .ciudad(e.getCliente().getCiudad())
                                        .estado(e.getCliente().getEstado())
                                        .build())
                                .idRemitente(e.getIdRemitente())
                                .idDestino(e.getIdDestino())
                                .descuentoAplicado(e.getDescuentoAplicado())
                                .estado(e.getEstado())
                                .pendiente(e.getPendiente())
                                .cambio(e.getCambio())
                                .build();
                        return movimientoRespnseDTO;
                    }).collect(Collectors.toList());

        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }
}
