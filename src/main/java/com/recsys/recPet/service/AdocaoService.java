package com.recsys.recPet.service;

import com.recsys.recPet.model.Adocao;
import com.recsys.recPet.repository.AdocaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdocaoService {
    private final AdocaoRepository adocaoRepository;

    public AdocaoService(AdocaoRepository adocaoRepository) {
        this.adocaoRepository = adocaoRepository;
    }

    public Adocao save(Adocao adocao){
        return adocaoRepository.save(adocao);
    }

    public Adocao findById(Long id){
        return adocaoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Adoção não encontrada"));
    }

    public Adocao update(Adocao adocao){
        Optional<Adocao> adocaoOptional = Optional.ofNullable(findById(adocao.getId()));

        if(adocaoOptional.isPresent()){
            return adocaoRepository.save(adocao);
        } else {
            throw new EntityNotFoundException("Adoção não encontrada");
        }
    }

    public List<Adocao> findAall(){
        return adocaoRepository.findAll();
    }

    public void delete(Long id){
        Adocao adocao = findById(id);
        adocaoRepository.delete(adocao);
    }

}
