package com.recsys.recPet.repository;

import com.recsys.recPet.model.Questionario;
import com.recsys.recPet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionarioRepository extends JpaRepository<Questionario, Long> {

    Questionario findByUser_Id(Long id);

    @Query("SELECT q From Questionario q WHERE q.user.email = :email ")
    Questionario findByUserEmail(@Param("email") String email);
}
