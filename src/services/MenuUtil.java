package services;

import java.util.Scanner;

public class MenuUtil {

    public static int exibir(Scanner sc, String titulo, String... opcoes) {
        System.out.println("\n====== " + titulo + " ======");
        for (int i = 0; i < opcoes.length; i++)
            System.out.println("[" + (i + 1) + "] " + opcoes[i]);
        System.out.println("[0] Voltar/Sair");
        System.out.print("Escolha: ");
        return lerInt(sc);
    }

    public static int lerInt(Scanner sc) {
        while (!sc.hasNextInt()) { sc.nextLine(); System.out.print("Digite um numero: "); }
        int v = sc.nextInt(); sc.nextLine();
        return v;
    }

    public static void pausar(Scanner sc) {
        System.out.print("\nENTER para voltar..."); sc.nextLine();
    }
}