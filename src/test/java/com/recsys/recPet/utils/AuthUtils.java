package com.recsys.recPet.utils;
import com.recsys.recPet.enums.TipoUsuario;
import com.recsys.recPet.model.User;
import com.recsys.recPet.repository.UserRepository;
import com.recsys.recPet.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class AuthUtils {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String generateAdminToken() {
        User user = userRepository.findByEmail("admin@test.com")
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail("admin@test.com");
                    newUser.setSenha(passwordEncoder.encode("123"));
                    newUser.setTipo(TipoUsuario.ADMIN);
                    return userRepository.save(newUser);
                });

        return tokenProvider.generateToken(user);
    }

    public String generateUserToken() {
        User regularUser = new User();
        regularUser.setEmail("user@test.com");
        regularUser.setTipo(TipoUsuario.ADOTANTE);

        return tokenProvider.generateToken(regularUser);
    }
}
