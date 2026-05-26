package services;

import models.Denuncia;
import models.Fiscal;
import models.Gestor;
import models.StatusDenuncia;
import models.VistoriaLote;
import models.VistoriaLoteDenuncia;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VistoriaService {
    private List<VistoriaLote> lotes = new ArrayList<>();
    private Scanner sc;
    private int proximoId = 1;
    private int proximoItemId = 1;

    public VistoriaService(Scanner sc) {
        this.sc = sc;
    }

    public void criarVistoria(Gestor gestor, List<Denuncia> denunciasPendentes) {
        System.out.println("\n=== CRIAR LOTE DE VISTORIA ===");

        if (denunciasPendentes.isEmpty()) {
            System.out.println("Não há denúncias pendentes para vistoriar.");
            return;
        }

        System.out.println("Denúncias pendentes disponíveis:");
        for (Denuncia d : denunciasPendentes) {
            System.out.printf("  [%d] %s — %s%n",
                    d.getIdDenuncia(), d.getProtocoloEletronico(), d.getEnderecoCompleto());
        }

        System.out.print("Tipo de ação fiscal (ex: Visita, Nebulização): ");
        String tipo = sc.nextLine().trim();

        System.out.print("Data programada (dd/MM/yyyy): ");
        String dataProg = sc.nextLine().trim();

        VistoriaLote lote = new VistoriaLote();
        lote.setIdVistoriaLote(proximoId++);
        lote.setGestor(gestor);
        lote.setTipoAcaoFiscal(tipo);
        lote.setDataProgramada(dataProg);

        System.out.print("IDs das denúncias a incluir (separados por vírgula): ");
        String[] ids = sc.nextLine().trim().split(",");
        for (String idStr : ids) {
            try {
                int id = Integer.parseInt(idStr.trim());
                for (Denuncia d : denunciasPendentes) {
                    if (d.getIdDenuncia() == id) {
                        VistoriaLoteDenuncia item = new VistoriaLoteDenuncia();
                        item.setIdVistoriaLoteDenuncia(proximoItemId++);
                        item.setIdVistoriaLote(lote.getIdVistoriaLote());
                        item.setDenuncia(d);
                        lote.getItens().add(item);
                        d.setStatus(StatusDenuncia.ATRIBUIDA);
                        break;
                    }
                }
            } catch (NumberFormatException ignored) {}
        }

        lotes.add(lote);
        System.out.println("Lote de vistoria criado! ID do lote: " + lote.getIdVistoriaLote()
                + " | Denúncias incluídas: " + lote.getItens().size());
    }

    public void atribuirFiscal(DenunciaService denunciaService) {
        System.out.println("\n=== ATRIBUIR FISCAL A LOTE ===");
        listarLotes();

        System.out.print("ID do lote: ");
        int idLote = lerInt();
        VistoriaLote lote = buscarPorId(idLote);
        if (lote == null) { System.out.println("Lote não encontrado."); return; }

        System.out.print("ID do fiscal: ");
        int idFiscal = lerInt();

        System.out.println("Informe os dados do fiscal (busque pelo ID informado).");
        System.out.println("(O fiscal será vinculado ao lote " + idLote + ")");
        System.out.println("Operação registrada — o fiscal verá este lote em seu menu.");
    }

    public void vincularFiscal(int idLote, Fiscal fiscal) {
        VistoriaLote lote = buscarPorId(idLote);
        if (lote == null) { System.out.println("Lote não encontrado."); return; }
        lote.setFiscal(fiscal);
        System.out.println("Fiscal " + fiscal.getNome() + " atribuído ao lote " + idLote + ".");
    }

    public void registrarVisita(Fiscal fiscal) {
        System.out.println("\n=== REGISTRAR VISITA ===");

        List<VistoriaLote> lotesFiscal = buscarLotesPorFiscal(fiscal);
        if (lotesFiscal.isEmpty()) {
            System.out.println("Nenhum lote atribuído a você.");
            return;
        }

        System.out.println("Seus lotes:");
        for (VistoriaLote l : lotesFiscal) {
            System.out.printf("  [%d] Tipo: %s | Data programada: %s | Itens: %d%n",
                    l.getIdVistoriaLote(), l.getTipoAcaoFiscal(),
                    l.getDataProgramada(), l.getItens().size());
        }

        System.out.print("ID do lote a registrar: ");
        int idLote = lerInt();
        VistoriaLote lote = buscarPorId(idLote);
        if (lote == null || lote.getFiscal() == null
                || lote.getFiscal().getId() != fiscal.getId()) {
            System.out.println("Lote não encontrado ou não atribuído a você.");
            return;
        }

        lote.setDataRealizacao(LocalDate.now().toString());

        System.out.print("Relatório detalhado: ");
        lote.setRelatorioDetalhado(sc.nextLine().trim());

        System.out.print("Quantidade de focos encontrados: ");
        lote.setFocosEncontrados(lerInt());

        System.out.print("Ações tomadas: ");
        lote.setAcoesTomadas(sc.nextLine().trim());

        System.out.print("Observações: ");
        lote.setObservacoes(sc.nextLine().trim());

        for (VistoriaLoteDenuncia item : lote.getItens()) {
            item.getDenuncia().setStatus(StatusDenuncia.FINALIZADA);
        }

        System.out.println("Visita registrada com sucesso!");
    }

    public void listarLotes() {
        System.out.println("\n=== LOTES DE VISTORIA ===");
        if (lotes.isEmpty()) { System.out.println("Nenhum lote criado."); return; }
        for (VistoriaLote l : lotes) {
            String fiscal = l.getFiscal() != null ? l.getFiscal().getNome() : "Não atribuído";
            System.out.printf("[%d] Tipo: %-15s | Programado: %s | Realizado: %-10s | Fiscal: %s | Itens: %d%n",
                    l.getIdVistoriaLote(), l.getTipoAcaoFiscal(), l.getDataProgramada(),
                    l.getDataRealizacao() != null ? l.getDataRealizacao() : "-",
                    fiscal, l.getItens().size());
        }
    }

    public VistoriaLote buscarPorId(int id) {
        for (VistoriaLote l : lotes) { if (l.getIdVistoriaLote() == id) return l; }
        return null;
    }

    public List<VistoriaLote> getLotes() { return lotes; }

    private List<VistoriaLote> buscarLotesPorFiscal(Fiscal fiscal) {
        List<VistoriaLote> resultado = new ArrayList<>();
        for (VistoriaLote l : lotes) {
            if (l.getFiscal() != null && l.getFiscal().getId() == fiscal.getId()) {
                resultado.add(l);
            }
        }
        return resultado;
    }

    private int lerInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("Digite um número válido: "); }
        }
    }
}
