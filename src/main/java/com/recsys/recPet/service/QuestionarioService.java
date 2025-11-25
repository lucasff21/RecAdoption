package com.recsys.recPet.service;

import com.recsys.recPet.dto.questionario.QuestionarioCreateDTO;
import com.recsys.recPet.dto.questionario.QuestionarioResponseDTO;
import com.recsys.recPet.model.Questionario;
import com.recsys.recPet.model.User;
import com.recsys.recPet.repository.QuestionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionarioService {

    private final QuestionarioRepository questionarioRepository;

    public QuestionarioService(QuestionarioRepository questionarioRepository) {
        this.questionarioRepository = questionarioRepository;
    }

    @Transactional(readOnly = true)
    public QuestionarioResponseDTO getQuestionarioByUser(User user) {
        return questionarioRepository.findByUserId(user.getId())
                .map(QuestionarioResponseDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado para este usuário."));
    }


    @Transactional
    public QuestionarioResponseDTO create(User user, QuestionarioCreateDTO dto) {

        if (questionarioRepository.findByUserId(user.getId()).isPresent()) {
            throw new IllegalStateException("O usuário já possui um questionário. Use a rota de atualização (PUT).");
        }

        Questionario questionario = new Questionario();
        questionario.setUser(user);

        mapDtoToEntity(dto, questionario);

        Questionario salvo = questionarioRepository.save(questionario);
        return new QuestionarioResponseDTO(salvo);
    }

    @Transactional
    public QuestionarioResponseDTO update(User user, QuestionarioCreateDTO dto) {
        Questionario questionario = questionarioRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Questionário não encontrado para este usuário. Crie um primeiro (POST)."));

        mapDtoToEntity(dto, questionario);

        Questionario salvo = questionarioRepository.save(questionario);
        return new QuestionarioResponseDTO(salvo);
    }

    private void mapDtoToEntity(QuestionarioCreateDTO dto, Questionario questionario) {
        questionario.setPreferenciaSexo(dto.getPreferenciaSexo());
        questionario.setTemCriancas(dto.getTemCriancas());
        questionario.setPreferenciaPorte(dto.getPreferenciaPorte());
        questionario.setNivelQuedaPelo(dto.getNivelQuedaPelo());
        questionario.setNivelLatido(dto.getNivelLatido());
        questionario.setInstintoGuarda(dto.getInstintoGuarda());
        questionario.setNivelEnergia(dto.getNivelEnergia());
        questionario.setMoradia(dto.getMoradia());
        questionario.setTempoDisponivel(dto.getTempoDisponivel());
        questionario.setExperienciaPets(dto.getExperienciaPets());
        questionario.setDisposicaoNecessidadesEspeciais(dto.getDisposicaoNecessidadesEspeciais());
        questionario.setPossuiCaes(dto.getPossuiCaes());
        questionario.setPossuiGatos(dto.getPossuiGatos());
        questionario.setCienteCustos(dto.getCienteCustos());

        questionario.setTermoCompromissoLongoPrazo(dto.getTermoCompromissoLongoPrazo());
        questionario.setTermoSaudeBemEstar(dto.getTermoSaudeBemEstar());
        questionario.setTermoPacienciaAdaptacao(dto.getTermoPacienciaAdaptacao());
        questionario.setTermoVistoria(dto.getTermoVistoria());
        questionario.setTermoDevolucaoNaoAbandono(dto.getTermoDevolucaoNaoAbandono());
        questionario.setTermoLegislacaoPosseResponsavel(dto.getTermoLegislacaoPosseResponsavel());
    }
}