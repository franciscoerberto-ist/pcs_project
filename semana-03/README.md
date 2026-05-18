# Semana 3 — Service · Validações · POST · PUT · DELETE

**Período:** 17–23 de maio · **Entrega:** 23/05  
**Objetivo:** Camada Service isolando a lógica de negócio, tratamento global de erros e CRUD completo com validações.

---

## Checklist de Entrega

### Service
- [ ] 1 `@Service` por entidade
- [ ] Lógica de negócio extraída dos Controllers para os Services
- [ ] `findById` lança `ResourceNotFoundException` quando o recurso não existe

### Exceções
- [ ] `ResourceNotFoundException` criada no pacote `exception`
- [ ] `GlobalExceptionHandler` com `@ControllerAdvice`
- [ ] 404 retorna JSON com mensagem (não página HTML do Tomcat)
- [ ] 400 retorna lista de campos com erro de validação

### Controllers
- [ ] POST: cria recurso → retorna **201 Created**
- [ ] PUT: atualiza recurso → retorna **200 OK**
- [ ] DELETE: remove recurso → retorna **204 No Content**
- [ ] `@Valid` presente em todos os endpoints POST e PUT
- [ ] Controllers chamam Service (não Repository diretamente)

### GitHub
- [ ] Commits separados por feature: exceção, service, controller
- [ ] Repositório atualizado com `git push`

---

## Conceitos Fundamentais

### Por que criar uma camada Service?

O Controller tem uma responsabilidade clara: receber a requisição HTTP e devolver uma resposta HTTP. Regras de negócio (verificar se o recurso existe, validar consistência, orquestrar múltiplas entidades) não pertencem ao Controller — pertencem ao Service.

| Sem Service (Semana 2)                  | Com Service (Semana 3)                        |
|-----------------------------------------|-----------------------------------------------|
| Controller chama Repository diretamente | Controller chama Service                      |
| Regras de negócio espalhadas no Controller | Regras de negócio centralizadas no Service |
| Código difícil de testar sem HTTP       | Service testável sem precisar do Tomcat       |
| Lógica duplicada em controllers         | Lógica reutilizável em qualquer lugar         |

### @Transactional

Garante que todas as operações de banco dentro do método sejam atômicas — ou tudo persiste, ou nada persiste.

```java
@Transactional                   // leitura + escrita: flush automático ao final
@Transactional(readOnly = true)  // só leitura: sem flush, mais rápido
```

> Use `readOnly = true` em métodos que apenas consultam. O Hibernate desativa o dirty-checking e libera memória mais cedo.

### Fluxo de uma requisição com Service

```
HTTP Request
    ↓
Controller           ← recebe/devolve HTTP, delega para o Service
    ↓
Service              ← valida regras de negócio, chama Repository
    ↓
Repository           ← acessa o banco
    ↓
Banco de Dados
```

---

## Passo 1 — Criar a Exceção Customizada

Crie a exceção que o Service vai lançar quando um recurso não for encontrado. O `GlobalExceptionHandler` do próximo passo a intercepta e devolve 404.

```java
// src/main/java/com/example/demo/exception/ResourceNotFoundException.java
package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus: quando essa exceção escapar de um controller sem handler, o Spring
// usa NOT_FOUND (404) automaticamente. Com GlobalExceptionHandler, funciona como documentação.
@ResponseStatus(HttpStatus.NOT_FOUND)
// Estende RuntimeException: não precisa de try/catch no chamador — o GlobalExceptionHandler captura
public class ResourceNotFoundException extends RuntimeException {

    // Recebe a mensagem descrevendo o que não foi encontrado
    // ex.: "Categoria não encontrada com id: 5"
    public ResourceNotFoundException(String message) {
        super(message); // repassa para RuntimeException → ex.getMessage() devolve essa string
    }
}
```

---

## Passo 2 — Criar o GlobalExceptionHandler

Sem um handler global, quando o código lança uma exceção o Spring devolve a página HTML de erro do Tomcat. O `@RestControllerAdvice` intercepta qualquer exceção não tratada e devolve JSON padronizado.

### DTO de resposta de erro

