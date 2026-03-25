# SALUS - Sistema de Denúncias de Dengue

> Sistema de gerenciamento de denúncias de dengue para a Prefeitura de Lins.

---

## Sobre o Projeto

O **SALUS** é um sistema desenvolvido para a Prefeitura de Lins com o objetivo de registrar e gerenciar denúncias relacionadas à dengue. Cidadãos podem reportar possíveis focos do mosquito *Aedes aegypti*, gestores realizam vistorias e classificam as denúncias, e administradores gerenciam as contas dos funcionários.

O projeto foi desenvolvido em duas versões:

- **V1 (CLI):** Versão em terminal utilizando `Scanner` para entrada de dados.
- **V2 (GUI):** Versão com interface gráfica construída com Java Swing.

---

## Funcionalidades

### Cidadão
- Registrar nova denúncia (nome, e-mail, telefone, local e descrição)
- Consultar denúncias pelo e-mail

### Gestor
- Login autenticado por e-mail, senha e cargo
- Visualizar todas as denúncias cadastradas
- Realizar vistoria e classificar denúncias como **Verídica** ou **Falsa**
- Acessar o dashboard com estatísticas gerais

### Administrador
- Login com credenciais fixas
- Cadastrar novos funcionários
- Listar todos os funcionários
- Ativar e desativar contas de funcionários

---

## Estrutura do Projeto

```
projetoDengueIntegrador_ADS/
├── src/
│   ├── SistemaSalus.java              # Entrada principal da versão CLI (V1)
│   ├── models/
│   │   ├── Denuncia.java              # Modelo de dados de denúncia
│   │   └── Funcionario.java           # Modelo de dados de funcionário
│   ├── services/
│   │   ├── DenunciaService.java       # Lógica de negócios de denúncias (V1)
│   │   └── FuncionarioService.java    # Lógica de negócios de funcionários (V1)
│   ├── utils/
│   │   └── MenuUtil.java              # Utilitários de menu para o terminal
│   ├── resources/
│   │   ├── logo.png                   # Logo da aplicação
│   │   └── qrcode.png                 # QR Code do repositório
│   └── v2/
│       ├── SalusSystem.java           # Ponto de entrada e controlador de navegação (V2)
│       ├── DenunciaServiceV2.java     # Serviço de denúncias adaptado para GUI
│       ├── FuncionarioServiceV2.java  # Serviço de funcionários adaptado para GUI
│       ├── ui/                        # Componentes visuais reutilizáveis
│       │   ├── SalusTheme.java        # Constantes de tema (cores, fontes, dimensões)
│       │   └── UIFactory.java         # Fábrica de componentes Swing padronizados
│       └── telas/                     # Telas individuais da aplicação
│           ├── Navegador.java         # Interface de callback para navegação entre telas
│           ├── TelaCarregamento.java   # Splash screen com barra de progresso
│           ├── TelaMenu.java          # Menu principal
│           ├── TelaCidadao.java       # Painel do cidadão
│           ├── TelaFazerDenuncia.java # Formulário de nova denúncia
│           ├── TelaBuscarDenuncia.java# Consulta de denúncias por e-mail
│           ├── TelaLoginGestor.java   # Login do gestor
│           ├── TelaGestor.java        # Painel do gestor
│           ├── TelaListarDenuncias.java # Listagem de denúncias (gestor)
│           ├── TelaVistoriar.java     # Vistoria e classificação de denúncias
│           ├── TelaAlterarStatus.java # Alteração de status de denúncias
│           ├── TelaLoginAdmin.java    # Login do administrador
│           ├── TelaAdmin.java         # Painel do administrador
│           ├── TelaAddFuncionario.java# Cadastro de funcionários
│           ├── TelaListarFuncionarios.java # Listagem de funcionários
│           └── TelaCreditos.java      # Tela de créditos com efeito Star Wars
├── ARQUITETURA.md                     # Documentação de arquitetura
├── integrantesdogrupo.txt             # Integrantes do grupo
└── projetoDengueIntegrador_ADS.iml    # Arquivo de projeto IntelliJ IDEA
```

---

