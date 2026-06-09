import models.*;
import services.*;

import java.util.List;
import java.util.Scanner;

public class SistemaSalus {

    private static final String ARQUIVO_CIDADAOS  = "cidadaos.txt";
    private static final String ARQUIVO_DENUNCIAS = "denuncias.txt";

    private final Scanner            scanner         = new Scanner(System.in);
    private final CidadaoService     cidadaoService  = new CidadaoService(scanner);
    private final DenunciaService    denunciaService = new DenunciaService(scanner);
    private final AdminService       adminService    = new AdminService(scanner);
    private final FuncionarioService funcService     = new FuncionarioService(scanner);
    private final VistoriaService    vistoriaService = new VistoriaService(scanner);
    private final RelatorioService   relatorioService= new RelatorioService(scanner);
    private final LogService         logService      = new LogService();

    public static void main(String[] args) {
        new SistemaSalus().executar();
    }

    private void executar() {
        System.out.println("============================================");
        System.out.println("       SISTEMA SALUS - Dengue");
        System.out.println("============================================");

        List<Cidadao> cidadaosArquivo = cidadaoService.carregarDoArquivo(ARQUIVO_CIDADAOS);
        if (!cidadaosArquivo.isEmpty()) {
            denunciaService.carregarDoArquivo(ARQUIVO_DENUNCIAS, cidadaosArquivo);
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
                case 1: opcaoCadastrarCidadao(); aguardarEnter(); break;
                case 2: opcaoCadastrarDenuncia(); aguardarEnter(); break;
                case 3: opcaoSalvar();            aguardarEnter(); break;
                case 4: opcaoLerExibir();         aguardarEnter(); break;
                case 5: menuPreCidadao();         aguardarEnter(); break;
                case 6: acessoGestor();           aguardarEnter(); break;
                case 7: acessoFiscal();           aguardarEnter(); break;
                case 8: acessoAdmin();            aguardarEnter(); break;
                case 0: System.out.println("Sistema encerrado. Ate logo!"); break;
                default: System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    // ============================================================ CIDADAO
    private void menuPreCidadao() {
        System.out.println("\n=== AREA DO CIDADAO ===");
        System.out.println("1. Cadastrar");
        System.out.println("2. Login");
        System.out.println("0. Voltar");
        System.out.print("Opcao: ");

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
            default: System.out.println("Opcao invalida.");
        }
    }

    private void menuCidadao(Cidadao cidadao) {
        int opcao;
        do {
            System.out.println("\n=== MENU CIDADAO — " + cidadao.getNome() + " ===");
            System.out.println("1. Registrar denuncia");
            System.out.println("2. Minhas denuncias");
            System.out.println("3. Consultar por protocolo");
            System.out.println("0. Sair");
            System.out.print("Opcao: ");
            opcao = lerInt();

            switch (opcao) {
                case 1:
                    Denuncia d = denunciaService.cadastrar(cidadao);
                    if (d != null) logService.registrar(cidadao, "CADASTRO_DENUNCIA:" + d.getProtocoloEletronico(), null);
                    aguardarEnter();
                    break;
                case 2:
                    denunciaService.listarPorCidadao(cidadao);
                    aguardarEnter();
                    break;
                case 3:
                    consultarProtocolo();
                    aguardarEnter();
                    break;
                case 0:
                    logService.registrar(cidadao, "LOGOUT_CIDADAO", null);
                    System.out.println("Ate logo, " + cidadao.getNome() + "!");
                    break;
                default: System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    // ============================================================ GESTOR
    private void acessoGestor() {
        Gestor gestor = funcService.loginGestor();
        if (gestor == null) return;
        logService.registrar(gestor, "LOGIN_GESTOR", null);
        menuGestor(gestor);
    }

    private void menuGestor(Gestor gestor) {
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

    // ============================================================ FISCAL
    private void acessoFiscal() {
        Fiscal fiscal = funcService.loginFiscal();
        if (fiscal == null) return;
        logService.registrar(fiscal, "LOGIN_FISCAL", null);
        menuFiscal(fiscal);
    }

    private void menuFiscal(Fiscal fiscal) {
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

    // ============================================================ ADMIN
    private void acessoAdmin() {
        Admin admin = adminService.loginAdmin();
        if (admin == null) return;
        logService.registrar(admin, "LOGIN_ADMIN", null);
        menuAdmin(admin);
    }

    private void menuAdmin(Admin admin) {
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

    // ============================================================ CADASTRO DIRETO
    private void opcaoCadastrarCidadao() {
        Cidadao c = cidadaoService.cadastrar();
        if (c != null) logService.registrar(c, "CADASTRO_CIDADAO", null);
    }

    private void opcaoCadastrarDenuncia() {
        List<Cidadao> lista = cidadaoService.getCidadaos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum cidadao cadastrado. Use a opcao 1 primeiro.");
            return;
        }
        System.out.println("\nCidadaos disponiveis:");
        for (Cidadao c : lista) {
            System.out.println("  [" + c.getId() + "] " + c.getNome());
        }
        System.out.print("ID do cidadao denunciante: ");
        Cidadao cidadao = cidadaoService.buscarPorId(lerInt());
        if (cidadao == null) { System.out.println("Cidadao nao encontrado."); return; }

        Denuncia d = denunciaService.cadastrar(cidadao);
        if (d != null) logService.registrar(cidadao, "CADASTRO_DENUNCIA:" + d.getProtocoloEletronico(), null);
    }

    // ============================================================ ARQUIVO
    private void opcaoSalvar() {
        System.out.println("\n=== SALVAR DADOS EM ARQUIVO ===");
        cidadaoService.salvar(ARQUIVO_CIDADAOS);
        denunciaService.salvar(ARQUIVO_DENUNCIAS);
    }

    private void opcaoLerExibir() {
        System.out.println("\n=== LER ARQUIVO E EXIBIR EM TELA ===");
        List<Cidadao>  cidadaosLidos  = cidadaoService.carregarDoArquivo(ARQUIVO_CIDADAOS);
        List<Denuncia> denunciasLidas = denunciaService.carregarDoArquivo(ARQUIVO_DENUNCIAS, cidadaosLidos);
        exibirCidadaos(cidadaosLidos);
        exibirDenuncias(denunciasLidas);
    }

    private void consultarProtocolo() {
        System.out.print("\nInforme o numero do protocolo: ");
        String protocolo = scanner.nextLine().trim();

        Denuncia d = denunciaService.buscarPorProtocolo(protocolo);
        if (d == null) { System.out.println("Protocolo nao encontrado."); return; }

        String cidadao = d.getCidadao() != null ? d.getCidadao().getNome() : "Desconhecido";
        System.out.println("\n========== DENUNCIA ==========");
        System.out.println("Protocolo : " + d.getProtocoloEletronico());
        System.out.println("Status    : " + d.getStatus());
        System.out.println("Cidadao   : " + cidadao);
        System.out.println("Endereco  : " + d.getEnderecoCompleto());
        System.out.println("Descricao : " + d.getDescricao());
        System.out.println("Data envio: " + d.getDataEnvio());
        System.out.println("Latitude  : " + d.getLatitude());
        System.out.println("Longitude : " + d.getLongitude());
        if (d.getGestorResponsavelNome() != null)
            System.out.println("Gestor    : " + d.getGestorResponsavelNome());
        if (d.getObservacaoVistoria() != null)
            System.out.println("Obs       : " + d.getObservacaoVistoria());
        System.out.println("==============================");
    }

    // ============================================================ EXIBICAO
    private void exibirCidadaos(List<Cidadao> lista) {
        System.out.println("\n--- CIDADAOS (" + lista.size() + " registro(s)) ---");
        if (lista.isEmpty()) { System.out.println("Nenhum registro."); return; }
        System.out.printf("%-4s %-25s %-18s %-28s %-16s %-12s%n",
                "ID", "Nome", "CPF", "E-mail", "Telefone", "Cadastro");
        System.out.println("-".repeat(105));
        for (Cidadao c : lista) {
            System.out.printf("%-4d %-25s %-18s %-28s %-16s %-12s%n",
                    c.getId(), c.getNome(), c.getCpf(),
                    c.getEmail(), c.getTelefone(), c.getDataCadastro());
        }
    }

    private void exibirDenuncias(List<Denuncia> lista) {
        System.out.println("\n--- DENUNCIAS (" + lista.size() + " registro(s)) ---");
        if (lista.isEmpty()) { System.out.println("Nenhum registro."); return; }
        for (Denuncia d : lista) {
            String nome = d.getCidadao() != null ? d.getCidadao().getNome() : "Desconhecido";
            System.out.printf("[%d] %s | %s | %s | Envio: %s%n",
                    d.getIdDenuncia(), d.getProtocoloEletronico(), d.getStatus(), nome, d.getDataEnvio());
            System.out.println("     Endereco: " + d.getEnderecoCompleto());
            if (d.getGestorResponsavelNome() != null)
                System.out.println("     Gestor  : " + d.getGestorResponsavelNome());
            if (d.getObservacaoVistoria() != null)
                System.out.println("     Obs     : " + d.getObservacaoVistoria());
            System.out.println();
        }
    }

    // ============================================================ UTILITARIOS
    private void aguardarEnter() {
        System.out.print("\nPressione ENTER para voltar ao menu...");
        scanner.nextLine();
    }

    private int lerInt() {
        while (true) {
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("Digite um numero valido: "); }
        }
    }
}
