package v2.telas;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import models.Denuncia;
import v2.services.DenunciaServiceV2;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Tela para buscar denúncias por e-mail.
 */
public class TelaBuscarDenuncia extends JPanel {

    private final Navegador navegador;
    private final DenunciaServiceV2 denService;

    private JTextField         campoEmail;
    private DefaultTableModel  modeloTabela;
    private JScrollPane        scrollRes;

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

        String[] colunas = {"#", "Status", "Local", "Descrição", "Data/Hora", "Observação"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tabela = new JTable(modeloTabela);
        tabela.setBackground(COR_AREA);
        tabela.setForeground(Color.WHITE);
        tabela.setFont(new Font("Monospaced", Font.PLAIN, 13));
        tabela.setRowHeight(28);
        tabela.setGridColor(COR_BORDA);
        tabela.setSelectionBackground(COR_BOTAO);
        tabela.setSelectionForeground(Color.WHITE);

        DefaultTableCellRenderer centralizador = new DefaultTableCellRenderer();
        centralizador.setHorizontalAlignment(SwingConstants.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizador);
        tabela.getColumnModel().getColumn(1).setCellRenderer(centralizador);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(30);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(80);

        JTableHeader header = tabela.getTableHeader();
        header.setBackground(new Color(15, 15, 50));
        header.setForeground(COR_BOTAO);
        header.setFont(new Font("Monospaced", Font.BOLD, 13));

        scrollRes = new JScrollPane(tabela);
        scrollRes.setMaximumSize(new Dimension(700, 250));
        scrollRes.setPreferredSize(new Dimension(700, 250));
        scrollRes.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnVoltar = botaoVoltar();

        btnBuscar.addActionListener(e -> {
            String email = campoEmail.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite seu e-mail!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Denuncia[] resultado = denService.buscarPorEmail(email);
            modeloTabela.setRowCount(0);
            if (resultado.length == 0) {
                JOptionPane.showMessageDialog(this, "Nenhuma denúncia encontrada para este e-mail.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (int i = 0; i < resultado.length; i++) {
                    Denuncia d = resultado[i];
                    modeloTabela.addRow(new Object[]{
                        i + 1, d.status, d.local, d.descricao, d.dataHora,
                        (d.observacao != null && !d.observacao.isEmpty()) ? d.observacao : "—"
                    });
                }
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
        modeloTabela.setRowCount(0);
    }
}
