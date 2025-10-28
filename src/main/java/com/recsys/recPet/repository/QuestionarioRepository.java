package com.recsys.recPet.repository;

import com.recsys.recPet.model.Questionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionarioRepository extends JpaRepository<Questionario, Long> {

    Questionario findByUserId(Long id);
}
