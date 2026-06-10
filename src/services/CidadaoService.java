package services;

import models.Cidadao;
import util.ValidadorBR;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CidadaoService {
    private static final String ARQUIVO = "cidadaos.txt";

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

        String erroCpf = ValidadorBR.mensagemCpf(cpf);
        if (erroCpf != null) { System.out.println(erroCpf); return null; }

        if (buscarPorCpf(cpf) != null) {
            System.out.println("CPF já cadastrado.");
            return null;
        }

        System.out.print("E-mail: ");
        String email = sc.nextLine().trim();

        String erroEmail = ValidadorBR.mensagemEmail(email);
        if (erroEmail != null) { System.out.println(erroEmail); return null; }

        if (buscarPorEmail(email) != null) {
            System.out.println("E-mail já cadastrado.");
            return null;
        }

        System.out.print("Telefone: ");
        String telefone = sc.nextLine().trim();

        String erroTelefone = ValidadorBR.mensagemTelefone(telefone);
        if (erroTelefone != null) { System.out.println(erroTelefone); return null; }

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

    public List<Cidadao> getCidadaos() { return cidadaos; }

    public void salvar() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            bw.write("id;nome;cpf;email;telefone;senhaHash;dataCadastro;statusConta");
            bw.newLine();
            for (Cidadao c : cidadaos) {
                bw.write(c.getId()                  + ";" +
                         esc(c.getNome())            + ";" +
                         esc(c.getCpf())             + ";" +
                         esc(c.getEmail())           + ";" +
                         esc(c.getTelefone())        + ";" +
                         esc(c.getSenhaHash())       + ";" +
                         esc(c.getDataCadastro())    + ";" +
                         esc(c.getStatusConta()));
                bw.newLine();
            }
            System.out.println("Salvo: " + ARQUIVO + " (" + cidadaos.size() + " registro(s))");
        } catch (IOException e) {
            System.out.println("Erro ao salvar cidadaos: " + e.getMessage());
        }
    }

    public List<Cidadao> carregarDoArquivo() {
        List<Cidadao> lista = new ArrayList<>();
        File f = new File(ARQUIVO);
        if (!f.exists()) return lista;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            br.readLine(); // cabecalho
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] v = linha.split(";", -1);
                if (v.length < 8) continue;
                Cidadao c = new Cidadao();
                c.setId(Integer.parseInt(v[0].trim()));
                c.setNome(unesc(v[1]));
                c.setCpf(unesc(v[2]));
                c.setEmail(unesc(v[3]));
                c.setTelefone(unesc(v[4]));
                c.setSenhaHash(unesc(v[5]));
                c.setDataCadastro(unesc(v[6]));
                c.setStatusConta(unesc(v[7]));
                lista.add(c);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar cidadaos: " + e.getMessage());
        }
        // atualiza a lista interna para que buscarPorId e login funcionem
        this.cidadaos = lista;
        if (!lista.isEmpty()) {
            this.proximoId = lista.get(lista.size() - 1).getId() + 1;
        }
        return lista;
    }

    private static String esc(String s) {
        return s == null ? "" : s.replace(";", "{SC}").replace("\n", " ").replace("\r", "");
    }

    private static String unesc(String s) {
        return s == null ? "" : s.trim().replace("{SC}", ";");
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
