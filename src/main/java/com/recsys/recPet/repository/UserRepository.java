package com.recsys.recPet.repository;

import com.recsys.recPet.enums.TipoUsuario;
import com.recsys.recPet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findByResetPasswordToken(String token);
    long countByTipo(TipoUsuario tipo);
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.adocoes a " +
            "LEFT JOIN FETCH a.animal an " +
            "WHERE u.id = :id")
    Optional<User> findByIdWithAdocoesAndAnimais(Long id);
}
