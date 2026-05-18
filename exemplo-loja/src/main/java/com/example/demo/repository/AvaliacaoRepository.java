package com.example.demo.repository;

import com.example.demo.entity.Avaliacao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends CrudRepository<Avaliacao, Long> {

    List<Avaliacao> findByProdutoId(Long produtoId);

    List<Avaliacao> findByNota(Integer nota);

    List<Avaliacao> findByNotaGreaterThanEqual(Integer nota);
}
