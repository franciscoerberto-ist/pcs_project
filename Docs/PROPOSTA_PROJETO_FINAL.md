# 🎮 Proposta de Projeto Final - Programação Cliente Servidor

## Disciplina
**Programação Cliente Servidor com Spring Boot**

## 📅 Datas Importantes
- **Período de desenvolvimento:** Semanas 1-5 (03 maio - 06 junho de 2026)
- **Semana de apresentações:** 27-31 de maio de 2026 (semana anterior às provas)
- **Data máxima de entrega:** 31 de maio de 2026
- **Avaliação:** Quem apresentar o projeto **não realiza a prova final (3ª avaliação)**

---

##  Objetivos do Projeto

### Objetivo Geral
Desenvolver uma **aplicação REST com Spring Boot** que implemente todas as camadas do padrão MVC (Model, View, Controller) com qualidade de código, testes automatizados e documentação profissional.

### Objetivos Específicos
1. ✅ Criar entidades JPA com validações e relacionamentos
2. ✅ Implementar Controllers REST com tratamento de erros
3. ✅ Desenvolver Services com lógica de negócio
4. ✅ Escrever testes automatizados (unitários e integração)
5. ✅ Documentar API com Swagger
6. ✅ Publicar no GitHub com documentação

---

##  Composição do Grupo

- **Tamanho:** 2-3 pessoas
- **Papéis sugeridos (não obrigatório):**
  - **Líder técnico:** Coordena arquitetura e prazos
  - **Desenvolvedor Backend:** Implementa Model, Service, Controller
  - **Desenvolvedor QA:** Escreve testes e documentação

---

##  Opções de Projeto

### Opção 1: GameTracker ⭐ (Recomendada)
Aplicação para gerenciar e avaliar videogames

**Entidades sugeridas:**
- Game (título, ano lançamento, descrição, capa)
- Genre (ação, RPG, estratégia, etc)
- Platform (PC, PS5, Xbox, Nintendo)
- Review (avaliação 1-10, comentário)
- User (email, nome, foto de perfil)

**Relacionamentos:**
- Game @ManyToOne Genre
- Game @OneToMany Review
- Game @ManyToMany Platform
- User @OneToMany Review

**Funcionalidades Esperadas:**
- Listar, criar, atualizar e deletar games
- Filtrar por gênero e plataforma
- Listar reviews de um game
- Avaliar games (1-10)
- Buscar games por título

---

### Opção 2: BlogManager
Aplicação para gerenciar blogs

**Entidades:** Post, Category, Tag, Comment, Author
**Funcionalidades:** CRUD de posts, comentários, busca por categoria

---

### Opção 3: StoreAPI
Aplicação de e-commerce

**Entidades:** Product, Category, Order, Item, Customer
**Funcionalidades:** CRUD produtos, carrinho, pedidos

---

### Opção 4: Seu Próprio Projeto
Você pode propor outro projeto, desde que contemple:
- Mínimo 4 entidades
- Mínimo 2 tipos de relacionamento
- Mínimo 10 endpoints REST
- Validações e tratamento de erros
- Testes automatizados

**Envie proposta até 06 de maio de 2026 para aprovação!**

---

##  Requisitos Técnicos

### Obrigatório
- [ ] **Banco de dados:** H2 (desenvolvimento) ou PostgreSQL (produção)
- [ ] **ORM:** JPA/Hibernate
- [ ] **Framework:** Spring Boot 3.x
- [ ] **Build:** Maven ou Gradle
- [ ] **Versionamento:** Git + GitHub
- [ ] **Linguagem:** Java 17+

### Esperado (Alta Nota)
- [ ] **Validações:** Bean Validation + Custom Validators
- [ ] **Tratamento de Erros:** @ControllerAdvice Global
- [ ] **Documentação:** Swagger/SpringFox
- [ ] **Testes:** JUnit 5 + Mockito (cobertura >70%)
- [ ] **DTOs:** Separação Entity/DTO
- [ ] **Paginação:** Pageable
- [ ] **README:** Documentação completa

### Bônus
- [ ] **Segurança:** Spring Security (autenticação básica)
- [ ] **Cache:** @Cacheable/@CacheEvict
- [ ] **Auditoria:** @CreationTimestamp, @UpdateTimestamp
- [ ] **Soft Delete:** Entidades com campo `deletedAt`
- [ ] **Atualizações Parciais:** PATCH endpoints

---

