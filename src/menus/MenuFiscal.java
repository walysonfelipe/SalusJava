package menus;

import models.Fiscal;
import services.FuncionarioService;
import services.LogService;
import services.VistoriaService;

import java.util.Scanner;

public class MenuFiscal extends MenuBase {
    private final FuncionarioService funcService;
    private final VistoriaService    vistoriaService;
    private final LogService         logService;

    public MenuFiscal(Scanner sc, FuncionarioService funcService,
                      VistoriaService vistoriaService, LogService logService) {
        super(sc);
        this.funcService     = funcService;
        this.vistoriaService = vistoriaService;
        this.logService      = logService;
    }

    public void abrir() {
        Fiscal fiscal = funcService.loginFiscal();
        if (fiscal == null) return;
        logService.registrar(fiscal, "LOGIN_FISCAL", null);
        menuLogado(fiscal);
    }

    private void menuLogado(Fiscal fiscal) {
        int opcao;
        do {
            System.out.println("\n=== MENU FISCAL — " + fiscal.getNome() + " ===");
            System.out.println("1. Registrar visita");
            System.out.println("2. Ver lotes atribuidos a mim");
            System.out.println("0. Sair");
            System.out.print("Opcao: ");
            opcao = lerInt();

            switch (opcao) {
                case 1:
                    vistoriaService.registrarVisita(fiscal);
                    logService.registrar(fiscal, "REGISTRO_VISITA", null);
                    aguardarEnter();
                    break;
                case 2:
                    vistoriaService.listarLotes();
                    aguardarEnter();
                    break;
                case 0:
                    logService.registrar(fiscal, "LOGOUT_FISCAL", null);
                    System.out.println("Saindo da area do fiscal...");
                    break;
                default: System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }
}
