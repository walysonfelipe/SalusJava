package services;

import models.Cidadao;
import models.Denuncia;
import models.StatusDenuncia;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DenunciaService {
    private static final String ARQUIVO = "denuncias.txt";

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
        cidadao.registrarDenuncia(d);
        System.out.println("Denúncia registrada! Protocolo: " + protocolo);
        return d;
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

    public void listar() {
        System.out.println("\n=== TODAS AS DENUNCIAS ===");
        if (denuncias.isEmpty()) { System.out.println("Nenhuma denuncia registrada."); return; }
        for (Denuncia d : denuncias) exibirResumo(d);
    }

    public void listarPendentes() {
        System.out.println("\n=== DENUNCIAS PENDENTES ===");
        boolean alguma = false;
        for (Denuncia d : denuncias) {
            if (d.getStatus() == StatusDenuncia.PENDENTE) { exibirResumo(d); alguma = true; }
        }
        if (!alguma) System.out.println("Nenhuma denuncia pendente.");
    }

    public void listarPorCidadao(Cidadao cidadao) {
        System.out.println("\n=== SUAS DENUNCIAS ===");
        boolean alguma = false;
        for (Denuncia d : denuncias) {
            if (d.getCidadao() != null && d.getCidadao().getId() == cidadao.getId()) {
                exibirResumo(d); alguma = true;
            }
        }
        if (!alguma) System.out.println("Voce nao possui denuncias registradas.");
    }

    public void dashboard() {
        System.out.println("\n========== DASHBOARD DE DENUNCIAS ==========");
        int total = denuncias.size();
        int pendentes = 0, emAnalise = 0, atribuidas = 0, finalizadas = 0;
        for (Denuncia d : denuncias) {
            switch (d.getStatus()) {
                case PENDENTE:   pendentes++;   break;
                case EM_ANALISE: emAnalise++;   break;
                case ATRIBUIDA:  atribuidas++;  break;
                case FINALIZADA: finalizadas++; break;
            }
        }
        System.out.println("Total        : " + total);
        System.out.println("Pendentes    : " + pendentes);
        System.out.println("Em analise   : " + emAnalise);
        System.out.println("Atribuidas   : " + atribuidas);
        System.out.println("Finalizadas  : " + finalizadas);
        System.out.println("=============================================");
    }

    private void exibirResumo(Denuncia d) {
        String nome = d.getCidadao() != null ? d.getCidadao().getNome() : "?";
        System.out.printf("[%d] %s | %-12s | %s | %s%n",
                d.getIdDenuncia(), d.getProtocoloEletronico(), d.getStatus(),
                nome, d.getEnderecoCompleto());
    }

    public List<Denuncia> getDenuncias() { return denuncias; }

    public void salvar() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            bw.write("idDenuncia;cidadaoId;enderecoCompleto;latitude;longitude;descricao;protocolo;dataEnvio;status;gestorId;gestorNome;observacao;dataHoraVistoria");
            bw.newLine();
            for (Denuncia d : denuncias) {
                int cidadaoId = d.getCidadao() != null ? d.getCidadao().getId() : 0;
                bw.write(d.getIdDenuncia()                          + ";" +
                         cidadaoId                                  + ";" +
                         esc(d.getEnderecoCompleto())               + ";" +
                         d.getLatitude()                            + ";" +
                         d.getLongitude()                           + ";" +
                         esc(d.getDescricao())                      + ";" +
                         esc(d.getProtocoloEletronico())            + ";" +
                         esc(d.getDataEnvio())                      + ";" +
                         d.getStatus()                              + ";" +
                         (d.getGestorResponsavelId() != null ? d.getGestorResponsavelId() : "") + ";" +
                         esc(d.getGestorResponsavelNome())          + ";" +
                         esc(d.getObservacaoVistoria())             + ";" +
                         esc(d.getDataHoraVistoria()));
                bw.newLine();
            }
            System.out.println("Salvo: " + ARQUIVO + " (" + denuncias.size() + " registro(s))");
        } catch (IOException e) {
            System.out.println("Erro ao salvar denuncias: " + e.getMessage());
        }
    }

    public List<Denuncia> carregarDoArquivo(List<Cidadao> cidadaosLidos) {
        List<Denuncia> lista = new ArrayList<>();
        File f = new File(ARQUIVO);
        if (!f.exists()) return lista;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            br.readLine(); // cabecalho
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] v = linha.split(";", -1);
                if (v.length < 9) continue;
                Denuncia d = new Denuncia();
                d.setIdDenuncia(Integer.parseInt(v[0].trim()));
                int cidadaoId = Integer.parseInt(v[1].trim());
                for (Cidadao c : cidadaosLidos) {
                    if (c.getId() == cidadaoId) { d.setCidadao(c); break; }
                }
                d.setEnderecoCompleto(unesc(v[2]));
                d.setLatitude(Double.parseDouble(v[3].trim()));
                d.setLongitude(Double.parseDouble(v[4].trim()));
                d.setDescricao(unesc(v[5]));
                d.setProtocoloEletronico(unesc(v[6]));
                d.setDataEnvio(unesc(v[7]));
                d.setStatus(StatusDenuncia.valueOf(v[8].trim()));
                if (v.length >= 13) {
                    String gestorIdStr = v[9].trim();
                    if (!gestorIdStr.isEmpty()) d.setGestorResponsavelId(Integer.parseInt(gestorIdStr));
                    String gestorNome = unesc(v[10]);
                    d.setGestorResponsavelNome(gestorNome.isEmpty() ? null : gestorNome);
                    String obs = unesc(v[11]);
                    d.setObservacaoVistoria(obs.isEmpty() ? null : obs);
                    String dataVistoria = unesc(v[12]);
                    d.setDataHoraVistoria(dataVistoria.isEmpty() ? null : dataVistoria);
                }
                lista.add(d);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar denuncias: " + e.getMessage());
        }
        this.denuncias = lista;
        if (!lista.isEmpty()) {
            this.proximoId = lista.get(lista.size() - 1).getIdDenuncia() + 1;
        }
        return lista;
    }

    public List<Denuncia> getDenunciasPendentes() {
        List<Denuncia> pendentes = new ArrayList<>();
        for (Denuncia d : denuncias) {
            if (d.getStatus() == StatusDenuncia.PENDENTE) pendentes.add(d);
        }
        return pendentes;
    }

    public List<Denuncia> getDenunciasPorGestor(int gestorId) {
        List<Denuncia> resultado = new ArrayList<>();
        for (Denuncia d : denuncias) {
            if (d.getGestorResponsavelId() != null && d.getGestorResponsavelId() == gestorId) resultado.add(d);
        }
        return resultado;
    }

    private static String esc(String s) {
        return s == null ? "" : s.replace(";", "{SC}").replace("\n", " ").replace("\r", "");
    }

    private static String unesc(String s) {
        return s == null ? "" : s.trim().replace("{SC}", ";");
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
