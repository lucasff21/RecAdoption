package com.recsys.recPet.service;

import com.recsys.recPet.dto.admin.AdocaoResponseAdminDTO;
import com.recsys.recPet.dto.adocao.AdocaoResponseDTO;import com.recsys.recPet.dto.adocao.AdocaoUpdateDTO;
import com.recsys.recPet.enums.adocao.AdocaoStatus;
import com.recsys.recPet.model.Adocao;
import com.recsys.recPet.model.Animal;
import com.recsys.recPet.model.User;
import com.recsys.recPet.repository.AdocaoRepository;
import com.recsys.recPet.repository.AnimalRepository;
import com.recsys.recPet.repository.UserRepository;
import com.recsys.recPet.repository.specification.AdocaoSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;



@Service
public class AdocaoService {
    private final AdocaoRepository adocaoRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;

    public AdocaoService(AdocaoRepository adocaoRepository,
                         UserRepository userRepository, AnimalRepository animalRepository) {
        this.adocaoRepository = adocaoRepository;
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
    }

    public Adocao criarSolicitacao(Long animalId, User user) {
        List<AdocaoStatus> statusAbertos = List.of(
                AdocaoStatus.PENDENTE,
                AdocaoStatus.EM_ANALISE,
                AdocaoStatus.APROVADO
        );

        boolean jaPossuiSolicitacaoAberta = adocaoRepository.existsByUserIdAndAnimalIdAndStatusIn(
                user.getId(),
                animalId,
                statusAbertos
        );

        if (jaPossuiSolicitacaoAberta) {
            throw new IllegalStateException("Você já possui uma solicitação em aberto para este animal.");
        }

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado!"));

        Adocao adocao = new Adocao();
        adocao.setUser(user);
        adocao.setAnimal(animal);
        adocao.setStatus(AdocaoStatus.PENDENTE);


        return adocaoRepository.save(adocao);
    }

    public Adocao save(Adocao adocao){
        return adocaoRepository.save(adocao);
    }

    public Adocao findByIdAndUser(Long id, User usuario) {
        Adocao adocao = adocaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adoção não encontrada com"));
        if (!adocao.getUser().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("Usuário não autorizado para acessar este recurso.");
        }
        return adocao;
    }

    public Adocao update(Long id, User usuario){
        Adocao adocaoExistente = findByIdAndUser(id, usuario);
        return adocaoRepository.save(adocaoExistente);
    }

    public PageImpl<AdocaoResponseAdminDTO> findAllAdocoes(AdocaoStatus status, String termo, Pageable pageable) {
        Specification<Adocao> spec = Specification
                .where(AdocaoSpecification.comStatus(status))
                .and(AdocaoSpecification.comTermoDeBusca(termo));

        Page<Adocao> adocaoPage = adocaoRepository.findAll(spec, pageable);

        List<AdocaoResponseAdminDTO> dtoList = adocaoPage.getContent().stream()
                .map(AdocaoResponseAdminDTO::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, adocaoPage.getTotalElements());
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

    public Adocao atualizarStatus(Long id, AdocaoUpdateDTO dto) {
        Adocao adocao = adocaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoção não encontrada com o id: " + id));

        if (adocao.isConcluida()) {
            throw new IllegalStateException("Esta adoção já foi concluída e não pode ter seu status alterado.");
        }

        if (dto.getObservacoes() != null) {
            adocao.setObservacoes(dto.getObservacoes());
        }

        switch (dto.getStatus()) {
            case FINALIZADO:
                this.finalizarProcessoAdocao(adocao);
                break;
            case RECUSADO:
                adocao.cancelar();
                break;
            default:
                adocao.setStatus(dto.getStatus());
                break;
        }

        return adocaoRepository.save(adocao);
    }

    private void finalizarProcessoAdocao(Adocao adocao) {
        adocao.finalizar();

        List<AdocaoStatus> statusAbertos = List.of(AdocaoStatus.PENDENTE, AdocaoStatus.EM_ANALISE, AdocaoStatus.APROVADO);
        List<Adocao> outrasAdocoes = adocaoRepository.findByAnimalAndIdNotAndStatusIn(
                adocao.getAnimal(),
                adocao.getId(),
                statusAbertos
        );

        for (Adocao outraAdocao : outrasAdocoes) {
            outraAdocao.cancelar();
        }

        adocaoRepository.saveAll(outrasAdocoes);
    }

    public void deletarAdocaoUusuario(Long usuarioId, Long adocaoId) {
        Optional<Adocao> adocaoOptional = adocaoRepository.findByIdAndUserId(adocaoId, usuarioId);

        adocaoOptional.ifPresent(adocaoRepository::delete);
    }

    public Page<AdocaoResponseAdminDTO> findAdocoesByAnimalId(Long animalId, Pageable pageable) {
        return adocaoRepository.findByAnimalId(animalId, pageable)
                .map(AdocaoResponseAdminDTO::fromEntity);
    }

    public Page<AdocaoResponseDTO> findPageByUserId(Long userId, Pageable pageable) {
        Page<Adocao> adocaoPage = adocaoRepository.findByUserId(userId, pageable);
        return adocaoPage.map(AdocaoResponseDTO::fromEntity);
    }
}
