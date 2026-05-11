# Semana 2 — Model + Repository + CRUD Básico

**Período:** 10–16 de maio · **Entrega:** 16/05  
**Objetivo:** 4 entidades JPA com relacionamentos, repositories e endpoints GET funcionando no Swagger.

---

## Checklist de Entrega

### Entidades
- [ ] Mínimo 4 entidades JPA criadas com `@Entity`
- [ ] Cada entidade tem `@Id` e `@GeneratedValue`
- [ ] Colunas com `@Column` e constraints corretas

### Relacionamentos
- [ ] Pelo menos 1 `@ManyToOne` implementado
- [ ] Pelo menos 1 `@OneToMany` ou `@ManyToMany`
- [ ] Tabelas aparecem no H2 Console corretamente

### Repositories
- [ ] 1 repository por entidade (`CrudRepository`)
- [ ] 1+ Query Method derivado por repository

### Controllers
- [ ] GETs (listar / por ID) funcionando no Swagger
- [ ] Retorno 404 correto para ID inexistente
- [ ] Commits claros: `feat: add Produto entity`
- [ ] Repositório GitHub atualizado

---

## Conceitos Fundamentais

### ORM (Object-Relational Mapping)

Traduz automaticamente objetos Java em linhas de tabelas SQL — e vice-versa.

| Elemento Java                          | Equivalente no Banco           |
|----------------------------------------|--------------------------------|
| `@Entity Produto`                      | `TABLE produtos`               |
| `@Id Long id`                          | `id BIGINT PRIMARY KEY`        |
| `String nome`                          | `nome VARCHAR(150)`            |
| `new Produto(...)` / `save()`          | `INSERT INTO produtos...`      |

**Por que usar ORM?** Sem ORM cada operação exige SQL manual: verboso, propenso a erros e acoplado ao banco.

### JPA vs Hibernate

| JPA                                | Hibernate                              |
|------------------------------------|----------------------------------------|
| É a **especificação**              | É a **implementação**                  |
| Define `@Entity`, `@Id`, `@Column` | Implementa a spec + extras             |
| `jakarta.persistence.*`            | Lazy loading, Cache L2, HQL            |
| Não tem implementação própria      | Usado automaticamente pelo Spring Boot |


---

## Passo 1 — Configurar o Ambiente

O `application.yml` do projeto exemplo-loja:

```yaml
server:
  port: 8080

spring:
  application:
    name: exemplo-loja
  datasource:
    url: jdbc:h2:mem:lojademo
    username: sa
    password:
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true

springdoc:
  swagger-ui:
    path: /swagger-ui.html
```

Suba a aplicação e acesse `http://localhost:8080/h2-console`.  
JDBC URL: `jdbc:h2:mem:lojademo` → **Connect** → deve abrir sem erro.

---

## Passo 2 — Criar as Entidades JPA

### Anotações essenciais

| Anotação                                  | O que faz                                   |
|-------------------------------------------|---------------------------------------------|
| `@Entity`                                 | Marca a classe como tabela JPA              |
| `@Table(name="...")`                      | Define o nome da tabela no banco            |
| `@Id`                                     | Chave primária (PK)                         |
| `@GeneratedValue(strategy=IDENTITY)`      | Auto-incremento gerenciado pelo banco       |
| `@Column(nullable=false, length=150)`     | Constraints da coluna                       |
| `@NotBlank` / `@Size` / `@Min` / `@Max`  | Validação antes de persistir                |
| `@CreationTimestamp`                      | Preenchido automaticamente no INSERT        |
| `@UpdateTimestamp`                        | Atualizado automaticamente em cada UPDATE   |
| `@Getter @Setter` (Lombok)                | Gera getters e setters                      |
| `@NoArgsConstructor @AllArgsConstructor`  | Construtores sem e com todos os campos      |
| `@ToString(exclude=...)` (Lombok)         | Evita StackOverflow em relacionamentos      |
| `@EqualsAndHashCode(exclude=...)` (Lombok)| Evita loop no equals com relacionamentos    |

> **Por que não usar `@Data`?** Em entidades com relacionamentos bidirecionais, `@Data` gera `toString` e `equals` que navegam pelos dois lados e causam StackOverflow. Use as anotações separadas com `exclude`.

