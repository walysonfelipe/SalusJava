import menus.*;
import models.Cidadao;
import models.Denuncia;
import services.*;

import java.util.List;
import java.util.Scanner;

public class SistemaSalus extends MenuBase {

    private static final String ARQUIVO_CIDADAOS  = "cidadaos.txt";
    private static final String ARQUIVO_DENUNCIAS = "denuncias.txt";

    private final CidadaoService     cidadaoService;
    private final DenunciaService    denunciaService;
    private final AdminService       adminService;
    private final FuncionarioService funcService;
    private final VistoriaService    vistoriaService;
    private final RelatorioService   relatorioService;
    private final LogService         logService;

    private final MenuCidadao menuCidadao;
    private final MenuGestor  menuGestor;
    private final MenuFiscal  menuFiscal;
    private final MenuAdmin   menuAdmin;

    public SistemaSalus() {
        super(new Scanner(System.in));
        cidadaoService   = new CidadaoService(sc);
        denunciaService  = new DenunciaService(sc);
        adminService     = new AdminService(sc);
        funcService      = new FuncionarioService(sc);
        vistoriaService  = new VistoriaService(sc);
        relatorioService = new RelatorioService(sc);
        logService       = new LogService();

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
                case 1: cadastrarCidadao();  aguardarEnter(); break;
                case 2: cadastrarDenuncia(); aguardarEnter(); break;
                case 3: salvar();            aguardarEnter(); break;
                case 4: lerExibir();         aguardarEnter(); break;
                case 5: menuCidadao.abrir(); aguardarEnter(); break;
                case 6: menuGestor.abrir();  aguardarEnter(); break;
                case 7: menuFiscal.abrir();  aguardarEnter(); break;
                case 8: menuAdmin.abrir();   aguardarEnter(); break;
                case 0: System.out.println("Sistema encerrado. Ate logo!"); break;
                default: System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private void cadastrarCidadao() {
        Cidadao c = cidadaoService.cadastrar();
        if (c != null) logService.registrar(c, "CADASTRO_CIDADAO", null);
    }

    private void cadastrarDenuncia() {
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

    private void salvar() {
        System.out.println("\n=== SALVAR DADOS EM ARQUIVO ===");
        cidadaoService.salvar(ARQUIVO_CIDADAOS);
        denunciaService.salvar(ARQUIVO_DENUNCIAS);
    }

    private void lerExibir() {
        System.out.println("\n=== LER ARQUIVO E EXIBIR EM TELA ===");
        List<Cidadao>  cidadaosLidos  = cidadaoService.carregarDoArquivo(ARQUIVO_CIDADAOS);
        List<Denuncia> denunciasLidas = denunciaService.carregarDoArquivo(ARQUIVO_DENUNCIAS, cidadaosLidos);
        exibirCidadaos(cidadaosLidos);
        exibirDenuncias(denunciasLidas);
    }

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
}
