package v2.telas;

import javax.swing.*;
import java.awt.*;

import models.Funcionario;
import v2.FuncionarioServiceV2;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Tela dinâmica para ativar ou desativar um funcionário.
 */
public class TelaAlterarStatus extends JPanel {

    private final Navegador navegador;
    private final FuncionarioServiceV2 funcService;

    public TelaAlterarStatus(Navegador navegador, FuncionarioServiceV2 funcService) {
        this.navegador = navegador;
        this.funcService = funcService;
    }

    /** Reconstrói o formulário para ativar ou desativar. */
    public void construir(boolean ativar) {
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        String acao = ativar ? "ATIVAR" : "DESATIVAR";
        Funcionario[] lista = funcService.listar();

        if (lista.length == 0) {
            JButton btnVoltar = botaoVoltar();
            btnVoltar.addActionListener(e -> navegador.irPara("admin"));
            add(Box.createVerticalGlue());
            add(titulo(acao + " FUNCIONÁRIO")); add(rigido(20));
            add(mensagem("Nenhum funcionário cadastrado."));
            add(rigido(20)); add(btnVoltar);
            add(Box.createVerticalGlue());
            revalidate(); repaint();
            return;
        }

        String[] opcoes = new String[lista.length];
        for (int i = 0; i < lista.length; i++) {
            opcoes[i] = String.format("[%d] %s  |  %s  |  %s",
                i + 1, lista[i].nome, lista[i].cargo, lista[i].ativo ? "ATIVO" : "INATIVO");
        }
        JComboBox<String> combo = new JComboBox<>(opcoes);
        combo.setFont(new Font("Arial", Font.PLAIN, 14));
        combo.setMaximumSize(new Dimension(600, 40));
        combo.setPreferredSize(new Dimension(600, 40));
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);

        Color corAcao = ativar ? new Color(34, 139, 34) : new Color(180, 30, 30);
        JButton btnConfirmar = criarBotao(acao, corAcao);
        btnConfirmar.setMaximumSize(new Dimension(250, 45));
        btnConfirmar.setPreferredSize(new Dimension(250, 45));
        JButton btnVoltar = botaoVoltar();

        btnConfirmar.addActionListener(e -> {
            String msg = funcService.alterarStatus(combo.getSelectedIndex(), ativar);
            JOptionPane.showMessageDialog(this, msg, "SALUS", JOptionPane.INFORMATION_MESSAGE);
            navegador.irPara("admin");
        });
        btnVoltar.addActionListener(e -> navegador.irPara("admin"));

        add(Box.createVerticalGlue());
        add(titulo(acao + " FUNCIONÁRIO")); add(rigido(20));
        add(rotulo("Selecione o Funcionário")); add(rigido(8));
        add(combo);           add(rigido(25));
        add(btnConfirmar);    add(rigido(12));
        add(btnVoltar);
        add(Box.createVerticalGlue());

        revalidate();
        repaint();
    }
}
