package v2.telas;

import javax.swing.*;
import java.awt.*;

import v2.services.DenunciaServiceV2;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Painel do Gestor com dashboard e navegação.
 */
public class TelaGestor extends JPanel {

    private final Navegador navegador;
    private final DenunciaServiceV2 denService;

    public TelaGestor(Navegador navegador, DenunciaServiceV2 denService) {
        this.navegador = navegador;
        this.denService = denService;
    }

    /** Reconstrói o painel a cada navegação para atualizar o dashboard. */
    public void construir() {
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        int[] dash = denService.getDashboard();
        JLabel lblDash = new JLabel(String.format(
            "Total: %d  |  Pendentes: %d  |  Verídicas: %d  |  Falsas: %d",
            dash[0], dash[1], dash[2], dash[3]));
        lblDash.setFont(new Font("Arial", Font.BOLD, 16));
        lblDash.setForeground(new Color(255, 200, 100));
        lblDash.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnListar   = criarBotao("Listar Denúncias",   COR_BOTAO);
        JButton btnVistoria = criarBotao("Vistoriar Denúncia", COR_BOTAO);
        JButton btnVoltar   = botaoVoltar();
        btnVoltar.setText("← Sair");

        btnListar  .addActionListener(e -> navegador.irPara("listar_denuncias_gestor"));
        btnVistoria.addActionListener(e -> navegador.irPara("vistoriar"));
        btnVoltar  .addActionListener(e -> navegador.irPara("menu"));

        add(Box.createVerticalGlue());
        add(titulo("PAINEL DO GESTOR")); add(rigido(15));
        add(lblDash);                    add(rigido(40));
        add(btnListar);                  add(rigido(12));
        add(btnVistoria);                add(rigido(30));
        add(btnVoltar);
        add(Box.createVerticalGlue());

        revalidate();
        repaint();
    }
}
