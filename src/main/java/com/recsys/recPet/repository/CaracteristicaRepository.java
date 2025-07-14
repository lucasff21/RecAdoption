package com.recsys.recPet.repository;

import com.recsys.recPet.model.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {
    Optional<Caracteristica> findByNome(String nome);
    List<Caracteristica> findByNomeIn(List<String> nomes);
}
