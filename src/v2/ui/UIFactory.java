package v2.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static v2.ui.SalusTheme.*;

/**
 * Fábrica de componentes Swing reutilizáveis do SALUS.
 * Todos os helpers visuais concentrados aqui.
 */
public final class UIFactory {

    private UIFactory() {}

    // ===== Painéis =====

    public static JPanel painelBase(int vertical, int horizontal) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(COR_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(vertical, horizontal, vertical, horizontal));
        return p;
    }

    public static JPanel emScrollPane(JPanel conteudo) {
        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setBackground(COR_FUNDO);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(COR_FUNDO);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(COR_FUNDO);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    // ===== Labels =====

    public static JLabel titulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 28));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    public static JLabel rotulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setForeground(COR_SUBTXT);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    public static JLabel mensagem(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 16));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    // ===== Espaçamento =====

    public static Component rigido(int altura) {
        return Box.createRigidArea(new Dimension(0, altura));
    }

    // ===== Campos =====

    public static JTextField criarCampo() {
        JTextField campo = new JTextField(20);
        estilizarCampo(campo);
        return campo;
    }

    public static void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBackground(COR_CAMPO);
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setMaximumSize(new Dimension(500, 40));
        campo.setPreferredSize(new Dimension(500, 40));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // ===== Botões =====

    public static JButton botaoVoltar() {
        JButton btn = new JButton("← Voltar");
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setForeground(COR_SUBTXT);
        btn.setBackground(COR_FUNDO);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(Color.WHITE); }
            public void mouseExited(MouseEvent e)  { btn.setForeground(COR_SUBTXT); }
        });
        return btn;
    }

    public static JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        botao.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        botao.setFont(new Font("Arial", Font.BOLD, 18));
        botao.setForeground(Color.WHITE);
        botao.setBackground(cor);
        botao.setOpaque(false);
        botao.setContentAreaFilled(false);
        botao.setMaximumSize(new Dimension(350, 50));
        botao.setPreferredSize(new Dimension(350, 50));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { botao.setBackground(cor.darker()); }
            public void mouseExited(MouseEvent e)  { botao.setBackground(cor); }
        });
        return botao;
    }

    // ===== Logo =====

    public static JLabel carregarLogo(int w, int h) {
        JLabel lbl = new JLabel();
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon icon = new ImageIcon(UIFactory.class.getResource("/resources/logo.png"));
        if (icon.getIconWidth() > 0) {
            Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(img));
        } else {
            lbl.setText("SALUS");
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.BOLD, 32));
        }
        return lbl;
    }
}
