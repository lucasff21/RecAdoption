package com.recsys.recPet.repository;

import com.recsys.recPet.model.Adocao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdocaoRepository extends JpaRepository<Adocao, Long> {
    List<Adocao> findByUserId(Long userId);
}
