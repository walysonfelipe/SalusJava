package v2.telas;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

import models.Denuncia;
import v2.services.DenunciaServiceV2;

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

        String[] colunas = {"#", "Descrição", "Status", "Nome", "Local", "Data/Hora"};
        Object[][] dados = new Object[lista.length][6];
        for (int i = 0; i < lista.length; i++) {
            Denuncia d = lista[i];
            dados[i] = new Object[]{i + 1, d.descricao, d.status, d.nome, d.local, d.dataHora};
        }

        JTable tabela = new JTable(dados, colunas) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela.setBackground(COR_AREA);
        tabela.setForeground(Color.WHITE);
        tabela.setFont(new Font("Monospaced", Font.PLAIN, 13));
        tabela.setRowHeight(28);
        tabela.setGridColor(COR_BORDA);
        tabela.setSelectionBackground(COR_BOTAO);
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setAutoCreateRowSorter(true);

        DefaultTableCellRenderer centralizador = new DefaultTableCellRenderer();
        centralizador.setHorizontalAlignment(SwingConstants.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizador);
        tabela.getColumnModel().getColumn(2).setCellRenderer(centralizador);
        tabela.getColumnModel().getColumn(5).setCellRenderer(centralizador);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(30);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(80);

        JTableHeader header = tabela.getTableHeader();
        header.setBackground(new Color(15, 15, 50));
        header.setForeground(COR_BOTAO);
        header.setFont(new Font("Monospaced", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(tabela);
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
