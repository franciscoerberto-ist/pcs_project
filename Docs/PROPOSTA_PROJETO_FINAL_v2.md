# 🎓 Proposta de Projeto Final - Programação Cliente Servidor

## Disciplina
**Programação Cliente Servidor com Spring Boot**

## 📅 Datas Importantes
- **Período de desenvolvimento:** Semanas 1-5 (03 maio - 06 junho de 2026)
- **Semana de apresentações:** 27-31 de maio de 2026 (semana anterior às provas)
- **Data máxima de entrega:** 31 de maio de 2026
- **Avaliação:** Quem apresentar o projeto **não realiza a prova final (3ª avaliação)**

---

## 🎯 Objetivos do Projeto

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

## 👥 Composição do Grupo

- **Tamanho:** 2-3 pessoas
- **Papéis sugeridos (não obrigatório):**
  - **Líder técnico:** Coordena arquitetura e prazos
  - **Desenvolvedor Backend:** Implementa Model, Service, Controller
  - **Desenvolvedor QA:** Escreve testes e documentação

---

## 🚀 Opções de Projeto

### Diretrizes Gerais
Seu projeto deve ter:
- ✅ Mínimo 4 entidades JPA
- ✅ Mínimo 2 tipos de relacionamento (OneToMany, ManyToOne, ManyToMany)
- ✅ Mínimo 10 endpoints REST (GET, POST, PUT, DELETE)
- ✅ Validações com Bean Validation
- ✅ Tratamento global de erros
- ✅ Testes automatizados (>70% cobertura)
- ✅ Documentação com Swagger
- ✅ README com instruções

---

## 📋 Sugestões de Projetos

### Opção 1: Sistema de Biblioteca 📚 (Recomendada)

**Descrição:** API para gerenciar uma biblioteca digital

**Entidades Sugeridas:**
- **Livro** (título, autor, ISBN, ano publicação, descrição, disponível)
- **Autor** (nome, nacionalidade, data nascimento, biografia)
- **Categoria** (ficção, não-ficção, técnico, infantil, etc)
- **Empréstimo** (data empréstimo, data devolução, usuario, livro)
- **Usuário** (nome, email, CPF, endereço, telefone)
- **Avaliação** (nota, comentário, usuario, livro)

**Relacionamentos:**
- Livro @ManyToOne Autor
- Livro @ManyToOne Categoria
- Empréstimo @ManyToOne Livro
- Empréstimo @ManyToOne Usuário
- Livro @OneToMany Avaliação
- Usuário @OneToMany Avaliação

**Funcionalidades Esperadas:**
- Listar, criar, atualizar e deletar livros
- Gerenciar empréstimos (registrar, renovar, finalizar)
- Filtrar livros por categoria, autor, disponibilidade
- Avaliar livros (1-5 estrelas)
- Gerar relatório de empréstimos em atraso
- Calcular multa por atraso

---

### Opção 2: Sistema de Reservas de Hotéis 🏨

**Entidades Sugeridas:**
- Hotel (nome, endereço, telefone, email, avaliação)
- Quarto (número, tipo, capacidade, preço/noite, hotel)
- Hospede (nome, email, CPF, telefone)
- Reserva (data entrada, data saída, total, quarto, hospede)
- Serviço (room service, spa, piscina, estacionamento)

**Funcionalidades:**
- Buscar quartos disponíveis por datas
- Criar reservas
- Cancelar reservas
- Adicionar serviços à reserva
- Calcular total com serviços
- Gerar recibo

---

### Opção 3: Sistema de Gerenciar Tarefas de Projeto 📊

**Entidades Sugeridas:**
- Projeto (nome, descrição, data inicio, data fim, status)
- Tarefa (título, descrição, prioridade, status, data vencimento)
- Usuário (nome, email, cargo)
- Comentário (texto, autor, tarefa, data)
- Anexo (nome arquivo, url, tarefa)
- Atribuição (usuario, tarefa, data atribuição)