```java
// src/main/java/com/example/demo/exception/ApiError.java
package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.List;

// record: classe imutável gerada pelo Java (Java 16+) — gera construtor, getters e toString automaticamente
// Será serializado pelo Jackson como JSON: { "status": 404, "message": "...", "timestamp": "...", "errors": null }
public record ApiError(
    int status,               // código HTTP (ex.: 404, 400, 500)
    String message,           // mensagem principal (ex.: "Categoria não encontrada com id: 1")
    LocalDateTime timestamp,  // momento do erro — preenchido automaticamente
    List<String> errors       // null para erros simples; lista "campo: motivo" para validação (@Valid)
) {
    // Construtor conveniente para erros simples (404, 500): timestamp gerado aqui
    public ApiError(int status, String message) {
        this(status, message, LocalDateTime.now(), null);
    }

    // Construtor conveniente para erros de validação (400): inclui a lista de campos inválidos
    public ApiError(int status, String message, List<String> errors) {
        this(status, message, LocalDateTime.now(), errors);
    }
}
```

### Handler global

```java
// src/main/java/com/example/demo/exception/GlobalExceptionHandler.java
package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

// @RestControllerAdvice = @ControllerAdvice + @ResponseBody
// Intercepta exceções de TODOS os controllers e devolve JSON (não HTML do Tomcat)
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Chamado quando qualquer Service lançar ResourceNotFoundException
    // Fluxo: Controller → Service → lança ResourceNotFoundException → este método → 404 JSON
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiError(404, ex.getMessage())); // ex.getMessage() = mensagem do construtor
    }

    // Chamado quando @Valid detectar campo inválido no RequestBody
    // Fluxo: Controller recebe POST/PUT → @Valid falha → MethodArgumentNotValidException → este método → 400 JSON
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        // Extrai cada campo com erro: "nome: não deve estar em branco", "preco: deve ser maior que 0"
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage()) // "campo: mensagem da anotação"
            .toList();

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiError(400, "Erro de validação", errors));
    }

    // Fallback: captura qualquer exceção não tratada pelos métodos acima
    // Evita que o Spring exponha stack trace ou HTML de erro ao cliente
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiError(500, "Erro interno: " + ex.getMessage()));
    }
}
```

### Exemplos de resposta JSON

**POST /api/v1/categorias** com `{ "nome": "" }` → 400:

```json
{
  "status": 400,
  "message": "Erro de validação",
  "timestamp": "2026-05-17T10:30:00",
  "errors": [
    "nome: não deve estar em branco"
  ]
}
```

**GET /api/v1/categorias/999** → 404:

```json
{
  "status": 404,
  "message": "Categoria não encontrada com id: 999",
  "timestamp": "2026-05-17T10:30:00",
  "errors": null
}
```

---

## Passo 3 — Criar os Services

Um Service por entidade. Anote a classe com `@Transactional` e sobrescreva com `readOnly = true` nos métodos de consulta.

### CategoriaService

```java
// src/main/java/com/example/demo/service/CategoriaService.java
package com.example.demo.service;

import com.example.demo.entity.Categoria;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service             // registra este bean no contexto Spring; pode ser injetado por @RequiredArgsConstructor
@Transactional       // todos os métodos desta classe rodam dentro de uma transação por padrão
@RequiredArgsConstructor // Lombok: gera construtor com todos os campos final → injeção sem @Autowired
public class CategoriaService {

    // Spring injeta o repository aqui via construtor — final garante imutabilidade
    private final CategoriaRepository repository;

    // readOnly = true: Hibernate desativa dirty-checking e flush → só leitura, mais rápido
    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        // CrudRepository.findAll() retorna Iterable — cast para List usado pelo controller
        return (List<Categoria>) repository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id) {
        // findById retorna Optional<Categoria>
        // orElseThrow: se vazio, lança ResourceNotFoundException → GlobalExceptionHandler retorna 404
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Categoria não encontrada com id: " + id));
        // Adapte a mensagem para sua entidade: "Produto não encontrado com id: " + id
    }

    public Categoria salvar(Categoria categoria) {
        // save() faz INSERT quando id == null, UPDATE quando id != null
        return repository.save(categoria);
    }

    public Categoria atualizar(Long id, Categoria dados) {
        // 1. Busca a entidade existente — lança 404 automaticamente se não existir
        Categoria categoria = buscarPorId(id);

        // 2. Copia apenas os campos fornecidos do body para a entidade gerenciada pelo JPA
        categoria.setNome(dados.getNome());
        if (dados.getDescricao() != null) {   // campo opcional: só atualiza se enviado
            categoria.setDescricao(dados.getDescricao());
        }
        // Repita o padrão acima para cada campo da sua entidade

        // 3. Persiste e retorna — o JPA emite UPDATE na transação
        return repository.save(categoria);
    }

    public void deletar(Long id) {
        // Verifica existência antes de deletar: deleteById é silencioso se id inexistente
        buscarPorId(id); // lança 404 se não existir
        repository.deleteById(id);
    }
}
```

