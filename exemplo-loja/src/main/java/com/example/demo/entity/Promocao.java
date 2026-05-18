package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "promocoes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "produtos")
@EqualsAndHashCode(exclude = "produtos")
public class Promocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nome;

    @DecimalMin("0.01") @DecimalMax("99.99")
    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal descontoPercent;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @JsonIgnore
    @ManyToMany(mappedBy = "promocoes")
    private Set<Produto> produtos = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;
}
