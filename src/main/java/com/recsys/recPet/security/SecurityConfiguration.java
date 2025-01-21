package com.recsys.recPet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserAuthenticationFilter userAuthenticationFilter;

    public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/api/cachorro/findall",
            "/api/cachorro/{id}",
            "/api/cachorro/download-image",

            "/users/create",
            "/users/login",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz ->
                        authz
                                // Permitir acesso para ADOTANTE e ADMIN em rotas de questionário
                                .requestMatchers(HttpMethod.GET, "/api/questionario/findall", "/api/questionario/{id}", "/users/findbyemail/{email}", "/users/{id}","/api/questionario/email/{email}" ).hasAnyAuthority("ADOTANTE", "ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/questionario", "/api/adocao").hasAnyAuthority("ADOTANTE", "ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/questionario/{id}" ).hasAnyAuthority("ADOTANTE", "ADMIN")


                                .requestMatchers(HttpMethod.GET, "/users/findall", "/api/adocao").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/cachorro/create", "/api/cachorro/uploade-image").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/cachorro/{id}", "/users/{id}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/cachorro", "/api/questionario/{id}", "/users/{id}", "/api/cachorro/delete/{fileName}").hasAuthority("ADMIN")

                                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()


                                .anyRequest().denyAll() // Negar todas as outras requisições
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
}