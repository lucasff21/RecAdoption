package com.recsys.recPet.service;

import com.recsys.recPet.dto.pagina.PaginaRequestDTO;
import com.recsys.recPet.dto.pagina.PaginaResponseDTO;
import com.recsys.recPet.model.Pagina;
import com.recsys.recPet.repository.PaginaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaginaService {

    private final PaginaRepository paginaRepository;

    @Transactional(readOnly = true)
    public Page<PaginaResponseDTO> listarTodas(Pageable pageable) {
        Page<Pagina> paginas = paginaRepository.findAll(pageable);
        return paginas.map(this::toResponseDTO);
    }

    @Transactional
    public PaginaResponseDTO criarPagina(PaginaRequestDTO dto) {
        Pagina novaPagina = new Pagina();
        novaPagina.setNome(dto.getNome());
        novaPagina.setTitulo(dto.getTitulo());
        novaPagina.setConteudo(dto.getConteudo());

        Pagina paginaSalva = paginaRepository.save(novaPagina);
        return toResponseDTO(paginaSalva);
    }

    @Transactional
    public PaginaResponseDTO atualizarPagina(Long id, PaginaRequestDTO dto) {
        Pagina paginaExistente = paginaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Página com ID " + id + " não encontrada."));

        paginaExistente.setNome(dto.getNome());
        paginaExistente.setTitulo(dto.getTitulo());
        paginaExistente.setConteudo(dto.getConteudo());

        Pagina paginaAtualizada = paginaRepository.save(paginaExistente);
        return toResponseDTO(paginaAtualizada);
    }

    @Transactional(readOnly = true)
    public PaginaResponseDTO buscarPorNome(String nome) {
        Pagina pagina = paginaRepository.findByNome(nome)
                .orElseThrow(() -> new EntityNotFoundException("Página '" + nome + "' não encontrada."));
        return toResponseDTO(pagina);
    }

    @Transactional(readOnly = true)
    public PaginaResponseDTO buscarPorId(Long id) {
        Pagina pagina = paginaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Página com ID " + id + " não encontrada."));
        return toResponseDTO(pagina);
    }

    private PaginaResponseDTO toResponseDTO(Pagina pagina) {
        PaginaResponseDTO dto = new PaginaResponseDTO();
        dto.setId(pagina.getId());
        dto.setNome(pagina.getNome());
        dto.setTitulo(pagina.getTitulo());
        dto.setConteudo(pagina.getConteudo());
        dto.setCreatedAt(pagina.getCreatedAt());
        dto.setUpdatedAt(pagina.getUpdatedAt());
        return dto;
    }
}