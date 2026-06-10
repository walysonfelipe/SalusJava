package menus;

import models.Cidadao;
import models.Denuncia;
import services.CidadaoService;
import services.DenunciaService;
import services.LogService;

import java.util.List;
import java.util.Scanner;

public class MenuArquivo extends MenuBase {

    private final CidadaoService  cidadaoService;
    private final DenunciaService denunciaService;
    private final LogService      logService;

    public MenuArquivo(Scanner sc, CidadaoService cidadaoService,
                       DenunciaService denunciaService, LogService logService) {
        super(sc);
        this.cidadaoService  = cidadaoService;
        this.denunciaService = denunciaService;
        this.logService      = logService;
    }

    public void cadastrarCidadao() {
        Cidadao c = cidadaoService.cadastrar();
        if (c != null) logService.registrar(c, "CADASTRO_CIDADAO", null);
    }

    public void cadastrarDenuncia() {
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

    public void salvar() {
        System.out.println("\n=== SALVAR DADOS EM ARQUIVO ===");
        cidadaoService.salvar();
        denunciaService.salvar();
    }

    public void lerExibir() {
        System.out.println("\n=== LER ARQUIVO E EXIBIR EM TELA ===");
        List<Cidadao>  cidadaosLidos  = cidadaoService.carregarDoArquivo();
        List<Denuncia> denunciasLidas = denunciaService.carregarDoArquivo(cidadaosLidos);
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
