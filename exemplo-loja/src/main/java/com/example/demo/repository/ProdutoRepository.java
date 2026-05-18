package com.example.demo.repository;

import com.example.demo.entity.Produto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends CrudRepository<Produto, Long> {

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByCategoriaId(Long categoriaId);

    List<Produto> findByPrecoBetween(BigDecimal min, BigDecimal max);

    boolean existsByNomeIgnoreCase(String nome);

    @Query("""
        SELECT p FROM Produto p
        WHERE p.preco <= :limite
        ORDER BY p.preco DESC
        """)
    List<Produto> abaixoDoLimite(@Param("limite") BigDecimal limite);
}
