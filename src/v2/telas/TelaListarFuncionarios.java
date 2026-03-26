package v2.telas;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

import models.Funcionario;
import v2.services.FuncionarioServiceV2;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Tela dinâmica para listar todos os funcionários cadastrados.
 */
public class TelaListarFuncionarios extends JPanel {

    private final Navegador navegador;
    private final FuncionarioServiceV2 funcService;
    private String voltarCard = "admin";

    public TelaListarFuncionarios(Navegador navegador, FuncionarioServiceV2 funcService) {
        this.navegador = navegador;
        this.funcService = funcService;
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
        topo.add(titulo("FUNCIONÁRIOS CADASTRADOS"));

        Funcionario[] lista = funcService.listar();

        String[] colunas = {"#", "Nome", "Cargo", "Setor", "E-mail", "Status"};
        Object[][] dados = new Object[lista.length][6];
        for (int i = 0; i < lista.length; i++) {
            Funcionario f = lista[i];
            dados[i] = new Object[]{i + 1, f.nome, f.cargo, f.setor, f.email, f.ativo ? "ATIVO" : "INATIVO"};
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
        tabela.getColumnModel().getColumn(5).setCellRenderer(centralizador);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(30);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(70);

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
