package com.example.demo.repository;

import com.example.demo.entity.Promocao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromocaoRepository extends CrudRepository<Promocao, Long> {

    List<Promocao> findByNomeContainingIgnoreCase(String nome);

    List<Promocao> findByDataInicioLessThanEqualAndDataFimGreaterThanEqual(
        LocalDate hoje, LocalDate hojeRepetido
    );
}
