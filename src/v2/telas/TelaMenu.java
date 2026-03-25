package v2.telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Tela do menu principal com seleção de perfil de acesso.
 */
public class TelaMenu extends JPanel {

    public TelaMenu(Navegador navegador, TelaCreditos telaCreditos) {
        setLayout(new BorderLayout());
        setBackground(COR_FUNDO);

        // Conteúdo central
        JPanel centro = painelBase(30, 50);

        JLabel logo      = carregarLogo(280, 106);
        JLabel subtitulo = rotulo("Selecione seu perfil de acesso:");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton btnCidadao = criarBotao("Cidadão",       COR_BOTAO);
        JButton btnGestor  = criarBotao("Gestor",        COR_BOTAO);
        JButton btnAdmin   = criarBotao("Administrador", COR_BOTAO);
        JButton btnSair    = criarBotao("Sair (ESC)",    COR_BOTAO);
        btnSair.setMaximumSize(new Dimension(250, 40));
        btnSair.setPreferredSize(new Dimension(250, 40));
        btnSair.setFont(new Font("Arial", Font.BOLD, 14));

        btnCidadao.addActionListener(e -> navegador.irPara("cidadao"));
        btnGestor .addActionListener(e -> navegador.irPara("login_gestor"));
        btnAdmin  .addActionListener(e -> navegador.irPara("login_admin"));
        btnSair   .addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null) w.dispose();
            System.exit(0);
        });

        centro.add(Box.createVerticalGlue());
        centro.add(logo);
        centro.add(rigido(15));
        centro.add(subtitulo);
        centro.add(rigido(40));
        centro.add(btnCidadao); centro.add(rigido(12));
        centro.add(btnGestor);  centro.add(rigido(12));
        centro.add(btnAdmin);   centro.add(rigido(30));
        centro.add(btnSair);
        centro.add(Box.createVerticalGlue());

        // Barra inferior: botão de créditos
        JPanel barraInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        barraInferior.setBackground(COR_FUNDO);

        JButton btnCreditos = new JButton("★ Créditos");
        btnCreditos.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCreditos.setForeground(new Color(120, 120, 160));
        btnCreditos.setBackground(COR_FUNDO);
        btnCreditos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 130), 1),
            BorderFactory.createEmptyBorder(5, 14, 5, 14)
        ));
        btnCreditos.setFocusPainted(false);
        btnCreditos.setContentAreaFilled(false);
        btnCreditos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreditos.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnCreditos.setForeground(Color.WHITE); }
            public void mouseExited (MouseEvent e) { btnCreditos.setForeground(new Color(120, 120, 160)); }
        });
        btnCreditos.addActionListener(e -> {
            telaCreditos.iniciar();
            navegador.irPara("creditos");
        });

        barraInferior.add(btnCreditos);

        add(centro,        BorderLayout.CENTER);
        add(barraInferior, BorderLayout.SOUTH);
    }
}
