package services;

import models.Cidadao;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CidadaoService {
    private List<Cidadao> cidadaos = new ArrayList<>();
    private Scanner sc;
    private int proximoId = 1;

    public CidadaoService(Scanner sc) {
        this.sc = sc;
    }

    public Cidadao cadastrar() {
        System.out.println("\n=== CADASTRO DE CIDADÃO ===");

        System.out.print("Nome completo: ");
        String nome = sc.nextLine().trim();

        System.out.print("CPF (somente números): ");
        String cpf = sc.nextLine().trim();

        if (buscarPorCpf(cpf) != null) {
            System.out.println("CPF já cadastrado.");
            return null;
        }

        System.out.print("E-mail: ");
        String email = sc.nextLine().trim();

        if (buscarPorEmail(email) != null) {
            System.out.println("E-mail já cadastrado.");
            return null;
        }

        System.out.print("Telefone: ");
        String telefone = sc.nextLine().trim();

        System.out.print("Senha: ");
        String senha = sc.nextLine().trim();

        Cidadao c = new Cidadao();
        c.setId(proximoId++);
        c.setNome(nome);
        c.setCpf(cpf);
        c.setEmail(email);
        c.setTelefone(telefone);
        c.setSenhaHash(senha);
        c.setDataCadastro(LocalDate.now().toString());

        cidadaos.add(c);
        System.out.println("Cidadão cadastrado com sucesso! ID: " + c.getId());
        return c;
    }

    public Cidadao login() {
        System.out.println("\n=== LOGIN CIDADÃO ===");
        System.out.print("E-mail: ");
        String email = sc.nextLine().trim();
        System.out.print("Senha: ");
        String senha = sc.nextLine().trim();

        for (Cidadao c : cidadaos) {
            if (c.getEmail().equals(email) && c.getSenhaHash().equals(senha)) {
                if (c.getStatusConta().equals("INATIVO")) {
                    System.out.println("Conta inativa. Entre em contato com o suporte.");
                    return null;
                }
                System.out.println("Bem-vindo(a), " + c.getNome() + "!");
                return c;
            }
        }
        System.out.println("E-mail ou senha incorretos.");
        return null;
    }

    public void listar() {
        System.out.println("\n=== CIDADÃOS CADASTRADOS ===");
        if (cidadaos.isEmpty()) {
            System.out.println("Nenhum cidadão cadastrado.");
            return;
        }
        for (Cidadao c : cidadaos) {
            System.out.printf("[%d] %s | CPF: %s | E-mail: %s | Status: %s%n",
                    c.getId(), c.getNome(), c.getCpf(), c.getEmail(), c.getStatusConta());
        }
    }

    public Cidadao buscarPorId(int id) {
        for (Cidadao c : cidadaos) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    private Cidadao buscarPorCpf(String cpf) {
        for (Cidadao c : cidadaos) {
            if (c.getCpf().equals(cpf)) return c;
        }
        return null;
    }

    private Cidadao buscarPorEmail(String email) {
        for (Cidadao c : cidadaos) {
            if (c.getEmail().equals(email)) return c;
        }
        return null;
    }
}
