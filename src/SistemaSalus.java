import models.*;
import services.*;
import java.util.Scanner;

public class SistemaSalus {

    private Scanner scanner = new Scanner(System.in);
    private CidadaoService cidadaoService = new CidadaoService(scanner);
    private AdminService adminService = new AdminService(scanner);
    private FuncionarioService funcService = new FuncionarioService(scanner);
    private DenunciaService denService = new DenunciaService(scanner);
    private VistoriaService vistoriaService = new VistoriaService(scanner);
    private LogService logService = new LogService();
    private RelatorioService relatorioService = new RelatorioService(scanner);

    // ------------------------------------------------------------------ run
    private void run() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       SISTEMA SALUS — Dengue         ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean rodando = true;
        while (rodando) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Área do Cidadão");
            System.out.println("2. Área do Gestor");
            System.out.println("3. Área do Fiscal / Agente");
            System.out.println("4. Área do Administrador");
            System.out.println("0. Sair");
            System.out.print("Opção: ");

            switch (lerInt()) {
                case 1: menuPreCidadao(); break;
                case 2: acessoGestor();   break;
                case 3: acessoFiscal();   break;
                case 4: acessoAdmin();    break;
                case 0: rodando = false;  break;
                default: System.out.println("Opção inválida.");
            }
        }
        System.out.println("Sistema encerrado. Até logo!");
    }

    // -------------------------------------------------------- acesso inicial
    private void menuPreCidadao() {
        System.out.println("\n=== ÁREA DO CIDADÃO ===");
        System.out.println("1. Cadastrar");
        System.out.println("2. Login");
        System.out.println("0. Voltar");
        System.out.print("Opção: ");

        switch (lerInt()) {
            case 1:
                Cidadao novo = cidadaoService.cadastrar();
                if (novo != null) {
                    logService.registrar(novo, "CADASTRO_CIDADAO", null);
                    menuCidadao(novo);
                }
                break;
            case 2:
                Cidadao logado = cidadaoService.login();
                if (logado != null) {
                    logService.registrar(logado, "LOGIN_CIDADAO", null);
                    menuCidadao(logado);
                }
                break;
            case 0: break;
            default: System.out.println("Opção inválida.");
        }
    }

    private void acessoGestor() {
        Gestor gestor = funcService.loginGestor();
        if (gestor != null) {
            logService.registrar(gestor, "LOGIN_GESTOR", null);
            menuGestor(gestor);
        }
    }

    private void acessoFiscal() {
        Fiscal fiscal = funcService.loginFiscal();
        if (fiscal != null) {
            logService.registrar(fiscal, "LOGIN_FISCAL", null);
            menuFiscal(fiscal);
        }
    }

    private void acessoAdmin() {
        Admin admin = adminService.login();
        if (admin != null) {
            logService.registrar(admin, "LOGIN_ADMIN", null);
            menuAdmin(admin);
        }
    }

    // --------------------------------------------------------- menu cidadão
    private void menuCidadao(Cidadao cidadao) {
        boolean ativo = true;
        while (ativo) {
            System.out.println("\n=== MENU CIDADÃO — " + cidadao.getNome() + " ===");
            System.out.println("1. Registrar denúncia");
            System.out.println("2. Minhas denúncias");
            System.out.println("3. Consultar protocolo");
            System.out.println("0. Sair");
            System.out.print("Opção: ");

            switch (lerInt()) {
                case 1:
                    Denuncia d = denService.cadastrar(cidadao);
                    if (d != null) logService.registrar(cidadao, "CADASTRO_DENUNCIA:" + d.getProtocoloEletronico(), null);
                    break;
                case 2:
                    denService.listarPorCidadao(cidadao);
                    break;
                case 3:
                    System.out.print("Informe o protocolo: ");
                    String prot = scanner.nextLine().trim();
                    Denuncia encontrada = denService.buscarPorProtocolo(prot);
                    if (encontrada != null) {
                        System.out.println("Protocolo : " + encontrada.getProtocoloEletronico());
                        System.out.println("Status    : " + encontrada.getStatus());
                        System.out.println("Endereço  : " + encontrada.getEnderecoCompleto());
                        System.out.println("Data envio: " + encontrada.getDataEnvio());
                    } else {
                        System.out.println("Protocolo não encontrado.");
                    }
                    break;
                case 0:
                    logService.registrar(cidadao, "LOGOUT_CIDADAO", null);
                    ativo = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    // ---------------------------------------------------------- menu gestor
    private void menuGestor(Gestor gestor) {
        boolean ativo = true;
        while (ativo) {
            System.out.println("\n=== MENU GESTOR — " + gestor.getNome() + " ===");
            System.out.println("1.  Listar denúncias pendentes");
            System.out.println("2.  Criar lote de vistoria");
            System.out.println("3.  Atribuir fiscal a lote");
            System.out.println("4.  Listar lotes de vistoria");
            System.out.println("5.  Dashboard de denúncias");
            System.out.println("6.  Gerar relatório epidemiológico");
            System.out.println("7.  Listar relatórios");
            System.out.println("8.  Cadastrar fiscal");
            System.out.println("9.  Listar fiscais");
            System.out.println("0.  Sair");
            System.out.print("Opção: ");

            switch (lerInt()) {
                case 1:
                    denService.listarPendentes();
                    break;
                case 2:
                    vistoriaService.criarVistoria(gestor, denService.getDenunciasPendentes());
                    logService.registrar(gestor, "CRIACAO_LOTE_VISTORIA", null);
                    break;
                case 3:
                    funcService.listarFiscais();
                    vistoriaService.listarLotes();
                    System.out.print("ID do lote: ");
                    int idLote = lerInt();
                    System.out.print("ID do fiscal: ");
                    int idFiscal = lerInt();
                    Fiscal fiscal = funcService.buscarFiscalPorId(idFiscal);
                    if (fiscal != null) {
                        vistoriaService.vincularFiscal(idLote, fiscal);
                        logService.registrar(gestor, "ATRIBUICAO_FISCAL:LOTE-" + idLote, null);
                    } else {
                        System.out.println("Fiscal não encontrado.");
                    }
                    break;
                case 4:
                    vistoriaService.listarLotes();
                    break;
                case 5:
                    denService.dashboard();
                    break;
                case 6:
                    RelatorioEpidemiologico rel = relatorioService.gerar(
                            denService.getDenuncias(), vistoriaService.getLotes(), gestor);
                    logService.registrar(gestor, "GERACAO_RELATORIO", null);
                    break;
                case 7:
                    relatorioService.listar();
                    break;
                case 8:
                    funcService.adicionarFiscal();
                    logService.registrar(gestor, "CADASTRO_FISCAL", null);
                    break;
                case 9:
                    funcService.listarFiscais();
                    break;
                case 0:
                    logService.registrar(gestor, "LOGOUT_GESTOR", null);
                    ativo = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    // ---------------------------------------------------------- menu fiscal
    private void menuFiscal(Fiscal fiscal) {
        boolean ativo = true;
        while (ativo) {
            System.out.println("\n=== MENU FISCAL — " + fiscal.getNome() + " ===");
            System.out.println("1. Registrar visita");
            System.out.println("2. Ver lotes atribuídos");
            System.out.println("0. Sair");
            System.out.print("Opção: ");

            switch (lerInt()) {
                case 1:
                    vistoriaService.registrarVisita(fiscal);
                    logService.registrar(fiscal, "REGISTRO_VISITA", null);
                    break;
                case 2:
                    vistoriaService.listarLotes();
                    break;
                case 0:
                    logService.registrar(fiscal, "LOGOUT_FISCAL", null);
                    ativo = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    // ---------------------------------------------------------- menu admin
    private void menuAdmin(Admin admin) {
        boolean ativo = true;
        while (ativo) {
            System.out.println("\n=== MENU ADMINISTRADOR — " + admin.getNome() + " ===");
            System.out.println("1. Listar cidadãos");
            System.out.println("2. Cadastrar gestor");
            System.out.println("3. Listar gestores");
            System.out.println("4. Listar fiscais");
            System.out.println("5. Alterar status de funcionário");
            System.out.println("6. Ver logs do sistema");
            System.out.println("7. Dashboard de denúncias");
            System.out.println("8. Listar todas as denúncias");
            System.out.println("0. Sair");
            System.out.print("Opção: ");

            switch (lerInt()) {
                case 1: cidadaoService.listar();                              break;
                case 2:
                    funcService.adicionarGestor();
                    logService.registrar(admin, "CADASTRO_GESTOR", null);
                    break;
                case 3: funcService.listarGestores();                         break;
                case 4: funcService.listarFiscais();                          break;
                case 5:
                    funcService.alterarStatus();
                    logService.registrar(admin, "ALTERACAO_STATUS_FUNCIONARIO", null);
                    break;
                case 6: logService.listar();                                  break;
                case 7: denService.dashboard();                               break;
                case 8: denService.listar();                                  break;
                case 0:
                    logService.registrar(admin, "LOGOUT_ADMIN", null);
                    ativo = false;
                    break;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    // ------------------------------------------------------- utilitários
    private int lerInt() {
        while (true) {
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("Digite um número válido: "); }
        }
    }

    public static void main(String[] args) {
        new SistemaSalus().run();
    }
}
