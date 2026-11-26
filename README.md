# Loja Solidária Irmã Dulce – PPI v2.0

### Sistema de Gerenciamento de Doações, Empréstimos e Estatísticas

## 📚 Prática Profissional Integrada (PPI)

- **Curso:** Análise e Desenvolvimento de Sistemas (ADS)
- **Instituição:** Instituto Federal Farroupilha (IFFar) – Campus Uruguaiana
- **Disciplina:** Prática Profissional Integrada (PPI)
- **Versão:** 2.0

---

## 🏪 Cliente

### Loja Solidária Irmã Dulce

A Loja Solidária Irmã Dulce é um projeto social voltado ao atendimento de famílias em situação de vulnerabilidade social por meio da doação e do empréstimo de roupas, calçados e outros itens. Para garantir a organização de suas atividades, a instituição realiza o controle das movimentações do estoque e a emissão de relatórios periódicos encaminhados à Secretaria Municipal de Desenvolvimento Social (SEDES).

---

# 📖 Sobre o Projeto

A versão **2.0** do sistema representa uma evolução significativa em relação à primeira versão do protótipo desenvolvido para a Loja Solidária.

Durante o levantamento de requisitos realizado junto à responsável pela instituição, foi identificado que o controle das movimentações era realizado de forma predominantemente manual, utilizando anotações e documentos digitais, dificultando o acompanhamento do estoque, a realização de empréstimos e a elaboração de relatórios.

Com base nessas necessidades, foi desenvolvido um **Sistema de Informação Gerencial (SIG)** em Java que automatiza o gerenciamento de:

- Doações recebidas;
- Estoque disponível;
- Empréstimos de itens;
- Cadastro de beneficiários;
- Relatórios mensais;
- Consultas estatísticas.

A aplicação utiliza arquivos CSV para persistência dos dados e segue regras de negócio observadas no funcionamento real da Loja Solidária.

---

# 🎯 Objetivos

## Objetivo Geral

Identificar as dificuldades operacionais enfrentadas pela Loja Solidária Irmã Dulce e propor uma solução tecnológica capaz de otimizar o gerenciamento de estoque, a organização dos dados e a emissão de relatórios.

## Objetivos do Sistemas 

- Registrar recebimentos (entradas);
- Registrar doações (saídas);
- Registrar empréstimos de peças;
- Facilitar o cadastro de beneficiários para empréstimos;
- Emitir relatórios mensais das movimentações do estoque de doações;
- Disponibilizar indicadores estatísticos para apoiar a tomada de decisão e a divulgação das atividades em canais de comunicação.

---

# 📑 Regras de Negócio

O sistema foi desenvolvido respeitando o fluxo de trabalho utilizado pela Loja Solidária Irmã Dulce.

As principais regras implementadas são:

- As entradas de doações são registradas por categoria.
- As saídas de doações são registradas apenas pela quantidade de itens distribuídos, sem distinção de categoria, reproduzindo o processo atualmente utilizado pela Loja Solidária devido ao elevado fluxo de atendimentos e à limitação de mão de obra.
- O estoque destinado aos empréstimos é independente do estoque de doações.
- Cada empréstimo está obrigatoriamente vinculado a um beneficiário previamente cadastrado.
- Um empréstimo permanece ativo até que sua devolução seja registrada.
- Todas as informações são persistidas em arquivos CSV, garantindo armazenamento permanente dos registros.

---

# ⚙️ Funcionalidades Implementadas

## 📦 Gestão de Doações

O módulo de doações permite o gerenciamento completo da entrada e saída de itens destinados à distribuição pela Loja Solidária.

**Funcionalidades disponíveis:**

- Exibir a quantidade total de itens em estoque;
- Registrar entradas e saída de doações;
- Listar todas as categorias cadastradas;
- Gerar relatório mensal das movimentações;

- Registro da data de cada movimentação automaticamente ;

---


## 🤝 Gestão de Empréstimos

O módulo de empréstimos controla o estoque destinado às peças emprestadas aos beneficiários.

O sistema permite:

- Adicionar peças ao estoque de empréstimos;
- Cadastro de beneficiários;
- Registrar empréstimos;
- Consultar empréstimos ativos;
- Pesquisa de cadastros por CPF;
- Registrar devoluções (baixa);
- Consultar o estoque disponível para empréstimos.

Cada cadastro de beneficiário contém as seguintes informações:

- Nome;
- CPF;
- Telefone.

---

## 📊 Estatísticas e Relatórios

O sistema disponibiliza consultas estatísticas para auxiliar no acompanhamento das atividades da Loja Solidária e na elaboração dos relatórios exigidos pela Secretaria Municipal de Desenvolvimento Social (SEDES).

### Doações

- Quantidade mensal de doações recebidas e repassadas em determinado ano;
- Demonstrativo mensal das doações recebidas em ordem decrescente;
- Demonstrativo mensal das doações repassadas em ordem decrescente.

### Atendimentos e Empréstimos

- Quantidade mensal de atendimentos realizados em determinado ano;
- Beneficiários com empréstimos ativos há mais de 10 dias.

