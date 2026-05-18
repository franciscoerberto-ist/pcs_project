package com.example.demo.service;

import com.example.demo.entity.Avaliacao;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository repository;
    private final ProdutoService produtoService;

    @Transactional(readOnly = true)
    public List<Avaliacao> listarTodas() {
        return (List<Avaliacao>) repository.findAll();
    }

    @Transactional(readOnly = true)
    public Avaliacao buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Avaliação não encontrada com id: " + id));
    }

    public Avaliacao salvar(Avaliacao avaliacao) {
        produtoService.buscarPorId(avaliacao.getProduto().getId());
        return repository.save(avaliacao);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Avaliacao> buscarPorProduto(Long produtoId) {
        return repository.findByProdutoId(produtoId);
    }
}
