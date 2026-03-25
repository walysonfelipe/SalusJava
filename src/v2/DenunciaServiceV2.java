package v2;

import models.Denuncia;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DenunciaServiceV2 {

    private final Denuncia[] denuncias = new Denuncia[100];
    private int total = 0;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public String cadastrar(String nome, String email, String telefone, String local, String descricao) {
        if (total >= 100) return "Limite de denúncias atingido!";
        Denuncia d = new Denuncia();
        d.nome = nome;
        d.email = email;
        d.telefone = telefone;
        d.local = local;
        d.descricao = descricao;
        d.dataHora = LocalDateTime.now().format(formatter);
        denuncias[total++] = d;
        return "Denúncia registrada com sucesso!";
    }

    public Denuncia[] buscarPorEmail(String email) {
        int count = 0;
        for (int i = 0; i < total; i++) {
            if (denuncias[i].email.equals(email)) count++;
        }
        Denuncia[] resultado = new Denuncia[count];
        int j = 0;
        for (int i = 0; i < total; i++) {
            if (denuncias[i].email.equals(email)) resultado[j++] = denuncias[i];
        }
        return resultado;
    }

    public Denuncia[] listar() {
        Denuncia[] resultado = new Denuncia[total];
        for (int i = 0; i < total; i++) resultado[i] = denuncias[i];
        return resultado;
    }

    public String vistoriar(int indice, String observacao, boolean veridico) {
        if (indice < 0 || indice >= total) return "Denúncia inválida!";
        denuncias[indice].observacao = observacao;
        denuncias[indice].dataHoraVistoria = LocalDateTime.now().format(formatter);
        denuncias[indice].status = veridico ? "VERIDICO" : "FALSA";
        return "Vistoria registrada com sucesso!";
    }

    // Retorna [total, pendentes, veridicas, falsas]
    public int[] getDashboard() {
        int pendentes = 0, veridicas = 0, falsas = 0;
        for (int i = 0; i < total; i++) {
            switch (denuncias[i].status) {
                case "PENDENTE" -> pendentes++;
                case "VERIDICO" -> veridicas++;
                case "FALSA"    -> falsas++;
            }
        }
        return new int[]{total, pendentes, veridicas, falsas};
    }
}
