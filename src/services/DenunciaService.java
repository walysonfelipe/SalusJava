package services;

import models.Cidadao;
import models.Denuncia;
import models.StatusDenuncia;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DenunciaService {
    private List<Denuncia> denuncias = new ArrayList<>();
    private Scanner sc;
    private int proximoId = 1;

    public DenunciaService(Scanner sc) {
        this.sc = sc;
    }

    public Denuncia cadastrar(Cidadao cidadao) {
        System.out.println("\n=== REGISTRAR DENÚNCIA ===");

        Denuncia d = new Denuncia();
        d.setIdDenuncia(proximoId++);
        d.setCidadao(cidadao);

        System.out.print("Endereço completo: ");
        d.setEnderecoCompleto(sc.nextLine().trim());

        System.out.print("Latitude (ex: -23.5505): ");
        d.setLatitude(lerDouble());

        System.out.print("Longitude (ex: -46.6333): ");
        d.setLongitude(lerDouble());

        System.out.print("Descrição do foco suspeito: ");
        d.setDescricao(sc.nextLine().trim());

        System.out.print("URL de foto (deixe em branco para pular): ");
        String url = sc.nextLine().trim();
        if (!url.isEmpty()) d.getMidiaUrls().add(url);

        String protocolo = gerarProtocolo(d.getIdDenuncia());
        d.setProtocoloEletronico(protocolo);
        d.setDataEnvio(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        denuncias.add(d);
        System.out.println("Denúncia registrada! Protocolo: " + protocolo);
        return d;
    }

    public void listar() {
        System.out.println("\n=== TODAS AS DENÚNCIAS ===");
        if (denuncias.isEmpty()) { System.out.println("Nenhuma denúncia registrada."); return; }
        for (Denuncia d : denuncias) {
            exibirResumo(d);
        }
    }

    public void listarPendentes() {
        System.out.println("\n=== DENÚNCIAS PENDENTES ===");
        boolean alguma = false;
        for (Denuncia d : denuncias) {
            if (d.getStatus() == StatusDenuncia.PENDENTE) {
                exibirResumo(d);
                alguma = true;
            }
        }
        if (!alguma) System.out.println("Nenhuma denúncia pendente.");
    }

    public void listarPorCidadao(Cidadao cidadao) {
        System.out.println("\n=== SUAS DENÚNCIAS ===");
        boolean alguma = false;
        for (Denuncia d : denuncias) {
            if (d.getCidadao().getId() == cidadao.getId()) {
                exibirResumo(d);
                alguma = true;
            }
        }
        if (!alguma) System.out.println("Você não possui denúncias registradas.");
    }

    public Denuncia buscarPorId(int id) {
        for (Denuncia d : denuncias) {
            if (d.getIdDenuncia() == id) return d;
        }
        return null;
    }

    public Denuncia buscarPorProtocolo(String protocolo) {
        for (Denuncia d : denuncias) {
            if (d.getProtocoloEletronico().equalsIgnoreCase(protocolo)) return d;
        }
        return null;
    }

    public void alterarStatus(int idDenuncia, StatusDenuncia novoStatus) {
        Denuncia d = buscarPorId(idDenuncia);
        if (d == null) { System.out.println("Denúncia não encontrada."); return; }
        d.setStatus(novoStatus);
        System.out.println("Status da denúncia " + d.getProtocoloEletronico() + " atualizado para: " + novoStatus);
    }

    public void dashboard() {
        System.out.println("\n========== DASHBOARD DE DENÚNCIAS ==========");
        int total = denuncias.size();
        int pendentes = 0, emAnalise = 0, atribuidas = 0, finalizadas = 0;
        for (Denuncia d : denuncias) {
            switch (d.getStatus()) {
                case PENDENTE:    pendentes++;   break;
                case EM_ANALISE:  emAnalise++;   break;
                case ATRIBUIDA:   atribuidas++;  break;
                case FINALIZADA:  finalizadas++; break;
            }
        }
        System.out.println("Total de denúncias : " + total);
        System.out.println("Pendentes          : " + pendentes);
        System.out.println("Em análise         : " + emAnalise);
        System.out.println("Atribuídas         : " + atribuidas);
        System.out.println("Finalizadas        : " + finalizadas);
        System.out.println("=============================================");
    }

    public List<Denuncia> getDenuncias() { return denuncias; }

    public List<Denuncia> getDenunciasPendentes() {
        List<Denuncia> pendentes = new ArrayList<>();
        for (Denuncia d : denuncias) {
            if (d.getStatus() == StatusDenuncia.PENDENTE) pendentes.add(d);
        }
        return pendentes;
    }

    private void exibirResumo(Denuncia d) {
        System.out.printf("[%d] Protocolo: %s | Status: %-12s | Endereço: %s | Data: %s%n",
                d.getIdDenuncia(), d.getProtocoloEletronico(), d.getStatus(),
                d.getEnderecoCompleto(), d.getDataEnvio());
    }

    private String gerarProtocolo(int id) {
        return String.format("PROT-%d-%04d", LocalDateTime.now().getYear(), id);
    }

    private double lerDouble() {
        while (true) {
            try { return Double.parseDouble(sc.nextLine().trim().replace(",", ".")); }
            catch (NumberFormatException e) { System.out.print("Valor inválido. Tente novamente: "); }
        }
    }
}
