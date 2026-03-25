package v2.telas;

import javax.swing.*;

import v2.FuncionarioServiceV2;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Tela de login do Gestor.
 */
public class TelaLoginGestor extends JPanel {

    private final Navegador navegador;
    private final FuncionarioServiceV2 funcService;

    private JTextField     campoEmail;
    private JPasswordField campoSenha;

    public TelaLoginGestor(Navegador navegador, FuncionarioServiceV2 funcService) {
        this.navegador = navegador;
        this.funcService = funcService;
        construir();
    }

    private void construir() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        campoEmail = criarCampo();
        campoSenha = new JPasswordField(20);
        estilizarCampo(campoSenha);

        JButton btnEntrar = criarBotao("Entrar", COR_BOTAO);
        JButton btnVoltar = botaoVoltar();

        btnEntrar.addActionListener(e -> {
            String email = campoEmail.getText().trim();
            String senha = new String(campoSenha.getPassword());
            if (funcService.loginGestor(email, senha)) {
                campoEmail.setText(""); campoSenha.setText("");
                navegador.irPara("gestor");
            } else {
                JOptionPane.showMessageDialog(this, "E-mail ou senha inválidos!", "Falha no Login", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnVoltar.addActionListener(e -> { campoEmail.setText(""); campoSenha.setText(""); navegador.irPara("menu"); });

        add(Box.createVerticalGlue());
        add(titulo("LOGIN — GESTOR")); add(rigido(30));
        add(rotulo("E-mail"));         add(rigido(5));
        add(campoEmail);               add(rigido(15));
        add(rotulo("Senha"));          add(rigido(5));
        add(campoSenha);               add(rigido(25));
        add(btnEntrar);                add(rigido(10));
        add(btnVoltar);
        add(Box.createVerticalGlue());
    }
}
