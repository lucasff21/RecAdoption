package com.recsys.recPet.repository;

import com.recsys.recPet.model.Pagina;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaginaRepository extends JpaRepository<Pagina, Long> {
    Optional<Pagina> findByNome(String nome);
}