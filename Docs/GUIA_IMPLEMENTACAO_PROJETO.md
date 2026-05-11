# 🛠️ Guia de Implementação - Projeto Final

## Índice
1. [Setup Inicial](#setup-inicial)
2. [Criar Projeto Maven](#criar-projeto-maven)
3. [Dependências necessárias](#dependências-necessárias)
4. [Implementar Entidades](#implementar-entidades)
5. [Criar Repositories](#criar-repositories)
6. [Implementar Services](#implementar-services)
7. [Criar Controllers](#criar-controllers)
8. [Configurar Swagger](#configurar-swagger)
9. [Escrever Testes](#escrever-testes)
10. [Fazer Deploy no GitHub](#fazer-deploy-no-github)

---

## Setup Inicial

### 1. Clonar ou Criar Projeto

**Opção A: Usando Spring Boot CLI**
```bash
# Instalar Spring Boot CLI (se não tiver)
# https://spring.io/projects/spring-boot

# Criar projeto
spring boot new --from https://start.spring.io gametracker \
  --dependencies web,data-jpa,h2,validation,test
```

**Opção B: Usar spring.io**
1. Ir para https://start.spring.io
2. Preencher:
   - Project: Maven Project
   - Language: Java
   - Spring Boot: 3.2.x
   - Packaging: Jar
   - Java: 17
3. Dependencies:
   - Spring Web
   - Spring Data JPA
   - H2 Database
   - Validation
   - Lombok (opcional)
4. Generate → Descompactar

**Opção C: Criar localmente**
```bash
mkdir gametracker
cd gametracker
git init
```

---

## Criar Projeto Maven

Se escolheu criar localmente, crie `pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>gametracker</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>GameTracker</name>
    <description>Aplicação REST para gerenciar videogames</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>

    <properties>
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- Spring Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Data JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- H2 Database -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Lombok (opcional) -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Swagger -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.1.0</version>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Mockito -->
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- AssertJ -->
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-core</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

---

## Dependências Necessárias

Execute para baixar:
```bash
./mvnw clean install
```

---

## Implementar Entidades

### Passo 1: Criar pasta entity

```bash
mkdir -p src/main/java/com/example/gametracker/entity
```

### Passo 2: Criar Game.java

```java
package com.example.gametracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 200)
    @Column(nullable = false, length = 200)
    private String title;

    @Min(value = 1950)
    @Max(value = 2025)
    private int releaseYear;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "game_platform",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    private Set<Platform> platforms = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

### Passo 3: Criar Genre.java, Platform.java, Review.java, User.java

(Parecido com Game, consulte GAMETRACKER_Exemplos_Por_Camada.md)

---

## Criar Repositories

```bash
mkdir -p src/main/java/com/example/gametracker/repository
```

### GameRepository.java

```java
package com.example.gametracker.repository;

import com.example.gametracker.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    
    List<Game> findByTitle(String title);
    
    Page<Game> findAll(Pageable pageable);
    
    @Query("SELECT g FROM Game g WHERE g.title LIKE %:search%")
    List<Game> searchByTitle(@Param("search") String search);
    
    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.reviews WHERE g.id = :id")
    Optional<Game> findByIdWithReviews(@Param("id") Long id);
}
```

Repita para Genre, Platform, Review, User.

---

## Implementar Services

```bash
mkdir -p src/main/java/com/example/gametracker/service
```

### GameService.java

```java
package com.example.gametracker.service;

import com.example.gametracker.entity.Game;
import com.example.gametracker.repository.GameRepository;
import com.example.gametracker.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final GameRepository gameRepository;
    private final GenreService genreService;
    private final PlatformService platformService;

    @Transactional(readOnly = true)
    public Page<Game> listAll(Pageable pageable) {
        log.info("Listando games com paginação: {}", pageable);
        return gameRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Game findById(Long id) {
        log.info("Buscando game com ID: {}", id);
        return gameRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Game não encontrado: " + id));
    }

    public Game save(Game game) {
        log.info("Salvando novo game: {}", game.getTitle());
        return gameRepository.save(game);
    }

    public Game update(Long id, Game gameDetails) {
        log.info("Atualizando game ID: {}", id);
        Game game = findById(id);
        if (gameDetails.getTitle() != null) game.setTitle(gameDetails.getTitle());
        if (gameDetails.getReleaseYear() > 0) game.setReleaseYear(gameDetails.getReleaseYear());
        if (gameDetails.getDescription() != null) game.setDescription(gameDetails.getDescription());
        return gameRepository.save(game);
    }

    public void delete(Long id) {
        log.info("Deletando game ID: {}", id);
        if (!gameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Game não encontrado");
        }
        gameRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Game> searchByTitle(String search) {
        return gameRepository.searchByTitle(search);
    }
}
```

---

## Criar Controllers

```bash
mkdir -p src/main/java/com/example/gametracker/controller
```

### GameController.java

```java
package com.example.gametracker.controller;

import com.example.gametracker.entity.Game;
import com.example.gametracker.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping
    public ResponseEntity<Page<Game>> listGames(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(gameService.listAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@Valid @RequestBody Game game) {
        Game saved = gameService.save(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(
            @PathVariable Long id,
            @Valid @RequestBody Game game) {
        
        Game updated = gameService.update(id, game);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Game>> searchGames(@RequestParam String q) {
        return ResponseEntity.ok(gameService.searchByTitle(q));
    }
}
```

---

## Configurar Swagger

Crie `src/main/java/com/example/gametracker/config/SwaggerConfig.java`:

```java
package com.example.gametracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("GameTracker API")
                .version("1.0.0")
                .description("API REST para gerenciar videogames"));
    }
}
```

Acesse em: `http://localhost:8080/swagger-ui.html`

---

## Escrever Testes

```bash
mkdir -p src/test/java/com/example/gametracker/service
```

### GameServiceTest.java

```java
package com.example.gametracker.service;

import com.example.gametracker.entity.Game;
import com.example.gametracker.repository.GameRepository;
import com.example.gametracker.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("GameService Tests")
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    @DisplayName("Deve buscar game por ID com sucesso")
    void testFindByIdSuccess() {
        // Arrange
        Game game = new Game();
        game.setId(1L);
        game.setTitle("The Witcher 3");

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        // Act
        Game result = gameService.findById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("The Witcher 3");
        verify(gameRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando game não existe")
    void testFindByIdNotFound() {
        // Arrange
        when(gameRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> gameService.findById(999L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Game não encontrado: 999");
    }

    @Test
    @DisplayName("Deve salvar novo game com sucesso")
    void testSaveSuccess() {
        // Arrange
        Game game = new Game();
        game.setTitle("Cyberpunk 2077");
        game.setReleaseYear(2020);

        when(gameRepository.save(game)).thenReturn(game);

        // Act
        Game result = gameService.save(game);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Cyberpunk 2077");
    }
}
```

Execute:
```bash
./mvnw test
```

---

## Fazer Deploy no GitHub

### 1. Inicializar Git
```bash
git init
git add .
git commit -m "Initial commit: GameTracker project"
```

### 2. Criar Repositório no GitHub
1. Ir para github.com
2. Novo repositório
3. Copiar URL

### 3. Conectar e Enviar
```bash
git remote add origin https://github.com/seu-usuario/gametracker.git
git branch -M main
git push -u origin main
```

### 4. Verificar
```bash
# No GitHub
# Seu repositório deve estar visível
# http://github.com/seu-usuario/gametracker
```

---

## Executar Localmente

```bash
# Build
./mvnw clean install

# Executar
./mvnw spring-boot:run

# Acessar
# http://localhost:8080/swagger-ui.html
```

---

**Pronto! Seu projeto está funcionando! 🎉**

