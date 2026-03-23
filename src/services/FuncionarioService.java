package services;

import models.Funcionario;
import utils.MenuUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class FuncionarioService {

    Funcionario[] funcionarios = new Funcionario[200];
    int total = 0;
    DateTimeFormatter formatoDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public void adicionar(Scanner scanner) {
        if (total >= 200) {
            System.out.println("Limite atingido!");
            return;
        }

        Funcionario novoFuncionario = new Funcionario();
        System.out.println("\n--- NOVO FUNCIONARIO ---");
        System.out.print("Nome: ");     novoFuncionario.nome = scanner.nextLine();
        System.out.print("CPF: ");      novoFuncionario.cpf = scanner.nextLine();
        System.out.print("Email: ");    novoFuncionario.email = scanner.nextLine();
        System.out.print("Telefone: "); novoFuncionario.telefone = scanner.nextLine();
        System.out.print("Senha: ");    novoFuncionario.senha = scanner.nextLine();
        System.out.print("Cargo: ");    novoFuncionario.cargo = scanner.nextLine();
        System.out.print("Setor: ");    novoFuncionario.setor = scanner.nextLine();
        novoFuncionario.dataCadastro = LocalDateTime.now().format(formatoDataHora);

        funcionarios[total] = novoFuncionario;
        total++;
        System.out.println("Funcionario cadastrado!");
    }

    public void listar() {
        if (total == 0) {
            System.out.println("Nenhum funcionario.");
            return;
        }

        for (int i = 0; i < total; i++) {
            System.out.println("[" + (i + 1) + "] " + funcionarios[i].nome
                    + " | " + funcionarios[i].cargo
                    + " | " + (funcionarios[i].ativo ? "ATIVO" : "INATIVO"));
        }
    }

    public void alterarStatus(Scanner scanner, boolean ativar) {
        listar();
        if (total == 0) return;

        System.out.print("Numero: ");
        int indiceFuncionario = MenuUtil.lerInt(scanner) - 1;

        if (indiceFuncionario < 0 || indiceFuncionario >= total) {
            System.out.println("Invalido!");
            return;
        }

        funcionarios[indiceFuncionario].ativo = ativar;
        System.out.println("Funcionario " + (ativar ? "ativado" : "desativado") + "!");
        MenuUtil.pausar(scanner);
    }

    public boolean loginAdmin(Scanner scanner) {
        System.out.print("\nEmail: ");
        String emailDigitado = scanner.nextLine();
        System.out.print("Senha: ");
        String senhaDigitada = scanner.nextLine();

        if (emailDigitado.equals("admin@prefeitura.gov") && senhaDigitada.equals("123456")) {
            return true;
        }

        System.out.println("Credenciais invalidas!");
        return false;
    }

    public boolean loginGestor(Scanner scanner) {
        System.out.print("\nEmail: ");
        String emailDigitado = scanner.nextLine();
        System.out.print("Senha: ");
        String senhaDigitada = scanner.nextLine();

        for (int i = 0; i < total; i++) {
            if (funcionarios[i].email.equals(emailDigitado)
                    && funcionarios[i].senha.equals(senhaDigitada)
                    && funcionarios[i].cargo.equalsIgnoreCase("gestor")
                    && funcionarios[i].ativo) {
                return true;
            }
        }

        System.out.println("Falha no login!");
        return false;
    }
}