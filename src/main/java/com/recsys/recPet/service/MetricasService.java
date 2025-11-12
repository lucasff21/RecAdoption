package com.recsys.recPet.service;

import com.recsys.recPet.enums.adocao.AdocaoStatus;
import com.recsys.recPet.enums.animal.Tipo;
import com.recsys.recPet.enums.TipoUsuario;
import com.recsys.recPet.dto.admin.metricas.*;
import com.recsys.recPet.repository.AdocaoRepository;
import com.recsys.recPet.repository.AnimalRepository;
import com.recsys.recPet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MetricasService {
    @Autowired
    private AdocaoRepository adocaoRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public SistemaMetricasDTO getSystemMetrics() {
        AdocaoMetricasDTO adocaoMetrics = new AdocaoMetricasDTO();
        adocaoMetrics.setTotalSolicitacoes(adocaoRepository.count());
        adocaoMetrics.setPendentes(adocaoRepository.countByStatus(AdocaoStatus.PENDENTE));
        adocaoMetrics.setEmAnalise(adocaoRepository.countByStatus(AdocaoStatus.EM_ANALISE));
        adocaoMetrics.setAprovadas(adocaoRepository.countByStatus(AdocaoStatus.APROVADO));
        adocaoMetrics.setRecusadas(adocaoRepository.countByStatus(AdocaoStatus.RECUSADO));
        adocaoMetrics.setFinalizadas(adocaoRepository.countByStatus(AdocaoStatus.FINALIZADO));

        AnimalMetricasDTO animalMetrics = new AnimalMetricasDTO();
        animalMetrics.setTotalAnimais(animalRepository.count());
        animalMetrics.setDisponiveis(animalRepository.countByDisponivelParaAdocao(true));
        animalMetrics.setNaoDisponiveis(animalRepository.countByDisponivelParaAdocao(false));
        animalMetrics.setTotalCachorros(animalRepository.countByTipo(Tipo.CACHORRO));
        animalMetrics.setTotalGatos(animalRepository.countByTipo(Tipo.GATO));

        long totalUsuarios = userRepository.count();
        long totalAdotantes = userRepository.countByTipo(TipoUsuario.ADOTANTE);
        long totalAdmins = userRepository.countByTipo(TipoUsuario.ADMIN);
        long totalModeradores = userRepository.countByTipo(TipoUsuario.MODERATOR);

        UsuarioMetricasDTO usuarioMetrics = new UsuarioMetricasDTO();
        usuarioMetrics.setTotalUsuarios(totalUsuarios);
        usuarioMetrics.setTotalAdotantes(totalAdotantes);
        usuarioMetrics.setTotalAdmins(totalAdmins);
        usuarioMetrics.setTotalModeradores(totalModeradores);

        return new SistemaMetricasDTO(adocaoMetrics, animalMetrics, usuarioMetrics);
    }
}

