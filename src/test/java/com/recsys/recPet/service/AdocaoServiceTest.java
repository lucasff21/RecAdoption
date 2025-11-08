package com.recsys.recPet.service;

import com.recsys.recPet.dto.adocao.AdocaoUpdateDTO;
import com.recsys.recPet.enums.adocao.AdocaoStatus;
import com.recsys.recPet.enums.TipoUsuario;
import com.recsys.recPet.model.Adocao;
import com.recsys.recPet.model.Animal;
import com.recsys.recPet.model.User;
import com.recsys.recPet.repository.AdocaoRepository;
import com.recsys.recPet.repository.AnimalRepository;
import com.recsys.recPet.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class AdocaoServiceTest {

    @Autowired
    private AdocaoService adocaoService;

    @Autowired
    private AdocaoRepository adocaoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnimalRepository animalRepository;


    private User buildUser(String email, TipoUsuario tipo) {
        User user = new User();
        user.setEmail(email);
        user.setTipo(tipo);
        user.setSenha("123");
        return userRepository.save(user);
    }


    private Animal buildAnimal(String nome) {
        Animal animal = new Animal();
        animal.setNome(nome);
        animal.setDisponivelParaAdocao(true);
        return animalRepository.save(animal);
    }


    private Adocao buildAdocao(User user, Animal animal, AdocaoStatus status) {
        Adocao adocao = new Adocao();
        adocao.setUser(user);
        adocao.setAnimal(animal);
        adocao.setStatus(status);
        return adocaoRepository.save(adocao);
    }


    @Test
    void deveCancelarOutrasAdocoesAbertasAoFinalizarUma() {
        // ARRANGE (

        // Criar usuários adotantes
        User user1 = buildUser("user1@test.com", TipoUsuario.ADOTANTE);
        User user2 = buildUser("user2@test.com", TipoUsuario.ADOTANTE);

        // Criar o Pet que será disputado
        Animal petDisputado = buildAnimal("Fido");

        // Criar várias solicitações de adoção para o MESMO pet

        // Esta é a adoção que vamos finalizar
        Adocao adocaoAFinalizar = buildAdocao(user1, petDisputado, AdocaoStatus.APROVADO);

        // Esta deve ser cancelada (está PENDENTE)
        Adocao adocaoACancelar1 = buildAdocao(user2, petDisputado, AdocaoStatus.PENDENTE);

        // Esta deve ser cancelada (está EM_ANALISE)
        Adocao adocaoACancelar2 = buildAdocao(user1, petDisputado, AdocaoStatus.EM_ANALISE);

        // Esta NÃO deve ser alterada (já está RECUSADO)
        Adocao adocaoJaRecusada = buildAdocao(user2, petDisputado, AdocaoStatus.RECUSADO);

        Animal outroPet = buildAnimal("Rex");
        Adocao adocaoOutroPet = buildAdocao(user1, outroPet, AdocaoStatus.PENDENTE);

        // ACT

        // Criar o DTO de atualização
        AdocaoUpdateDTO updateDTO = new AdocaoUpdateDTO();
        updateDTO.setStatus(AdocaoStatus.FINALIZADO);
        updateDTO.setObservacoes("Adoção concluída com sucesso.");

        adocaoService.atualizarStatus(adocaoAFinalizar.getId(), updateDTO);

        // ASSERT

        // Buscar as entidades atualizadas do banco de dados
        Adocao adocaoFinalizadaCheck = adocaoRepository.findById(adocaoAFinalizar.getId()).get();
        Adocao adocaoCancelada1Check = adocaoRepository.findById(adocaoACancelar1.getId()).get();
        Adocao adocaoCancelada2Check = adocaoRepository.findById(adocaoACancelar2.getId()).get();
        Adocao adocaoRecusadaCheck = adocaoRepository.findById(adocaoJaRecusada.getId()).get();
        Adocao adocaoOutroPetCheck = adocaoRepository.findById(adocaoOutroPet.getId()).get();

        // Verificação 1: A adoção principal foi FINALIZADA
        assertThat(adocaoFinalizadaCheck.getStatus()).isEqualTo(AdocaoStatus.FINALIZADO);

        // Verificação 2: As outras adoções ABERTAS do MESMO pet foram canceladas
        assertThat(adocaoCancelada1Check.getStatus()).isEqualTo(AdocaoStatus.RECUSADO);
        assertThat(adocaoCancelada2Check.getStatus()).isEqualTo(AdocaoStatus.RECUSADO);

        // Verificação 3: A adoção que já estava fechada (RECUSADO) não foi alterada
        assertThat(adocaoRecusadaCheck.getStatus()).isEqualTo(AdocaoStatus.RECUSADO);

        // Verificação 4: A adoção do OUTRO pet não foi alterada
        assertThat(adocaoOutroPetCheck.getStatus()).isEqualTo(AdocaoStatus.PENDENTE);
    }
}