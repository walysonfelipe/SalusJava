package v2.telas;

import javax.swing.*;
import java.awt.*;

import models.Denuncia;
import v2.DenunciaServiceV2;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Tela para buscar denúncias por e-mail.
 */
public class TelaBuscarDenuncia extends JPanel {

    private final Navegador navegador;
    private final DenunciaServiceV2 denService;

    private JTextField campoEmail;
    private JTextArea  areaResultado;

    public TelaBuscarDenuncia(Navegador navegador, DenunciaServiceV2 denService) {
        this.navegador = navegador;
        this.denService = denService;
        construir();
    }

    private void construir() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        campoEmail = criarCampo();
        JButton btnBuscar = criarBotao("Buscar", COR_BOTAO);
        btnBuscar.setMaximumSize(new Dimension(200, 45));
        btnBuscar.setPreferredSize(new Dimension(200, 45));

        areaResultado = new JTextArea(10, 40);
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultado.setBackground(COR_AREA);
        areaResultado.setForeground(Color.WHITE);
        areaResultado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollRes = new JScrollPane(areaResultado);
        scrollRes.setMaximumSize(new Dimension(600, 250));
        scrollRes.setPreferredSize(new Dimension(600, 250));
        scrollRes.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnVoltar = botaoVoltar();

        btnBuscar.addActionListener(e -> {
            String email = campoEmail.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite seu e-mail!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Denuncia[] resultado = denService.buscarPorEmail(email);
            if (resultado.length == 0) {
                areaResultado.setText("Nenhuma denúncia encontrada para este e-mail.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < resultado.length; i++) {
                    Denuncia d = resultado[i];
                    sb.append("─────────────────────────────────────────────\n");
                    sb.append(String.format("#%d  |  Status: %s  |  %s\n", i + 1, d.status, d.dataHora));
                    sb.append(String.format("  Local: %s\n", d.local));
                    sb.append(String.format("  Descrição: %s\n", d.descricao));
                    if (d.observacao != null && !d.observacao.isEmpty()) {
                        sb.append(String.format("  Observação: %s\n", d.observacao));
                        sb.append(String.format("  Vistoria em: %s\n", d.dataHoraVistoria));
                    }
                }
                areaResultado.setText(sb.toString());
                areaResultado.setCaretPosition(0);
            }
        });
        btnVoltar.addActionListener(e -> navegador.irPara("cidadao"));

        add(Box.createVerticalGlue());
        add(titulo("MINHAS DENÚNCIAS"));      add(rigido(20));
        add(rotulo("Seu E-mail"));             add(rigido(5));
        add(campoEmail);                       add(rigido(10));
        add(btnBuscar);                        add(rigido(15));
        add(scrollRes);                        add(rigido(15));
        add(btnVoltar);
        add(Box.createVerticalGlue());
    }

    public void limparCampos() {
        campoEmail.setText("");
        areaResultado.setText("");
    }
}
