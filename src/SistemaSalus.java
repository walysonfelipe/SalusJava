import menus.*;
import models.Cidadao;
import services.*;

import java.util.List;
import java.util.Scanner;

public class SistemaSalus extends MenuBase {

    private final CidadaoService     cidadaoService;
    private final DenunciaService    denunciaService;

    private final MenuArquivo menuArquivo;
    private final MenuCidadao menuCidadao;
    private final MenuGestor  menuGestor;
    private final MenuFiscal  menuFiscal;
    private final MenuAdmin   menuAdmin;

    public SistemaSalus() {
        super(new Scanner(System.in));
        cidadaoService   = new CidadaoService(sc);
        denunciaService  = new DenunciaService(sc);
        AdminService       adminService     = new AdminService(sc);
        FuncionarioService funcService      = new FuncionarioService(sc);
        VistoriaService    vistoriaService  = new VistoriaService(sc);
        RelatorioService   relatorioService = new RelatorioService(sc);
        LogService         logService       = new LogService();

        menuArquivo = new MenuArquivo(sc, cidadaoService, denunciaService, logService);
        menuCidadao = new MenuCidadao(sc, cidadaoService, denunciaService, logService);
        menuGestor  = new MenuGestor(sc, funcService, denunciaService, vistoriaService, relatorioService, logService);
        menuFiscal  = new MenuFiscal(sc, funcService, vistoriaService, logService);
        menuAdmin   = new MenuAdmin(sc, adminService, cidadaoService, funcService, denunciaService, logService);
    }

    public static void main(String[] args) {
        new SistemaSalus().executar();
    }

    private void executar() {
        System.out.println("============================================");
        System.out.println("       SISTEMA SALUS - Dengue");
        System.out.println("============================================");

        List<Cidadao> cidadaosArquivo = cidadaoService.carregarDoArquivo();
        if (!cidadaosArquivo.isEmpty()) {
            denunciaService.carregarDoArquivo(cidadaosArquivo);
        }

        int opcao;
        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Cadastrar Cidadao (Objeto A)");
            System.out.println("2. Cadastrar Denuncia (Objeto B)");
            System.out.println("3. Salvar dados em arquivo texto");
            System.out.println("4. Ler arquivo e exibir em tela");
            System.out.println("--- Acesso por perfil ---");
            System.out.println("5. Area do Cidadao (Login)");
            System.out.println("6. Area do Gestor");
            System.out.println("7. Area do Fiscal / Agente");
            System.out.println("8. Area do Administrador");
            System.out.println("0. Sair");
            System.out.print("Opcao: ");
            opcao = lerInt();

            switch (opcao) {
                case 1: menuArquivo.cadastrarCidadao();  aguardarEnter(); break;
                case 2: menuArquivo.cadastrarDenuncia(); aguardarEnter(); break;
                case 3: menuArquivo.salvar();            aguardarEnter(); break;
                case 4: menuArquivo.lerExibir();         aguardarEnter(); break;
                case 5: menuCidadao.abrir();             aguardarEnter(); break;
                case 6: menuGestor.abrir();              aguardarEnter(); break;
                case 7: menuFiscal.abrir();              aguardarEnter(); break;
                case 8: menuAdmin.abrir();               aguardarEnter(); break;
                case 0: System.out.println("Sistema encerrado. Ate logo!"); break;
                default: System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }
}
