package com.recsys.recPet.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${front.url}")
    private String urlFront;

    private final UserAuthenticationFilter userAuthenticationFilter;

    public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/api/cachorro/findall",
            "/api/cachorro/{id}",
            "/users/create",
            "/users/login",
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
                                
                                // Permitir acesso para ADOTANTE e ADMIN em rotas de questionário
                                .requestMatchers(HttpMethod.GET, "/api/questionario/findall", "/api/questionario/{id}", "/users/findbyemail/{email}", "/users/{id}","/api/questionario/email/{email}", "/api/adocao", "/api/adocao/usuario/{usuarioId}" ).hasAnyAuthority("ADOTANTE", "ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/questionario", "/api/adocao/create" ).hasAnyAuthority("ADOTANTE", "ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/questionario/{id}" ).hasAnyAuthority("ADOTANTE", "ADMIN")


                                .requestMatchers(HttpMethod.GET, "/admin/users").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/cachorro/create", "/api/cachorro/uploade-image").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/cachorro/{id}", "/users/{id}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/cachorro", "/api/questionario/{id}", "/users/{id}", "/api/cachorro/delete/{fileName}").hasAuthority("ADMIN")
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")

                                .anyRequest().denyAll()
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
}
