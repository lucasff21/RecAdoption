# RecPet

O RecPet é o sistema de backend para uma plataforma de adoção de animais. Sua principal função é gerenciar e coletar os dados de animais, usuários e processos de adoção, servindo como a fonte de dados principal para um futuro sistema de recomendação de adoção.

Repositório frontend: [RecPet](https://github.com/lucasff21/recpet-front)

## 🚀 Tecnologias Utilizadas

- Java 17

- Spring Boot 3

- Maven (Gerenciador de dependências)

- PostgreSQL (Banco de dados relacional)

- Flyway (Ferramenta de migração e versionamento de banco de dados)

- Cloudinary (Para armazenamento e gerenciamento de imagens na nuvem)

- Testcontainers (Para testes de integração)

## 📋 Pré-requisitos

- JDK 17 (ou superior)

- Apache Maven

- Uma instância do PostgreSQL rodando localmente (ou em um container Docker).

- Recomendado: Crie um banco de dados vazio chamado recpet.

- Docker (Necessário para rodar os testes de integração, que usam Testcontainers).

## ⚙️ Configuração Local (Obrigatório)

Este projeto usa Perfis (Profiles) do Spring Boot para gerenciar segredos e configurações de ambiente.

O arquivo application.properties principal contém apenas placeholders (ex: ${DB_URL}).

Você DEVE criar um arquivo chamado `application-local.properties` para fornecer os valores reais.

### Passo 1: Crie o arquivo

Crie o arquivo no seguinte caminho:
`src/main/resources/application-local.properties`

###  Passo 2: Preencha o arquivo

Copie e cole o conteúdo abaixo no seu `application-local.properties` e preencha com seus segredos:


### Passo 3: Adicione ao .gitignore

## 🚀 Executar Localmente

Siga estes passos no seu terminal:

1. Clone o repositório:

```bash
git clone git@github.com:lucasff21/RecAdoption.git
cd RecAdoption
```

2. Compile e instale as dependências:
   (Este comando também rodará os testes, que precisam do Docker rodando)

```bash
mvn clean install
```

Se os testes falharem, mas você quiser compilar mesmo assim, use:

```bash
mvn clean install -DskipTests
```

3. Execute as Migrações do Banco de Dados

```bash
mvn flyway:migrate
```

4. Execute a Aplicação (perfil "local")
```bash
mvn -Dspring.profiles.active=local spring-boot:run
```
ou

```bash
mvn "-Dspring-boot.run.profiles=local" spring-boot:run
```

O servidor estará rodando em http://localhost:8080.