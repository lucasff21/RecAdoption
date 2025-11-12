package com.recsys.recPet.repository;

import com.recsys.recPet.enums.adocao.AdocaoStatus;
import com.recsys.recPet.model.Adocao;
import com.recsys.recPet.model.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdocaoRepository extends JpaRepository<Adocao, Long>, JpaSpecificationExecutor<Adocao> {
    List<Adocao> findByUserId(Long userId);
    Page<Adocao> findByUserId(Long userId, Pageable pageable);
    List<Adocao> findByAnimalAndIdNotAndStatusIn(Animal animal, Long idExcluido, List<AdocaoStatus> statuses);
    Optional<Adocao> findByIdAndUserId(Long id, Long userId);
    Page<Adocao> findByAnimalId(Long animalId, Pageable pageable);
    long countByStatus(AdocaoStatus status);
}