### Ordem de criação: comece pela entidade sem relacionamentos

**Regra:** crie primeiro a entidade que outros vão referenciar — assim o JPA consegue criar as FKs na ordem certa.

Ordem do projeto exemplo-loja:
1. `Categoria` — sem FK para ninguém
2. `Produto` — FK para `Categoria`
3. `Avaliacao` — FK para `Produto`
4. `Promocao` — ManyToMany com `Produto`

---

### Entidade 1 — Categoria (sem relacionamentos)

```java
// src/main/java/com/example/demo/entity/Categoria.java
package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "produtos")           // evita StackOverflow no log/debug
@EqualsAndHashCode(exclude = "produtos")  // evita loop no equals
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK auto-incrementada pelo banco
    private Long id;

    @NotBlank                           // não aceita null, vazio ou só espaços
    @Size(max = 100)                    // validação de negócio: nome curto e legível
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 300)
    @Column(length = 300)
    private String descricao;           // campo opcional

    // Preenchido automaticamente pelo Hibernate na primeira persistência; nunca atualizado.
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;

    // @JsonIgnore: evita que o Jackson serialize Categoria → produtos → categoria → ...
    // Use GET /api/v1/produtos/categoria/{id} para buscar os produtos de uma categoria.
    @JsonIgnore
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Produto> produtos = new ArrayList<>();
}
```

---

### Entidade 2 — Produto (com relacionamentos)

```java
// src/main/java/com/example/demo/entity/Produto.java
package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "produtos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"avaliacoes", "promocoes"})       // evita StackOverflow ao logar
@EqualsAndHashCode(exclude = {"avaliacoes", "promocoes"})
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank                            // nome obrigatório
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nome;

    @DecimalMin("0.01")                  // preço deve ser positivo
    @Column(precision = 10, scale = 2)   // até 99_999_999.99
    private BigDecimal preco;

    @Size(max = 500)
    @Column(length = 500)
    private String descricao;            // campo opcional

    // MUITOS Produtos → UMA Categoria  (lado DONO: cria coluna categoria_id)
    // Categoria aparece inline no JSON — Categoria.produtos está @JsonIgnore, sem loop.
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // @JsonIgnore: evita loop de serialização Produto → Avaliacao → Produto → ...
    // Use GET /api/v1/avaliacoes/produto/{id} para buscar avaliações de um produto.
    @JsonIgnore
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    // @JsonIgnore: evita loop de serialização Produto → Promocao → produtos → Produto → ...
    // Use GET /api/v1/promocoes para buscar promoções; a tabela de junção é "produto_promocao".
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "produto_promocao",
        joinColumns = @JoinColumn(name = "produto_id"),
        inverseJoinColumns = @JoinColumn(name = "promocao_id")
    )
    private Set<Promocao> promocoes = new HashSet<>();

    // Preenchido na criação e nunca mais alterado.
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;

    // Atualizado automaticamente pelo Hibernate a cada save().
    @UpdateTimestamp
    private LocalDateTime atualizadoEm;
}
```

---

### Entidade 3 — Avaliacao

```java
// src/main/java/com/example/demo/entity/Avaliacao.java
package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacoes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "produto")       // evita StackOverflow ao logar
@EqualsAndHashCode(exclude = "produto")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(1) @Max(5)                      // nota de 1 a 5 estrelas
    @Column(nullable = false)
    private Integer nota;

    @Size(max = 500)
    @Column(length = 500)
    private String comentario;           // texto livre, opcional

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String autor;                // nome de quem avaliou

    // Produto aparece inline no JSON — Produto.avaliacoes está @JsonIgnore, sem loop.
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)  // FK obrigatória
    private Produto produto;

    // Sem campo "atualizadoEm" pois avaliações não são editadas.
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;
}
```

---

### Entidade 4 — Promocao

```java
// src/main/java/com/example/demo/entity/Promocao.java
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

    // Percentual de desconto; ex.: 15.50 = 15,50% de desconto.
    @DecimalMin("0.01") @DecimalMax("99.99")
    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal descontoPercent;

    @Column(nullable = false)
    private LocalDate dataInicio;        // primeiro dia em que a promoção é válida

    @Column(nullable = false)
    private LocalDate dataFim;           // último dia em que a promoção é válida

    // @JsonIgnore: evita loop Promocao → produtos → promocoes → Promocao → ...
    // mappedBy indica que Produto é o dono e controla a tabela de junção.
    @JsonIgnore
    @ManyToMany(mappedBy = "promocoes")
    private Set<Produto> produtos = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;
}
```