##  Estrutura de Pastas Recomendada

```
gametracker/
├── .github/
│   └── workflows/          # GitHub Actions (CI/CD opcional)
├── src/
│   ├── main/
│   │   ├── java/com/example/gametracker/
│   │   │   ├── entity/            # Entidades JPA
│   │   │   │   ├── Game.java
│   │   │   │   ├── Genre.java
│   │   │   │   ├── Platform.java
│   │   │   │   ├── Review.java
│   │   │   │   └── User.java
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   │   ├── GameDTO.java
│   │   │   │   ├── ReviewDTO.java
│   │   │   │   └── ...
│   │   │   ├── repository/        # Spring Data JPA
│   │   │   │   ├── GameRepository.java
│   │   │   │   ├── GenreRepository.java
│   │   │   │   └── ...
│   │   │   ├── service/           # Lógica de Negócio
│   │   │   │   ├── GameService.java
│   │   │   │   ├── GenreService.java
│   │   │   │   └── ...
│   │   │   ├── controller/        # REST Endpoints
│   │   │   │   ├── GameController.java
│   │   │   │   ├── GenreController.java
│   │   │   │   └── ...
│   │   │   ├── exception/         # Exceções Customizadas
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── ValidationException.java
│   │   │   │   └── ...
│   │   │   ├── handler/           # Exception Handler Global
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── validator/         # Custom Validators
│   │   │   │   └── ValidRatingValidator.java
│   │   │   ├── config/            # Configurações
│   │   │   │   ├── SwaggerConfig.java
│   │   │   │   └── ...
│   │   │   └── GameTrackerApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── data.sql
│   └── test/
│       └── java/com/example/gametracker/
│           ├── entity/            # Testes de Entidades
│           ├── repository/        # Testes de Repository
│           ├── service/           # Testes de Service
│           └── controller/        # Testes de Controller
├── pom.xml                        # Maven
├── build.gradle                   # Gradle (opcional)
├── README.md                      # Documentação Principal
├── GUIA_IMPLEMENTACAO.md         # Guia de Desenvolvimento
├── API_DOCS.md                   # Documentação da API
└── .gitignore
```

---

##  Ferramentas Recomendadas

### Desenvolvimento
| Ferramenta | Versão | Propósito |
|-----------|--------|----------|
| **Java** | 17+ | Linguagem |
| **Spring Boot** | 3.2+ | Framework |
| **Maven** | 3.8+ | Build |
| **H2 Database** | 2.1+ | BD Desenvolvimento |
| **PostgreSQL** | 14+ | BD Produção (opcional) |
| **IntelliJ IDEA / VS Code** | Última | IDE |
| **Git** | 2.34+ | Versionamento |

### Testes
| Ferramenta | Versão | Propósito |
|-----------|--------|----------|
| **JUnit 5** | 5.9+ | Framework de testes |
| **Mockito** | 5.2+ | Mock de dependências |
| **AssertJ** | 3.24+ | Assertions fluentes |
| **Testcontainers** | 1.17+ | Containers para testes |

### Documentação & Análise
| Ferramenta | Versão | Propósito |
|-----------|--------|----------|
| **Swagger/SpringFox** | 3.0+ | Documentação API |
| **SonarQube** | Comunidade | Análise de código |
| **JaCoCo** | 0.8.10+ | Cobertura de testes |

### Colaboração
| Ferramenta | Propósito |
|-----------|----------|
| **GitHub** | Repositório + Documentação |
| **GitHub Projects** | Kanban do projeto |
| **GitHub Issues** | Rastreamento de tarefas |
| **GitHub Actions** | CI/CD (opcional) |

---

##  Rubria de Avaliação

### Total: 100 pontos

#### 1. IMPLEMENTAÇÃO (40 pontos)

| Critério | Pontos | Descrição |
|----------|--------|-----------|
| **Entidades JPA** | 8 | Mínimo 4 entidades bem modeladas |
| **Relacionamentos** | 8 | Mínimo 2 tipos diferentes (@OneToMany, @ManyToOne, @ManyToMany) |
| **Controllers REST** | 8 | Mínimo 10 endpoints (GET, POST, PUT, DELETE) |
| **Services** | 8 | Lógica de negócio em Services, não em Controllers |
| **Repositories** | 8 | Spring Data JPA com queries customizadas |

#### 2. QUALIDADE DE CÓDIGO (25 pontos)

