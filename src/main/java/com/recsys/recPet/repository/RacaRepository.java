package com.recsys.recPet.repository;

import com.recsys.recPet.model.Raca;
import com.recsys.recPet.enums.animal.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RacaRepository extends JpaRepository<Raca, Integer> {
    List<Raca> findByEspecie(Tipo especie);
}