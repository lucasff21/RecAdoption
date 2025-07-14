package com.recsys.recPet.repository;

import com.recsys.recPet.model.AnimalCaracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalCaracteristicaRepository extends JpaRepository<AnimalCaracteristica, Long>
{
}
