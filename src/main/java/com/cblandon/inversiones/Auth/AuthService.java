package com.cblandon.inversiones.Auth;

import com.cblandon.inversiones.Auth.dto.AuthResponseDTO;
import com.cblandon.inversiones.Auth.dto.LoginRequestDTO;
import com.cblandon.inversiones.Auth.dto.RegisterRequestDTO;
import com.cblandon.inversiones.Excepciones.RequestException;
import com.cblandon.inversiones.Mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cblandon.inversiones.Jwt.JwtService;
import com.cblandon.inversiones.User.Role;
import com.cblandon.inversiones.User.User;
import com.cblandon.inversiones.User.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

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

            //logger.error("error en el login: no existe el usuario " + username + " en el sistema!");
            throw new RequestException("Usuario o contraseña invalido", "1");
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

    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) throws Exception {
        Optional<User> consultarUser = userRepository.findByUsername(registerRequestDTO.getUsername());
        if (!consultarUser.isEmpty()) {
            throw new RequestException("usuario " + registerRequestDTO.getUsername() + " ya se encuentra creado", "1");
        }

        User user = Mapper.mapper.registerRequestDTOToUser(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        return AuthResponseDTO.builder()
                .token(jwtService.getToken(user))
                .build();

    }

}