---

## Passo 3 — Os 3 Tipos de Relacionamento JPA

### @ManyToOne — Muitos → Um (lado DONO)

Cria a FK na tabela da entidade que tem a anotação.

```java
// Em Produto.java — cria coluna categoria_id na tabela produtos
@ManyToOne
@JoinColumn(name = "categoria_id")
private Categoria categoria;
```

```java
// Em Avaliacao.java — cria coluna produto_id na tabela avaliacoes; FK obrigatória
@ManyToOne
@JoinColumn(name = "produto_id", nullable = false)
private Produto produto;
```

### @OneToMany — Um → Muitos (lado INVERSO)

`mappedBy` referencia o campo `@ManyToOne` da entidade filha — não cria coluna extra.

```java
// Em Categoria.java — lado inverso; "categoria" aponta para Produto.categoria
@JsonIgnore
@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
private List<Produto> produtos = new ArrayList<>();

// Em Produto.java — lado inverso; "produto" aponta para Avaliacao.produto
@JsonIgnore
@OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
private List<Avaliacao> avaliacoes = new ArrayList<>();
```

> `@JsonIgnore` é obrigatório no lado `@OneToMany` quando o outro lado serializa o pai — sem ele o Jackson entra em loop infinito.

### @ManyToMany — Tabela de junção automática

O lado **DONO** define a tabela de junção com `@JoinTable`. O lado **INVERSO** usa `mappedBy`.

```java
// Em Produto.java — lado DONO: cria tabela produto_promocao
@JsonIgnore
@ManyToMany
@JoinTable(
    name = "produto_promocao",
    joinColumns = @JoinColumn(name = "produto_id"),
    inverseJoinColumns = @JoinColumn(name = "promocao_id")
)
private Set<Promocao> promocoes = new HashSet<>();

// Em Promocao.java — lado INVERSO
@JsonIgnore
@ManyToMany(mappedBy = "promocoes")
private Set<Produto> produtos = new HashSet<>();
```

### Resumo: como o banco fica

```
categorias          produtos              avaliacoes
──────────          ─────────             ──────────
id (PK)             id (PK)               id (PK)
nome                nome                  nota
descricao           preco                 comentario
criado_em           descricao             autor
                    categoria_id (FK)     produto_id (FK)
                    criado_em             criado_em
                    atualizado_em

promocoes           produto_promocao (junção)
─────────           ────────────────────────
id (PK)             produto_id  (FK → produtos)
nome                promocao_id (FK → promocoes)
desconto_percent
data_inicio
data_fim
criado_em
```

---

## Passo 4 — Criar os Repositories

Um repository por entidade. Basta criar uma interface que estende `CrudRepository` — o Spring implementa automaticamente `save`, `findById`, `findAll`, `delete`, etc.

### CategoriaRepository

```java
// src/main/java/com/example/demo/repository/CategoriaRepository.java
package com.example.demo.repository;

import com.example.demo.entity.Categoria;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends CrudRepository<Categoria, Long> {

    // SQL gerado: WHERE LOWER(nome) LIKE LOWER('%nome%')
    List<Categoria> findByNomeContainingIgnoreCase(String nome);

    // Retorna Optional para evitar NPE quando não encontrado
    Optional<Categoria> findByNomeIgnoreCase(String nome);

    // Útil para validar duplicatas antes de salvar uma nova categoria
    boolean existsByNomeIgnoreCase(String nome);
}
```

### ProdutoRepository

```java
// src/main/java/com/example/demo/repository/ProdutoRepository.java
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

    // SQL gerado: WHERE LOWER(nome) LIKE LOWER('%nome%')
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    // SQL gerado: WHERE categoria_id = ?
    List<Produto> findByCategoriaId(Long categoriaId);

    // SQL gerado: WHERE preco BETWEEN ? AND ?
    List<Produto> findByPrecoBetween(BigDecimal min, BigDecimal max);

    // Útil para validar duplicatas antes de salvar um novo produto
    boolean existsByNomeIgnoreCase(String nome);

    // JPQL explícita: ORDER BY + condição — mais legível que derived query
    @Query("""
        SELECT p FROM Produto p
        WHERE p.preco <= :limite
        ORDER BY p.preco DESC
        """)
    List<Produto> abaixoDoLimite(@Param("limite") BigDecimal limite);
}
```

