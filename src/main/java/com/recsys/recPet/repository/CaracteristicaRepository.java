package com.recsys.recPet.repository;

import com.recsys.recPet.model.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long>, JpaSpecificationExecutor<Caracteristica> {
    List<Caracteristica> findByNomeContainingIgnoreCase(String nome);
}