**Funcionalidades:**
- CRUD de projetos
- CRUD de tarefas
- Atribuir tarefas a usuários
- Adicionar comentários em tarefas
- Upload de anexos
- Filtrar tarefas por status, prioridade, usuário
- Listar tarefas vencidas

---

### Opção 4: Sistema de Ecommerce Simplificado 🛒

**Entidades Sugeridas:**
- Produto (nome, descrição, preço, estoque, categoria)
- Categoria (nome, descrição)
- Cliente (nome, email, cpf, endereço)
- Pedido (data, status, total, cliente)
- ItemPedido (quantidade, preço_unitário, produto, pedido)
- Avaliação (nota, comentário, cliente, produto)

**Funcionalidades:**
- Listar produtos (com filtro, paginação)
- Buscar produto por nome
- Criar pedido
- Adicionar itens ao pedido
- Atualizar quantidade
- Calcular total do pedido
- Avaliar produtos comprados

---

### Opção 5: Sistema de Agendamento de Consultas 👨‍⚕️

**Entidades Sugeridas:**
- Médico (nome, CRM, especialidade, telefone, horário_atendimento)
- Paciente (nome, CPF, email, telefone, endereço)
- Consulta (data, hora, status, médico, paciente)
- Especialidade (nome, descrição)
- Receita (data, medicamentos, paciente)
- Avaliação (nota, comentário, paciente, médico)

**Funcionalidades:**
- Listar médicos por especialidade
- Buscar horários disponíveis
- Agendar consultas
- Cancelar consultas
- Emitir receitas
- Avaliar médicos

---

### Opção 6: Seu Próprio Projeto ✨

Você pode propor outro projeto, desde que:
- Tenha mínimo 4 entidades bem relacionadas
- Tenha lógica de negócio clara
- Tenha casos de uso práticos e realistas
- Seja viável implementar em 5 semanas

**Envie proposta até 06 de maio de 2026 para aprovação!**

---

## 📋 Requisitos Técnicos

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
- [ ] **Estrutura:** Separação clara de responsabilidades

### Bônus (Nota Máxima)
- [ ] **Segurança:** Spring Security (autenticação básica ou JWT)
- [ ] **Cache:** @Cacheable/@CacheEvict
- [ ] **Auditoria:** @CreationTimestamp, @UpdateTimestamp
- [ ] **Soft Delete:** Entidades com campo `deletedAt`
- [ ] **Atualizações Parciais:** PATCH endpoints
- [ ] **Busca Avançada:** Filtros complexos
- [ ] **CI/CD:** GitHub Actions para testes automáticos

---

## 📁 Estrutura de Pastas Recomendada

```
seu-projeto/
├── .github/
│   └── workflows/          # GitHub Actions (CI/CD opcional)
├── src/
│   ├── main/
│   │   ├── java/com/example/projeto/
│   │   │   ├── entity/            # Entidades JPA
│   │   │   │   ├── Livro.java
│   │   │   │   ├── Autor.java
│   │   │   │   ├── Categoria.java
│   │   │   │   ├── Emprestimo.java
│   │   │   │   └── ...
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   │   ├── LivroDTO.java
│   │   │   │   ├── EmprestimoDTO.java
│   │   │   │   └── ...
│   │   │   ├── repository/        # Spring Data JPA
│   │   │   │   ├── LivroRepository.java
│   │   │   │   ├── AutorRepository.java
│   │   │   │   └── ...
│   │   │   ├── service/           # Lógica de Negócio
│   │   │   │   ├── LivroService.java
│   │   │   │   ├── EmprestimoService.java
│   │   │   │   └── ...
│   │   │   ├── controller/        # REST Endpoints
│   │   │   │   ├── LivroController.java
│   │   │   │   ├── EmprestimoController.java
│   │   │   │   └── ...
│   │   │   ├── exception/         # Exceções Customizadas
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── ValidationException.java
│   │   │   │   └── ...
│   │   │   ├── handler/           # Exception Handler Global
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── validator/         # Custom Validators
│   │   │   │   ├── ValidAvaliacaoValidator.java
│   │   │   │   └── ...
│   │   │   ├── config/            # Configurações
│   │   │   │   ├── SwaggerConfig.java
│   │   │   │   └── ...
│   │   │   └── Application.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── data.sql
│   └── test/
│       └── java/com/example/projeto/
│           ├── entity/            # Testes de Entidades
│           ├── repository/        # Testes de Repository
│           ├── service/           # Testes de Service
│           └── controller/        # Testes de Controller
├── pom.xml                        # Maven
├── build.gradle                   # Gradle (opcional)
├── README.md                      # Documentação Principal
├── GUIA_USO.md                   # Guia de Uso da API
├── API_ENDPOINTS.md              # Lista de Endpoints
└── .gitignore
```

