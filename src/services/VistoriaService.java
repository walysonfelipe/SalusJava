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
    private int proximoId     = 1;
    private int proximoItemId = 1;

    public VistoriaService(Scanner sc) {
        this.sc = sc;
    }

    public void criarVistoria(Gestor gestor, List<Denuncia> denunciasPendentes) {
        System.out.println("\n=== CRIAR LOTE DE VISTORIA ===");

        if (denunciasPendentes.isEmpty()) {
            System.out.println("Nao ha denuncias pendentes para vistoriar.");
            return;
        }

        System.out.println("Denuncias pendentes:");
        for (Denuncia d : denunciasPendentes) {
            System.out.printf("  [%d] %s — %s%n",
                    d.getIdDenuncia(), d.getProtocoloEletronico(), d.getEnderecoCompleto());
        }

        System.out.print("Tipo de acao fiscal (ex: Visita, Nebulizacao): ");
        String tipo = sc.nextLine().trim();

        System.out.print("Data programada (dd/MM/yyyy): ");
        String dataProg = sc.nextLine().trim();

        VistoriaLote lote = new VistoriaLote();
        lote.setIdVistoriaLote(proximoId++);
        lote.setGestor(gestor);
        lote.setTipoAcaoFiscal(tipo);
        lote.setDataProgramada(dataProg);

        System.out.print("IDs das denuncias a incluir (separados por virgula): ");
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
        System.out.println("Lote criado! ID: " + lote.getIdVistoriaLote()
                + " | Denuncias incluidas: " + lote.getItens().size());
    }

    public void vincularFiscal(int idLote, Fiscal fiscal) {
        VistoriaLote lote = buscarPorId(idLote);
        if (lote == null) { System.out.println("Lote nao encontrado."); return; }
        lote.setFiscal(fiscal);
        System.out.println("Fiscal " + fiscal.getNome() + " atribuido ao lote " + idLote + ".");
    }

    public void registrarVisita(Fiscal fiscal) {
        System.out.println("\n=== REGISTRAR VISITA ===");

        List<VistoriaLote> lotesFiscal = buscarLotesPorFiscal(fiscal);
        if (lotesFiscal.isEmpty()) {
            System.out.println("Nenhum lote atribuido a voce.");
            return;
        }

        System.out.println("Seus lotes:");
        for (VistoriaLote l : lotesFiscal) {
            System.out.printf("  [%d] Tipo: %-15s | Programado: %-12s | Realizado: %-10s | Itens: %d%n",
                    l.getIdVistoriaLote(), l.getTipoAcaoFiscal(), l.getDataProgramada(),
                    l.getDataRealizacao() != null ? l.getDataRealizacao() : "-",
                    l.getItens().size());
        }

        System.out.print("ID do lote a registrar: ");
        int idLote = lerInt();
        VistoriaLote lote = buscarPorId(idLote);
        if (lote == null || lote.getFiscal() == null || lote.getFiscal().getId() != fiscal.getId()) {
            System.out.println("Lote nao encontrado ou nao atribuido a voce.");
            return;
        }

        System.out.print("Relatorio detalhado: ");
        lote.setRelatorioDetalhado(sc.nextLine().trim());

        System.out.print("Quantidade de focos encontrados: ");
        lote.setFocosEncontrados(lerInt());

        System.out.print("Acoes tomadas: ");
        lote.setAcoesTomadas(sc.nextLine().trim());

        System.out.print("Observacoes: ");
        lote.setObservacoes(sc.nextLine().trim());

        lote.setDataRealizacao(LocalDate.now().toString());

        for (VistoriaLoteDenuncia item : lote.getItens()) {
            item.getDenuncia().setStatus(StatusDenuncia.FINALIZADA);
        }

        System.out.println("Visita registrada! " + lote.getItens().size()
                + " denuncia(s) finalizadas.");
    }

    public void listarLotes() {
        System.out.println("\n=== LOTES DE VISTORIA ===");
        if (lotes.isEmpty()) { System.out.println("Nenhum lote criado."); return; }
        for (VistoriaLote l : lotes) {
            String fiscal  = l.getFiscal()  != null ? l.getFiscal().getNome()  : "Nao atribuido";
            String gestor  = l.getGestor()  != null ? l.getGestor().getNome()  : "-";
            String realiz  = l.getDataRealizacao() != null ? l.getDataRealizacao() : "Pendente";
            System.out.printf("[%d] %-15s | Prog: %-12s | Realiz: %-12s | Gestor: %-20s | Fiscal: %-20s | Itens: %d%n",
                    l.getIdVistoriaLote(), l.getTipoAcaoFiscal(), l.getDataProgramada(),
                    realiz, gestor, fiscal, l.getItens().size());
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
            if (l.getFiscal() != null && l.getFiscal().getId() == fiscal.getId())
                resultado.add(l);
        }
        return resultado;
    }

    private int lerInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("Digite um numero valido: "); }
        }
    }
}
