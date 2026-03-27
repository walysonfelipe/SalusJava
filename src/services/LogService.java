package services;

import models.Admin;
import models.Cidadao;
import models.Funcionario;
import models.Logs;
import utils.MenuUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LogService {
    private final List<Logs> logs = new ArrayList<>();
    private int proximoId = 1;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public void registrar(Cidadao cidadao, Funcionario funcionario, Admin admin,
                          String tipoUsuario, String descricao, String ip) {
        Logs log = new Logs();
        log.setIdLog(proximoId++);
        log.setCidadao(cidadao);
        log.setFuncionario(funcionario);
        log.setAdmin(admin);
        log.setTipoUsuario(tipoUsuario);
        log.setDescricaoAcao(descricao);
        log.setIpOrigemAcao(ip != null ? ip : "127.0.0.1");
        log.setDataLogAcao(LocalDateTime.now().format(fmt));
        logs.add(log);
    }

    public void listar(Scanner scanner) {
        if (logs.isEmpty()) {
            System.out.println("Nenhum log registrado.");
            MenuUtil.pausar(scanner);
            return;
        }
        System.out.println("\n--- LOGS DO SISTEMA ---");
        for (Logs log : logs) {
            String usuario;
            if (log.getCidadao() != null) {
                usuario = log.getCidadao().getNomeCidadao();
            } else if (log.getFuncionario() != null) {
                usuario = log.getFuncionario().getNomeFuncionario();
            } else if (log.getAdmin() != null) {
                usuario = log.getAdmin().getNomeAdmin();
            } else {
                usuario = "Sistema";
            }
            System.out.printf("[%d] %s | %-8s | %-20s | %s | IP: %s%n",
                log.getIdLog(),
                log.getDataLogAcao(),
                log.getTipoUsuario(),
                usuario,
                log.getDescricaoAcao(),
                log.getIpOrigemAcao());
        }
        MenuUtil.pausar(scanner);
    }
}