### ProdutoService

```java
// src/main/java/com/example/demo/service/ProdutoService.java
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
    // Injeta outro Service para reutilizar sua lógica de validação
    // Regra: Services podem chamar outros Services, nunca Repositories de outras entidades
    private final CategoriaService categoriaService;

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return (List<Produto>) repository.findAll();
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        // Mensagem do 404 deve identificar entidade + id para facilitar debug
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Produto não encontrado com id: " + id));
    }

    public Produto salvar(Produto produto) {
        // Valida FK: garante que a categoria enviada no body realmente existe no banco
        // Sem isso, o banco lançaria ConstraintViolation (500) em vez de 404 amigável
        if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
            categoriaService.buscarPorId(produto.getCategoria().getId()); // lança 404 se não existir
        }
        // Se sua entidade também tem FK para outra entidade, repita o padrão acima
        return repository.save(produto);
    }

    public Produto atualizar(Long id, Produto dados) {
        // 1. Valida existência do produto
        Produto produto = buscarPorId(id);

        // 2. Atualiza apenas campos não-nulos (permite PATCH parcial via PUT)
        if (dados.getNome() != null)      produto.setNome(dados.getNome());
        if (dados.getPreco() != null)     produto.setPreco(dados.getPreco());
        if (dados.getDescricao() != null) produto.setDescricao(dados.getDescricao());

        // 3. Troca de categoria: valida que a nova categoria existe antes de substituir
        if (dados.getCategoria() != null && dados.getCategoria().getId() != null) {
            categoriaService.buscarPorId(dados.getCategoria().getId()); // lança 404 se inválida
            produto.setCategoria(dados.getCategoria());
        }
        return repository.save(produto);
    }

    public void deletar(Long id) {
        buscarPorId(id); // garante 404 antes de chamar deleteById
        repository.deleteById(id);
    }

    // Métodos de busca específicos delegam direto ao Repository
    @Transactional(readOnly = true)
    public List<Produto> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorCategoria(Long categoriaId) {
        return repository.findByCategoriaId(categoriaId);
    }
}
```

### AvaliacaoService

```java
// src/main/java/com/example/demo/service/AvaliacaoService.java
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
    // Injeta ProdutoService para validar que o produto existe antes de salvar avaliação
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
        // Valida FK: garante que o produto existe antes de associar a avaliação
        // Se produto.getId() for inválido, retorna 404 antes de tentar o INSERT
        produtoService.buscarPorId(avaliacao.getProduto().getId());
        return repository.save(avaliacao);
        // Avaliacao não tem método atualizar — avaliações normalmente não são editadas
    }

    public void deletar(Long id) {
        buscarPorId(id); // 404 se não existir antes de tentar deletar
        repository.deleteById(id);
    }

    // Endpoint GET /api/v1/avaliacoes/produto/{produtoId} usa este método
    @Transactional(readOnly = true)
    public List<Avaliacao> buscarPorProduto(Long produtoId) {
        return repository.findByProdutoId(produtoId);
    }
}
```

### PromocaoService