## Tecnologias Utilizadas

| Tecnologia         | Uso                                         |
|--------------------|---------------------------------------------|
| Java SE            | Linguagem principal                         |
| Java Swing         | Interface gráfica (V2)                      |
| Java AWT           | Layouts e componentes gráficos              |
| Java Time API      | Formatação de datas e horários              |
| Java Collections   | Armazenamento em memória com arrays         |
| IntelliJ IDEA      | IDE de desenvolvimento                      |

---

## Arquitetura

O projeto segue a separação em camadas:

```
Models (Dados)  →  Services (Regras de Negócio)  →  Telas (Interface)  ←  UI (Tema & Componentes)
```

- **Models:** Classes POJO que representam os dados (`Denuncia`, `Funcionario`).
- **Services:** Contém toda a lógica de negócio e validações.
- **UI (`v2/ui/`):** Camada de design system — `SalusTheme` centraliza cores, fontes e dimensões; `UIFactory` fornece métodos-fábrica para criar componentes Swing padronizados (botões, painéis, labels, campos de texto, etc.).
- **Telas (`v2/telas/`):** Cada classe representa uma tela independente da aplicação. A interface `Navegador` define o contrato de navegação entre telas via callback.
- **SalusSystem:** Atua como controlador central, instanciando os serviços e gerenciando a troca de telas via `CardLayout`.

Os serviços foram refatorados da V1 para a V2, eliminando a dependência do `Scanner` e retornando dados em vez de imprimir no terminal.

---

## Como Executar

### Pré-requisitos

- Java 8 ou superior instalado

### Via IntelliJ IDEA (Recomendado)

1. Abra o diretório do projeto no IntelliJ IDEA.
2. Para a **V1 (CLI):** clique com o botão direito em `src/SistemaSalus.java` e selecione **Run**.
3. Para a **V2 (GUI):** clique com o botão direito em `src/v2/SalusSystem.java` e selecione **Run**.

### Via Terminal

**V1 - CLI:**
```bash
javac -cp src src/SistemaSalus.java src/models/*.java src/services/*.java src/utils/MenuUtil.java
java -cp src SistemaSalus
```

**V2 - GUI:**
```bash
javac -cp src src/v2/*.java src/v2/ui/*.java src/v2/telas/*.java src/models/*.java src/services/*.java
java -cp src v2.SalusSystem
```

---

## Credenciais Padrão

| Perfil         | E-mail                    | Senha   |
|----------------|---------------------------|---------|
| Administrador  | `admin@prefeitura.gov`    | `123456` |
| Gestor         | Cadastrado via sistema    | Definida no cadastro |

> **Nota:** Gestores precisam ter cargo `GESTOR` cadastrado por um administrador.

---

## Armazenamento de Dados

O sistema utiliza **armazenamento em memória** (arrays Java), sem banco de dados persistente:

| Entidade     | Capacidade Máxima |
|--------------|-------------------|
| Denúncias    | 100 registros     |
| Funcionários | 200 registros     |

> Todos os dados são perdidos ao encerrar a aplicação.

---

## Fluxo de Status das Denúncias

```
PENDENTE  →  VERÍDICO  (vistoria confirmada)
PENDENTE  →  FALSA     (vistoria negada)
```

---

## Tela de Créditos

A versão GUI conta com uma tela de créditos com efeito de rolagem perspectivada no estilo *Star Wars*, exibindo os integrantes do grupo, tecnologias utilizadas e um QR Code com link para o repositório no GitHub.

---

## Integrantes do Grupo

- Walyson Assis
- Gabriel Martins
- Eduardo Xavier
- Gabriel Cardinale
- Filipe Alexandre

---

## Informações Acadêmicas

| Campo       | Informação                                      |
|-------------|-------------------------------------------------|
| Curso       | Análise e Desenvolvimento de Sistemas (ADS)     |
| Semestre    | 3º Semestre                                     |
| Tipo        | Projeto Integrador de POO                       |
| Ano         | 2026                                            |

---

## Repositório

[https://github.com/walysonfelipe/SalusJava](https://github.com/walysonfelipe/SalusJava)
