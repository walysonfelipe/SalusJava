package services;

import models.Cidadao;
import models.Denuncia;
import utils.MenuUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DenunciaService {
    private final List<Denuncia> denuncias = new ArrayList<>();
    private int proximoId = 1;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private String gerarProtocolo() {
        int ano = LocalDateTime.now().getYear();
        return String.format("SALUS-%d-%04d", ano, proximoId);
    }

    public void cadastrar(Scanner scanner, Cidadao cidadao) {
        Denuncia d = new Denuncia();
        d.setIdDenuncia(proximoId);
        d.setProtocolo(gerarProtocolo());
        d.setCidadao(cidadao);
        proximoId++;

        System.out.println("\n--- NOVA DENUNCIA ---");
        System.out.print("Endereco: ");
        d.setEnderecoDenuncia(scanner.nextLine());
        System.out.print("Descricao: ");
        d.setDescricaoDenuncia(scanner.nextLine());
        System.out.print("Tipo de acao fiscal (NOTIFICACAO/AUTUACAO/VISTORIA): ");
        d.setTipoAcaoFiscal(scanner.nextLine());
        d.setDataEnvio(LocalDateTime.now().format(fmt));

        denuncias.add(d);
        System.out.println("Denuncia registrada com sucesso!");
        System.out.println("Protocolo: " + d.getProtocolo());
    }

    public void buscarPorCidadao(Cidadao cidadao, Scanner scanner) {
        System.out.println("\n--- MINHAS DENUNCIAS ---");
        boolean encontrou = false;
        for (Denuncia d : denuncias) {
            if (d.getCidadao().getIdCidadao() == cidadao.getIdCidadao()) {
                encontrou = true;
                System.out.printf("Protocolo: %-18s | Status: %-10s | Data: %s%n",
                    d.getProtocolo(), d.getStatusDenuncia(), d.getDataEnvio());
                System.out.println("  Endereco : " + d.getEnderecoDenuncia());
                System.out.println("  Descricao: " + d.getDescricaoDenuncia());
                System.out.println("  Tipo Acao: " + d.getTipoAcaoFiscal());
                System.out.println();
            }
        }
        if (!encontrou) System.out.println("Nenhuma denuncia encontrada.");
        MenuUtil.pausar(scanner);
    }

    public void listar() {
        if (denuncias.isEmpty()) {
            System.out.println("Nenhuma denuncia registrada.");
            return;
        }
        for (Denuncia d : denuncias) {
            System.out.printf("[%d] %s | %-10s | %s%n",
                d.getIdDenuncia(), d.getProtocolo(),
                d.getStatusDenuncia(), d.getDescricaoDenuncia());
        }
    }

    public void listarPendentes() {
        System.out.println("\n--- DENUNCIAS PENDENTES ---");
        boolean encontrou = false;
        for (Denuncia d : denuncias) {
            if (d.getStatusDenuncia().equals("PENDENTE")) {
                encontrou = true;
                System.out.printf("[%d] %s | %s | %s%n",
                    d.getIdDenuncia(), d.getProtocolo(),
                    d.getEnderecoDenuncia(), d.getCidadao().getNomeCidadao());
            }
        }
        if (!encontrou) System.out.println("Nenhuma denuncia pendente.");
    }

    public Denuncia buscarPorId(int id) {
        for (Denuncia d : denuncias) {
            if (d.getIdDenuncia() == id) return d;
        }
        return null;
    }

    public void dashboard() {
        long pendentes = denuncias.stream().filter(d -> d.getStatusDenuncia().equals("PENDENTE")).count();
        long veridico  = denuncias.stream().filter(d -> d.getStatusDenuncia().equals("VERIDICO")).count();
        long falsas    = denuncias.stream().filter(d -> d.getStatusDenuncia().equals("FALSA")).count();
        System.out.println("\n--- DASHBOARD ---");
        System.out.printf("Total: %d | Pendentes: %d | Verdadeiras: %d | Falsas: %d%n",
            denuncias.size(), pendentes, veridico, falsas);
    }
}