```java
// src/main/java/com/example/demo/service/PromocaoService.java
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
        // Sem FK para validar — apenas persiste
        // Se sua entidade tiver regra de negócio (ex.: dataInicio < dataFim), valide aqui
        return repository.save(promocao);
    }

    public Promocao atualizar(Long id, Promocao dados) {
        // Padrão: busca → copia campos não-nulos → salva
        Promocao promocao = buscarPorId(id);
        if (dados.getNome() != null)             promocao.setNome(dados.getNome());
        if (dados.getDescontoPercent() != null)  promocao.setDescontoPercent(dados.getDescontoPercent());
        if (dados.getDataInicio() != null)       promocao.setDataInicio(dados.getDataInicio());
        if (dados.getDataFim() != null)          promocao.setDataFim(dados.getDataFim());
        // Adicione os demais campos da sua entidade seguindo o mesmo padrão
        return repository.save(promocao);
    }

    public void deletar(Long id) {
        buscarPorId(id); // garante 404 antes de deletar
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Promocao> listarVigentes() {
        LocalDate hoje = LocalDate.now(); // data atual do servidor
        // Query method: dataInicio <= hoje AND dataFim >= hoje
        // O mesmo valor (hoje) é passado duas vezes pois o Spring Data não reutiliza parâmetros
        return repository.findByDataInicioLessThanEqualAndDataFimGreaterThanEqual(hoje, hoje);
    }
}
```

---

## Passo 4 — Atualizar os Controllers

Substitua a injeção do Repository pelo Service. Adicione os endpoints de escrita com `@Valid`.

> **Regra:** `@Valid` no parâmetro do Controller ativa as anotações de validação da entidade (`@NotBlank`, `@Min`, etc.). Sem `@Valid` elas são completamente ignoradas.

### CategoriaController (completo)

```java
// src/main/java/com/example/demo/controller/CategoriaController.java
package com.example.demo.controller;

import com.example.demo.entity.Categoria;
import com.example.demo.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                          // @Controller + @ResponseBody: todos os métodos retornam JSON
@RequestMapping("/api/v1/categorias")    // prefixo de todos os endpoints desta classe
@RequiredArgsConstructor                 // Lombok: construtor com campo service (sem @Autowired)
public class CategoriaController {

    // Semana 3: injeta Service, não Repository diretamente
    private final CategoriaService service;

    // GET /api/v1/categorias → 200 OK com lista (pode ser vazia [])
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // GET /api/v1/categorias/1 → 200 OK, ou 404 se não existir (lançado no Service)
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id)); // 404 tratado pelo GlobalExceptionHandler
    }

    // POST /api/v1/categorias → 201 Created com o objeto salvo (id preenchido)
    // @Valid: ativa as anotações @NotBlank/@Size da entidade — sem ele são ignoradas
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)  // 201 indica criação de recurso (mais correto que 200)
    public Categoria criar(@Valid @RequestBody Categoria categoria) {
        return service.salvar(categoria);
    }

    // PUT /api/v1/categorias/1 → 200 OK com objeto atualizado, ou 404 se não existir
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Categoria categoria) {  // @Valid valida o body do PUT também
        return ResponseEntity.ok(service.atualizar(id, categoria));
    }

    // DELETE /api/v1/categorias/1 → 204 No Content (sem body na resposta), ou 404
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 = sucesso sem corpo — padrão REST para DELETE
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
```

### ProdutoController (completo)

```java
// src/main/java/com/example/demo/controller/ProdutoController.java
package com.example.demo.controller;

import com.example.demo.entity.Produto;
import com.example.demo.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    // GET /api/v1/produtos
    @GetMapping
    public ResponseEntity<List<Produto>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // GET /api/v1/produtos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // GET /api/v1/produtos/busca?nome=tenis → busca case-insensitive por trecho do nome
    // @RequestParam: lê o parâmetro de query string (?nome=...)
    @GetMapping("/busca")
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    // GET /api/v1/produtos/categoria/3 → todos os produtos com categoria_id = 3
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Produto>> porCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(service.buscarPorCategoria(categoriaId));
    }

    // POST /api/v1/produtos → 201 Created
    // Body esperado: { "nome": "...", "preco": 9.99, "categoria": { "id": 1 } }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto criar(@Valid @RequestBody Produto produto) {
        return service.salvar(produto); // Service valida se a categoria existe
    }

    // PUT /api/v1/produtos/1 → 200 OK com produto atualizado
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Produto produto) {
        return ResponseEntity.ok(service.atualizar(id, produto));
    }

    // DELETE /api/v1/produtos/1 → 204 No Content
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
```