---

## 🛠️ Ferramentas Recomendadas

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
| **Postman/Insomnia** | Última | Testes de API |

### Testes
| Ferramenta | Versão | Propósito |
|-----------|--------|----------|
| **JUnit 5** | 5.9+ | Framework de testes |
| **Mockito** | 5.2+ | Mock de dependências |
| **AssertJ** | 3.24+ | Assertions fluentes |

### Documentação & Análise
| Ferramenta | Versão | Propósito |
|-----------|--------|----------|
| **Swagger/SpringFox** | 3.0+ | Documentação API |
| **JaCoCo** | 0.8.10+ | Cobertura de testes |

---

## 📊 Rubria de Avaliação

### Total: 100 pontos

#### 1. IMPLEMENTAÇÃO (40 pontos)

| Critério | Pontos | Descrição |
|----------|--------|-----------|
| **Entidades JPA** | 8 | Mínimo 4 entidades bem modeladas com anotações apropriadas |
| **Relacionamentos** | 8 | Mínimo 2 tipos diferentes (@OneToMany, @ManyToOne, @ManyToMany) configurados corretamente |
| **Controllers REST** | 8 | Mínimo 10 endpoints (GET, POST, PUT, DELETE) funcionando |
| **Services** | 8 | Lógica de negócio bem separada em Services, não em Controllers |
| **Repositories** | 8 | Spring Data JPA com métodos derivados e @Query customizadas |

#### 2. QUALIDADE DE CÓDIGO (25 pontos)

| Critério | Pontos | Descrição |
|----------|--------|-----------|
| **Validações** | 7 | Bean Validation + Custom Validators implementados |
| **Tratamento de Erros** | 7 | @ControllerAdvice, exceções customizadas, mensagens claras |
| **DTOs** | 6 | Separação clara Entity/DTO, não expor entidades na API |
| **Padrões de Projeto** | 5 | Segue padrões MVC, boas práticas, código limpo |

#### 3. TESTES (15 pontos)

| Critério | Pontos | Descrição |
|----------|--------|-----------|
| **Testes Unitários** | 5 | Testes de Service e Repository com Mockito |
| **Testes de Integração** | 5 | Testes de Controller com MockMvc |
| **Cobertura** | 5 | Mínimo 70% de cobertura de código (JaCoCo) |

#### 4. DOCUMENTAÇÃO (12 pontos)

| Critério | Pontos | Descrição |
|----------|--------|-----------|
| **Swagger/API Docs** | 4 | API documentada com Swagger, @Api, @ApiOperation |
| **README** | 4 | Instruções claras de setup, uso, dependências |
| **Código Comentado** | 2 | Métodos complexos comentados, comentários significativos |
| **Exemplos de Requisição** | 2 | Exemplos JSON para rotas POST/PUT, cURLs ou Postman |

#### 5. GITHUB & APRESENTAÇÃO (8 pontos)

