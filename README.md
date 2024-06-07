# EcoSupport

## Sumário
- [Integrantes;](#integrantes)
- [Instruções para uso da aplicação;](#instruções-para-uso-da-aplicação)
- [Modelo Entidade Relacional;](#modelo-entidade-relacional)
- [Link para vídeo de funcionamento do projeto;](#link-para-vídeo-de-funcionamento-do-projeto)
- [Link para vídeo Pitch;](#link-para-vídeo-pitch)
- [Listagem de Endpoints;](#listagem-de-endpoints)
- [Link para o Swagger.](#link-para-o-swagger)

## Integrantes
- Beatriz Lucas - RM99104 (Responsável por atuar em Disruptive Architectures, Compliance & Quality Assurance e DevOps Tools & Cloud Computing);
- Enzo Farias - RM98792 (Responsável por atuar em Advanced Business Development with .NET, Java Advanced e DevOps Tools & Cloud Computing);
- Ewerton Gonçalves - RM98571 (Responsável por atuar em Mastering Relational and Non-Relational Database e Disruptive Architectures);
- Guilherme Tantulli - RM97890 (Responsável por atuar em Compliance & Quality Assurance e Mobile App Development);
- Thiago Zupelli - RM99085 (Responsável por atuar em Advanced Business Development with .NET, Java Advanced e Mastering Relational and Non-Relational Database).


## Instruções para uso da aplicação
### Iniciando a Aplicação

1. **Execução da Aplicação**:
    - Para iniciar a aplicação, você deve executar o arquivo **`OceanApplication.java`**. Isso pode ser feito diretamente dentro de sua IDE (Ambiente de Desenvolvimento Integrado) de preferência.
    - Localize o arquivo **`OceanApplication.java`** na pasta de projetos.
    - Clique com o botão direito no arquivo e selecione **`Run 'OceanApplication'`** ou use o atalho para executar o arquivo diretamente, geralmente encontrado na barra de ferramentas ou menu de contexto.
2. **Verificação do Servidor**:
    - Após executar a aplicação, verifique se o servidor foi iniciado corretamente observando a saída no console da IDE, que deve indicar que o Spring Boot está rodando na porta **`8080`**.

### Aplicação em Nuvem

1. **Acessando a aplicação em Nuvem**:
    - Para a utilização da aplicação em nuvem, por não conter nenhum tipo de controle de usuários. Logo, para utiliza-la, basta inserir a url **`https://ecosupport-production.up.railway.app`** e seguir a orientação dos endpoints.

**Utilização da API**

### Exemplos de Requisições para a API

Aqui estão exemplos de como interagir com cada tipo de recurso:

**1. Usuários**

- **Listar todos os usuários**
    
    ```bash
    GET http://localhost:8080/usuarios
    GET https://ecosupport-production.up.railway.app/usuarios
    
    ```
    
- **Buscar um usuário pelo ID**
    
    ```bash
    GET http://localhost:8080/usuarios/{id}
    GET https://ecosupport-production.up.railway.app/usuarios/{id}
    
    ```
    
- **Criar um novo usuário**
    
    ```bash
    POST http://localhost:8080/usuarios
    -Header 'Content-Type: application/json'
    -data '{
        "nome":"FIAP Tech",
        "email":"fiap@gs.com.br",
        "senha":"gsfiap2024",
        "tipo":"instituicao",
        "empresa":null,
        "instituicao": {
            "id": 2
        },
        "pessoaFisica" : null
    }'
    
    ```
    
- **Atualizar um usuário**
    
    ```bash
    PUT http://localhost:8080/usuarios/{id}
    -Header 'Content-Type: application/json'
    -data '{
        "nome":"FIAP Empreendimentos",
        "email":"fiap@empreendimentos.com.br",
        "senha":"gsfiap2024",
        "tipo":"empresa",
        "empresa":{
            "id": 2
        },
        "instituicao": null,
        "pessoaFisica" : null
    }'
    
    ```
    
- **Deletar um usuário**
    
    ```bash
    DELETE http://localhost:8080/usuarios/{id}
    
    ```
    

**2. Empresas**

- **Listar todas as empresas**
    
    ```bash
    GET http://localhost:8080/empresas
    GET https://ecosupport-production.up.railway.app/empresas
    
    ```
    
- **Buscar uma empresa pelo ID**
    
    ```bash
    GET http://localhost:8080/empresas/{id}
    GET https://ecosupport-production.up.railway.app/empresas/{id}
    
    ```
    
- **Criar uma nova empresa**
    
    ```bash
    POST http://localhost:8080/empresas
    -Header 'Content-Type: application/json'
    -data '{
        "nome":"FIAP Tech",
        "cnpj":"12.345.678/9101-11",
        "email":"tech@fiap.com.br",
        "telefone":"11912345678",
        "endereco":"Avenida Paulista, 1100"
    }'
    
    ```
    
- **Atualizar uma empresa**
    
    ```bash
    PUT http://localhost:8080/empresas/{id}
    -Header: 'Content-Type: application/json'
    -data '{
        "nome":"FIAP Empreendimentos",
        "cnpj":"12.345.678/1011-12",
        "email":"empreendimentos@fiap.com.br",
        "telefone":"11987654321",
        "endereco":"Avenida Paulista, 1111"
    }'
    
    ```
    
- **Deletar uma empresa**
    
    ```bash
    DELETE http://localhost:8080/empresas/{id}
    
    ```
    

**3. Instituições, PessoasFísicas, TermosCondições, Serviços, Contratos, Exibições e Transações**

- O formato para estes será similar ao descrito para Usuários e Empresas. Substitua o caminho do endpoint e os detalhes do corpo da requisição conforme apropriado para cada tipo de entidade. As operações incluirão listagem, busca por ID, criação, atualização e deleção.

### Respostas Esperadas

- **Sucesso**: As respostas para requisições bem-sucedidas incluirão os dados solicitados ou uma confirmação de sucesso.
    
    ```json
    
        {
            "id": 1,
            "nome": "João Silva",
            "email": "joao.silva@gmail.com",
            "senha": "senha123",
            "tipo": "pf",
            "empresa": null,
            "instituicao": null,
            "pessoaFisica": {
                "id": 1,
                "nome": "João Silva",
                "cpf": "123.456.789-00",
                "email": "joao.silva@gmail.com",
                "senha": "senha123"
            },
            "_links": {
                "self": {
                    "href": "http://ecosupport-production.up.railway.app/usuariocontroller/1"
                },
                "delete": {
                    "href": "http://ecosupport-production.up.railway.app/usuariocontroller/1/delete"
                },
                "contents": {
                    "href": "http://ecosupport-production.up.railway.app/usuariocontroller/contents"
                }
            }
        }
    
    ```
    
- **Erro**: Respostas de erro fornecerão detalhes sobre o que deu errado.
    
    ```json
    
    {
      "status": "error",
      "message": "Usuário não encontrado.",
      "code": 404
    }
    
    ```
    

### Considerações Adicionais

- **Ambiente de Desenvolvimento**: Certifique-se de que sua IDE está configurada com o JDK apropriado para o projeto e com todas as dependências, definidas geralmente no **`pom.xml`** (Maven).

## Modelo Entidade Relacional
Banco de Dados:\
![Modelo Entidade Relacional](./images/MER_DB.png)

## Link para vídeo de funcionamento do projeto
[![Vídeo TPC](http://img.youtube.com/vi/2Q60fBdK_GI/0.jpg)](https://www.youtube.com/watch?v=2Q60fBdK_GI)

## Link para vídeo Pitch
[![Vídeo TPC](http://img.youtube.com/vi/lOF3FIVVJzI/0.jpg)](https://www.youtube.com/watch?v=lOF3FIVVJzI)

## Listagem de Endpoints
### Endpoints para Usuários:
GET /usuarios - Buscar todos os usuários.\
GET /usuarios/{id} - Buscar um usuário pelo ID.\
POST /usuarios - Criar um novo usuário.\
PUT /usuarios/{id} - Atualizar um usuário existente.\
DELETE /usuarios/{id} - Deletar um usuário pelo ID.

### Endpoints para Pessoas Físicas:
GET /pessoas-fisicas - Buscar todas as pessoas físicas.\
GET /pessoas-fisicas/{id} - Buscar uma pessoa física pelo ID.\
POST /pessoas-fisicas - Criar uma nova pessoa física.\
PUT /pessoas-fisicas/{id} - Atualizar uma pessoa física existente.\
DELETE /pessoas-fisicas/{id} - Deletar uma pessoa física pelo ID.

### Endpoints para Instituições:
GET /instituicoes - Buscar todas as instituições.\
GET /instituicoes/{id} - Buscar uma instituição pelo ID.\
POST /instituicoes - Criar uma nova instituição.\
PUT /instituicoes/{id} - Atualizar uma instituição existente.\
DELETE /instituicoes/{id} - Deletar uma instituição pelo ID.

### Endpoints para Empresas:
GET /empresas - Buscar todas as empresas.\
GET /empresas/{id} - Buscar uma empresa pelo ID.\
POST /empresas - Criar uma nova empresa.\
PUT /empresas/{id} - Atualizar uma empresa existente.\
DELETE /empresas/{id} - Deletar uma empresa pelo ID.

### Endpoints para Serviços:
GET /servicos - Buscar todos os serviços.\
GET /servicos/{id} - Buscar um serviço pelo ID.\
POST /servicos - Criar um novo serviço.\
PUT /servicos/{id} - Atualizar um serviço existente.\
DELETE /servicos/{id} - Deletar um serviço pelo ID.

### Endpoints para Contratos:
GET /contratos - Buscar todos os contratos.\
GET /contratos/{id} - Buscar um contrato pelo ID.\
POST /contratos - Criar um novo contrato.\
PUT /contratos/{id} - Atualizar um contrato existente.\
DELETE /contratos/{id} - Deletar um contrato pelo ID.

### Endpoints para Transações:
GET /transacoes - Buscar todas as transações.\
GET /transacoes/{id} - Buscar uma transação pelo ID.\
POST /transacoes - Criar novas transações.\
PUT /transacoes/{id} - Atualizar uma transação existente.\
DELETE /transacoes/{id} - Deletar uma transação pelo ID.

### Endpoints para Exibições:
GET /exibicoes - Buscar todas as exibições.\
GET /exibicoes/{id} - Buscar uma exibição pelo ID.\
POST /exibicoes - Criar uma nova exibição.\
PUT /exibicoes/{id} - Atualizar uma exibição existente.\
DELETE /exibicoes/{id} - Deletar uma exibição pelo ID.

### Endpoints para Termos e Condições:
GET /termos-condicoes - Buscar todos os termos e condições.\
GET /termos-condicoes/{id} - Buscar uma informação de termos e condições pelo ID.\
POST /termos-condicoes - Criar uma nova informação de termos e condições.\
PUT /termos-condicoes/{id} - Atualizar uma informação de termos e condições existente.\
DELETE /termos-condicoes/{id} - Deletar uma informação de termos e condições pelo ID.

## Link para o Swagger
[Swagger](http://localhost:8080/docs)\
OBS: a API precisa estar rodando para funcionamento do mesmo.