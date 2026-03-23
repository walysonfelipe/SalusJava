import java.util.Scanner;

import services.DenunciaService;
import utils.MenuUtil;
import services.FuncionarioService;

public class SistemaSalus {

    Scanner scanner = new Scanner(System.in);
    DenunciaService denService = new DenunciaService();
    FuncionarioService funcService = new FuncionarioService();

    void menuCidadao() {
        int op;
        do {
            op = MenuUtil.exibir(scanner, "CIDADAO", "Fazer denuncia", "Ver minhas denuncias");
            switch (op) {
                case 1 -> denService.cadastrar(scanner);
                case 2 -> denService.buscarPorEmail(scanner);
            }
        } while (op != 0);
    }

    void menuAdmin() {
        int op;
        do {
            op = MenuUtil.exibir(scanner, "ADMINISTRADOR",
                    "Adicionar funcionario", "Listar funcionarios",
                    "Desativar funcionario", "Ativar funcionario");
            switch (op) {
                case 1 -> funcService.adicionar(scanner);
                case 2 -> { funcService.listar(); MenuUtil.pausar(scanner); }
                case 3 -> funcService.alterarStatus(scanner, false);
                case 4 -> funcService.alterarStatus(scanner, true);
            }
        } while (op != 0);
    }

    void menuGestor() {
        int op;
        do {
            denService.dashboard();
            op = MenuUtil.exibir(scanner, "GESTOR", "Listar denuncias", "Vistoriar denuncia");
            switch (op) {
                case 1 -> { denService.listar(); MenuUtil.pausar(scanner); }
                case 2 -> denService.vistoriar(scanner);
            }
        } while (op != 0);
    }

    void run() {
        int op;
        do {
            op = MenuUtil.exibir(scanner, "SISTEMA SALUS - PREFEITURA DE LINS",
                    "Cidadao", "Gestor", "Administrador");

            switch (op) {
                case 1 -> menuCidadao();
                case 2 -> { if (funcService.loginGestor(scanner)) menuGestor(); }
                case 3 -> { if (funcService.loginAdmin(scanner)) menuAdmin(); }
            }
        } while (op != 0);
        System.out.println("Ate logo!");
    }

    public static void main(String[] args) {
        SistemaSalus sistema = new SistemaSalus();
        sistema.run();
    }
}