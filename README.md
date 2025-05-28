# 💊 Projeto Bloco 2 - Farmácia

Este é um projeto Java utilizando Maven, desenvolvido como parte do curso de formação da Generation Brasil. A aplicação simula o backend de uma farmácia, com funcionalidades de cadastro, listagem e categorização de produtos.

## 🚀 Tecnologias Utilizadas

* Java 21+
* Spring Boot
* Maven
* Spring Data JPA
* H2 Database (ou outro, se aplicável)
* Swagger (opcional)

## 🧪 Funcionalidades

* Cadastro de produtos de farmácia
* Categorização dos produtos
* Listagem de produtos com filtros por nome e categoria
* Atualização e remoção de produtos
* Relacionamento entre produtos e categorias (Many-to-One)

## 🗃️ Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com.projeto.farmacia/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── repository/
│   │       └── FarmaciaApplication.java
│   └── resources/
│       ├── application.properties
│       └── ...
```

## ▶️ Como Rodar o Projeto

1. Clone o repositório:

   ```bash
   git clone https://github.com/JovaniOUnico/projeto_final_bloco_02.git
   cd projeto_final_bloco_02
   ```

2. Execute o projeto via Maven:

   ```bash
   ./mvnw spring-boot:run
   ```

3. Acesse a aplicação via:

   ```
   http://localhost:8080
   ```

## 📬 Endpoints de Exemplo

* `GET /produtos`: lista todos os produtos
* `GET /categorias`: lista todas as categorias
* `POST /produtos`: cria um novo produto
* `PUT /produtos/{id}`: atualiza um produto
* `DELETE /produtos/{id}`: deleta um produto

## 🧑‍💻 Autor

Desenvolvido por [@JovaniOUnico](https://github.com/JovaniOUnico) como projeto final do Bloco 2 na Generation Brasil.

---

Se quiser, posso adaptar o README com base no conteúdo real do código. Para isso, você pode me enviar os arquivos `pom.xml` e a estrutura das pastas `src/`. Deseja fazer isso?
