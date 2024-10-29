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

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserAuthenticationFilter userAuthenticationFilter;

    public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/api/cachorro",
            "/users",
            "/users/*",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz ->
                        authz
                                // Permitir acesso para ADOTANTE e ADMIN em rotas de questionário
                                .requestMatchers(HttpMethod.GET, "/api/adocao", "/api/adocao/*", "/api/questionario", "/api/questionário","/api/questionário/*").hasAnyAuthority("ADOTANTE", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/adocao", "/api/adocao/*", "/api/questionario", "/api/questionário").hasAnyAuthority("ADOTANTE", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/adocao", "/api/adocao/*", "/api/questionario/*").hasAnyAuthority("ADOTANTE", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/adocao", "/api/adocao/*", "/api/questionario/*").hasAnyAuthority("ADMIN", "ADOTANTE")


                                .requestMatchers(HttpMethod.POST, "/api/cachorro").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/cachorro").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/cachorro").hasAuthority("ADMIN")

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