package com.cblandon.inversiones.Security.filters;


import com.cblandon.inversiones.Security.jwt.JwtUtils;
import com.cblandon.inversiones.User.UserDetailsServiceImpl;
import com.cblandon.inversiones.Utils.ResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization");

        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);

            if (jwtUtils.isTokenValid(token)) {
                String username = jwtUtils.getUsernameFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                String message = "";
                try {
                    jwtUtils.getClaim(token, Claims::getExpiration);
                } catch (RuntimeException e) {
                    message = "token-has-expired";
                }
                responseClient(response,
                        new ResponseHandler().generateResponseWithoutData(message, HttpStatus.UNAUTHORIZED));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void responseClient(final HttpServletResponse response, final ResponseEntity<Object> responseEntity) {
        try {
            response.setStatus(responseEntity.getStatusCodeValue());
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
            response.getWriter().flush();
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
