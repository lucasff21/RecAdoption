package com.recsys.recPet.repository;

import com.recsys.recPet.model.Questionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionarioRepository extends JpaRepository<Questionario, Long> {

    Optional<Questionario> findByUserId(Long id);
}
