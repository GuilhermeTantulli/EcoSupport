# EcoSupport API Documentation

## Endpoints

### `@POST /usuarios`

Este endpoint envia um conglomerado de informações para cadastro.

### Cadastro de Pessoa Física

#### Exemplo

```json
{
  "id": 1,
  "nome": "nome",
  "email": "email",
  "senha": "senha",
  "tipo": "pf",
  "empresa": null,
  "instituicao": null,
  "pessoaFisica": {
    "id": 2
  }
}
```

### Cadastro de Empresa

#### Exemplo

```json
{
  "id": 1,
  "nome": "nome",
  "email": "email",
  "senha": "senha",
  "tipo": "empresa",
  "empresa": {
    "id": 2
  },
  "instituicao": null,
  "pessoaFisica": null
}
```

### Cadastro de Instituicao

#### Exemplo

```json
{
  "id": 1,
  "nome": "nome",
  "email": "email",
  "senha": "senha",
  "tipo": "empresa",
  "empresa": null,
  "instituicao": {
    "id": 2
  },
  "pessoaFisica": null
}
```

### `@GET /usuarios`

Este endpoint retorna a lista de usuários registrados no sistema. Para realizar login.

#### Exemplo de Resposta

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
  }
}
```

## Pessoa Física (pf)

Se o campo 'pessoaFisica' for diferente de null, o usuário é redirecionado para IndividualDashboardActivity.

Dados usados:

* "id": Identificador único da pessoa física.
* "nome": Nome da pessoa física.
* "cpf": Cadastro de Pessoa Física.
* "email": Email da pessoa física.
* "senha": Senha da pessoa física.


## Empresa

Empresa (empresa)

Se o campo empresa for diferente de null, o usuário é redirecionado para DashboardActivity.

Dados usados:

* "id": Identificador único da empresa.
* "nome": Nome da empresa.
* "cnpj": Cadastro Nacional da Pessoa Jurídica.
* "email": Email da empresa.
* "telefone": Telefone da empresa.
* "endereco": Endereço da empresa.

## Instituicao

Instituicao (instituicao)

Se o campo empresa for diferente de null, o usuário é redirecionado para ServiceDashboardActivity.

Dados usados:

* "id": Identificador único da empresa.
* "nome": Nome da empresa.
* "cnpj": Cadastro Nacional da Pessoa Jurídica.
* "email": Email da empresa.
* "telefone": Telefone da empresa.
* "endereco": Endereço da empresa.

------------------------------------

### `@GET /contratos`

Este endpoint retorna a lista de contratos de empresas registradas no sistema.

#### Exemplo de Resposta

```json
{
  "id": 1,
  "empresa": {
    "id": 1,
    "nome": "EcoClean Solutions",
    "cnpj": "12.345.678/0001-99",
    "email": "contato@ecoclean.com",
    "telefone": "(11) 1234-5678",
    "endereco": "Rua Verde, 123, São Paulo, SP"
  },
  "tipoContrato": "Limpeza de Praia",
  "dataInicio": "2023-01-01",
  "dataFim": "2023-12-31",
  "valor": 50000.0,
  "status": "Em Progresso",
  "assinaturaPendente": "N"
}
```

## Dashboard

Aqui usamos duas informações cruciais de cada contrato para exibir a informação do Dashboard

Dados usados:

* status: se há ou não contrato ativo
* valor: valor total recebido pelo contrato
