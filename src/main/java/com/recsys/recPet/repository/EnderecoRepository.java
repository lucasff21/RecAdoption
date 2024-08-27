package com.recsys.recPet.repository;

import com.recsys.recPet.animals.Cachorro;
import com.recsys.recPet.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
