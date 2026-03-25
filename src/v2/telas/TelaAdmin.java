package v2.telas;

import javax.swing.*;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Painel do Administrador com opções de gerenciamento de funcionários.
 */
public class TelaAdmin extends JPanel {

    private final Navegador navegador;
    private final TelaAddFuncionario telaAddFuncionario;

    public TelaAdmin(Navegador navegador, TelaAddFuncionario telaAddFuncionario) {
        this.navegador = navegador;
        this.telaAddFuncionario = telaAddFuncionario;
    }

    /** Reconstrói o painel a cada navegação. */
    public void construir() {
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton btnAdicionar = criarBotao("Adicionar Funcionário",  COR_BOTAO);
        JButton btnListar    = criarBotao("Listar Funcionários",    COR_BOTAO);
        JButton btnDesativar = criarBotao("Desativar Funcionário",  COR_BOTAO);
        JButton btnAtivar    = criarBotao("Ativar Funcionário",     COR_BOTAO);
        JButton btnVoltar    = botaoVoltar();
        btnVoltar.setText("← Sair");

        btnAdicionar.addActionListener(e -> { telaAddFuncionario.limparCampos(); navegador.irPara("add_funcionario"); });
        btnListar   .addActionListener(e -> navegador.irPara("listar_funcs_admin"));
        btnDesativar.addActionListener(e -> navegador.irPara("desativar_func"));
        btnAtivar   .addActionListener(e -> navegador.irPara("ativar_func"));
        btnVoltar   .addActionListener(e -> navegador.irPara("menu"));

        add(Box.createVerticalGlue());
        add(titulo("PAINEL DO ADMINISTRADOR")); add(rigido(40));
        add(btnAdicionar); add(rigido(12));
        add(btnListar);    add(rigido(12));
        add(btnDesativar); add(rigido(12));
        add(btnAtivar);    add(rigido(30));
        add(btnVoltar);
        add(Box.createVerticalGlue());

        revalidate();
        repaint();
    }
}
