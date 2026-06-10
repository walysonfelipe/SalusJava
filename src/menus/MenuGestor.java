package menus;

import models.Fiscal;
import models.Gestor;
import services.DenunciaService;
import services.FuncionarioService;
import services.LogService;
import services.RelatorioService;
import services.VistoriaService;

import java.util.Scanner;

public class MenuGestor extends MenuBase {
    private final FuncionarioService funcService;
    private final DenunciaService    denunciaService;
    private final VistoriaService    vistoriaService;
    private final RelatorioService   relatorioService;
    private final LogService         logService;

    public MenuGestor(Scanner sc, FuncionarioService funcService,
                      DenunciaService denunciaService, VistoriaService vistoriaService,
                      RelatorioService relatorioService, LogService logService) {
        super(sc);
        this.funcService      = funcService;
        this.denunciaService  = denunciaService;
        this.vistoriaService  = vistoriaService;
        this.relatorioService = relatorioService;
        this.logService       = logService;
    }

    public void abrir() {
        Gestor gestor = funcService.loginGestor();
        if (gestor == null) return;
        logService.registrar(gestor, "LOGIN_GESTOR", null);
        menuLogado(gestor);
    }

    private void menuLogado(Gestor gestor) {
        int opcao;
        do {
            System.out.println("\n=== MENU GESTOR — " + gestor.getNome() + " ===");
            System.out.println("1.  Listar denuncias pendentes");
            System.out.println("2.  Criar lote de vistoria");
            System.out.println("3.  Atribuir fiscal a lote");
            System.out.println("4.  Listar lotes de vistoria");
            System.out.println("5.  Dashboard de denuncias");
            System.out.println("6.  Gerar relatorio epidemiologico");
            System.out.println("7.  Listar relatorios");
            System.out.println("8.  Cadastrar fiscal");
            System.out.println("9.  Listar fiscais");
            System.out.println("0.  Sair");
            System.out.print("Opcao: ");
            opcao = lerInt();

            switch (opcao) {
                case 1:
                    denunciaService.listarPendentes();
                    aguardarEnter();
                    break;
                case 2:
                    vistoriaService.criarVistoria(gestor, denunciaService.getDenunciasPendentes());
                    logService.registrar(gestor, "CRIACAO_LOTE_VISTORIA", null);
                    aguardarEnter();
                    break;
                case 3:
                    atribuirFiscalALote(gestor);
                    aguardarEnter();
                    break;
                case 4:
                    vistoriaService.listarLotes();
                    aguardarEnter();
                    break;
                case 5:
                    denunciaService.dashboard();
                    aguardarEnter();
                    break;
                case 6:
                    relatorioService.gerar(denunciaService.getDenuncias(), vistoriaService.getLotes(), gestor);
                    logService.registrar(gestor, "GERACAO_RELATORIO", null);
                    aguardarEnter();
                    break;
                case 7:
                    relatorioService.listar();
                    aguardarEnter();
                    break;
                case 8:
                    funcService.adicionarFiscal();
                    logService.registrar(gestor, "CADASTRO_FISCAL", null);
                    aguardarEnter();
                    break;
                case 9:
                    funcService.listarFiscais();
                    aguardarEnter();
                    break;
                case 0:
                    logService.registrar(gestor, "LOGOUT_GESTOR", null);
                    System.out.println("Saindo da area do gestor...");
                    break;
                default: System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private void atribuirFiscalALote(Gestor gestor) {
        funcService.listarFiscais();
        vistoriaService.listarLotes();

        if (funcService.getFiscais().isEmpty()) {
            System.out.println("Nenhum fiscal cadastrado. Cadastre um fiscal primeiro (opcao 8).");
            return;
        }

        System.out.print("ID do lote: ");
        int idLote = lerInt();
        System.out.print("ID do fiscal: ");
        int idFiscal = lerInt();

        Fiscal fiscal = funcService.buscarFiscalPorId(idFiscal);
        if (fiscal != null) {
            vistoriaService.vincularFiscal(idLote, fiscal);
            logService.registrar(gestor, "ATRIBUICAO_FISCAL:LOTE-" + idLote, null);
        } else {
            System.out.println("Fiscal nao encontrado.");
        }
    }
}
