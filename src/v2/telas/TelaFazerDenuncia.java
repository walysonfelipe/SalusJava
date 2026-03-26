package v2.telas;

import javax.swing.*;
import java.awt.*;

import v2.services.DenunciaServiceV2;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Formulário para registrar uma nova denúncia.
 */
public class TelaFazerDenuncia extends JPanel {

    private final Navegador navegador;
    private final DenunciaServiceV2 denService;

    private JTextField campoNome, campoEmail, campoTel, campoLocal;
    private JTextArea  campoDesc;

    public TelaFazerDenuncia(Navegador navegador, DenunciaServiceV2 denService) {
        this.navegador = navegador;
        this.denService = denService;
        construir();
    }

    private void construir() {
        JPanel conteudo = painelBase(30, 100);

        campoNome  = criarCampo();
        campoEmail = criarCampo();
        campoTel   = criarCampo();
        campoLocal = criarCampo();

        campoDesc = new JTextArea(4, 30);
        campoDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        campoDesc.setBackground(COR_CAMPO);
        campoDesc.setForeground(Color.WHITE);
        campoDesc.setCaretColor(Color.WHITE);
        campoDesc.setLineWrap(true);
        campoDesc.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(campoDesc);
        scrollDesc.setMaximumSize(new Dimension(500, 100));
        scrollDesc.setPreferredSize(new Dimension(500, 100));
        scrollDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollDesc.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));

        JButton btnRegistrar = criarBotao("Registrar Denúncia", COR_BOTAO);
        JButton btnVoltar    = botaoVoltar();

        btnRegistrar.addActionListener(e -> {
            String nome  = campoNome.getText().trim();
            String email = campoEmail.getText().trim();
            String tel   = campoTel.getText().trim();
            String local = campoLocal.getText().trim();
            String desc  = campoDesc.getText().trim();
            if (nome.isEmpty() || email.isEmpty() || local.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios (*)!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String msg = denService.cadastrar(nome, email, tel, local, desc);
            JOptionPane.showMessageDialog(this, msg, "SALUS", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            navegador.irPara("cidadao");
        });
        btnVoltar.addActionListener(e -> navegador.irPara("cidadao"));

        conteudo.add(Box.createVerticalGlue());
        conteudo.add(titulo("NOVA DENÚNCIA"));  conteudo.add(rigido(20));
        conteudo.add(rotulo("Nome *"));         conteudo.add(rigido(5));
        conteudo.add(campoNome);                conteudo.add(rigido(10));
        conteudo.add(rotulo("E-mail *"));       conteudo.add(rigido(5));
        conteudo.add(campoEmail);               conteudo.add(rigido(10));
        conteudo.add(rotulo("Telefone"));       conteudo.add(rigido(5));
        conteudo.add(campoTel);                 conteudo.add(rigido(10));
        conteudo.add(rotulo("Local do Foco *")); conteudo.add(rigido(5));
        conteudo.add(campoLocal);               conteudo.add(rigido(10));
        conteudo.add(rotulo("Descrição *"));    conteudo.add(rigido(5));
        conteudo.add(scrollDesc);               conteudo.add(rigido(20));
        conteudo.add(btnRegistrar);             conteudo.add(rigido(10));
        conteudo.add(btnVoltar);
        conteudo.add(Box.createVerticalGlue());

        setLayout(new BorderLayout());
        add(emScrollPane(conteudo), BorderLayout.CENTER);
    }

    public void limparCampos() {
        campoNome.setText(""); campoEmail.setText("");
        campoTel.setText(""); campoLocal.setText(""); campoDesc.setText("");
    }
}
