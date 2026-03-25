package v2.telas;

import javax.swing.*;
import java.awt.*;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Tela de carregamento com barra de progresso animada.
 */
public class TelaCarregamento extends JPanel {

    private JProgressBar progressBar;
    private JLabel statusLabel;
    private Timer timer;
    private int progresso = 0;

    private final Navegador navegador;

    public TelaCarregamento(Navegador navegador) {
        this.navegador = navegador;
        construir();
    }

    private void construir() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));

        JLabel logo = carregarLogo(280, 106);

        UIManager.put("ProgressBar.selectionForeground", Color.WHITE);
        UIManager.put("ProgressBar.selectionBackground", Color.WHITE);

        progressBar = new JProgressBar(0, 100);
        progressBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI());
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Arial", Font.BOLD, 16));
        progressBar.setForeground(COR_BOTAO);
        progressBar.setBackground(new Color(30, 30, 60));
        progressBar.setPreferredSize(new Dimension(500, 35));
        progressBar.setMaximumSize(new Dimension(500, 35));
        progressBar.setMinimumSize(new Dimension(500, 35));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = new JLabel("Inicializando...");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(new Color(200, 200, 200));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel escHint = new JLabel("Pressione ESC para sair");
        escHint.setFont(new Font("Arial", Font.PLAIN, 11));
        escHint.setForeground(new Color(120, 120, 160));
        escHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(logo);
        add(rigido(50));
        add(progressBar);
        add(rigido(12));
        add(statusLabel);
        add(Box.createVerticalGlue());
        add(escHint);
        add(rigido(10));
    }

    public void iniciarCarregamento() {
        progresso = 0;
        String[] msgs = {
            "Inicializando módulos...", "Conectando ao servidor...",
            "Carregando banco de dados...", "Verificando permissões...",
            "Preparando interface...", "Quase lá...", "Finalizando..."
        };
        timer = new Timer(50, e -> {
            progresso += 1;
            progressBar.setValue(progresso);
            int idx = (progresso * msgs.length) / 100;
            if (idx >= msgs.length) idx = msgs.length - 1;
            statusLabel.setText(msgs[idx]);
            if (progresso >= 100) {
                timer.stop();
                Timer delay = new Timer(500, ev -> navegador.irPara("menu"));
                delay.setRepeats(false);
                delay.start();
            }
        });
        timer.start();
    }
}