| Critério | Pontos | Descrição |
|----------|--------|-----------|
| **Validações** | 7 | Bean Validation + Custom Validators |
| **Tratamento de Erros** | 7 | @ControllerAdvice, exceções customizadas |
| **DTOs** | 6 | Separação clara Entity/DTO |
| **Padrões de Projeto** | 5 | Segue padrões MVC, boas práticas |

#### 3. TESTES (15 pontos)

| Critério | Pontos | Descrição |
|----------|--------|-----------|
| **Testes Unitários** | 5 | Testes de Service e Repository com Mockito |
| **Testes de Integração** | 5 | Testes de Controller com MockMvc |
| **Cobertura** | 5 | Mínimo 70% de cobertura de código |

#### 4. DOCUMENTAÇÃO (12 pontos)

| Critério | Pontos | Descrição |
|----------|--------|-----------|
| **Swagger/API Docs** | 4 | API documentada com Swagger |
| **README** | 4 | Instruções de setup e uso |
| **Código Comentado** | 2 | Métodos complexos comentados |
| **Exemplos de Requisição** | 2 | Exemplos JSON nas rotas POST/PUT |

#### 5. GITHUB & APRESENTAÇÃO (8 pontos)

| Critério | Pontos | Descrição |
|----------|--------|-----------|
| **Repositório** | 3 | Commits bem organizados, .gitignore, README |
| **Apresentação** | 5 | Demo ao vivo, explicação clara do código |

---

## 🎬 Modelo de Apresentação

### Data & Tempo
- **Duração:** 10-15 minutos por grupo
- **Local:** Sala de aula ou online
- **Semana:** 27-31 de maio de 2026

### Estrutura da Apresentação (Sugerida)

#### 1. Introdução (1-2 minutos)
```
"Bom dia! Somos [Nomes], e vamos apresentar o GameTracker, 
uma aplicação REST para gerenciar e avaliar videogames."

- Nome do projeto
- Objetivo
- Nomes dos integrantes
```

#### 2. Arquitetura (2 minutos)
```
Mostrar diagrama ou verbal:
- Estrutura do projeto (pastas)
- Padrão MVC utilizado
- Tecnologias (Spring Boot, JPA, etc)

"Utilizamos o padrão MVC com:
- Model: 5 entidades JPA
- Controller: 3 controllers REST
- Service: Lógica de negócio centralizada"
```

#### 3. Demo Prática (5-7 minutos)
```
IMPORTANTE: Mostrar código FUNCIONANDO
Usar Postman/Insomnia ou Browser

Demonstrações:
1. GET /api/v1/games              → Listar todos
2. GET /api/v1/games/1            → Buscar um
3. POST /api/v1/games             → Criar novo
4. PUT /api/v1/games/1            → Atualizar
5. DELETE /api/v1/games/1         → Deletar
6. GET /api/v1/games/search?q=... → Buscar por título
7. GET /api/v1/games/1/reviews    → Reviews do game
```

#### 4. Qualidade & Testes (2-3 minutos)
```
Mostrar:
- Resultado dos testes: "Todos os testes passando ✓"
- Cobertura: "Cobertura de testes: 75%"
- Validações: "Validar título vazio → Erro 400"
- Tratamento de erros: "Game não existe → Erro 404"
```

#### 5. Documentação (1 minuto)
```
Mostrar:
- Swagger em /swagger-ui.html
- README no GitHub
```

#### 6. Conclusão (1 minuto)
```
"O projeto foi desenvolvido seguindo boas práticas de 
desenvolvimento, com separação de responsabilidades, 
testes automatizados e documentação completa.

Obrigado!"
```

---

## 📝 Exemplo de Apresentação - Roteiro Prático

### Preparação Técnica
```bash
# Terminal 1: Subir aplicação
./mvnw spring-boot:run

# Terminal 2: Usar Postman/Insomnia ou cURL
curl -X GET http://localhost:8080/api/v1/games
curl -X POST http://localhost:8080/api/v1/games \
  -H "Content-Type: application/json" \
  -d '{
    "title": "The Witcher 3",
    "releaseYear": 2015,
    "genreId": 1,
    "platformIds": [1, 2]
  }'
```

### Roteiro na Apresentação
1. **Listar games** → GET /api/v1/games
   - Mostrar lista de games
   - "Como você vê, conseguimos listar todos os games com sucesso"

