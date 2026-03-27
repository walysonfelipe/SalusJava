package services;

import models.Funcionario;
import utils.MenuUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FuncionarioService {
    private final List<Funcionario> funcionarios = new ArrayList<>();
    private int proximoId = 1;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public void adicionar(Scanner scanner) {
        Funcionario f = new Funcionario();
        f.setIdFuncionario(String.valueOf(proximoId++));
        System.out.println("\n--- NOVO FUNCIONARIO ---");
        System.out.print("Nome: ");
        f.setNomeFuncionario(scanner.nextLine());
        System.out.print("CPF: ");
        f.setCpfFuncionario(scanner.nextLine());
        System.out.print("Email: ");
        f.setEmailFuncionario(scanner.nextLine());
        System.out.print("Telefone: ");
        f.setTelefoneFuncionario(scanner.nextLine());
        System.out.print("Senha: ");
        f.setSenhaHash(scanner.nextLine());
        System.out.print("Cargo: ");
        f.setCargoFuncionario(scanner.nextLine());
        System.out.print("Salario: ");
        f.setSalarioFuncionario(scanner.nextLine());
        System.out.print("Certificado: ");
        f.setCertificado(scanner.nextLine());
        f.setDataCadastro(LocalDateTime.now().format(fmt));
        funcionarios.add(f);
        System.out.println("Funcionario cadastrado com sucesso! ID: " + f.getIdFuncionario());
    }

    public void listar() {
        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionario cadastrado.");
            return;
        }
        System.out.println("\n--- FUNCIONARIOS ---");
        for (Funcionario f : funcionarios) {
            System.out.printf("[%s] %-20s | %-15s | %-10s | Cadastro: %s%n",
                f.getIdFuncionario(), f.getNomeFuncionario(),
                f.getCargoFuncionario(),
                f.isAtivoFuncionario() ? "ATIVO" : "INATIVO",
                f.getDataCadastro());
        }
    }

    public void alterarStatus(Scanner scanner, boolean ativar) {
        listar();
        if (funcionarios.isEmpty()) return;
        System.out.print("ID do funcionario: ");
        String id = scanner.nextLine();
        for (Funcionario f : funcionarios) {
            if (f.getIdFuncionario().equals(id)) {
                f.setAtivoFuncionario(ativar);
                System.out.println("Funcionario " + (ativar ? "ativado" : "desativado") + "!");
                MenuUtil.pausar(scanner);
                return;
            }
        }
        System.out.println("Funcionario nao encontrado!");
    }

    public Funcionario loginGestor(Scanner scanner) {
        System.out.print("\nEmail: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        for (Funcionario f : funcionarios) {
            if (f.getEmailFuncionario().equals(email)
                    && f.getSenhaHash().equals(senha)
                    && f.getCargoFuncionario().equalsIgnoreCase("gestor")
                    && f.isAtivoFuncionario()) {
                System.out.println("Login realizado! Bem-vindo(a), " + f.getNomeFuncionario());
                return f;
            }
        }
        System.out.println("Falha no login!");
        return null;
    }
}
