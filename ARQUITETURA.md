# 📐 Arquitetura do Projeto SALUS

> Sistema de denúncias da Prefeitura de Lins para combate à Dengue.

---

## 📁 Estrutura de Pastas

```
src/
├── SistemaSalus.java        ← Ponto de entrada CLI (V1) ⚠️ NÃO MEXER
├── models/
│   ├── Denuncia.java        ← Modelo de dados: denúncia  ⚠️ NÃO MEXER
│   └── Funcionario.java     ← Modelo de dados: funcionário  ⚠️ NÃO MEXER
├── services/
│   ├── DenunciaService.java ← Lógica de negócio: denúncias  ⚠️ NÃO MEXER
│   └── FuncionarioService.java ← Lógica de negócio: funcionários  ⚠️ NÃO MEXER
├── utils/
│   └── MenuUtil.java        ← Utilitário de menus (somente CLI) ⚠️ NÃO MEXER
├── resources/
│   └── logo.png             ← Imagem do logotipo
└── v2/
    └── SalusSystem.java     ← Interface gráfica (V2 - Swing)
```

---

## 📦 Models (`models/`)

Os **models** representam as entidades de dados do sistema. São classes simples (POJOs) com atributos públicos, sem lógica de negócio.

### `Denuncia.java`

Representa uma denúncia feita por um cidadão.

| Atributo | Tipo | Descrição |
|---|---|---|
| `nome` | `String` | Nome do denunciante |
| `email` | `String` | E-mail do denunciante |
| `telefone` | `String` | Telefone de contato |
| `local` | `String` | Local do foco de dengue |
| `descricao` | `String` | Descrição do problema |
| `status` | `String` | Status da denúncia (`PENDENTE`, `VERIDICO`, `FALSA`) — padrão: `PENDENTE` |
| `dataHora` | `String` | Data/hora do registro |
| `observacao` | `String` | Observação do gestor na vistoria |
| `dataHoraVistoria` | `String` | Data/hora da vistoria |

### `Funcionario.java`

Representa um funcionário da prefeitura (gestor ou outro cargo).

| Atributo | Tipo | Descrição |
|---|---|---|
| `nome` | `String` | Nome do funcionário |
| `cpf` | `String` | CPF |
| `email` | `String` | E-mail corporativo |
| `telefone` | `String` | Telefone |
| `senha` | `String` | Senha de acesso |
| `cargo` | `String` | Cargo (ex: `"gestor"`) |
| `setor` | `String` | Setor de lotação |
| `dataCadastro` | `String` | Data/hora do cadastro |
| `ativo` | `boolean` | Se está ativo — padrão: `true` |

> [!TIP]
> Os models são **compartilháveis** entre V1 e V2 sem modificação. Ambas as versões podem importar diretamente de `models.*`.

---

## ⚙️ Services (`services/`)

Os **services** contêm toda a **lógica de negócio** do sistema. Eles manipulam os arrays de models e executam operações como cadastro, busca, listagem e vistoria.

### `DenunciaService.java`

Gerencia o ciclo de vida das denúncias.

| Método | Descrição |
|---|---|
| `cadastrar(Scanner)` | Cadastra nova denúncia (pede dados via terminal) |
| `buscarPorEmail(Scanner)` | Busca denúncias pelo e-mail do cidadão |
| `listar()` | Lista todas as denúncias com status |
| `vistoriar(Scanner)` | Permite ao gestor registrar vistoria e classificar como `VERIDICO` ou `FALSA` |
| `dashboard()` | Exibe contadores: total, pendentes, verídicas e falsas |

**Armazenamento:** Array de `Denuncia[100]` com contador `total`.

### `FuncionarioService.java`

Gerencia funcionários e autenticação.

| Método | Descrição |
|---|---|
| `adicionar(Scanner)` | Cadastra novo funcionário |
| `listar()` | Lista todos os funcionários com status ativo/inativo |
| `alterarStatus(Scanner, boolean)` | Ativa ou desativa um funcionário |
| `loginAdmin(Scanner)` | Login de administrador (credenciais fixas: `admin@prefeitura.gov` / `123456`) |
| `loginGestor(Scanner)` | Login de gestor (valida email, senha, cargo e status ativo) |

**Armazenamento:** Array de `Funcionario[200]` com contador `total`.

