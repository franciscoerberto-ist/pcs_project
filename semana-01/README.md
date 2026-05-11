# Semana 1 — Setup do Projeto

**Período:** 03–09 de maio · **Entrega:** 09/05  
**Objetivo:** Repositório no GitHub com projeto Spring Boot rodando.

---

## Checklist de Entrega

- [ ] Git instalado e configurado (nome + e-mail)
- [ ] Conta no GitHub criada
- [ ] Projeto Spring Boot gerado (6 dependências)
- [ ] Aplicação sobe sem erros ("Started Application...")
- [ ] Estrutura de pacotes criada
- [ ] Repositório público no GitHub com `.gitignore`
- [ ] `README.md` com nome do projeto e integrantes 
- [ ] Commit inicial: `feat: initial Spring Boot setup`

---

## Passo 1 — Instalar o Git

**Windows:**
1. Acesse `git-scm.com/download/win` e baixe o instalador
2. Execute e mantenha as opções padrão
3. Abra o **Git Bash** pelo menu Iniciar

**Mac:**
```bash
brew install git
```

**Linux:**
```bash
sudo apt install git
```

**Verificar:**
```bash
git --version
# Deve retornar: git version 2.x.x
```

---

## Passo 2 — Configurar o Git

Execute **uma única vez** por máquina:

```bash
git config --global user.name "Seu Nome Completo"
git config --global user.email "seu@email.com"

# Verificar:
git config --global --list
```

> Use o mesmo e-mail que usará no GitHub — o professor identifica o autor pelos commits.

---

## Passo 3 — Criar Conta no GitHub

1. Acesse **github.com** → clique em **Sign Up**
2. Informe e-mail, senha e nome de usuário (ex: `joao-silva`)
3. Confirme o e-mail recebido
4. Anote seu nome de usuário — você vai usar em todo o projeto

---

## Passo 4 — Criar o Projeto no Spring Initializr

Acesse **start.spring.io** e configure:

| Campo        | Valor               |
|--------------|---------------------|
| Project      | Maven               |
| Language     | Java                |
| Spring Boot  | 3.2.x               |
| Group        | `com.seunome`       |
| Artifact     | `nome-do-projeto`   |
| Java Version | 17                  |

### Dependências (adicione as 6):

| Dependência          | Para que serve                  |
|----------------------|---------------------------------|
| Spring Web           | Endpoints REST + Tomcat embutido |
| Spring Data JPA      | ORM com Hibernate               |
| H2 Database          | Banco em memória (dev)          |
| Validation           | Bean Validation (`@NotBlank`)   |
| Lombok               | Reduz código repetitivo         |
| SpringDoc OpenAPI    | Swagger UI automático           |

Clique em **GENERATE** → extraia o `.zip` → abra na IDE (IntelliJ ou VS Code).

---

## Passo 5 — Configurar o `application.yml`

Renomeie `application.properties` para `application.yml` e cole:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:projetodb
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

springdoc:
  swagger-ui:
    path: /swagger-ui.html
```

---

## Passo 6 — Criar a Estrutura de Pacotes

Dentro de `src/main/java/com/seunome/projeto/`, crie as pastas:

```
projeto/
├── controller/     ← recebe requisições HTTP
├── service/        ← lógica de negócio
├── repository/     ← acesso ao banco
├── entity/         ← tabelas JPA
└── exception/      ← erros customizados
```

Na IDE: clique com botão direito no pacote principal → **New → Package**.

---

## Passo 7 — Subir a Aplicação

```bash
./mvnw spring-boot:run
```

Verifique no console:
```
Started ...Application in X seconds
```

Então acesse:
- `http://localhost:8080/h2-console` — banco H2 (JDBC URL: `jdbc:h2:mem:projetodb`)
- `http://localhost:8080/swagger-ui.html` — documentação da API

---

## Passo 8 — Criar Repositório no GitHub e Primeiro Push

### 8.1 Criar repositório no GitHub

1. Clique em **+** → **New repository**
2. Nome: `nome-do-projeto` (ex: `sistema-pedidos`)
3. Visibilidade: **Public** (o professor precisa acessar)
4. **NÃO** marque "Add a README" — vem do projeto local
5. Clique em **Create repository**

### 8.2 Verificar o `.gitignore`

O Spring Initializr já gera um `.gitignore`. Confirme que contém:

```
target/
.idea/
.vscode/
*.iml
.DS_Store
Thumbs.db
```

> Nunca suba a pasta `target/` — ela tem centenas de MB de arquivos compilados.

### 8.3 Primeiro commit e push

```bash
# Dentro da pasta do projeto (onde está o pom.xml):
git init
git branch -M main
git remote add origin https://github.com/SEUUSUARIO/nome-do-projeto.git

git add .
git commit -m "feat: initial Spring Boot setup"
git push -u origin main
```

---

## Boas Práticas de Commit

| Faça assim                              | Evite                |
|-----------------------------------------|----------------------|
| `feat: add Produto entity`              | `.`                  |
| `feat: add ProdutoRepository`           | `atualizei`          |
| `fix: correct price validation`         | `asdfgh`             |
| `docs: update README`                   | `final final v2`     |

**Prefixos:** `feat:` (novo recurso) · `fix:` (correção) · `docs:` (documentação) · `refactor:` (refatoração)

---

## Estrutura Final Esperada no GitHub

```
seu-projeto/
├── src/
│   └── main/
│       ├── java/com/seunome/projeto/
│       │   ├── controller/
│       │   ├── service/
│       │   ├── repository/
│       │   ├── entity/
│       │   └── exception/
│       └── resources/
│           └── application.yml
├── pom.xml
├── README.md
└── .gitignore
```