### AvaliacaoRepository

```java
// src/main/java/com/example/demo/repository/AvaliacaoRepository.java
package com.example.demo.repository;

import com.example.demo.entity.Avaliacao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends CrudRepository<Avaliacao, Long> {

    // Retorna todas as avaliações de um produto específico
    List<Avaliacao> findByProdutoId(Long produtoId);

    // Filtra avaliações por nota exata (ex.: todas com nota = 5)
    List<Avaliacao> findByNota(Integer nota);

    // Filtra avaliações com nota maior ou igual ao valor (ex.: nota >= 4)
    List<Avaliacao> findByNotaGreaterThanEqual(Integer nota);
}
```

### PromocaoRepository

```java
// src/main/java/com/example/demo/repository/PromocaoRepository.java
package com.example.demo.repository;

import com.example.demo.entity.Promocao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromocaoRepository extends CrudRepository<Promocao, Long> {

    List<Promocao> findByNomeContainingIgnoreCase(String nome);

    // Retorna promoções vigentes na data informada: dataInicio <= hoje E dataFim >= hoje
    // O parâmetro é passado duas vezes pois o Spring Data não reutiliza o mesmo valor
    // em condições AND com campos diferentes.
    List<Promocao> findByDataInicioLessThanEqualAndDataFimGreaterThanEqual(
        LocalDate hoje, LocalDate hojeRepetido
    );
}
```

### Palavras-chave para Query Methods

| Palavra-chave            | Exemplo                              |
|--------------------------|--------------------------------------|
| `findBy` + Atributo      | `findByNome`                         |
| `Containing`             | `findByNomeContaining`               |
| `IgnoreCase`             | `findByNomeIgnoreCase`               |
| `Between`                | `findByPrecoBetween`                 |
| `LessThan / GreaterThan` | `findByPrecoLessThan`                |
| `GreaterThanEqual`       | `findByNotaGreaterThanEqual`         |
| `LessThanEqual`          | `findByDataInicioLessThanEqual`      |
| `OrderBy`                | `findByNomeOrderByPrecoAsc`          |
| `And / Or`               | `findByDataInicioLessThanEqualAndDataFimGreaterThanEqual` |
| `existsBy / countBy`     | `existsByNomeIgnoreCase`             |

---

## Passo 5 — Criar os Controllers (apenas GET)

Nesta semana: **só GETs**. POST/PUT/DELETE chegam na Semana 3 junto com a camada Service.

> Controller fala direto com Repository nesta semana. Na Semana 3 você vai extrair a lógica para Services.

### CategoriaController

```java
// src/main/java/com/example/demo/controller/CategoriaController.java
package com.example.demo.controller;

import com.example.demo.entity.Categoria;
import com.example.demo.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor  // injeta via construtor — sem @Autowired
public class CategoriaController {

    private final CategoriaRepository repository;

    // GET /api/v1/categorias — HTTP 200 com lista completa
    @GetMapping
    public ResponseEntity<Iterable<Categoria>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    // GET /api/v1/categorias/{id} — HTTP 200 se encontrado, 404 caso contrário
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscar(@PathVariable long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
```

### ProdutoController

```java
// src/main/java/com/example/demo/controller/ProdutoController.java
package com.example.demo.controller;

import com.example.demo.entity.Produto;
import com.example.demo.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoRepository repository;

    // GET /api/v1/produtos
    @GetMapping
    public ResponseEntity<Iterable<Produto>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    // GET /api/v1/produtos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/v1/produtos/busca?nome=tenis — busca case-insensitive por trecho do nome
    @GetMapping("/busca")
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(repository.findByNomeContainingIgnoreCase(nome));
    }

    // GET /api/v1/produtos/categoria/3 — todos os produtos de uma categoria
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Produto>> porCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(repository.findByCategoriaId(categoriaId));
    }
}
```

### AvaliacaoController

