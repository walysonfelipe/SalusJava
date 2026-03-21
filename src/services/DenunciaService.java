package services;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DenunciaService {

    Denuncia[] denuncias = new Denuncia[100];
    int total = 0;
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public void cadastrar(Scanner sc) {
        if (total >= 100) { System.out.println("Limite atingido!"); return; }

        Denuncia d = new Denuncia();
        System.out.println("\n--- NOVA DENUNCIA ---");
        System.out.print("Nome: ");       d.nome = sc.nextLine();
        System.out.print("E-mail: ");     d.email = sc.nextLine();
        System.out.print("Telefone: ");   d.telefone = sc.nextLine();
        System.out.print("Local: ");      d.local = sc.nextLine();
        System.out.print("Descricao: ");  d.descricao = sc.nextLine();
        d.dataHora = LocalDateTime.now().format(fmt);

        denuncias[total] = d;
        total++;
        System.out.println("Denuncia registrada!");
    }

    public void buscarPorEmail(Scanner sc) {
        if (total == 0) { System.out.println("Nenhuma denuncia."); return; }

        System.out.print("\nSeu e-mail: ");
        String email = sc.nextLine();
        boolean achou = false;

        for (int i = 0; i < total; i++) {
            if (denuncias[i].email.equals(email)) {
                achou = true;
                System.out.println("\n#" + (i + 1) + " | " + denuncias[i].status
                        + " | " + denuncias[i].dataHora);
                System.out.println("  Local: " + denuncias[i].local);
                System.out.println("  Desc:  " + denuncias[i].descricao);
            }
        }
        if (!achou) System.out.println("Nenhuma denuncia para esse e-mail.");
        MenuUtil.pausar(sc);
    }

    public void listar() {
        if (total == 0) { System.out.println("Nenhuma denuncia."); return; }
        for (int i = 0; i < total; i++)
            System.out.println("[" + (i + 1) + "] " + denuncias[i].descricao
                    + " (" + denuncias[i].status + ")");
    }

    public void vistoriar(Scanner sc) {
        if (total == 0) { System.out.println("Nenhuma denuncia."); return; }
        listar();

        System.out.print("Numero da denuncia: ");
        int id = MenuUtil.lerInt(sc) - 1;
        if (id < 0 || id >= total) { System.out.println("Invalida!"); return; }

        System.out.println("Desc: " + denuncias[id].descricao);
        System.out.println("Local: " + denuncias[id].local);
        System.out.print("Observacao: ");
        denuncias[id].observacao = sc.nextLine();
        denuncias[id].dataHoraVistoria = LocalDateTime.now().format(fmt);

        System.out.print("Procedente? (1-Sim / 2-Nao): ");
        denuncias[id].status = (MenuUtil.lerInt(sc) == 1) ? "VISTORIADO" : "FALSA";
        System.out.println("Vistoria registrada!");
        MenuUtil.pausar(sc);
    }

    public void dashboard() {
        int p = 0, v = 0, f = 0;
        for (int i = 0; i < total; i++) {
            if (denuncias[i].status.equals("PENDENTE"))   p++;
            if (denuncias[i].status.equals("VISTORIADO")) v++;
            if (denuncias[i].status.equals("FALSA"))      f++;
        }
        System.out.println("\n--- DASHBOARD ---");
        System.out.println("Total: " + total + " | Pendentes: " + p
                + " | Vistoriadas: " + v + " | Falsas: " + f);
    }
}