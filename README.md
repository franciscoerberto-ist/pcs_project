# Programação Cliente Servidor — Spring Boot

Repositório de exemplo e guias para a disciplina de Programação Cliente Servidor.

## Estrutura

```
pcs/
├── semana-01/README.md     → Setup: Git, GitHub, Spring Initializr
├── semana-02/README.md     → Model + Repository + CRUD Básico
└── exemplo-loja/           → Projeto exemplo completo (usado em aula)
```

## Projeto Exemplo

O projeto `exemplo-loja` é uma API REST de loja com **4 entidades JPA**, relacionamentos e endpoints GET — construído incrementalmente ao longo das semanas 1 e 2.

### Entidades

| Entidade     | Relacionamentos                         |
|--------------|-----------------------------------------|
| `Categoria`  | @OneToMany → Produto                    |
| `Produto`    | @ManyToOne → Categoria · @OneToMany → Avaliacao · @ManyToMany → Promocao |
| `Avaliacao`  | @ManyToOne → Produto                    |
| `Promocao`   | @ManyToMany ↔ Produto                   |

### Como rodar o exemplo

```bash
cd exemplo-loja
./mvnw spring-boot:run
```

Acesse:
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console  *(JDBC URL: `jdbc:h2:mem:lojademo`)*

## Cronograma

| Semana | Período         | Conteúdo                                          | Entrega  |
|--------|-----------------|---------------------------------------------------|----------|
| 1      | 03–09 mai       | Git · GitHub · Spring Initializr · Pacotes        | 09/05    |
| 2      | 10–16 mai       | Entidades JPA · Relacionamentos · Repository · GET| 16/05    |
| 3      | 17–23 mai       | Service · Validações · POST · PUT · DELETE        | 23/05    |
| 4      | 24–30 mai       | Testes · Documentação · Polishing                 | 30/05    |
| 5      | 31 mai–06 jun   | Apresentações                                     | 31/05    |