```java
// src/main/java/com/example/demo/controller/AvaliacaoController.java
package com.example.demo.controller;

import com.example.demo.entity.Avaliacao;
import com.example.demo.repository.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoRepository repository;

    // GET /api/v1/avaliacoes
    @GetMapping
    public ResponseEntity<Iterable<Avaliacao>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    // GET /api/v1/avaliacoes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscar(@PathVariable long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/v1/avaliacoes/produto/5 — todas as avaliações do produto com id=5
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<Avaliacao>> porProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(repository.findByProdutoId(produtoId));
    }
}
```

### PromocaoController

```java
// src/main/java/com/example/demo/controller/PromocaoController.java
package com.example.demo.controller;

import com.example.demo.entity.Promocao;
import com.example.demo.repository.PromocaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/promocoes")
@RequiredArgsConstructor
public class PromocaoController {

    private final PromocaoRepository repository;

    // GET /api/v1/promocoes
    @GetMapping
    public ResponseEntity<Iterable<Promocao>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    // GET /api/v1/promocoes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Promocao> buscar(@PathVariable long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/v1/promocoes/vigentes — promoções ativas hoje (dataInicio <= hoje <= dataFim)
    @GetMapping("/vigentes")
    public ResponseEntity<List<Promocao>> vigentes() {
        LocalDate hoje = LocalDate.now();
        return ResponseEntity.ok(
            repository.findByDataInicioLessThanEqualAndDataFimGreaterThanEqual(hoje, hoje)
        );
    }
}
```

---

## Passo 6 — Testar no Swagger e H2 Console

### H2 Console

```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:lojademo
User: sa  |  Password: (vazio)
```

Execute:
```sql
SHOW TABLES;                   -- deve listar: categorias, produtos, avaliacoes, promocoes, produto_promocao
SELECT * FROM CATEGORIAS;
SELECT * FROM PRODUTOS;        -- coluna categoria_id deve existir
SELECT * FROM PRODUTO_PROMOCAO;-- tabela de junção gerada pelo @ManyToMany
```

### Swagger UI

```
http://localhost:8080/swagger-ui.html
```

Teste obrigatório:

| Endpoint                              | Resultado esperado         |
|---------------------------------------|----------------------------|
| `GET /api/v1/categorias`              | `[]` — status 200          |
| `GET /api/v1/categorias/999`          | status **404 Not Found**   |
| `GET /api/v1/produtos`                | `[]` — status 200          |
| `GET /api/v1/produtos/999`            | status **404 Not Found**   |
| `GET /api/v1/produtos/busca?nome=abc` | `[]` — status 200          |
| `GET /api/v1/promocoes/vigentes`      | `[]` — status 200          |
| `GET /api/v1/avaliacoes/produto/1`    | `[]` — status 200          |

---

## Passo 7 — Commit

Faça um commit separado para cada entidade enquanto desenvolve — não espere ter tudo pronto:

```bash
git add .
git commit -m "feat: add Categoria entity"

git add .
git commit -m "feat: add Produto entity with ManyToOne Categoria"

git add .
git commit -m "feat: add Avaliacao entity with ManyToOne Produto"

git add .
git commit -m "feat: add Promocao entity with ManyToMany Produto"

git add .
git commit -m "feat: add repositories with query methods"

git add .
git commit -m "feat: add GET controllers for all entities"

git push
```

---

## Dicas

- **Comece pela entidade mais simples** (sem relacionamentos): `Categoria`, `Tipo`, `Status`.
- **Suba a app após cada entidade** — não espere criar as 4 de uma vez para testar.
- **`show-sql: true`** é seu melhor amigo — veja cada `INSERT/SELECT` gerado pelo Hibernate no console.
- **`mappedBy`**: sempre no lado `@OneToMany`, apontando para o campo `@ManyToOne` da outra classe.
- **Não coloque `@ManyToMany` nos dois lados sem `mappedBy`** — causará tabela de junção duplicada.
- **`@JsonIgnore`** nos lados `@OneToMany` e `@ManyToMany` inverso — sem ele o Jackson entra em loop.
- **`@ToString(exclude=...)` e `@EqualsAndHashCode(exclude=...)`** — sempre que houver relacionamento bidirecional; `@Data` não serve para entidades JPA.
