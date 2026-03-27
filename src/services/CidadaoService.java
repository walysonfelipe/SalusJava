package services;

import models.Cidadao;
import utils.MenuUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CidadaoService {
    private final List<Cidadao> cidadaos = new ArrayList<>();
    private int proximoId = 1;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Cidadao cadastrar(Scanner scanner) {
        System.out.println("\n--- CADASTRO DE CIDADAO ---");
        Cidadao c = new Cidadao();
        c.setIdCidadao(proximoId++);
        System.out.print("Nome: ");
        c.setNomeCidadao(scanner.nextLine());
        System.out.print("CPF: ");
        c.setCpfCidadao(scanner.nextLine());
        System.out.print("Email: ");
        c.setEmailCidadao(scanner.nextLine());
        System.out.print("Telefone: ");
        c.setTelefoneCidadao(scanner.nextLine());
        System.out.print("Senha: ");
        c.setSenhaCidadao(scanner.nextLine());
        c.setDataCadastro(LocalDateTime.now().format(fmt));
        cidadaos.add(c);
        System.out.println("Cidadao cadastrado com sucesso! Bem-vindo(a), " + c.getNomeCidadao());
        return c;
    }

    public Cidadao login(Scanner scanner) {
        System.out.print("\nEmail: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        for (Cidadao c : cidadaos) {
            if (c.getEmailCidadao().equals(email) && c.getSenhaCidadao().equals(senha)) {
                System.out.println("Login realizado! Bem-vindo(a), " + c.getNomeCidadao());
                return c;
            }
        }
        System.out.println("Email ou senha invalidos!");
        return null;
    }

    public void listar() {
        if (cidadaos.isEmpty()) {
            System.out.println("Nenhum cidadao cadastrado.");
            return;
        }
        for (Cidadao c : cidadaos) {
            System.out.printf("[%d] %s | %s | Cadastro: %s%n",
                c.getIdCidadao(), c.getNomeCidadao(),
                c.getEmailCidadao(), c.getDataCadastro());
        }
    }
}
