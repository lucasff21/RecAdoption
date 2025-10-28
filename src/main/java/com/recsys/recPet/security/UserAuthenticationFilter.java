package com.recsys.recPet.security;

import com.recsys.recPet.model.User;
import com.recsys.recPet.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    public UserAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Rota: " + request.getRequestURI());

        if (checkIfEndpointIsNotPublic(request)) {
            String token = recoveryToken(request);

            if (token == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token não encontrado.");
                return;
            }

            try {
                String subject = jwtTokenProvider.getSubjectFromToken(token);

                if (subject == null) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido.");
                    return;
                }

                User user = userRepository.findByEmail(subject)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro interno no servidor");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }


    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        AntPathMatcher pathMatcher = new AntPathMatcher();

        return Arrays.stream(SecurityConfiguration.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED)
                .noneMatch(pattern -> pathMatcher.match(pattern, requestURI));
    }

}
