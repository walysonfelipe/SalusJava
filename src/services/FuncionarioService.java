package services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class FuncionarioService {

    Funcionario[] funcionarios = new Funcionario[200];
    int total = 0;
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public void adicionar(Scanner sc) {
        if (total >= 200) { System.out.println("Limite atingido!"); return; }

        Funcionario f = new Funcionario();
        System.out.println("\n--- NOVO FUNCIONARIO ---");
        System.out.print("Nome: ");     f.nome = sc.nextLine();
        System.out.print("CPF: ");      f.cpf = sc.nextLine();
        System.out.print("Email: ");    f.email = sc.nextLine();
        System.out.print("Telefone: "); f.telefone = sc.nextLine();
        System.out.print("Senha: ");    f.senha = sc.nextLine();
        System.out.print("Cargo: ");    f.cargo = sc.nextLine();
        System.out.print("Setor: ");    f.setor = sc.nextLine();
        f.dataCadastro = LocalDateTime.now().format(fmt);

        funcionarios[total] = f;
        total++;
        System.out.println("Funcionario cadastrado!");
    }

    public void listar() {
        if (total == 0) { System.out.println("Nenhum funcionario."); return; }
        for (int i = 0; i < total; i++)
            System.out.println("[" + (i + 1) + "] " + funcionarios[i].nome
                    + " | " + funcionarios[i].cargo
                    + " | " + (funcionarios[i].ativo ? "ATIVO" : "INATIVO"));
    }

    public void alterarStatus(Scanner sc, boolean ativar) {
        listar();
        if (total == 0) return;

        System.out.print("Numero: ");
        int id = MenuUtil.lerInt(sc) - 1;
        if (id < 0 || id >= total) { System.out.println("Invalido!"); return; }

        funcionarios[id].ativo = ativar;
        System.out.println("Funcionario " + (ativar ? "ativado" : "desativado") + "!");
        MenuUtil.pausar(sc);
    }

    public boolean loginAdmin(Scanner sc) {
        System.out.print("\nEmail: "); String e = sc.nextLine();
        System.out.print("Senha: "); String s = sc.nextLine();
        if (e.equals("admin@prefeitura.gov") && s.equals("123456")) return true;
        System.out.println("Credenciais invalidas!");
        return false;
    }

    public boolean loginGestor(Scanner sc) {
        System.out.print("\nEmail: "); String e = sc.nextLine();
        System.out.print("Senha: "); String s = sc.nextLine();

        for (int i = 0; i < total; i++) {
            if (funcionarios[i].email.equals(e) && funcionarios[i].senha.equals(s)
                    && funcionarios[i].cargo.equalsIgnoreCase("gestor")
                    && funcionarios[i].ativo)
                return true;
        }
        System.out.println("Falha no login!");
        return false;
    }
}