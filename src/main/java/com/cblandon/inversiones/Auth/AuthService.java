package com.cblandon.inversiones.Auth;

import com.cblandon.inversiones.Auth.dto.AuthResponseDTO;
import com.cblandon.inversiones.Auth.dto.LoginRequestDTO;
import com.cblandon.inversiones.Auth.dto.RegisterRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cblandon.inversiones.Jwt.JwtService;
import com.cblandon.inversiones.User.Role;
import com.cblandon.inversiones.User.User;
import com.cblandon.inversiones.User.UserRepository;

import lombok.RequiredArgsConstructor;

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
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword()));
        UserDetails user = userRepository.findByUsername(loginRequestDTO.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponseDTO.builder()
                .token(token)
                .build();

    }

    public AuthResponseDTO register(RegisterRequestDTO request) throws Exception {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return AuthResponseDTO.builder()
                .token(jwtService.getToken(user))
                .build();

    }

}
