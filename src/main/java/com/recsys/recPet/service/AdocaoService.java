package com.recsys.recPet.service;

import com.recsys.recPet.dto.adocao.AdocaoResponseDTO;
import com.recsys.recPet.model.Adocao;
import com.recsys.recPet.repository.AdocaoRepository;
import com.recsys.recPet.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

@Service
public class AdocaoService {
    private final AdocaoRepository adocaoRepository;
    private final UserRepository userRepository;

    public AdocaoService(AdocaoRepository adocaoRepository,
                         UserRepository userRepository) {
        this.adocaoRepository = adocaoRepository;
        this.userRepository = userRepository;
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

    public Page<Adocao> findAllAdocoesWithQuestionario(Pageable pageable) {
        return adocaoRepository.findAll(pageable);
    }

    public List<AdocaoResponseDTO> getAdocoesByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }

        List<Adocao> adocoes = adocaoRepository.findByUserId(userId);

        return adocoes.stream()
                .map(AdocaoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
