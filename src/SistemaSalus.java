import java.util.Scanner;

import services.DenunciaService;
import services.MenuUtil;
import  services.FuncionarioService;

public class SistemaSalus {

    Scanner sc = new Scanner(System.in);
    DenunciaService denService = new DenunciaService();
    FuncionarioService funcService = new FuncionarioService();

    void menuCidadao() {
        int op;
        do {
            op = MenuUtil.exibir(sc, "CIDADAO", "Fazer denuncia", "Ver minhas denuncias");
            if (op == 1) denService.cadastrar(sc);
            if (op == 2) denService.buscarPorEmail(sc);
        } while (op != 0);
    }

    void menuAdmin() {
        int op;
        do {
            op = MenuUtil.exibir(sc, "ADMINISTRADOR",
                    "Adicionar funcionario", "Listar funcionarios",
                    "Desativar funcionario", "Ativar funcionario");
            if (op == 1) funcService.adicionar(sc);
            if (op == 2) { funcService.listar(); MenuUtil.pausar(sc); }
            if (op == 3) funcService.alterarStatus(sc, false);
            if (op == 4) funcService.alterarStatus(sc, true);
        } while (op != 0);
    }

    void menuGestor() {
        int op;
        do {
            denService.dashboard();
            op = MenuUtil.exibir(sc, "GESTOR", "Listar denuncias", "Vistoriar denuncia");
            if (op == 1) { denService.listar(); MenuUtil.pausar(sc); }
            if (op == 2) denService.vistoriar(sc);
        } while (op != 0);
    }

    void run() {
        int op;
        do {
            op = MenuUtil.exibir(sc, "SISTEMA SALUS - PREFEITURA DE LINS",
                    "Cidadao", "Gestor", "Administrador");
            if (op == 1) menuCidadao();
            if (op == 2 && funcService.loginGestor(sc)) menuGestor();
            if (op == 3 && funcService.loginAdmin(sc)) menuAdmin();
        } while (op != 0);
        System.out.println("Ate logo!");
    }

    public static void main(String[] args) {
        SistemaSalus sistema = new SistemaSalus();
        sistema.run();
    }
}