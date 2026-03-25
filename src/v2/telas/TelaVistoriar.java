package v2.telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import models.Denuncia;
import v2.DenunciaServiceV2;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Tela dinâmica para vistoriar uma denúncia (verídico / falsa).
 */
public class TelaVistoriar extends JPanel {

    private final Navegador navegador;
    private final DenunciaServiceV2 denService;

    public TelaVistoriar(Navegador navegador, DenunciaServiceV2 denService) {
        this.navegador = navegador;
        this.denService = denService;
    }

    /** Reconstrói o formulário de vistoria. */
    public void construir() {
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        Denuncia[] lista = denService.listar();

        if (lista.length == 0) {
            JButton btnVoltar = botaoVoltar();
            btnVoltar.addActionListener(e -> navegador.irPara("gestor"));
            add(Box.createVerticalGlue());
            add(titulo("VISTORIAR DENÚNCIA")); add(rigido(20));
            add(mensagem("Nenhuma denúncia para vistoriar."));
            add(rigido(20));
            add(btnVoltar);
            add(Box.createVerticalGlue());
            revalidate(); repaint();
            return;
        }

        String[] opcoes = new String[lista.length];
        for (int i = 0; i < lista.length; i++) {
            opcoes[i] = String.format("[%d] %s  —  %s", i + 1, lista[i].descricao, lista[i].status);
        }
        JComboBox<String> combo = new JComboBox<>(opcoes);
        combo.setFont(new Font("Arial", Font.PLAIN, 14));
        combo.setMaximumSize(new Dimension(600, 40));
        combo.setPreferredSize(new Dimension(600, 40));
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea campoObs = new JTextArea(3, 30);
        campoObs.setFont(new Font("Arial", Font.PLAIN, 14));
        campoObs.setBackground(COR_CAMPO);
        campoObs.setForeground(Color.WHITE);
        campoObs.setCaretColor(Color.WHITE);
        campoObs.setLineWrap(true);
        campoObs.setWrapStyleWord(true);
        JScrollPane scrollObs = new JScrollPane(campoObs);
        scrollObs.setMaximumSize(new Dimension(500, 90));
        scrollObs.setPreferredSize(new Dimension(500, 90));
        scrollObs.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollObs.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));

        JButton btnVeridico = criarBotao("✓  Verídico", new Color(34, 139, 34));
        JButton btnFalsa    = criarBotao("✗  Falsa",    new Color(180, 30, 30));
        btnVeridico.setMaximumSize(new Dimension(200, 45)); btnVeridico.setPreferredSize(new Dimension(200, 45));
        btnFalsa   .setMaximumSize(new Dimension(200, 45)); btnFalsa   .setPreferredSize(new Dimension(200, 45));

        JButton btnVoltar = botaoVoltar();

        ActionListener acaoVistoria = e -> {
            String obs = campoObs.getText().trim();
            if (obs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite uma observação!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean veridico = (e.getSource() == btnVeridico);
            String msg = denService.vistoriar(combo.getSelectedIndex(), obs, veridico);
            JOptionPane.showMessageDialog(this, msg, "SALUS", JOptionPane.INFORMATION_MESSAGE);
            navegador.irPara("gestor");
        };
        btnVeridico.addActionListener(acaoVistoria);
        btnFalsa   .addActionListener(acaoVistoria);
        btnVoltar  .addActionListener(e -> navegador.irPara("gestor"));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        painelBotoes.setBackground(COR_FUNDO);
        painelBotoes.setMaximumSize(new Dimension(500, 60));
        painelBotoes.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelBotoes.add(btnVeridico);
        painelBotoes.add(btnFalsa);

        add(Box.createVerticalGlue());
        add(titulo("VISTORIAR DENÚNCIA")); add(rigido(20));
        add(rotulo("Selecione a Denúncia")); add(rigido(8));
        add(combo);                          add(rigido(15));
        add(rotulo("Observação da Vistoria")); add(rigido(8));
        add(scrollObs);                      add(rigido(20));
        add(painelBotoes);                   add(rigido(15));
        add(btnVoltar);
        add(Box.createVerticalGlue());

        revalidate();
        repaint();
    }
}
