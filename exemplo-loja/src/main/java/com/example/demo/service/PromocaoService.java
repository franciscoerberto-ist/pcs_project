package com.example.demo.service;

import com.example.demo.entity.Promocao;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PromocaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PromocaoService {

    private final PromocaoRepository repository;

    @Transactional(readOnly = true)
    public List<Promocao> listarTodas() {
        return (List<Promocao>) repository.findAll();
    }

    @Transactional(readOnly = true)
    public Promocao buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Promoção não encontrada com id: " + id));
    }

    public Promocao salvar(Promocao promocao) {
        return repository.save(promocao);
    }

    public Promocao atualizar(Long id, Promocao dados) {
        Promocao promocao = buscarPorId(id);
        if (dados.getNome() != null)            promocao.setNome(dados.getNome());
        if (dados.getDescontoPercent() != null) promocao.setDescontoPercent(dados.getDescontoPercent());
        if (dados.getDataInicio() != null)      promocao.setDataInicio(dados.getDataInicio());
        if (dados.getDataFim() != null)         promocao.setDataFim(dados.getDataFim());
        return repository.save(promocao);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Promocao> listarVigentes() {
        LocalDate hoje = LocalDate.now();
        return repository.findByDataInicioLessThanEqualAndDataFimGreaterThanEqual(hoje, hoje);
    }
}
