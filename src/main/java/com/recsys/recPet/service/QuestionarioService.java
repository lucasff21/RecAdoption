package com.recsys.recPet.service;

import com.recsys.recPet.model.Questionario;
import com.recsys.recPet.repository.QuestionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionarioService {

    @Autowired
    private QuestionarioRepository questionarioRepository;

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

    public void delete(Long id){
        Questionario questionario = findById(id);
        questionarioRepository.delete(questionario);
    }

    public Questionario findByUser(Long id){
        return questionarioRepository.findByUser_Id(id);
    }
}