2. **Criar game** → POST /api/v1/games
   - Enviar JSON válido
   - "Observe que a validação funciona... vamos tentar com dados inválidos"

3. **Testar validação** → POST com título vazio
   - Mostrar erro 400 com mensagem clara
   - "Viu? A validação bloqueia dados inválidos"

4. **Buscar por ID** → GET /api/v1/games/1
   - Mostrar detalhes completo com relacionamentos
   - "E aqui está o game com seus relacionamentos carregados"

5. **Listar reviews** → GET /api/v1/games/1/reviews
   - Mostrar reviews do game
   - "Cada game pode ter várias reviews"

6. **Atualizar** → PUT /api/v1/games/1
   - Enviar mudanças
   - "Atualizamos o game com sucesso"

7. **Deletar** → DELETE /api/v1/games/1
   - Deletar e tentar buscar novamente (404)
   - "O game foi deletado e não pode ser encontrado"

8. **Swagger** → Abrir navegador em /swagger-ui.html
   - Mostrar documentação automática
   - "Todos os endpoints estão documentados automaticamente"

9. **GitHub** → Abrir repo no navegador
   - Mostrar commits bem organizados
   - Mostrar README com instruções
   - "O código está versionado no GitHub com histórico completo"

---

## 🚀 Guia: Como Subir Projeto no GitHub

### Passo 1: Criar Repositório no GitHub

