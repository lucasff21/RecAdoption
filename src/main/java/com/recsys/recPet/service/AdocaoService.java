package com.recsys.recPet.service;

import com.recsys.recPet.dto.AdocaoResponseDTO;
import com.recsys.recPet.dto.admin.AdminAdocaoDTO;
import com.recsys.recPet.model.Adocao;
import com.recsys.recPet.model.Questionario;
import com.recsys.recPet.repository.AdocaoRepository;
import com.recsys.recPet.repository.QuestionarioRepository;
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
    private final QuestionarioRepository questionarioRepository;
    private final UserRepository userRepository;

    public AdocaoService(AdocaoRepository adocaoRepository, QuestionarioRepository questionarioRepository ,
                         UserRepository userRepository) {
        this.adocaoRepository = adocaoRepository;
        this.questionarioRepository = questionarioRepository;
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

    public Page<AdminAdocaoDTO> findAllAdocoesWithQuestionario(Pageable pageable) {
        Page<Adocao> adocaoPage = adocaoRepository.findAll(pageable);

        return adocaoPage.map(adocao -> {
            Optional<Questionario> questionnaire = Optional.ofNullable(questionarioRepository.findByUser_Id(adocao.getUser().getId()));
            return new AdminAdocaoDTO(adocao, questionnaire.orElse(null));
        });
    }

    public List<AdocaoResponseDTO> getAdocoesByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }

        List<Adocao> adocoes = adocaoRepository.findByUserId(userId);

        return adocoes.stream()
                .map(AdocaoResponseDTO::new)
                .collect(Collectors.toList());
    }
}