| Critério | Pontos | Descrição |
|----------|--------|-----------|
| **Repositório** | 3 | Commits bem organizados, .gitignore, README, histórico claro |
| **Apresentação** | 5 | Demo ao vivo funcionando, explicação clara do código, slides ou apresentação visual |

---

## 🎬 Modelo de Apresentação

### Data & Tempo
- **Duração:** 10-15 minutos por grupo
- **Local:** Sala de aula ou online
- **Semana:** 27-31 de maio de 2026

### Estrutura da Apresentação (Sugerida)

#### 1. Introdução (1-2 minutos)
```
"Bom dia! Somos [Nomes], e vamos apresentar o [Nome do Projeto], 
uma aplicação para [Descrever objetivo principal]."

- Nome do projeto
- Objetivo do sistema
- Nomes dos integrantes
- Breve motivação/caso de uso
```

#### 2. Visão Geral da Arquitetura (2 minutos)
```
"O projeto foi estruturado seguindo o padrão MVC:

- MODEL: [X] entidades JPA com [Y] tipos de relacionamentos
- CONTROLLER: [Z] controllers REST com [N] endpoints
- SERVICE: Lógica de negócio centralizada em Services
- PERSISTÊNCIA: Spring Data JPA com banco [H2/PostgreSQL]

Tecnologias: Spring Boot 3.2, JPA/Hibernate, Maven, JUnit 5"
```

**Mostrar (opcional):**
- Diagrama ER das entidades
- Estrutura de pastas do projeto
- Pilha tecnológica visual

#### 3. Demo Prática (5-7 minutos)

⭐ **IMPORTANTE:** Mostrar a aplicação **FUNCIONANDO**
⭐ Usar Postman/Insomnia, cURL ou Browser

**Exemplo para Sistema de Biblioteca:**

```bash
# 1. Listar todos os livros
GET /api/v1/livros

# 2. Buscar livro específico
GET /api/v1/livros/1

# 3. Criar novo livro
POST /api/v1/livros
{
  "titulo": "Clean Code",
  "autor": 1,
  "categoria": 1,
  "isbn": "978-0132350884",
  "anoPublicacao": 2008
}

# 4. Criar empréstimo
POST /api/v1/emprestimos
{
  "livroId": 1,
  "usuarioId": 1,
  "dataEmprestimo": "2026-05-03"
}

# 5. Filtrar livros por categoria
GET /api/v1/livros/categoria/1

# 6. Buscar empréstimos em atraso
GET /api/v1/emprestimos/atrasados

# 7. Avaliar livro
POST /api/v1/livros/1/avaliacoes
{
  "nota": 5,
  "comentario": "Excelente livro!"
}

# 8. Testar validação (erro 400)
POST /api/v1/livros
{ "titulo": "" }  # Vai retornar erro de validação

# 9. Testar recurso não encontrado (erro 404)
GET /api/v1/livros/999
```

#### 4. Qualidade & Testes (2-3 minutos)

Mostrar:
```
✅ Testes passando:
   $ ./mvnw test
   [INFO] Running com.example.biblioteca.service.LivroServiceTest
   [INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0

✅ Cobertura de testes: 78%
   Via JaCoCo Report

✅ Validações funcionando:
   POST /api/v1/livros com título vazio
   → Erro 400: "Título é obrigatório"

✅ Tratamento de erros:
   GET /api/v1/livros/999
   → Erro 404: "Livro não encontrado"

✅ Busca avançada:
   GET /api/v1/livros?categoria=ficção&ano=2020
   → Retorna 5 livros
```

#### 5. Documentação (1 minuto)

Mostrar:
```
1. Swagger acessível em:
   http://localhost:8080/swagger-ui.html
   [Abrir no navegador, mostrar endpoints documentados]

2. README no GitHub:
   [Mostrar arquivo README.md]
   - Como instalar
   - Como executar
   - Endpoints disponíveis
   - Exemplos de uso
```

