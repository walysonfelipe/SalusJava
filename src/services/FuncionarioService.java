package services;

import models.Fiscal;
import models.Gestor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FuncionarioService {
    private List<Gestor> gestores = new ArrayList<>();
    private List<Fiscal> fiscais  = new ArrayList<>();
    private Scanner sc;
    private int proximoId = 1;

    public FuncionarioService(Scanner sc) {
        this.sc = sc;
        seedGestor();
    }

    private void seedGestor() {
        Gestor g = new Gestor();
        g.setId(proximoId++);
        g.setNome("Gestor Padrao");
        g.setEmail("gestor@salus.gov.br");
        g.setSenhaHash("gestor123");
        g.setMatricula("MAT-001");
        g.setSetor("Controle de Endemias");
        g.setDataCadastro(LocalDate.now().toString());
        gestores.add(g);
    }

    // ---------------------------------------------------------- gestores
    public void adicionarGestor() {
        System.out.println("\n=== CADASTRAR GESTOR ===");
        Gestor g = new Gestor();
        g.setId(proximoId++);

        System.out.print("Nome     : ");
        g.setNome(sc.nextLine().trim());
        System.out.print("E-mail   : ");
        g.setEmail(sc.nextLine().trim());
        System.out.print("Senha    : ");
        g.setSenhaHash(sc.nextLine().trim());
        System.out.print("Telefone : ");
        g.setTelefone(sc.nextLine().trim());
        System.out.print("Matricula: ");
        g.setMatricula(sc.nextLine().trim());
        System.out.print("Setor    : ");
        g.setSetor(sc.nextLine().trim());
        g.setDataCadastro(LocalDate.now().toString());

        gestores.add(g);
        System.out.println("Gestor cadastrado! ID: " + g.getId());
    }

    public void listarGestores() {
        System.out.println("\n=== GESTORES ===");
        if (gestores.isEmpty()) { System.out.println("Nenhum gestor cadastrado."); return; }
        for (Gestor g : gestores) {
            System.out.printf("[%d] %-25s | Matricula: %-10s | Setor: %-20s | %s%n",
                    g.getId(), g.getNome(), g.getMatricula(), g.getSetor(),
                    g.isAtivo() ? "ATIVO" : "INATIVO");
        }
    }

    public Gestor loginGestor() {
        System.out.println("\n=== LOGIN GESTOR ===");
        System.out.print("E-mail: ");
        String email = sc.nextLine().trim();
        System.out.print("Senha : ");
        String senha = sc.nextLine().trim();

        for (Gestor g : gestores) {
            if (g.getEmail().equals(email) && g.getSenhaHash().equals(senha)) {
                if (!g.isAtivo()) { System.out.println("Conta inativa."); return null; }
                System.out.println("Bem-vindo(a), " + g.getNome() + "!");
                return g;
            }
        }
        System.out.println("Credenciais invalidas.");
        return null;
    }

    public Gestor buscarGestorPorId(int id) {
        for (Gestor g : gestores) { if (g.getId() == id) return g; }
        return null;
    }

    public List<Gestor> getGestores() { return gestores; }

    // ----------------------------------------------------------- fiscais
    public void adicionarFiscal() {
        System.out.println("\n=== CADASTRAR FISCAL ===");
        Fiscal f = new Fiscal();
        f.setId(proximoId++);

        System.out.print("Nome     : ");
        f.setNome(sc.nextLine().trim());
        System.out.print("E-mail   : ");
        f.setEmail(sc.nextLine().trim());
        System.out.print("Senha    : ");
        f.setSenhaHash(sc.nextLine().trim());
        System.out.print("Telefone : ");
        f.setTelefone(sc.nextLine().trim());
        System.out.print("Matricula: ");
        f.setMatricula(sc.nextLine().trim());
        System.out.print("Setor    : ");
        f.setSetor(sc.nextLine().trim());
        f.setDataCadastro(LocalDate.now().toString());

        fiscais.add(f);
        System.out.println("Fiscal cadastrado! ID: " + f.getId());
    }

    public void listarFiscais() {
        System.out.println("\n=== FISCAIS / AGENTES ===");
        if (fiscais.isEmpty()) { System.out.println("Nenhum fiscal cadastrado."); return; }
        for (Fiscal f : fiscais) {
            System.out.printf("[%d] %-25s | Matricula: %-10s | Setor: %-20s | %s%n",
                    f.getId(), f.getNome(), f.getMatricula(), f.getSetor(),
                    f.isAtivo() ? "ATIVO" : "INATIVO");
        }
    }

    public Fiscal loginFiscal() {
        System.out.println("\n=== LOGIN FISCAL ===");
        System.out.print("E-mail: ");
        String email = sc.nextLine().trim();
        System.out.print("Senha : ");
        String senha = sc.nextLine().trim();

        for (Fiscal f : fiscais) {
            if (f.getEmail().equals(email) && f.getSenhaHash().equals(senha)) {
                if (!f.isAtivo()) { System.out.println("Conta inativa."); return null; }
                System.out.println("Bem-vindo(a), " + f.getNome() + "!");
                return f;
            }
        }
        System.out.println("Credenciais invalidas.");
        return null;
    }

    public Fiscal buscarFiscalPorId(int id) {
        for (Fiscal f : fiscais) { if (f.getId() == id) return f; }
        return null;
    }

    public List<Fiscal> getFiscais() { return fiscais; }

    // ------------------------------------------------------- alterar status
    public void alterarStatus() {
        System.out.println("\n=== ALTERAR STATUS DE FUNCIONARIO ===");
        System.out.println("1. Gestor   2. Fiscal");
        System.out.print("Tipo: ");
        int tipo = lerInt();
        System.out.print("ID do funcionario: ");
        int id = lerInt();

        if (tipo == 1) {
            Gestor g = buscarGestorPorId(id);
            if (g == null) { System.out.println("Gestor nao encontrado."); return; }
            g.setAtivo(!g.isAtivo());
            System.out.println("Status atualizado: " + (g.isAtivo() ? "ATIVO" : "INATIVO"));
        } else {
            Fiscal f = buscarFiscalPorId(id);
            if (f == null) { System.out.println("Fiscal nao encontrado."); return; }
            f.setAtivo(!f.isAtivo());
            System.out.println("Status atualizado: " + (f.isAtivo() ? "ATIVO" : "INATIVO"));
        }
    }

    private int lerInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("Digite um numero valido: "); }
        }
    }
}
