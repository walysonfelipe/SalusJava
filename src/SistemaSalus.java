import java.util.Scanner;

import services.DenunciaService;
import utils.MenuUtil;
import services.FuncionarioService;

public class SistemaSalus {

    Scanner sc = new Scanner(System.in);
    DenunciaService denService = new DenunciaService();
    FuncionarioService funcService = new FuncionarioService();

    void menuCidadao() {
        int op;
        do {
            op = MenuUtil.exibir(sc, "CIDADAO", "Fazer denuncia", "Ver minhas denuncias");
            switch (op) {
                case 1 -> denService.cadastrar(sc);
                case 2 -> denService.buscarPorEmail(sc);
            }
        } while (op != 0);
    }

    void menuAdmin() {
        int op;
        do {
            op = MenuUtil.exibir(sc, "ADMINISTRADOR",
                    "Adicionar funcionario", "Listar funcionarios",
                    "Desativar funcionario", "Ativar funcionario");
            switch (op) {
                case 1 -> funcService.adicionar(sc);
                case 2 -> { funcService.listar(); MenuUtil.pausar(sc); }
                case 3 -> funcService.alterarStatus(sc, false);
                case 4 -> funcService.alterarStatus(sc, true);
            }
        } while (op != 0);
    }

    void menuGestor() {
        int op;
        do {
            denService.dashboard();
            op = MenuUtil.exibir(sc, "GESTOR", "Listar denuncias", "Vistoriar denuncia");
            switch (op) {
                case 1 -> { denService.listar(); MenuUtil.pausar(sc); }
                case 2 -> denService.vistoriar(sc);
            }
        } while (op != 0);
    }

    void run() {
        int op;
        do {
            op = MenuUtil.exibir(sc, "SISTEMA SALUS - PREFEITURA DE LINS",
                    "Cidadao", "Gestor", "Administrador");
            switch (op) {
                case 1 -> menuCidadao();
                case 2 -> { if (funcService.loginGestor(sc)) menuGestor(); }
                case 3 -> { if (funcService.loginAdmin(sc)) menuAdmin(); }
            }
        } while (op != 0);
        System.out.println("Ate logo!");
    }

    public static void main(String[] args) {
        SistemaSalus sistema = new SistemaSalus();
        sistema.run();
    }
}