package v2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class TelaCreditos extends JPanel {

    private static final String REPO_URL = "https://github.com/walysonfelipe/SalusJava";

    // ====================================================
    //  EDITE AQUI os nomes dos integrantes do grupo
    // ====================================================
    private static final String[] CREDITOS = {
        "", "", "", "",
        "✦  SALUS  ✦",
        "",
        "Sistema de Denúncias de Dengue",
        "Prefeitura de Lins — SP",
        "", "",
        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
        "", "",
        "DESENVOLVIDO POR",
        "", "",
        "Walyson Assis",
        "",
        "Gabriel Martins",
        "",
        "Eduardo Xavier",
        "",
        "Gabriel Cardinale",
        "",
        "Filipe Alexandre",
        "", "",
        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
        "", "",
        "TECNOLOGIAS",
        "",
        "Java SE  ·  Swing  ·  AWT",
        "",
        "Paradigma Orientado a Objetos",
        "", "",
        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
        "", "",
        "CURSO",
        "",
        "Análise e Desenvolvimento de Sistemas",
        "",
        "Projeto Integrador  ·  2026",
        "", "",
        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
        "", "",
        "Acesse o código-fonte:",
        "github.com/walysonfelipe/SalusJava",
        "", "", "", "", "", "",
    };

    private final StarWarsPanel starWarsPanel;

    public TelaCreditos(Runnable aoVoltar) {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 112));

        add(criarPainelQR(),          BorderLayout.NORTH);
        starWarsPanel = new StarWarsPanel(CREDITOS);
        add(starWarsPanel,            BorderLayout.CENTER);
        add(criarRodape(aoVoltar),    BorderLayout.SOUTH);
    }

    // ==================== CICLO DE VIDA ====================

    public void iniciar() {
        starWarsPanel.iniciar();
    }

    public void parar() {
        starWarsPanel.parar();
    }

    // ==================== PAINEL QR CODE ====================

    private JPanel criarPainelQR() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 18));
        painel.setBackground(new Color(25, 25, 112));
        painel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(237, 119, 2)));

        // Carrega o QR code do arquivo local
        JLabel qrLabel = new JLabel();
        qrLabel.setPreferredSize(new Dimension(150, 150));
        qrLabel.setHorizontalAlignment(JLabel.CENTER);
        qrLabel.setBorder(BorderFactory.createLineBorder(new Color(237, 119, 2), 1));
        try (InputStream is = getClass().getResourceAsStream("/resources/qrcode.png")) {
            if (is != null) {
                BufferedImage img = ImageIO.read(is);
                Image scaled = img.getScaledInstance(148, 148, Image.SCALE_SMOOTH);
                qrLabel.setIcon(new ImageIcon(scaled));
            } else {
                qrLabel.setText("<html><center>QR<br>indisponível</center></html>");
                qrLabel.setForeground(new Color(180, 180, 220));
                qrLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            }
        } catch (Exception ignored) {
            qrLabel.setText("<html><center>QR<br>indisponível</center></html>");
            qrLabel.setForeground(new Color(180, 180, 220));
            qrLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        }
        painel.add(qrLabel);

        // Informações do repositório
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(new Color(25, 25, 112));

        JLabel titulo = new JLabel("Repositório do Projeto");
        titulo.setFont(new Font("Arial", Font.BOLD, 17));
        titulo.setForeground(new Color(237, 119, 2));

        JLabel url = new JLabel(REPO_URL);
        url.setFont(new Font("Monospaced", Font.PLAIN, 13));
        url.setForeground(Color.WHITE);

        JLabel dica = new JLabel("Escaneie para acessar o código-fonte");
        dica.setFont(new Font("Arial", Font.ITALIC, 11));
        dica.setForeground(new Color(180, 180, 220));

        info.add(titulo);
        info.add(Box.createRigidArea(new Dimension(0, 6)));
        info.add(url);
        info.add(Box.createRigidArea(new Dimension(0, 4)));
        info.add(dica);
        painel.add(info);

        return painel;
    }

    // ==================== RODAPÉ ====================

    private JPanel criarRodape(Runnable aoVoltar) {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rodape.setBackground(new Color(25, 25, 112));
        rodape.setBorder(BorderFactory.createEmptyBorder(12, 0, 18, 0));

        JButton btnVoltar = new JButton("← Voltar ao Menu");
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 14));
        btnVoltar.setForeground(new Color(237, 119, 2));
        btnVoltar.setBackground(new Color(25, 25, 112));
        btnVoltar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(237, 119, 2), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnVoltar.setBackground(new Color(15, 15, 50));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnVoltar.setBackground(new Color(25, 25, 112));
            }
        });
        btnVoltar.addActionListener(e -> {
            parar();
            aoVoltar.run();
        });

        rodape.add(btnVoltar);
        return rodape;
    }

    // ==================== PAINEL PERSPECTIVE CRAWL ====================

    private static class StarWarsPanel extends JPanel {

        private static final Color LARANJA = new Color(237, 119, 2);
        private static final Color LILAS   = new Color(180, 180, 220);
        private static final Color CINZA   = new Color(100, 100, 140);

        // Distância focal (perspectiva): maior = menos distorção
        private static final float PERSP     = 520f;
        // Espaçamento entre linhas no "mundo"
        private static final float LINE_H    = 58f;
        // Velocidade de scroll
        private static final float SPEED     = 0.5f;
        // Tamanho base da fonte em scale=1.0 (fundo da tela)
        private static final int   BASE_FONT = 40;

        private final String[] linhas;
        private float scrollPos = 0f;
        private Timer timer;

        StarWarsPanel(String[] linhas) {
            this.linhas = linhas;
            setBackground(new Color(25, 25, 112));
        }

        void iniciar() {
            parar();
            scrollPos = 0f;
            timer = new Timer(16, e -> {   // ~60 fps
                scrollPos += SPEED;
                // Reinicia quando o texto inteiro já passou do topo
                if (scrollPos > linhas.length * LINE_H + PERSP * 3f)
                    scrollPos = 0f;
                repaint();
            });
            timer.start();
        }

        void parar() {
            if (timer != null) { timer.stop(); timer = null; }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                                RenderingHints.VALUE_RENDER_QUALITY);

            int   cx     = getWidth()  / 2;
            int   panH   = getHeight();
            // Ponto de fuga: 18% do topo
            int   horizY = (int)(panH * 0.18f);

            AffineTransform base = g2.getTransform();

            // Pre-calcula posição e escala de cada linha
            float[] scales   = new float[linhas.length];
            float[] screenYs = new float[linhas.length];
            for (int i = 0; i < linhas.length; i++) {
                float pos = scrollPos - i * LINE_H;
                // Permite pos negativo para preparar a entrada
                if (pos < -PERSP + 100f) continue;
                
                float sc = PERSP / (PERSP + pos);
                if (sc < 0.05f) continue; // Muito pequeno/longe, já passou do topo
                
                float sy = horizY + (panH - horizY) * sc;
                scales[i]   = sc;
                screenYs[i] = sy;
            }

            // Desenha todas as linhas visíveis (do topo para baixo)
            for (int i = 0; i < linhas.length; i++) {
                if (scales[i] == 0f || linhas[i].isBlank()) continue;
                float scale   = scales[i];
                float screenY = screenYs[i];

                float alpha = 1f;

                // Sumir um a um bem simples ao chegar no teto.
                // A justificação de usar 0.5f a 0.35f é que, devido à perspectiva 3D,
                // quando a escala fica menor que 0.35, a velocidade visual na tela cai drasticamente.
                // Antes, tentar sumir até 0.1 fazia o texto demorar mais de 2 minutos pra sumir, parecendo travado.
                if (scale < 0.5f) {
                    alpha = (scale - 0.35f) / (0.5f - 0.35f);
                }
                
                // Aparecer um a um bem simples na base (scale começando de 1.0 descendo até 0.8)
                if (scale > 0.8f) {
                    alpha = (1.0f - scale) / (1.0f - 0.8f);
                }

                if (alpha <= 0f) continue;
                if (alpha > 1f) alpha = 1f;

                g2.setFont(obterFont(linhas[i], BASE_FONT));
                g2.setColor(obterCor(linhas[i]));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                float tw = g2.getFontMetrics().stringWidth(linhas[i]);

                AffineTransform t = new AffineTransform(base);
                t.translate(cx, screenY);
                t.scale(scale, scale);
                g2.setTransform(t);
                g2.drawString(linhas[i], -tw / 2f, 0f);
                g2.setTransform(base);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }

            g2.dispose();
        }

        private Font obterFont(String linha, int size) {
            if (linha.contains("✦"))
                return new Font("Arial", Font.BOLD, size);
            if (linha.equals("DESENVOLVIDO POR") || linha.equals("TECNOLOGIAS")
                    || linha.equals("CURSO") || linha.equals("Acesse o código-fonte:"))
                return new Font("Arial", Font.BOLD, size);
            if (linha.startsWith("━"))
                return new Font("Arial", Font.PLAIN, size);
            if (linha.contains("·") || linha.contains("Projeto"))
                return new Font("Arial", Font.ITALIC, size);
            return new Font("Arial", Font.PLAIN, size);
        }

        private Color obterCor(String linha) {
            if (linha.contains("✦"))                                          return LARANJA;
            if (linha.equals("DESENVOLVIDO POR") || linha.equals("TECNOLOGIAS")
                    || linha.equals("CURSO") || linha.equals("Acesse o código-fonte:")) return LILAS;
            if (linha.startsWith("━"))                                         return CINZA;
            if (linha.contains("·") || linha.contains("Projeto"))              return LILAS;
            return Color.WHITE;
        }
    }
}
