package com.example.demo.controller;

import com.example.demo.entity.Promocao;
import com.example.demo.service.PromocaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promocoes")
@RequiredArgsConstructor
public class PromocaoController {

    private final PromocaoService service;

    @GetMapping
    public ResponseEntity<List<Promocao>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promocao> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/vigentes")
    public ResponseEntity<List<Promocao>> vigentes() {
        return ResponseEntity.ok(service.listarVigentes());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Promocao criar(@Valid @RequestBody Promocao promocao) {
        return service.salvar(promocao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promocao> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Promocao promocao) {
        return ResponseEntity.ok(service.atualizar(id, promocao));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
