package com.recsys.recPet.repository;

import com.recsys.recPet.animals.Cachorro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CachorroRepository extends JpaRepository<Cachorro, Long> {
}
