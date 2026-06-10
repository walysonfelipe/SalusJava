package menus;

import models.Cidadao;
import models.Denuncia;
import services.CidadaoService;
import services.DenunciaService;
import services.LogService;

import java.util.Scanner;

public class MenuCidadao extends MenuBase {
    private final CidadaoService cidadaoService;
    private final DenunciaService denunciaService;
    private final LogService logService;

    public MenuCidadao(Scanner sc, CidadaoService cidadaoService,
                       DenunciaService denunciaService, LogService logService) {
        super(sc);
        this.cidadaoService  = cidadaoService;
        this.denunciaService = denunciaService;
        this.logService      = logService;
    }

    public void abrir() {
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
                    menuLogado(novo);
                }
                break;
            case 2:
                Cidadao logado = cidadaoService.login();
                if (logado != null) {
                    logService.registrar(logado, "LOGIN_CIDADAO", null);
                    menuLogado(logado);
                }
                break;
            case 0: break;
            default: System.out.println("Opcao invalida.");
        }
    }

    private void menuLogado(Cidadao cidadao) {
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

    private void consultarProtocolo() {
        System.out.print("\nInforme o numero do protocolo: ");
        String protocolo = sc.nextLine().trim();

        Denuncia d = denunciaService.buscarPorProtocolo(protocolo);
        if (d == null) { System.out.println("Protocolo nao encontrado."); return; }

        String nome = d.getCidadao() != null ? d.getCidadao().getNome() : "Desconhecido";
        System.out.println("\n========== DENUNCIA ==========");
        System.out.println("Protocolo : " + d.getProtocoloEletronico());
        System.out.println("Status    : " + d.getStatus());
        System.out.println("Cidadao   : " + nome);
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
}
