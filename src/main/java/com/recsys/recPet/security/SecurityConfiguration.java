package com.recsys.recPet.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${front.url}")
    private String urlFront;

    private final UserAuthenticationFilter userAuthenticationFilter;

    public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/api/cachorro/findall",
            "/users/create",
            "/users/login",
            "/users/password-reset/request",
            "/users/new-password",
            "/paginas/{nome}",
            "/api/cachorro/caracteristicas"
    };

    public SecurityConfiguration(UserAuthenticationFilter userAuthenticationFilter) {
        this.userAuthenticationFilter = userAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/cachorro/{id}").permitAll()

                                // Permitir acesso para ADOTANTE e ADMIN em rotas de questionário
                                .requestMatchers(HttpMethod.GET, "/api/questionario/findall", "/api/questionario/{id}", "/api/adocao").hasAnyAuthority("ADOTANTE", "ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/questionario", "/api/adocao/create" ).hasAnyAuthority("ADOTANTE", "ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/questionario/{id}", "users/me").hasAnyAuthority("ADOTANTE", "ADMIN")

                                .requestMatchers(HttpMethod.GET, "/admin/users", "/admin/animais", "/admin/animais/{id}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/cachorro/create", "/api/cachorro/uploade-image").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/cachorro/{id}", "/users/{id}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/cachorro/{id}", "/api/questionario/{id}", "/users/{id}").hasAuthority("ADMIN")
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")

                                .anyRequest().denyAll()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )
                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(urlFront));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException, IOException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(String.format("{\"status\":%d,\"error\":\"%s\",\"message\":\"%s\"}",
                    HttpServletResponse.SC_UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase(), "Credenciais de autenticação inválidas ou ausentes."));
            response.getWriter().flush();
        }
    }

    private static class CustomAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write(String.format("{\"status\":%d,\"error\":\"%s\",\"message\":\"%s\"}",
                    HttpServletResponse.SC_FORBIDDEN, HttpStatus.FORBIDDEN.getReasonPhrase(), "Você não tem permissão para acessar este recurso."));
            response.getWriter().flush();
        }
    }
}
