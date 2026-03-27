package services;

import models.Denuncia;
import models.Funcionario;
import models.VistoriaLote;
import models.VistoriaLoteDenuncia;
import utils.MenuUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VistoriaService {
    private final List<VistoriaLote> lotes = new ArrayList<>();
    private int proximoIdLote = 1;
    private int proximoIdItem = 1;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public void criarVistoria(Scanner scanner, Funcionario gestor, DenunciaService denunciaService) {
        denunciaService.listarPendentes();

        System.out.print("\nID da denuncia a vistoriar (0 para cancelar): ");
        int idDenuncia = MenuUtil.lerInt(scanner);
        if (idDenuncia == 0) return;

        Denuncia denuncia = denunciaService.buscarPorId(idDenuncia);
        if (denuncia == null) {
            System.out.println("Denuncia nao encontrada!");
            return;
        }
        if (!denuncia.getStatusDenuncia().equals("PENDENTE")) {
            System.out.println("Essa denuncia ja foi vistoriada!");
            return;
        }

        System.out.println("\n--- NOVA VISTORIA ---");
        VistoriaLote lote = new VistoriaLote();
        lote.setIdVistoriaLote(proximoIdLote++);
        lote.setFuncionario(gestor);
        lote.setDenuncia(denuncia);
        lote.setDataVistoria(LocalDateTime.now().format(fmt));

        System.out.print("Tipo de acao fiscal (NOTIFICACAO/AUTUACAO/MULTA): ");
        lote.setTipoAcaoFiscal(scanner.nextLine());
        System.out.print("Descricao geral da vistoria: ");
        lote.setDescricaoVistoria(scanner.nextLine());

        VistoriaLoteDenuncia item = new VistoriaLoteDenuncia();
        item.setIdVistoriaLoteDenuncia(proximoIdItem++);
        item.setIdVistoriaLote(lote.getIdVistoriaLote());
        item.setDenuncia(denuncia);
        item.setFuncionarioFiscal(gestor);

        System.out.print("Tipo de imovel: ");
        item.setTipoImovel(scanner.nextLine());
        System.out.print("Moradores: ");
        item.setMoradores(scanner.nextLine());
        System.out.print("Acessibilidade: ");
        item.setAcessibilidade(scanner.nextLine());
        System.out.print("Tipo de reservatorio: ");
        item.setTipoReserv(scanner.nextLine());
        System.out.print("Condicao do reservatorio: ");
        item.setCondicReserv(scanner.nextLine());
        System.out.print("Foto/evidencia (caminho ou descricao): ");
        item.setFotoEvidencia(scanner.nextLine());

        System.out.print("Procedencia constatada? (1-Sim / 2-Nao): ");
        int proc = MenuUtil.lerInt(scanner);
        item.setConstatadaProcedencia(proc == 1 ? "PROCEDENTE" : "IMPROCEDENTE");
        item.setStatusDenunciaPosVistoria(proc == 1 ? "VERIDICO" : "FALSA");

        denuncia.setStatusDenuncia(item.getStatusDenunciaPosVistoria());
        denuncia.getVistorias().add(item);

        lote.getItens().add(item);
        lotes.add(lote);

        System.out.println("Vistoria registrada com sucesso!");
        System.out.println("Denuncia " + denuncia.getProtocolo() + " atualizada para: " + denuncia.getStatusDenuncia());
        MenuUtil.pausar(scanner);
    }

    public void listarLotes(Scanner scanner) {
        if (lotes.isEmpty()) {
            System.out.println("Nenhuma vistoria registrada.");
            MenuUtil.pausar(scanner);
            return;
        }
        System.out.println("\n--- VISTORIAS REALIZADAS ---");
        for (VistoriaLote lote : lotes) {
            System.out.printf("[%d] %s | Fiscal: %s | Tipo: %s%n",
                lote.getIdVistoriaLote(),
                lote.getDataVistoria(),
                lote.getFuncionario().getNomeFuncionario(),
                lote.getTipoAcaoFiscal());
            System.out.println("     Descricao: " + lote.getDescricaoVistoria());
            for (VistoriaLoteDenuncia item : lote.getItens()) {
                System.out.printf("     -> Denuncia [%d] %s | Imovel: %s | %s | %s%n",
                    item.getDenuncia().getIdDenuncia(),
                    item.getDenuncia().getProtocolo(),
                    item.getTipoImovel(),
                    item.getConstatadaProcedencia(),
                    item.getStatusDenunciaPosVistoria());
            }
            System.out.println();
        }
        MenuUtil.pausar(scanner);
    }
}
