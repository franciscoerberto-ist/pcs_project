package com.example.demo.service;

import com.example.demo.entity.Produto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaService categoriaService;

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return (List<Produto>) repository.findAll();
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Produto não encontrado com id: " + id));
    }

    public Produto salvar(Produto produto) {
        if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
            categoriaService.buscarPorId(produto.getCategoria().getId());
        }
        return repository.save(produto);
    }

    public Produto atualizar(Long id, Produto dados) {
        Produto produto = buscarPorId(id);
        if (dados.getNome() != null)      produto.setNome(dados.getNome());
        if (dados.getPreco() != null)     produto.setPreco(dados.getPreco());
        if (dados.getDescricao() != null) produto.setDescricao(dados.getDescricao());
        if (dados.getCategoria() != null && dados.getCategoria().getId() != null) {
            categoriaService.buscarPorId(dados.getCategoria().getId());
            produto.setCategoria(dados.getCategoria());
        }
        return repository.save(produto);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorCategoria(Long categoriaId) {
        return repository.findByCategoriaId(categoriaId);
    }
}