1. Acesse [github.com](https://github.com)
2. Clique em **"New"** (novo repositório)
3. Preencha:
   - **Repository name:** `gametracker` (seu nome do projeto)
   - **Description:** "Aplicação REST para gerenciar videogames"
   - **Visibility:** Public (para compartilhar)
   - **Initialize:** NÃO marque "Add README"
   - Clique **Create repository**

### Passo 2: Configurar Git Localmente

```bash
# Navegar para pasta do projeto
cd gametracker

# Iniciar repositório Git
git init

# Adicionar repositório remoto (copie de GitHub)
git remote add origin https://github.com/SEU_USUARIO/gametracker.git

# Criar branch main
git branch -M main
```

### Passo 3: Preparar .gitignore

Crie arquivo `.gitignore` na raiz do projeto:

```
# IDE
.idea/
.vscode/
*.swp
*.swo

# Build
target/
build/
*.class
*.jar
*.war

# Dependencies
.gradle/
.m2/

# OS
.DS_Store
Thumbs.db

# Environment
.env
.env.local
application-local.yml

# Logs
logs/
*.log

# Database
*.db
*.sqlite
```

### Passo 4: Estruturar Arquivos

```bash
# Adicionar todos os arquivos
git add .

# Criar primeiro commit
git commit -m "Initial commit: GameTracker project setup"

# Enviar para GitHub
git push -u origin main
```

### Passo 5: Adicionar README.md

Crie `README.md` na raiz:

```markdown
# GameTracker 🎮

Aplicação REST para gerenciar e avaliar videogames.

## Tecnologias
- Spring Boot 3.2
- JPA/Hibernate
- PostgreSQL/H2
- JUnit 5
- Swagger

## Como Usar

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Instalação
git clone https://github.com/seu-usuario/gametracker.git
cd gametracker
./mvnw clean install

### Executar
./mvnw spring-boot:run

### API Documentation
http://localhost:8080/swagger-ui.html

## Testes
./mvnw test

## Estrutura
src/
├── entity/      # Entidades JPA
├── dto/         # Data Transfer Objects
├── repository/  # Spring Data JPA
├── service/     # Lógica de negócio
├── controller/  # REST Endpoints
└── exception/   # Tratamento de erros

## Endpoints

### Games
- GET /api/v1/games - Listar todos
- POST /api/v1/games - Criar novo
- GET /api/v1/games/{id} - Buscar por ID
- PUT /api/v1/games/{id} - Atualizar
- DELETE /api/v1/games/{id} - Deletar

### Genres
- GET /api/v1/genres - Listar todos
- POST /api/v1/genres - Criar novo
- ...

## Autores
- [Seu Nome](https://github.com/seu-usuario)
- [Nome 2](https://github.com/usuario2)
- [Nome 3](https://github.com/usuario3)

## Licença
MIT
```

### Passo 6: Workflow de Desenvolvimento

```bash
# 1. Criar branch para nova feature
git checkout -b feature/game-validations

# 2. Fazer mudanças no código
# ... editar arquivos ...

# 3. Verificar mudanças
git status
git diff

# 4. Adicionar e commitar
git add src/main/java/...
git commit -m "feat: add game title validations"

# 5. Enviar para GitHub
git push origin feature/game-validations

# 6. No GitHub: Criar Pull Request
# - Descrever mudanças
# - Pedir review

# 7. Após aprovação: Merge na main
# (Pode fazer direto no GitHub ou localmente)

# 8. Atualizar main localmente
git checkout main
git pull origin main
```

### Passo 7: Estrutura de Commits

```bash
# ✅ Bons commits:
git commit -m "feat: add game CRUD endpoints"
git commit -m "fix: fix lazy loading exception in reviews"
git commit -m "test: add unit tests for GameService"
git commit -m "docs: update README with API documentation"
git commit -m "refactor: extract validation logic to ValidGameDTO"

# ❌ Ruins:
git commit -m "atualizações"
git commit -m "corrigindo bugs"
git commit -m "teste"
```

### Passo 8: Checklist antes de Entregar

```bash
# 1. Todos os arquivos commitados?
git status  # Deve estar limpo

# 2. Todos os commits enviados?
git log origin/main

# 3. README atualizado?
cat README.md

# 4. .gitignore correto?
cat .gitignore

# 5. Testes passando?
./mvnw test

# 6. Aplicação roda localmente?
./mvnw spring-boot:run

# 7. Swagger acessível?
# http://localhost:8080/swagger-ui.html
```

---

## 📋 Checklist de Entrega

- [ ] Repositório no GitHub criado
- [ ] README.md completo
- [ ] GUIA_IMPLEMENTACAO.md
- [ ] Todos os commits significativos
- [ ] Testes passando (>70% cobertura)
- [ ] Swagger documentando API
- [ ] Aplicação rodando localmente
- [ ] .gitignore configurado
- [ ] Apresentação preparada (10-15 min)
- [ ] Código funcional para demo

---

## 🎁 Benefício: Substituição da Prova Final

**Se apresentar o projeto:**
- ✅ **NÃO precisa fazer a 3ª avaliação (prova)**
- ✅ **Nota do projeto = Nota da avaliação**
- ✅ **Pré-requisito:** Nota ≥ 60 (para passar)

---

## 📞 Dúvidas Frequentes

### P: Posso fazer sozinho?
R: Sim, mas o projeto é para 2-3 pessoas. Fazendo sozinho, você precisa fazer tudo.

### P: Posso mudar de projeto na metade?
R: Não. Escolha bem no início. Se tiver dúvida, envie proposta para aprovação.

### P: Quanto tempo leva?
R: Aproximadamente 50-60 horas no total (5 semanas × 10-12h/semana).

### P: Como entregar?
R: Via GitHub, com link do repositório enviado até 31 de maio.

### P: Precisa de apresentação se entregar antes?
R: Sim, a apresentação é obrigatória na semana 27-31 de maio.

### P: E se alguém não trabalhar no grupo?
R: Você deve indicar no README quem contribuiu com o quê. Professor pode dividir notas.

### P: Posso usar banco de dados diferente?
R: Sim, mas H2 é recomendado para testes. PostgreSQL para produção.

---

## 🏆 Projetos Exemplo

### Nível Básico (Nota 60-70)
```
✓ 4 entidades
✓ CRUD básico
✓ Poucas validações
✓ Sem testes
✓ Sem documentação
```

### Nível Intermediário (Nota 70-85)
```
✓ 5+ entidades
✓ CRUD completo
✓ Validações bean
✓ Testes >50%
✓ Swagger
✓ README
```

### Nível Avançado (Nota 85-100)
```
✓ 5+ entidades
✓ CRUD + filtros
✓ Validações bean + custom
✓ Testes >70%
✓ Swagger completo
✓ README + Guia
✓ Segurança basic
✓ Cache
✓ DTOs bem modelados
✓ Tratamento global de erros
```

---

## 📅 Timeline Recomendado

| Semana | Atividade |
|--------|-----------|
| **1** (03-09 mai) | Formar grupo + Escolher projeto + Setup inicial |
| **2** (10-16 mai) | Implementar entidades + Controllers básicos |
| **3** (17-23 mai) | Services + Validações + Começar testes |
| **4** (24-30 mai) | Testes + Documentação + Polishing |
| **5** (31 mai-06 jun) | **APRESENTAÇÕES** (27-31 mai) |

---

**Boa sorte com o projeto! 🚀**

