package services;

import models.Denuncia;
import utils.MenuUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DenunciaService {

    Denuncia[] denuncias = new Denuncia[100];
    int total = 0;
    DateTimeFormatter formatoDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public void cadastrar(Scanner scanner) {
        if (total >= 100) {
            System.out.println("Limite atingido!");
            return;
        }

        Denuncia novaDenuncia = new Denuncia();
        System.out.println("\n--- NOVA DENUNCIA ---");
        System.out.print("Nome: ");       novaDenuncia.nome = scanner.nextLine();
        System.out.print("E-mail: ");     novaDenuncia.email = scanner.nextLine();
        System.out.print("Telefone: ");   novaDenuncia.telefone = scanner.nextLine();
        System.out.print("Local: ");      novaDenuncia.local = scanner.nextLine();
        System.out.print("Descricao: ");  novaDenuncia.descricao = scanner.nextLine();
        novaDenuncia.dataHora = LocalDateTime.now().format(formatoDataHora);

        denuncias[total] = novaDenuncia;
        total++;
        System.out.println("Denuncia registrada!");
    }

    public void buscarPorEmail(Scanner scanner) {
        if (total == 0) {
            System.out.println("Nenhuma denuncia.");
            return;
        }

        System.out.print("\nSeu e-mail: ");
        String emailBusca = scanner.nextLine();
        boolean encontrou = false;

        for (int i = 0; i < total; i++) {
            if (denuncias[i].email.equals(emailBusca)) {
                encontrou = true;
                System.out.println("\n#" + (i + 1) + " | " + denuncias[i].status
                        + " | " + denuncias[i].dataHora);
                System.out.println("  Local: " + denuncias[i].local);
                System.out.println("  Descricao: " + denuncias[i].descricao);
            }
        }

        if (!encontrou) System.out.println("Nenhuma denuncia para esse e-mail.");
        MenuUtil.pausar(scanner);
    }

    public void listar() {
        if (total == 0) {
            System.out.println("Nenhuma denuncia.");
            return;
        }

        for (int i = 0; i < total; i++) {
            System.out.println("[" + (i + 1) + "] " + denuncias[i].descricao
                    + " (" + denuncias[i].status + ")");
        }
    }

    public void vistoriar(Scanner scanner) {
        if (total == 0) {
            System.out.println("Nenhuma denuncia.");
            return;
        }

        listar();
        System.out.print("Numero da denuncia: ");
        int indiceDenuncia = MenuUtil.lerInt(scanner) - 1;

        if (indiceDenuncia < 0 || indiceDenuncia >= total) {
            System.out.println("Invalida!");
            return;
        }

        System.out.println("Descricao: " + denuncias[indiceDenuncia].descricao);
        System.out.println("Local: " + denuncias[indiceDenuncia].local);
        System.out.print("Observacao: ");
        denuncias[indiceDenuncia].observacao = scanner.nextLine();
        denuncias[indiceDenuncia].dataHoraVistoria = LocalDateTime.now().format(formatoDataHora);

        System.out.print("Procedente? (1-Sim / 2-Nao): ");
        denuncias[indiceDenuncia].status = (MenuUtil.lerInt(scanner) == 1) ? "VERIDICO" : "FALSA";
        System.out.println("Vistoria registrada!");
        MenuUtil.pausar(scanner);
    }

    public void dashboard() {
        int pendentes = 0, veridico = 0, falsas = 0;

        for (int i = 0; i < total; i++) {
            if (denuncias[i].status.equals("PENDENTE"))   pendentes++;
            if (denuncias[i].status.equals("VERIDICO"))  veridico++;
            if (denuncias[i].status.equals("FALSA"))      falsas++;
        }

        System.out.println("\n--- DASHBOARD ---");
        System.out.println("Total: " + total + " | Pendentes: " + pendentes
                + " | Vistorias Veridicas: " + veridico + " | Vistorias Falsas: " + falsas);
    }
}