> [!IMPORTANT]
> Os services atuais dependem de `Scanner` para entrada de dados (I/O via terminal). **Na V2 (interface gráfica), eles não são compatíveis diretamente**, pois a entrada vem de campos `JTextField` / `JOptionPane` do Swing, e não de `Scanner`. Veja a seção [Diretriz para V2](#-diretriz-para-a-v2) abaixo.

---

## 🔧 Utils (`utils/`)

Utilitários auxiliares reutilizáveis.

### `MenuUtil.java`

Utilitário exclusivo para a versão **CLI (V1)**. Métodos estáticos para exibir menus e ler entradas no terminal.

| Método | Descrição |
|---|---|
| `exibir(Scanner, titulo, opcoes...)` | Exibe menu numerado no terminal e retorna a opção escolhida |
| `lerInt(Scanner)` | Lê um inteiro com validação (repete até digitar número válido) |
| `pausar(Scanner)` | Pausa a execução até o usuário pressionar ENTER |

> [!NOTE]
> O `MenuUtil` é **exclusivo da V1** (CLI). Na V2 (interface gráfica), menus são substituídos por botões e telas Swing, portanto **não há necessidade de reutilizar este utilitário**.

---

## 🚀 SistemaSalus.java (Ponto de Entrada — V1)

> [!CAUTION]
> **Este arquivo NÃO deve ser modificado.** Ele é o ponto de entrada da versão CLI (V1) e deve ser mantido intacto como referência funcional.

`SistemaSalus.java` é a classe principal da **versão 1 (terminal)**. Ela:

1. Instancia os services (`DenunciaService` e `FuncionarioService`)
2. Exibe o menu principal com 3 perfis: **Cidadão**, **Gestor** e **Administrador**
3. Delega as ações para os métodos dos services via `Scanner`
4. Fluxo de autenticação: Gestor e Administrador precisam fazer login antes de acessar seus menus

**Fluxo geral:**

```
Menu Principal
├── [1] Cidadão → Fazer denúncia / Ver minhas denúncias
├── [2] Gestor → Login → Dashboard + Listar + Vistoriar
├── [3] Administrador → Login → CRUD de Funcionários
└── [0] Sair
```

---

## 🖥️ V2 — `SalusSystem.java` (Interface Gráfica — Swing)

A pasta `v2/` contém a versão com **interface gráfica** usando `javax.swing`. Atualmente possui:

- **Tela de carregamento** — barra de progresso animada com logo
- **Tela de menu principal** — botões estilizados para Cidadão, Gestor e Administrador
- **Fullscreen** — roda sem bordas, maximizado, com ESC para sair
- **Estilo visual** — fundo azul escuro (`#191970`), botões laranja (`#ED7702`), bordas arredondadas e efeito hover

> Os módulos dos perfis (Cidadão, Gestor, Administrador) ainda estão em desenvolvimento — atualmente exibem `JOptionPane` informativo.

---

## 📋 Diretriz para a V2

### ✅ O que REUTILIZAR da V1

| Camada | Reutilizável? | Motivo |
|---|---|---|
| `models/` | ✅ **Sim** | São POJOs puros, sem dependência de I/O. Importar diretamente. |
| `services/` | ⚠️ **Parcialmente** | A lógica de negócio (regras, validações, armazenamento) pode ser reaproveitada, mas os métodos atuais dependem de `Scanner`. |
| `utils/MenuUtil` | ❌ **Não** | Exclusivo para CLI. Na V2, menus são componentes visuais (botões, painéis). |
| `SistemaSalus.java` | ❌ **Não mexer** | Ponto de entrada da V1. Manter intacto. |

### 🔄 Como adaptar os Services para V2

Os services atuais fazem **I/O direto** via `Scanner` (leitura) e `System.out.println` (escrita). Na V2 com Swing, a entrada e saída são feitas por componentes gráficos.

**Estratégia recomendada:**

1. **Manter os services originais** (`services/`) intactos para a V1
2. **Criar novos services dentro de `v2/`** que **repliquem a mesma lógica de negócio**, mas recebam os dados como **parâmetros** (Strings, objetos) ao invés de `Scanner`
3. **Retornar resultados** como objetos ou listas, em vez de imprimir no terminal

**Exemplo de adaptação:**

```java
// V1 (services/DenunciaService.java) — depende de Scanner
public void cadastrar(Scanner scanner) {
    Denuncia d = new Denuncia();
    System.out.print("Nome: ");
    d.nome = scanner.nextLine();
    // ...
}

// V2 (v2/DenunciaServiceV2.java) — recebe dados prontos
public String cadastrar(String nome, String email, String telefone,
                        String local, String descricao) {
    Denuncia d = new Denuncia();
    d.nome = nome;
    d.email = email;
    // ...
    return "Denúncia registrada!";
}
```

### 📌 Resumo da Regra

> **Reutilize o que existir fora de `v2/` sempre que possível. Caso o código não seja compatível com interface gráfica (dependência de `Scanner`, `System.out`, etc.), recrie dentro de `v2/` com a mesma lógica, adaptada para receber e retornar dados via parâmetros.**
