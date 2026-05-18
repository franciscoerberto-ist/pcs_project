# Programação Cliente Servidor — Spring Boot

Repositório de exemplos e guias semanais para a disciplina de Programação Cliente Servidor.

## Estrutura

```
pcs/
├── semana-01/README.md     → Setup: Git, GitHub, Spring Initializr, Pacotes
├── semana-02/README.md     → Entidades JPA · Relacionamentos · Repository · GET
├── semana-03/README.md     → Service · Validações · POST · PUT · DELETE
└── exemplo-loja/           → Projeto de demonstração completo
```

---

## Projeto Exemplo — `exemplo-loja`

API REST de uma loja com **4 entidades JPA**, camada Service, tratamento global de erros e CRUD completo.

### Entidades e relacionamentos

| Entidade    | Relacionamentos                                                           |
|-------------|---------------------------------------------------------------------------|
| `Categoria` | `@OneToMany` → Produto                                                    |
| `Produto`   | `@ManyToOne` → Categoria · `@OneToMany` → Avaliacao · `@ManyToMany` ↔ Promocao |
| `Avaliacao` | `@ManyToOne` → Produto                                                    |
| `Promocao`  | `@ManyToMany` ↔ Produto                                                   |

### Endpoints disponíveis

| Método | Endpoint                            | Descrição                         | Status |
|--------|-------------------------------------|-----------------------------------|--------|
| GET    | `/api/v1/categorias`                | Lista todas as categorias         | 200    |
| GET    | `/api/v1/categorias/{id}`           | Busca categoria por id            | 200/404|
| POST   | `/api/v1/categorias`                | Cria nova categoria               | 201    |
| PUT    | `/api/v1/categorias/{id}`           | Atualiza categoria                | 200/404|
| DELETE | `/api/v1/categorias/{id}`           | Remove categoria                  | 204/404|
| GET    | `/api/v1/produtos`                  | Lista todos os produtos           | 200    |
| GET    | `/api/v1/produtos/{id}`             | Busca produto por id              | 200/404|
| GET    | `/api/v1/produtos/busca?nome=...`   | Busca por nome (case-insensitive) | 200    |
| GET    | `/api/v1/produtos/categoria/{id}`   | Produtos de uma categoria         | 200    |
| POST   | `/api/v1/produtos`                  | Cria novo produto                 | 201    |
| PUT    | `/api/v1/produtos/{id}`             | Atualiza produto                  | 200/404|
| DELETE | `/api/v1/produtos/{id}`             | Remove produto                    | 204/404|
| GET    | `/api/v1/avaliacoes`                | Lista todas as avaliações         | 200    |
| GET    | `/api/v1/avaliacoes/{id}`           | Busca avaliação por id            | 200/404|
| GET    | `/api/v1/avaliacoes/produto/{id}`   | Avaliações de um produto          | 200    |
| POST   | `/api/v1/avaliacoes`                | Cria nova avaliação               | 201    |
| DELETE | `/api/v1/avaliacoes/{id}`           | Remove avaliação                  | 204/404|
| GET    | `/api/v1/promocoes`                 | Lista todas as promoções          | 200    |
| GET    | `/api/v1/promocoes/{id}`            | Busca promoção por id             | 200/404|
| GET    | `/api/v1/promocoes/vigentes`        | Promoções ativas hoje             | 200    |
| POST   | `/api/v1/promocoes`                 | Cria nova promoção                | 201    |
| PUT    | `/api/v1/promocoes/{id}`            | Atualiza promoção                 | 200/404|
| DELETE | `/api/v1/promocoes/{id}`            | Remove promoção                   | 204/404|

---

## Como rodar

### Opção 1 — Docker (recomendado, sem instalar Java ou Maven)

```bash
cd exemplo-loja
docker compose up --build
```

A primeira execução faz o build da imagem (baixa dependências Maven, compila). As seguintes são instantâneas.

Acesse:
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console *(JDBC URL: `jdbc:h2:mem:lojademo`, User: `sa`, Password: vazio)*

Para parar:

```bash
docker compose down
```

### Opção 2 — Maven local (requer Java 17+ e Maven 3.9+)

```bash
cd exemplo-loja
mvn package -DskipTests
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

> **Java 26:** o projeto usa Lombok 1.18.46. Versões anteriores do Lombok falham no JDK 26 — certifique-se de que o `pom.xml` não seja revertido para uma versão mais antiga.

---

## Stack

| Componente         | Versão  |
|--------------------|---------|
| Spring Boot        | 3.4.5   |
| Java (target)      | 17      |
| Lombok             | 1.18.46 |
| H2 Database        | (embutido) |
| Springdoc OpenAPI  | 2.3.0   |

---

## Cronograma

| Semana | Período       | Conteúdo                                            | Entrega |
|--------|---------------|-----------------------------------------------------|---------|
| 1      | 03–09 mai     | Git · GitHub · Spring Initializr · Pacotes          | 09/05   |
| 2      | 10–16 mai     | Entidades JPA · Relacionamentos · Repository · GET  | 16/05   |
| 3      | 17–23 mai     | Service · Validações · POST · PUT · DELETE          | 23/05   |
| 4      | 24–30 mai     | Testes · Documentação · Polishing                   | 30/05   |
| 5      | 31 mai–06 jun | Apresentações                                       | 31/05   |