#### 6. Conclusão (1 minuto)
```
"O projeto foi desenvolvido seguindo:
- Separação clara de responsabilidades (MVC)
- Validações em múltiplas camadas
- Testes automatizados com >70% cobertura
- Documentação completa com Swagger
- Versionamento profissional no GitHub

Obrigado!"
```

---

## 📝 Roteiro Prático de Demo

### Setup Antes da Apresentação

```bash
# Terminal 1: Subir aplicação
cd seu-projeto
./mvnw spring-boot:run

# Verificar que aplicação iniciou:
# "Started Application in X seconds"
# "Swagger UI available at http://localhost:8080/swagger-ui.html"

# Terminal 2: Deixar preparado Postman/cURL
# Abrir Postman com requisições de exemplo prontas
```

### Fluxo da Demo

1. **Abrir navegador** → http://localhost:8080/swagger-ui.html
   - Mostrar Swagger UI
   - "Veem? A API está documentada automaticamente!"

2. **Mudar para Postman** → Executar 1ª requisição (GET /api/v1/livros)
   - "Aqui estão todos os livros cadastrados"
   - Mostrar response formatado

3. **Executar POST** → Criar novo livro
   - Mostrar JSON bonito sendo enviado
   - "Note que as validações acontecem antes de salvar"
   - Response com 201 CREATED

4. **Testar Validação** → POST com dados inválidos
   - Mostrar erro 400 com mensagem clara
   - "Veem como protegemos a integridade dos dados?"

5. **Testar 404** → GET de livro que não existe
   - "Se tentar acessar um recurso que não existe, recebe 404 apropriado"

6. **Mostrar Business Logic** → GET /api/v1/emprestimos/atrasados
   - "Aqui rodamos lógica de negócio complexa"
   - "Service calcula quais empréstimos estão vencidos"

7. **Mostrar Testes** → Voltar ao terminal
   - Executar: `./mvnw test`
   - "Todos os testes passam com sucesso"
   - Mostrar resultado: "Tests run: 25, Failures: 0"

8. **GitHub** → Abrir repositório
   - Mostrar commits bem organizados
   - README com instruções
   - "Código versionado profissionalmente"

---

## 🚀 Guia: Como Subir Projeto no GitHub

### Passo 1: Criar Repositório no GitHub

