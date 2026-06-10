package menus;

import models.Admin;
import services.AdminService;
import services.CidadaoService;
import services.DenunciaService;
import services.FuncionarioService;
import services.LogService;

import java.util.Scanner;

public class MenuAdmin extends MenuBase {
    private final AdminService       adminService;
    private final CidadaoService     cidadaoService;
    private final FuncionarioService funcService;
    private final DenunciaService    denunciaService;
    private final LogService         logService;

    public MenuAdmin(Scanner sc, AdminService adminService, CidadaoService cidadaoService,
                     FuncionarioService funcService, DenunciaService denunciaService,
                     LogService logService) {
        super(sc);
        this.adminService    = adminService;
        this.cidadaoService  = cidadaoService;
        this.funcService     = funcService;
        this.denunciaService = denunciaService;
        this.logService      = logService;
    }

    public void abrir() {
        Admin admin = adminService.loginAdmin();
        if (admin == null) return;
        logService.registrar(admin, "LOGIN_ADMIN", null);
        menuLogado(admin);
    }

    private void menuLogado(Admin admin) {
        int opcao;
        do {
            System.out.println("\n=== MENU ADMINISTRADOR — " + admin.getNome() + " ===");
            System.out.println("1. Listar cidadaos");
            System.out.println("2. Cadastrar gestor");
            System.out.println("3. Listar gestores");
            System.out.println("4. Listar fiscais");
            System.out.println("5. Alterar status de funcionario");
            System.out.println("6. Ver logs do sistema");
            System.out.println("7. Dashboard de denuncias");
            System.out.println("8. Listar todas as denuncias");
            System.out.println("0. Sair");
            System.out.print("Opcao: ");
            opcao = lerInt();

            switch (opcao) {
                case 1:
                    cidadaoService.listar();
                    aguardarEnter();
                    break;
                case 2:
                    funcService.adicionarGestor();
                    logService.registrar(admin, "CADASTRO_GESTOR", null);
                    aguardarEnter();
                    break;
                case 3:
                    funcService.listarGestores();
                    aguardarEnter();
                    break;
                case 4:
                    funcService.listarFiscais();
                    aguardarEnter();
                    break;
                case 5:
                    funcService.alterarStatus();
                    logService.registrar(admin, "ALTERACAO_STATUS_FUNCIONARIO", null);
                    aguardarEnter();
                    break;
                case 6:
                    logService.listar();
                    aguardarEnter();
                    break;
                case 7:
                    denunciaService.dashboard();
                    aguardarEnter();
                    break;
                case 8:
                    denunciaService.listar();
                    aguardarEnter();
                    break;
                case 0:
                    logService.registrar(admin, "LOGOUT_ADMIN", null);
                    System.out.println("Saindo da area do administrador...");
                    break;
                default: System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }
}
