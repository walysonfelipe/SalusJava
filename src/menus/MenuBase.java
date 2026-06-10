package menus;

import java.util.Scanner;

public abstract class MenuBase {
    protected final Scanner sc;

    protected MenuBase(Scanner sc) {
        this.sc = sc;
    }

    protected int lerInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("Digite um numero valido: "); }
        }
    }

    protected void aguardarEnter() {
        System.out.print("\nPressione ENTER para voltar ao menu...");
        sc.nextLine();
    }
}
