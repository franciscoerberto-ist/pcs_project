package com.example.demo.service;

import com.example.demo.entity.Categoria;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;

    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return (List<Categoria>) repository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Categoria não encontrada com id: " + id));
    }

    public Categoria salvar(Categoria categoria) {
        return repository.save(categoria);
    }

    public Categoria atualizar(Long id, Categoria dados) {
        Categoria categoria = buscarPorId(id);
        categoria.setNome(dados.getNome());
        if (dados.getDescricao() != null) {
            categoria.setDescricao(dados.getDescricao());
        }
        return repository.save(categoria);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
