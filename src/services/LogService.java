package services;

import models.Logs;
import models.Usuario;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogService {
    private List<Logs> logs = new ArrayList<>();
    private int proximoId = 1;

    public void registrar(Usuario usuario, String acao, String ip) {
        Logs log = new Logs();
        log.setIdLog(proximoId++);
        log.setUsuario(usuario);
        log.setAcaoRealizada(acao);
        log.setIpOrigem(ip != null ? ip : "127.0.0.1");
        log.setTimestamp(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        logs.add(log);
    }

    public void listar() {
        System.out.println("\n=== LOG DO SISTEMA ===");
        if (logs.isEmpty()) { System.out.println("Nenhum registro encontrado."); return; }
        for (Logs l : logs) {
            String nomeUsuario = l.getUsuario() != null ? l.getUsuario().getNome() : "Sistema";
            System.out.printf("[%d] %s | Usuário: %-20s | Ação: %-30s | IP: %s%n",
                    l.getIdLog(), l.getTimestamp(), nomeUsuario,
                    l.getAcaoRealizada(), l.getIpOrigem());
        }
    }
}
