package services;

import models.Admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminService {
    private final List<Admin> admins = new ArrayList<>();
    private int proximoId = 1;

    public AdminService() {
        Admin admin = new Admin();
        admin.setIdAdmin(proximoId++);
        admin.setNomeAdmin("Administrador");
        admin.setEmailAdmin("admin@prefeitura.gov");
        admin.setSenhaAdmin("123456");
        admin.setNotifHabilitado(true);
        admin.setPermissoesAdmin("TOTAL");
        admin.setRestricaoIp(false);
        admins.add(admin);
    }

    public Admin login(Scanner scanner) {
        System.out.print("\nEmail: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        for (Admin a : admins) {
            if (a.getEmailAdmin().equals(email) && a.getSenhaAdmin().equals(senha)) {
                System.out.println("Login admin realizado! Bem-vindo(a), " + a.getNomeAdmin());
                return a;
            }
        }
        System.out.println("Credenciais invalidas!");
        return null;
    }
}