### AvaliacaoController (completo)

```java
// src/main/java/com/example/demo/controller/AvaliacaoController.java
package com.example.demo.controller;

import com.example.demo.entity.Avaliacao;
import com.example.demo.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService service;

    // GET /api/v1/avaliacoes
    @GetMapping
    public ResponseEntity<List<Avaliacao>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // GET /api/v1/avaliacoes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // GET /api/v1/avaliacoes/produto/5 → todas as avaliações do produto id=5
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<Avaliacao>> porProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(service.buscarPorProduto(produtoId));
    }

    // POST /api/v1/avaliacoes → 201 Created
    // Body: { "nota": 5, "comentario": "...", "autor": "Maria", "produto": { "id": 1 } }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Avaliacao criar(@Valid @RequestBody Avaliacao avaliacao) {
        return service.salvar(avaliacao); // Service valida que produto.id existe
    }

    // Sem PUT — avaliações não são editadas (apenas criadas e deletadas)
    // DELETE /api/v1/avaliacoes/{id} → 204 No Content
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
```

### PromocaoController (completo)

```java
// src/main/java/com/example/demo/controller/PromocaoController.java
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

    // GET /api/v1/promocoes
    @GetMapping
    public ResponseEntity<List<Promocao>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // GET /api/v1/promocoes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Promocao> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // GET /api/v1/promocoes/vigentes → promoções ativas hoje (dataInicio <= hoje <= dataFim)
    // Importante: este endpoint deve ficar ANTES de /{id} — senão o Spring tentaria tratar
    // "vigentes" como um Long e lançaria erro de conversão de tipo
    @GetMapping("/vigentes")
    public ResponseEntity<List<Promocao>> vigentes() {
        return ResponseEntity.ok(service.listarVigentes());
    }

    // POST /api/v1/promocoes → 201 Created
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Promocao criar(@Valid @RequestBody Promocao promocao) {
        return service.salvar(promocao);
    }

    // PUT /api/v1/promocoes/1 → 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<Promocao> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Promocao promocao) {
        return ResponseEntity.ok(service.atualizar(id, promocao));
    }

    // DELETE /api/v1/promocoes/1 → 204 No Content
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
```

---

## Passo 5 — Testar no Swagger

Acesse `http://localhost:8080/swagger-ui.html` e siga a ordem abaixo — há dependências entre entidades.

### Sequência recomendada

**1. Criar uma Categoria (POST)**
```
POST /api/v1/categorias
Body:
{
  "nome": "Eletrônicos",
  "descricao": "Smartphones, tablets e acessórios"
}
→ Esperado: 201 Created com o objeto criado (id preenchido)
```

**2. Criar um Produto vinculado à Categoria (POST)**
```
POST /api/v1/produtos
Body:
{
  "nome": "Smartphone Pro",
  "preco": 2499.90,
  "descricao": "Tela 6.7 polegadas",
  "categoria": { "id": 1 }
}
→ Esperado: 201 Created
```

**3. Criar uma Avaliação para o Produto (POST)**
```
POST /api/v1/avaliacoes
Body:
{
  "nota": 5,
  "comentario": "Excelente produto!",
  "autor": "Maria Silva",
  "produto": { "id": 1 }
}
→ Esperado: 201 Created
```

**4. Criar uma Promoção (POST)**
```
POST /api/v1/promocoes
Body:
{
  "nome": "Promoção de Lançamento",
  "descontoPercent": 15.00,
  "dataInicio": "2026-05-17",
  "dataFim": "2026-05-31"
}
→ Esperado: 201 Created
```

**5. Atualizar a Categoria (PUT)**
```
PUT /api/v1/categorias/1
Body:
{
  "nome": "Eletrônicos e Gadgets",
  "descricao": "Smartphones, tablets, smartwatches e acessórios"
}
→ Esperado: 200 OK com dados atualizados
```