---

## 🔐 Controle de Acesso

A aplicação possui uma tela de login responsável pela autenticação do usuário antes do acesso às funcionalidades do sistema.

---

# 🧱 Arquitetura do Sistema

O projeto foi desenvolvido utilizando conceitos de **Programação Orientada a Objetos (POO)** e separação de responsabilidades.

## Modelos (Entidades)

Representam os objetos de negócio:

- Beneficiario
- Transacao
- Doacao
- Emprestimo
- Peca

## Persistência (DAO)

Responsáveis pela leitura e escrita dos arquivos CSV:

- GerenciadorArquivos
- BeneficiarioDAO
- DoacaoDAO
- EmprestimoDAO

## Views

Responsáveis pela interação com o usuário:

- Menu
- Login
- Main

## Controle
Responsável receber as entradas (inputs) do usuário e coordenar a comunicação entre modelos e DAOs; 

- CadastroDoacao
- CadastroEmprestimo

## Utilitários e Serviços

Responsável pelo processamento, filtragem e geração de indicadores estatísticos do sistema:

- FiltraEstatisticas

---

# 🛠️ Tecnologias Utilizadas

- Java
- Programação Orientada a Objetos (POO)
- Java Collections Framework
- Date/Time (LocalDate)
- Arquivos CSV
- Git
- GitHub
- Discord (reuniões da equipe)
- UML para modelagem do sistema
- Google Docs para escrita do artigo
- phpMyAdmin e MySQL para estruturar o banco de dados para possível implementação futura

---

# 📁 Estrutura do Projeto

```text
LojaSolidariaPPI/
│
├── Beneficiario.java
├── BeneficiarioDAO.java
├── CadastroDoacao.java
├── CadastroEmprestimo.java
├── Doacao.java
├── DoacaoDAO.java
├── Emprestimo.java
├── EmprestimoDAO.java
├── FiltraEstatisticas.java
├── GerenciadorArquivos.java
├── Login.java
├── Main.java
├── Menu.java
├── Peca.java
└── Transacao.java
│
├── Beneficiarios.csv
├── Emprestimos.csv
├── EstoqueDoacoes.csv
├── EstoqueParaEmprestimos.csv
│
└── README.md
```

---

# 🚀 Como Executar

## Pré-requisitos

- Java JDK 17 ou superior
- Git (opcional)

### Clonando o projeto

```bash
git clone https://github.com/mateus02916-cloud/LojaSolidariaPPI.git
```

### Compilando

```bash
javac *.java
```

### Executando

```bash
java Main
```

---

# 📈 Evolução da Versão 1.0 para a Versão 2.0

| Funcionalidade | v1.0 | v2.0 |
|----------------|:---:|:---:|
| Controle de estoque de doações| ✅ | ✅ |
| Relatórios mensais | ✅ | ✅ |
| Persistência em CSV | ✅ | ✅ |
| Cadastro de beneficiários (p/ empréstimos) | ❌ | ✅ |
| Controle de estoque para empréstimos | ❌ | ✅ |
| Empréstimos | ❌ | ✅ |
| Estatísticas | ❌ | ✅ |
| Login | ✅ | ✅ |
| Arquitetura DAO | ❌ | ✅ |
| Modelagem UML | 🔃 | ✅ |
| Persistência em banco relacional| 🔃 | 🔃 |
| Interface gráfica | ❌ | ❌ |


---

# 🧪 Status do Projeto

✅ **Protótipo funcional desenvolvido**

Até o momento foram concluídas as seguintes etapas:

- Levantamento de requisitos;
- Análise do problema;
- Modelagem UML;
- Desenvolvimento do protótipo funcional;
- Implementação da persistência em arquivos CSV;
- Implementação dos módulos de doações e empréstimos;
- Implementação de relatórios e estatísticas.

A arquitetura foi planejada para permitir futuras expansões, incluindo a migração para um banco de dados relacional.

---

# 🔮 Trabalhos Futuros

- Desenvolvimento de interface gráfica (JavaFX);
- Migração para banco de dados MySQL;
- Geração automática de relatórios em PDF;
- Controle de usuários e níveis de permissão;
- Cadastro dinâmico de categorias;
- Evolução para uma arquitetura MVC.

---

# 👥 Equipe

Projeto desenvolvido em grupo por estudantes do curso de **Análise e Desenvolvimento de Sistemas do Instituto Federal Farroupilha – Campus Uruguaiana**, como atividade da **Prática Profissional Integrada (PPI)**.

> Desenvolvido em parceria com a Loja Solidária Irmã Dulce, aplicando conhecimentos acadêmicos na solução de um problema real da comunidade.

---

# 🤝 Agradecimentos

Agradecemos à equipe da **Loja Solidária Irmã Dulce** pela colaboração durante o levantamento de requisitos, validação das funcionalidades e compartilhamento dos processos utilizados pela instituição.

---

# 📌 Repositório

🔗 **GitHub:**

https://github.com/mateus02916-cloud/LojaSolidariaPPI

---
