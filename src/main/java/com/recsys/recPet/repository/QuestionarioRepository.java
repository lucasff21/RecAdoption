package com.recsys.recPet.repository;

import com.recsys.recPet.model.Questionario;
import com.recsys.recPet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionarioRepository extends JpaRepository<Questionario, Long> {

    Questionario findByUser_Id(Long id);
}
