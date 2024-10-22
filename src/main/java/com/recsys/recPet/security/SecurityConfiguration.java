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
            "/api/cachorro/findAll",
            "/users",
            "/users/*",

    };

    public static final String[] ENDPOINTS_ADOTANTE = {

    };

    public static final String[] ENDPOINTS_ADMIN = {
            "/api/cachorro",
            "/api/cachorro/*",

            "/api/questionario",
            "/api/questionario/*"

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                                // Permissões para Admin
                                .requestMatchers(HttpMethod.GET,"/api/questionario", "/api/questionario/*").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/questionario", "/api/questionario/*").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/questionario", "/api/questionario/*").hasAuthority("ADMIN")
                                //.requestMatchers(HttpMethod.POST, "/api/cachorro").hasAuthority("ADMIN")
                                .requestMatchers(ENDPOINTS_ADMIN).hasAuthority("ADMIN")


                                .requestMatchers(HttpMethod.POST, "/api/questionario").hasAuthority("ADOTANTE") // Adotante pode fazer POST

                                .anyRequest().denyAll() // Deny all for other requests
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