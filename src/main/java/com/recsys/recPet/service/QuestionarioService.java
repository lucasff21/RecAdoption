package com.recsys.recPet.service;

import com.recsys.recPet.exception.handler.ResourceNotFoundException;
import com.recsys.recPet.model.Questionario;
import com.recsys.recPet.model.User;
import com.recsys.recPet.repository.QuestionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionarioService {

    private final QuestionarioRepository questionarioRepository;

    public QuestionarioService(QuestionarioRepository questionarioRepository) {
        this.questionarioRepository = questionarioRepository;
    }

    public Questionario save(Questionario questionario) {
        return questionarioRepository.save(questionario);
    }

    public Questionario findById(Long id) {
        return questionarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Questionario não encontrado"));
    }

    public Questionario update(Questionario questionario) {
        Optional<Questionario> questionarioOptional = questionarioRepository.findById(questionario.getId());

        if(questionarioOptional.isPresent()){
            return questionarioRepository.save(questionario);
        } else {
            throw new EntityNotFoundException("Questionario não encontrada");
        }
    }

    public List<Questionario> findAll(){
        return questionarioRepository.findAll();
    }

    public void delete(Long id, User user) {
        Questionario questionario = questionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Questionário não encontrado"));

        if (!questionario.getUser().getId().equals(user.getId())) {
            throw new  ResourceNotFoundException("Questionário não encontrado");
        }

        questionarioRepository.delete(questionario);
    }

    public Questionario findByUser(Long id){
        return questionarioRepository.findByUserId(id);
    }

    public Questionario getQuestionarioByUser(User user){
        return questionarioRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Questionário não encontrado"));
    }
}