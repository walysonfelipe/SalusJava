package services;

import models.Denuncia;
import models.RelatorioEpidemiologico;
import models.StatusDenuncia;
import models.Usuario;
import models.VistoriaLote;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RelatorioService {
    private List<RelatorioEpidemiologico> relatorios = new ArrayList<>();
    private Scanner sc;
    private int proximoId = 1;

    public RelatorioService(Scanner sc) {
        this.sc = sc;
    }

    public RelatorioEpidemiologico gerar(List<Denuncia> denuncias, List<VistoriaLote> vistorias,
                                         Usuario geradoPor) {
        System.out.println("\n=== GERAR RELATÓRIO EPIDEMIOLÓGICO ===");
        System.out.print("Período (ex: 05/2026): ");
        String periodo = sc.nextLine().trim();
        System.out.print("Região (ex: Zona Norte, Centro): ");
        String regiao = sc.nextLine().trim();

        RelatorioEpidemiologico r = new RelatorioEpidemiologico();
        r.setIdRelatorio(proximoId++);
        r.setPeriodo(periodo);
        r.setRegiao(regiao);
        r.setGeradoPor(geradoPor);
        r.setDataGeracao(LocalDate.now().toString());

        int totalDenuncias = denuncias.size();
        int finalizadas = 0;
        for (Denuncia d : denuncias) {
            if (d.getStatus() == StatusDenuncia.FINALIZADA) finalizadas++;
        }

        int totalVistorias = vistorias.size();
        int totalFocos = 0;
        int focosEliminados = 0;
        for (VistoriaLote l : vistorias) {
            totalFocos += l.getFocosEncontrados();
            if (l.getDataRealizacao() != null) focosEliminados += l.getFocosEncontrados();
        }

        r.setTotalDenuncias(totalDenuncias);
        r.setTotalVistorias(totalVistorias);
        r.setTotalFocosEncontrados(totalFocos);
        r.setTotalFocosEliminados(focosEliminados);
        r.setIndiceInfestacao(totalDenuncias > 0 ? (double) totalFocos / totalDenuncias : 0.0);

        relatorios.add(r);
        exibir(r);
        return r;
    }

    public void listar() {
        System.out.println("\n=== RELATÓRIOS GERADOS ===");
        if (relatorios.isEmpty()) { System.out.println("Nenhum relatório gerado."); return; }
        for (RelatorioEpidemiologico r : relatorios) {
            System.out.printf("[%d] Período: %s | Região: %s | Gerado em: %s | Índice: %.2f%n",
                    r.getIdRelatorio(), r.getPeriodo(), r.getRegiao(),
                    r.getDataGeracao(), r.getIndiceInfestacao());
        }
    }

    private void exibir(RelatorioEpidemiologico r) {
        System.out.println("\n========== RELATÓRIO EPIDEMIOLÓGICO ==========");
        System.out.println("ID           : " + r.getIdRelatorio());
        System.out.println("Período      : " + r.getPeriodo());
        System.out.println("Região       : " + r.getRegiao());
        System.out.println("Gerado por   : " + (r.getGeradoPor() != null ? r.getGeradoPor().getNome() : "-"));
        System.out.println("Data geração : " + r.getDataGeracao());
        System.out.println("----------------------------------------------");
        System.out.println("Total denúncias      : " + r.getTotalDenuncias());
        System.out.println("Total vistorias      : " + r.getTotalVistorias());
        System.out.println("Focos encontrados    : " + r.getTotalFocosEncontrados());
        System.out.println("Focos eliminados     : " + r.getTotalFocosEliminados());
        System.out.printf( "Índice de infestação : %.2f%n", r.getIndiceInfestacao());
        System.out.println("==============================================");
    }
}
