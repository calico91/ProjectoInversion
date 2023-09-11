package com.cblandon.inversiones.Auth;

import com.cblandon.inversiones.Auth.dto.AuthResponseDTO;
import com.cblandon.inversiones.Auth.dto.LoginRequestDTO;
import com.cblandon.inversiones.Auth.dto.RegisterRequestDTO;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.Mapper;
import com.cblandon.inversiones.Roles.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cblandon.inversiones.Jwt.JwtService;
import com.cblandon.inversiones.Roles.Role;
import com.cblandon.inversiones.User.User;
import com.cblandon.inversiones.User.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;


    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Optional<User> user = userRepository.findByUsername(loginRequestDTO.getUsername());


        if (user.isEmpty()) {

            throw new RequestException("Usuario o contraseña invalido", "2");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword()));
        UserDetails userLogin = userRepository.findByUsername(loginRequestDTO.getUsername()).orElseThrow();
        String token = jwtService.getToken(userLogin);
        return AuthResponseDTO.builder()
                .token(token)
                .build();

    }

    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        Optional<User> consultarUser = userRepository.findByUsername(registerRequestDTO.getUsername());
        if (!consultarUser.isEmpty()) {

            throw new RequestException("Usuario ya se encuentra registrado", "2");
        }

        try {
            User user = User.builder()
                    .username(registerRequestDTO.getUsername())
                    .firstname(registerRequestDTO.getFirstname())
                    .lastname(registerRequestDTO.getLastname())
                    .country(registerRequestDTO.getCountry())
                    .build();

            user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
            Set<Roles> authorities = registerRequestDTO.getRoles().stream()
                    .map(role -> Roles.builder()
                            .name(Role.valueOf(role))
                            .build())
                    .collect(Collectors.toSet());

            user.setRoles(authorities);

            userRepository.save(user);

            return AuthResponseDTO.builder()
                    .token(jwtService.getToken(user))
                    .build();
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }


    }

}
