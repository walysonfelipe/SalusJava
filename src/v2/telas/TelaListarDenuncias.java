package v2.telas;

import javax.swing.*;
import java.awt.*;

import models.Denuncia;
import v2.DenunciaServiceV2;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Tela dinâmica para listar todas as denúncias.
 */
public class TelaListarDenuncias extends JPanel {

    private final Navegador navegador;
    private final DenunciaServiceV2 denService;
    private String voltarCard = "gestor";

    public TelaListarDenuncias(Navegador navegador, DenunciaServiceV2 denService) {
        this.navegador = navegador;
        this.denService = denService;
    }

    /** Reconstrói a lista e define o card de retorno. */
    public void construir(String voltarCard) {
        this.voltarCard = voltarCard;
        removeAll();
        setLayout(new BorderLayout());
        setBackground(COR_FUNDO);

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topo.setBackground(COR_FUNDO);
        topo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        topo.add(titulo("LISTA DE DENÚNCIAS"));

        Denuncia[] lista = denService.listar();
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBackground(COR_AREA);
        area.setForeground(Color.WHITE);
        area.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        if (lista.length == 0) {
            area.setText("  Nenhuma denúncia registrada.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lista.length; i++) {
                Denuncia d = lista[i];
                sb.append("══════════════════════════════════════════════════════\n");
                sb.append(String.format("[%d]  %s  →  %s  |  %s\n", i + 1, d.descricao, d.status, d.dataHora));
                sb.append(String.format("     Nome: %s | E-mail: %s | Tel: %s\n", d.nome, d.email, d.telefone));
                sb.append(String.format("     Local: %s\n", d.local));
                if (d.observacao != null && !d.observacao.isEmpty()) {
                    sb.append(String.format("     Vistoria: %s | Obs: %s\n", d.dataHoraVistoria, d.observacao));
                }
            }
            area.setText(sb.toString());
            area.setCaretPosition(0);
        }

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBackground(COR_FUNDO);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        JButton btnVoltar = botaoVoltar();
        btnVoltar.addActionListener(e -> navegador.irPara(this.voltarCard));
        rodape.add(btnVoltar);

        add(topo,   BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
}
