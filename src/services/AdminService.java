package services;

import models.Admin;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminService {
    private List<Admin> admins = new ArrayList<>();
    private Scanner sc;

    public AdminService(Scanner sc) {
        this.sc = sc;
        seedAdmin();
    }

    private void seedAdmin() {
        Admin a = new Admin();
        a.setId(1);
        a.setNome("Administrador");
        a.setEmail("admin@salus.gov.br");
        a.setSenhaHash("admin123");
        a.setNivelAcesso(1);
        admins.add(a);
    }

    public Admin login() {
        System.out.println("\n=== LOGIN ADMINISTRADOR ===");
        System.out.print("E-mail: ");
        String email = sc.nextLine().trim();
        System.out.print("Senha: ");
        String senha = sc.nextLine().trim();

        for (Admin a : admins) {
            if (a.getEmail().equals(email) && a.getSenhaHash().equals(senha)) {
                System.out.println("Bem-vindo(a), " + a.getNome() + "!");
                return a;
            }
        }
        System.out.println("Credenciais inválidas.");
        return null;
    }

    public void listar() {
        System.out.println("\n=== ADMINISTRADORES ===");
        for (Admin a : admins) {
            System.out.printf("[%d] %s | E-mail: %s | Nível: %d%n",
                    a.getId(), a.getNome(), a.getEmail(), a.getNivelAcesso());
        }
    }
}