**6. Testar validação — dados inválidos (POST)**
```
POST /api/v1/categorias
Body: { "nome": "" }
→ Esperado: 400 Bad Request
{
  "status": 400,
  "message": "Erro de validação",
  "errors": ["nome: não deve estar em branco"]
}
```

**7. Testar 404 — recurso inexistente (GET)**
```
GET /api/v1/categorias/999
→ Esperado: 404 Not Found
{
  "status": 404,
  "message": "Categoria não encontrada com id: 999"
}
```

**8. Deletar (DELETE)**
```
DELETE /api/v1/avaliacoes/1
→ Esperado: 204 No Content (sem body)
```

### Tabela de status HTTP esperados

| Operação               | Endpoint                         | Status Esperado   |
|------------------------|----------------------------------|-------------------|
| Listar todos           | `GET /api/v1/categorias`         | 200 OK            |
| Buscar por ID          | `GET /api/v1/categorias/1`       | 200 OK            |
| ID inexistente         | `GET /api/v1/categorias/999`     | 404 Not Found     |
| Criar (válido)         | `POST /api/v1/categorias`        | 201 Created       |
| Criar (inválido)       | `POST /api/v1/categorias`        | 400 Bad Request   |
| Atualizar              | `PUT /api/v1/categorias/1`       | 200 OK            |
| Deletar                | `DELETE /api/v1/categorias/1`    | 204 No Content    |

---

## Passo 6 — Commit

Faça um commit para cada parte enquanto implementa — não espere ter tudo pronto:

```bash
git add .
git commit -m "feat: add ResourceNotFoundException and GlobalExceptionHandler"

git add .
git commit -m "feat: add CategoriaService with CRUD"

git add .
git commit -m "feat: add ProdutoService with CRUD"

git add .
git commit -m "feat: add AvaliacaoService with CRUD"

git add .
git commit -m "feat: add PromocaoService with CRUD"

git add .
git commit -m "feat: add POST, PUT, DELETE to all controllers"

git push
```

---

## Estrutura Final Esperada

```
src/main/java/com/example/demo/
├── controller/
│   ├── CategoriaController.java    ← GET + POST + PUT + DELETE
│   ├── ProdutoController.java      ← GET + POST + PUT + DELETE
│   ├── AvaliacaoController.java    ← GET + POST + DELETE
│   └── PromocaoController.java     ← GET + POST + PUT + DELETE
├── service/                        ← NOVO: lógica de negócio
│   ├── CategoriaService.java
│   ├── ProdutoService.java
│   ├── AvaliacaoService.java
│   └── PromocaoService.java
├── repository/
│   ├── CategoriaRepository.java
│   ├── ProdutoRepository.java
│   ├── AvaliacaoRepository.java
│   └── PromocaoRepository.java
├── entity/
│   ├── Categoria.java
│   ├── Produto.java
│   ├── Avaliacao.java
│   └── Promocao.java
└── exception/                      ← NOVO: erros customizados
    ├── ResourceNotFoundException.java
    ├── ApiError.java
    └── GlobalExceptionHandler.java
```

---

## Dicas

- **Nunca injete Repository direto no Controller** — mistura responsabilidades e impede testes unitários do Service.
- **`@Valid` é obrigatório no Controller** — sem ele as anotações da entidade (`@NotBlank`, `@Min`, `@Size`) são completamente ignoradas.
- **`@Transactional` fica no Service, não no Controller** — o Controller não sabe do banco; essa é responsabilidade do Service.
- **Retorne 201 no POST, não 200** — indica que um novo recurso foi criado, não apenas processado.
- **Retorne 204 no DELETE** — o 200 implica body na resposta; DELETE bem-sucedido não tem nada para devolver.
- **O `GlobalExceptionHandler` trata tudo** — não coloque `try/catch` nos Controllers ou Services para os casos cobertos por ele.
- **Valide no Service que entidades relacionadas existem** — antes de salvar um Produto, confirme que a Categoria referenciada existe; chame `categoriaService.buscarPorId()`, que já lança 404 se não existir.
- **`buscarPorId` antes do `deleteById`** — garante 404 consistente se o recurso não existir, em vez do comportamento silencioso do `deleteById` em alguns cenários.
