package v2.telas;

import javax.swing.*;
import java.awt.*;

import v2.FuncionarioServiceV2;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Formulário para cadastrar um novo funcionário.
 */
public class TelaAddFuncionario extends JPanel {

    private final Navegador navegador;
    private final FuncionarioServiceV2 funcService;

    private JTextField campoNome, campoCPF, campoEmail, campoTel,
                       campoSenha, campoCargo, campoSetor;

    public TelaAddFuncionario(Navegador navegador, FuncionarioServiceV2 funcService) {
        this.navegador = navegador;
        this.funcService = funcService;
        construir();
    }

    private void construir() {
        JPanel conteudo = painelBase(30, 100);

        campoNome  = criarCampo();
        campoCPF   = criarCampo();
        campoEmail = criarCampo();
        campoTel   = criarCampo();
        campoSenha = criarCampo();
        campoCargo = criarCampo();
        campoSetor = criarCampo();

        JButton btnCadastrar = criarBotao("Cadastrar Funcionário", COR_BOTAO);
        JButton btnVoltar    = botaoVoltar();

        btnCadastrar.addActionListener(e -> {
            String nome  = campoNome.getText().trim();
            String cpf   = campoCPF.getText().trim();
            String email = campoEmail.getText().trim();
            String tel   = campoTel.getText().trim();
            String senha = campoSenha.getText().trim();
            String cargo = campoCargo.getText().trim();
            String setor = campoSetor.getText().trim();
            if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty() || cargo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios (*)!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String msg = funcService.adicionar(nome, cpf, email, tel, senha, cargo, setor);
            JOptionPane.showMessageDialog(this, msg, "SALUS", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            navegador.irPara("admin");
        });
        btnVoltar.addActionListener(e -> navegador.irPara("admin"));

        conteudo.add(Box.createVerticalGlue());
        conteudo.add(titulo("NOVO FUNCIONÁRIO")); conteudo.add(rigido(20));
        conteudo.add(rotulo("Nome *"));          conteudo.add(rigido(5)); conteudo.add(campoNome);  conteudo.add(rigido(10));
        conteudo.add(rotulo("CPF *"));           conteudo.add(rigido(5)); conteudo.add(campoCPF);   conteudo.add(rigido(10));
        conteudo.add(rotulo("E-mail *"));        conteudo.add(rigido(5)); conteudo.add(campoEmail); conteudo.add(rigido(10));
        conteudo.add(rotulo("Telefone"));        conteudo.add(rigido(5)); conteudo.add(campoTel);   conteudo.add(rigido(10));
        conteudo.add(rotulo("Senha *"));         conteudo.add(rigido(5)); conteudo.add(campoSenha); conteudo.add(rigido(10));
        conteudo.add(rotulo("Cargo *"));         conteudo.add(rigido(5)); conteudo.add(campoCargo); conteudo.add(rigido(10));
        conteudo.add(rotulo("Setor"));           conteudo.add(rigido(5)); conteudo.add(campoSetor); conteudo.add(rigido(20));
        conteudo.add(btnCadastrar);              conteudo.add(rigido(10));
        conteudo.add(btnVoltar);
        conteudo.add(Box.createVerticalGlue());

        setLayout(new BorderLayout());
        add(emScrollPane(conteudo), BorderLayout.CENTER);
    }

    public void limparCampos() {
        campoNome.setText(""); campoCPF.setText(""); campoEmail.setText("");
        campoTel.setText(""); campoSenha.setText(""); campoCargo.setText(""); campoSetor.setText("");
    }
}