1. Acesse [github.com](https://github.com)
2. Clique em **"New"** ou **"+"** → **"New repository"**
3. Preencha:
   - **Repository name:** `biblioteca-api` (seu nome)
   - **Description:** "API REST para gerenciar biblioteca"
   - **Visibility:** **Public** (para compartilhar)
   - **Initialize:** Deixe em branco (NÃO marque nada)
   - Clique **Create repository**

### Passo 2: Configurar Git Localmente

```bash
# Navegar para pasta do projeto
cd biblioteca-api

# Iniciar repositório Git
git init

# Adicionar repositório remoto (copie a URL do GitHub)
git remote add origin https://github.com/SEU_USUARIO/biblioteca-api.git

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
*.iml

# Build
target/
build/
*.class
*.jar
*.war
*.ear

# Dependencies
.gradle/
.m2/
node_modules/

# OS
.DS_Store
Thumbs.db

# Environment
.env
.env.local
application-local.yml
application-local.yaml

# Logs
logs/
*.log

# Database
*.db
*.sqlite
*.sqlite3

# Temp
*.tmp
temp/
```

### Passo 4: Estruturar Arquivos e Primeiro Commit

```bash
# Adicionar todos os arquivos
git add .

# Criar primeiro commit
git commit -m "Initial commit: Project setup with Spring Boot"

# Enviar para GitHub
git push -u origin main
```

### Passo 5: Adicionar README.md

Crie `README.md` na raiz do projeto:

```markdown
# Biblioteca API 📚

API REST para gerenciar uma biblioteca digital.

## Tecnologias
- Spring Boot 3.2
- JPA/Hibernate
- PostgreSQL/H2
- JUnit 5 + Mockito
- Swagger

## Como Usar

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Instalação
git clone https://github.com/seu-usuario/biblioteca-api.git
cd biblioteca-api
./mvnw clean install

### Executar
./mvnw spring-boot:run

A aplicação rodará em `http://localhost:8080`

### Documentação da API
http://localhost:8080/swagger-ui.html

## Testes
./mvnw test

## Estrutura do Projeto
```
src/
├── entity/      # Entidades JPA
├── dto/         # Data Transfer Objects
├── repository/  # Spring Data JPA
├── service/     # Lógica de negócio
├── controller/  # REST Endpoints
└── exception/   # Tratamento de erros
```

## Endpoints Principais

### Livros
- `GET /api/v1/livros` - Listar todos
- `POST /api/v1/livros` - Criar novo
- `GET /api/v1/livros/{id}` - Buscar por ID
- `PUT /api/v1/livros/{id}` - Atualizar
- `DELETE /api/v1/livros/{id}` - Deletar
- `GET /api/v1/livros/categoria/{categoriaId}` - Filtrar por categoria

### Empréstimos
- `GET /api/v1/emprestimos` - Listar todos
- `POST /api/v1/emprestimos` - Criar empréstimo
- `GET /api/v1/emprestimos/atrasados` - Listar em atraso
- `PUT /api/v1/emprestimos/{id}/devolver` - Devolver livro

## Exemplo de Requisição

### Criar Livro
```bash
curl -X POST http://localhost:8080/api/v1/livros \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Clean Code",
    "autor": "Robert C. Martin",
    "isbn": "978-0132350884",
    "anoPublicacao": 2008,
    "categoria": 1
  }'
```

### Criar Empréstimo
```bash
curl -X POST http://localhost:8080/api/v1/emprestimos \
  -H "Content-Type: application/json" \
  -d '{
    "livroId": 1,
    "usuarioId": 1,
    "dataEmprestimo": "2026-05-03"
  }'
```

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
git checkout -b feature/criar-emprestimos

# 2. Fazer mudanças no código
# ... editar arquivos ...

# 3. Verificar mudanças
git status
git diff

# 4. Adicionar e commitar
git add src/main/java/...
git commit -m "feat: implement loan creation endpoint"

# 5. Enviar para GitHub
git push origin feature/criar-emprestimos

# 6. No GitHub: Criar Pull Request
# - Descrever mudanças
# - Pedir review

# 7. Após aprovação: Merge na main
# (Pode fazer direto no GitHub ou localmente)

# 8. Atualizar main localmente
git checkout main
git pull origin main
```

### Passo 7: Estrutura de Commits (Conventional Commits)

```bash
# ✅ Bons commits:
git commit -m "feat: add book search by category"
git commit -m "fix: fix lazy loading exception in loans"
git commit -m "test: add unit tests for LoanService"
git commit -m "docs: update README with API documentation"
git commit -m "refactor: extract validation logic to validator package"
git commit -m "perf: add caching to book list endpoint"

# ❌ Ruins:
git commit -m "atualizações"
git commit -m "corrigindo bugs"
git commit -m "teste"
git commit -m "WIP"
```

### Passo 8: Checklist Antes de Entregar

```bash
# 1. Todos os arquivos commitados?
git status  # Deve estar limpo (clean)

# 2. Todos os commits enviados?
git log origin/main

# 3. README.md existe e é informativo?
cat README.md

# 4. .gitignore está correto?
cat .gitignore

# 5. Testes passando?
./mvnw test

# 6. Aplicação roda localmente?
./mvnw spring-boot:run

# 7. Swagger acessível?
# http://localhost:8080/swagger-ui.html

# 8. Cobertura de testes adequada?
./mvnw jacoco:report
# open target/site/jacoco/index.html
```

---

## 📋 Checklist de Entrega Final

- [ ] Repositório no GitHub criado e público
- [ ] README.md completo com instruções
- [ ] Todos os commits significativos no histórico
- [ ] Testes passando (./mvnw test)
- [ ] Cobertura de testes >70%
- [ ] Swagger documentando API
- [ ] Aplicação rodando localmente
- [ ] .gitignore configurado
- [ ] Apresentação preparada (10-15 min)
- [ ] Código funcional para demo ao vivo
- [ ] API_ENDPOINTS.md com lista completa
- [ ] Nomes dos integrantes no README

---

## 🎁 Benefício: Substituição da Prova Final

**Se apresentar o projeto:**
- ✅ **NÃO precisa fazer a 3ª avaliação (prova final)**
- ✅ **Nota do projeto = Nota da avaliação**
- ✅ **Pré-requisito:** Nota ≥ 60 (para passar)

---

## 📞 Dúvidas Frequentes

### P: Posso fazer sozinho?
R: Sim, mas o projeto é para 2-3 pessoas. Fazendo sozinho, você precisa fazer tudo (entidades, controllers, services, testes, documentação).

### P: Posso mudar de projeto na metade?
R: Não. Escolha bem no início. Se tiver dúvida, envie proposta para aprovação até 06 de maio.

### P: Quanto tempo leva?
R: Aproximadamente 50-60 horas no total (5 semanas × 10-12h/semana).

### P: Como entregar?
R: Via GitHub. Envie o link do repositório até 31 de maio via email ou plataforma.

### P: Precisa de apresentação se entregar antes?
R: Sim, a apresentação é obrigatória na semana 27-31 de maio.

### P: E se alguém não trabalhar no grupo?
R: Indique no README quem contribuiu com o quê (commits mostram contribuição). Professor pode avaliar individualmente.

### P: Posso usar banco de dados diferente?
R: Sim, mas H2 é recomendado para testes. PostgreSQL para produção é ótimo.

### P: Preciso usar todas as entidades sugeridas?
R: Não, são sugestões. Você pode criar outras desde que tenha mínimo 4 entidades bem relacionadas.

### P: Como calcular cobertura de testes?
R: Use JaCoCo: `./mvnw jacoco:report`. Abre `target/site/jacoco/index.html`

### P: Posso usar Lombok?
R: Sim, é recomendado para reduzir boilerplate (getters, setters, constructors).

---

## 🏆 Níveis de Projeto

### Nível Básico (Nota 60-70)
```
✓ 4 entidades
✓ CRUD básico
✓ Poucas validações
✓ Sem testes ou poucos testes
✓ Sem documentação ou documentação mínima
```

### Nível Intermediário (Nota 70-85)
```
✓ 5+ entidades
✓ CRUD completo
✓ Validações Bean Validation
✓ Testes >50%
✓ Swagger documentando
✓ README informativo
✓ Tratamento de erros básico
```

### Nível Avançado (Nota 85-100)
```
✓ 5+ entidades
✓ CRUD + filtros avançados
✓ Validações bean + custom
✓ Testes >70%
✓ Swagger completo
✓ README + Guia API
✓ Global Exception Handler
✓ DTOs bem modelados
✓ Paginação
✓ Segurança (Spring Security)
✓ Cache (@Cacheable)
✓ Boas práticas SOLID
```

---

## 📅 Timeline Recomendado

| Semana | Data | Atividade |
|--------|------|-----------|
| 1 | 03-09 mai | **Formar grupo + Escolher projeto + Setup inicial** |
| 2 | 10-16 mai | **Implementar entidades + Controllers básicos** |
| 3 | 17-23 mai | **Services + Validações + Começar testes** |
| 4 | 24-30 mai | **Testes + Documentação + Polishing** |
| 5 | 31 mai-06 jun | **APRESENTAÇÕES** (27-31 mai) |

---

**Boa sorte com o projeto! 🚀**

A qualidade do que você entregar dependerá do esforço, organização e qualidade do código. Foque em boas práticas, testes e documentação!

