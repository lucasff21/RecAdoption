package com.recsys.recPet.repository;

import com.recsys.recPet.enums.TipoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.recsys.recPet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Page<User> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(String nome, String email, Pageable pageable);

    Page<User> findByTipoUsuario(TipoUsuario tipoUsuario, Pageable pageable);

    Page<User> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseAndTipoUsuario(
            String nome, String email, TipoUsuario tipoUsuario, Pageable pageable
    );
}
