import models.Admin;
import models.Cidadao;
import models.Funcionario;
import services.*;
import utils.MenuUtil;

import java.util.Scanner;

public class SistemaSalus {

    private final Scanner scanner = new Scanner(System.in);
    private final CidadaoService cidadaoService = new CidadaoService();
    private final AdminService adminService = new AdminService();
    private final FuncionarioService funcService = new FuncionarioService();
    private final DenunciaService denService = new DenunciaService();
    private final VistoriaService vistoriaService = new VistoriaService();
    private final LogService logService = new LogService();

    private void menuCidadao(Cidadao cidadao) {
        int op;
        do {
            op = MenuUtil.exibir(scanner, "CIDADAO - " + cidadao.getNomeCidadao(),
                    "Fazer denuncia",
                    "Ver minhas denuncias");
            switch (op) {
                case 1 -> {
                    denService.cadastrar(scanner, cidadao);
                    logService.registrar(cidadao, null, null, "CIDADAO", "Nova denuncia registrada", null);
                }
                case 2 -> denService.buscarPorCidadao(cidadao, scanner);
            }
        } while (op != 0);
    }

    private void menuPreCidadao() {
        int op;
        do {
            op = MenuUtil.exibir(scanner, "AREA DO CIDADAO", "Cadastrar", "Entrar");
            switch (op) {
                case 1 -> {
                    Cidadao c = cidadaoService.cadastrar(scanner);
                    logService.registrar(c, null, null, "CIDADAO", "Novo cadastro de cidadao", null);
                    menuCidadao(c);
                }
                case 2 -> {
                    Cidadao c = cidadaoService.login(scanner);
                    if (c != null) {
                        logService.registrar(c, null, null, "CIDADAO", "Login de cidadao", null);
                        menuCidadao(c);
                    }
                }
            }
        } while (op != 0);
    }

    private void menuAdmin(Admin admin) {
        int op;
        do {
            op = MenuUtil.exibir(scanner, "ADMINISTRADOR - " + admin.getNomeAdmin(),
                    "Adicionar funcionario",
                    "Listar funcionarios",
                    "Desativar funcionario",
                    "Ativar funcionario",
                    "Listar cidadaos",
                    "Ver logs do sistema");
            switch (op) {
                case 1 -> {
                    funcService.adicionar(scanner);
                    logService.registrar(null, null, admin, "ADMIN",
                        "Novo funcionario adicionado por " + admin.getEmailAdmin(), null);
                }
                case 2 -> { funcService.listar(); MenuUtil.pausar(scanner); }
                case 3 -> {
                    funcService.alterarStatus(scanner, false);
                    logService.registrar(null, null, admin, "ADMIN",
                        "Funcionario desativado por " + admin.getEmailAdmin(), null);
                }
                case 4 -> {
                    funcService.alterarStatus(scanner, true);
                    logService.registrar(null, null, admin, "ADMIN",
                        "Funcionario ativado por " + admin.getEmailAdmin(), null);
                }
                case 5 -> { cidadaoService.listar(); MenuUtil.pausar(scanner); }
                case 6 -> logService.listar(scanner);
            }
        } while (op != 0);
    }

    private void menuGestor(Funcionario gestor) {
        int op;
        do {
            denService.dashboard();
            op = MenuUtil.exibir(scanner, "GESTOR - " + gestor.getNomeFuncionario(),
                    "Listar todas as denuncias",
                    "Realizar vistoria",
                    "Ver vistorias realizadas");
            switch (op) {
                case 1 -> { denService.listar(); MenuUtil.pausar(scanner); }
                case 2 -> {
                    vistoriaService.criarVistoria(scanner, gestor, denService);
                    logService.registrar(null, gestor, null, "GESTOR", "Vistoria realizada", null);
                }
                case 3 -> vistoriaService.listarLotes(scanner);
            }
        } while (op != 0);
    }

    private void run() {
        int op;
        do {
            op = MenuUtil.exibir(scanner, "SISTEMA SALUS - PREFEITURA DE LINS",
                    "Cidadao", "Gestor", "Administrador");
            switch (op) {
                case 1 -> menuPreCidadao();
                case 2 -> {
                    Funcionario g = funcService.loginGestor(scanner);
                    if (g != null) {
                        logService.registrar(null, g, null, "GESTOR", "Login de gestor", null);
                        menuGestor(g);
                    }
                }
                case 3 -> {
                    Admin a = adminService.login(scanner);
                    if (a != null) {
                        logService.registrar(null, null, a, "ADMIN",
                            "Login de admin: " + a.getEmailAdmin(), null);
                        menuAdmin(a);
                    }
                }
            }
        } while (op != 0);
        System.out.println("Ate logo!");
    }

    public static void main(String[] args) {
        SistemaSalus sistema = new SistemaSalus();
        sistema.run();
    }
}
