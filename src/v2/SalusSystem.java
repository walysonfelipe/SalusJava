package v2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import models.Denuncia;
import models.Funcionario;

public class SalusSystem extends JFrame {

    // ===== Layout =====
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // ===== Loading =====
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private Timer timer;
    private int progresso = 0;

    // ===== Services =====
    private final DenunciaServiceV2 denService = new DenunciaServiceV2();
    private final FuncionarioServiceV2 funcService = new FuncionarioServiceV2();

    // ===== Créditos =====
    private TelaCreditos telaCreditos;

    // ===== Painéis dinâmicos (reconstruídos a cada navegação) =====
    private final JPanel painelGestor          = new JPanel();
    private final JPanel painelListarDenuncias = new JPanel();
    private final JPanel painelVistoriar       = new JPanel();
    private final JPanel painelAdmin           = new JPanel();
    private final JPanel painelListarFuncs     = new JPanel();
    private final JPanel painelAlterarStatus   = new JPanel();

    // ===== Campos: Fazer Denúncia =====
    private JTextField campoNomeDen, campoEmailDen, campoTelDen, campoLocalDen;
    private JTextArea  campoDescDen;

    // ===== Campos: Buscar Denúncia =====
    private JTextField campoEmailBusca;
    private JTextArea  areaResultadoBusca;

    // ===== Campos: Login Gestor =====
    private JTextField    campoEmailGestor;
    private JPasswordField campoSenhaGestor;

    // ===== Campos: Login Admin =====
    private JTextField    campoEmailAdmin;
    private JPasswordField campoSenhaAdmin;

    // ===== Campos: Adicionar Funcionário =====
    private JTextField campoNomeFunc, campoCPF, campoEmailFunc, campoTelFunc,
                       campoSenhaFunc, campoCargoFunc, campoSetorFunc;

    // ===== Cores =====
    private static final Color COR_FUNDO   = new Color(25, 25, 112);
    private static final Color COR_BOTAO   = new Color(237, 119, 2);
    private static final Color COR_SUBTXT  = new Color(180, 180, 220);
    private static final Color COR_AREA    = new Color(15, 15, 50);

    // ==================== CONSTRUTOR ====================

    public SalusSystem() {
        setTitle("SALUS - Sistema de Saúde");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        // Telas estáticas
        mainPanel.add(criarTelaCarregamento(),       "loading");
        mainPanel.add(criarTelaMenu(),               "menu");
        mainPanel.add(criarTelaCidadao(),            "cidadao");
        mainPanel.add(criarTelaFazerDenuncia(),      "fazer_denuncia");
        mainPanel.add(criarTelaBuscarDenuncia(),     "buscar_denuncia");
        mainPanel.add(criarTelaLoginGestor(),        "login_gestor");
        mainPanel.add(criarTelaLoginAdmin(),         "login_admin");
        mainPanel.add(criarTelaAddFuncionario(),     "add_funcionario");

        // Tela de créditos
        telaCreditos = new TelaCreditos(() -> irPara("menu"));
        mainPanel.add(telaCreditos, "creditos");

        // Telas dinâmicas (reconstruídas via construir*())
        mainPanel.add(painelGestor,          "gestor");
        mainPanel.add(painelListarDenuncias, "listar_denuncias");
        mainPanel.add(painelVistoriar,       "vistoriar");
        mainPanel.add(painelAdmin,           "admin");
        mainPanel.add(painelListarFuncs,     "listar_funcs");
        mainPanel.add(painelAlterarStatus,   "alterar_status");

        add(mainPanel);
        cardLayout.show(mainPanel, "loading");
        iniciarCarregamento();

        // ESC para sair
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "sair");
        getRootPane().getActionMap().put("sair", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { dispose(); System.exit(0); }
        });
    }

    // ==================== NAVEGAÇÃO ====================

    private void irPara(String tela) {
        cardLayout.show(mainPanel, tela);
    }

    private void irParaCreditos() {
        telaCreditos.iniciar();
        irPara("creditos");
    }

    private void irParaGestor() {
        construirTelaGestor();
        irPara("gestor");
    }

    private void irParaListarDenuncias(String voltarCard) {
        construirTelaListarDenuncias(voltarCard);
        irPara("listar_denuncias");
    }

    private void irParaVistoriar() {
        construirTelaVistoriar();
        irPara("vistoriar");
    }

    private void irParaAdmin() {
        construirTelaAdmin();
        irPara("admin");
    }

    private void irParaListarFuncs(String voltarCard) {
        construirTelaListarFuncs(voltarCard);
        irPara("listar_funcs");
    }

    private void irParaAlterarStatus(boolean ativar) {
        construirTelaAlterarStatus(ativar);
        irPara("alterar_status");
    }

    // ==================== TELA CARREGAMENTO ====================

    private JPanel criarTelaCarregamento() {
        JPanel painel = painelBase(60, 50);

        JLabel logo = carregarLogo(280, 106);

        UIManager.put("ProgressBar.selectionForeground", Color.WHITE);
        UIManager.put("ProgressBar.selectionBackground", Color.WHITE);

        progressBar = new JProgressBar(0, 100);
        progressBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI());
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Arial", Font.BOLD, 16));
        progressBar.setForeground(COR_BOTAO);
        progressBar.setBackground(new Color(30, 30, 60));
        progressBar.setPreferredSize(new Dimension(500, 35));
        progressBar.setMaximumSize(new Dimension(500, 35));
        progressBar.setMinimumSize(new Dimension(500, 35));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = new JLabel("Inicializando...");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(new Color(200, 200, 200));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel escHint = new JLabel("Pressione ESC para sair");
        escHint.setFont(new Font("Arial", Font.PLAIN, 11));
        escHint.setForeground(new Color(120, 120, 160));
        escHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        painel.add(Box.createVerticalGlue());
        painel.add(logo);
        painel.add(rigido(50));
        painel.add(progressBar);
        painel.add(rigido(12));
        painel.add(statusLabel);
        painel.add(Box.createVerticalGlue());
        painel.add(escHint);
        painel.add(rigido(10));
        return painel;
    }

    private void iniciarCarregamento() {
        String[] msgs = {
            "Inicializando módulos...", "Conectando ao servidor...",
            "Carregando banco de dados...", "Verificando permissões...",
            "Preparando interface...", "Quase lá...", "Finalizando..."
        };
        timer = new Timer(50, e -> {
            progresso += 1;
            progressBar.setValue(progresso);
            int idx = (progresso * msgs.length) / 100;
            if (idx >= msgs.length) idx = msgs.length - 1;
            statusLabel.setText(msgs[idx]);
            if (progresso >= 100) {
                timer.stop();
                Timer delay = new Timer(500, ev -> irPara("menu"));
                delay.setRepeats(false);
                delay.start();
            }
        });
        timer.start();
    }

    // ==================== TELA MENU PRINCIPAL ====================

    private JPanel criarTelaMenu() {
        // Painel externo: BorderLayout para ancorar o botão de créditos no canto inferior direito
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(COR_FUNDO);

        // Conteúdo central (BoxLayout vertical com os botões principais)
        JPanel centro = painelBase(30, 50);

        JLabel logo      = carregarLogo(280, 106);
        JLabel subtitulo = rotulo("Selecione seu perfil de acesso:");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton btnCidadao = criarBotao("Cidadão",       COR_BOTAO);
        JButton btnGestor  = criarBotao("Gestor",        COR_BOTAO);
        JButton btnAdmin   = criarBotao("Administrador", COR_BOTAO);
        JButton btnSair    = criarBotao("Sair (ESC)",    COR_BOTAO);
        btnSair.setMaximumSize(new Dimension(250, 40));
        btnSair.setPreferredSize(new Dimension(250, 40));
        btnSair.setFont(new Font("Arial", Font.BOLD, 14));

        btnCidadao.addActionListener(e -> irPara("cidadao"));
        btnGestor .addActionListener(e -> irPara("login_gestor"));
        btnAdmin  .addActionListener(e -> irPara("login_admin"));
        btnSair   .addActionListener(e -> { dispose(); System.exit(0); });

        centro.add(Box.createVerticalGlue());
        centro.add(logo);
        centro.add(rigido(15));
        centro.add(subtitulo);
        centro.add(rigido(40));
        centro.add(btnCidadao); centro.add(rigido(12));
        centro.add(btnGestor);  centro.add(rigido(12));
        centro.add(btnAdmin);   centro.add(rigido(30));
        centro.add(btnSair);
        centro.add(Box.createVerticalGlue());

        // Barra inferior: botão de créditos ancorado à direita
        JPanel barraInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        barraInferior.setBackground(COR_FUNDO);

        JButton btnCreditos = new JButton("★ Créditos");
        btnCreditos.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCreditos.setForeground(new Color(120, 120, 160));
        btnCreditos.setBackground(COR_FUNDO);
        btnCreditos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 130), 1),
            BorderFactory.createEmptyBorder(5, 14, 5, 14)
        ));
        btnCreditos.setFocusPainted(false);
        btnCreditos.setContentAreaFilled(false);
        btnCreditos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreditos.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnCreditos.setForeground(Color.WHITE); }
            public void mouseExited (MouseEvent e) { btnCreditos.setForeground(new Color(120, 120, 160)); }
        });
        btnCreditos.addActionListener(e -> irParaCreditos());

        barraInferior.add(btnCreditos);

        outer.add(centro,        BorderLayout.CENTER);
        outer.add(barraInferior, BorderLayout.SOUTH);
        return outer;
    }

    // ==================== TELA CIDADÃO ====================

    private JPanel criarTelaCidadao() {
        JPanel painel = painelBase(30, 50);

        JButton btnDenuncia = criarBotao("Fazer Denúncia",       COR_BOTAO);
        JButton btnBuscar   = criarBotao("Ver Minhas Denúncias", COR_BOTAO);
        JButton btnVoltar   = botaoVoltar();

        btnDenuncia.addActionListener(e -> { limparCamposDenuncia(); irPara("fazer_denuncia"); });
        btnBuscar  .addActionListener(e -> { campoEmailBusca.setText(""); areaResultadoBusca.setText(""); irPara("buscar_denuncia"); });
        btnVoltar  .addActionListener(e -> irPara("menu"));

        painel.add(Box.createVerticalGlue());
        painel.add(titulo("CIDADÃO"));
        painel.add(rigido(40));
        painel.add(btnDenuncia); painel.add(rigido(12));
        painel.add(btnBuscar);   painel.add(rigido(30));
        painel.add(btnVoltar);
        painel.add(Box.createVerticalGlue());
        return painel;
    }

    // ==================== TELA FAZER DENÚNCIA ====================

    private JPanel criarTelaFazerDenuncia() {
        JPanel conteudo = painelBase(30, 100);

        campoNomeDen  = criarCampo();
        campoEmailDen = criarCampo();
        campoTelDen   = criarCampo();
        campoLocalDen = criarCampo();

        campoDescDen = new JTextArea(4, 30);
        campoDescDen.setFont(new Font("Arial", Font.PLAIN, 14));
        campoDescDen.setBackground(new Color(40, 40, 90));
        campoDescDen.setForeground(Color.WHITE);
        campoDescDen.setCaretColor(Color.WHITE);
        campoDescDen.setLineWrap(true);
        campoDescDen.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(campoDescDen);
        scrollDesc.setMaximumSize(new Dimension(500, 100));
        scrollDesc.setPreferredSize(new Dimension(500, 100));
        scrollDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollDesc.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 180), 1));

        JButton btnRegistrar = criarBotao("Registrar Denúncia", COR_BOTAO);
        JButton btnVoltar    = botaoVoltar();

        btnRegistrar.addActionListener(e -> {
            String nome  = campoNomeDen.getText().trim();
            String email = campoEmailDen.getText().trim();
            String tel   = campoTelDen.getText().trim();
            String local = campoLocalDen.getText().trim();
            String desc  = campoDescDen.getText().trim();
            if (nome.isEmpty() || email.isEmpty() || local.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios (*)!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String msg = denService.cadastrar(nome, email, tel, local, desc);
            JOptionPane.showMessageDialog(this, msg, "SALUS", JOptionPane.INFORMATION_MESSAGE);
            limparCamposDenuncia();
            irPara("cidadao");
        });
        btnVoltar.addActionListener(e -> irPara("cidadao"));

        conteudo.add(Box.createVerticalGlue());
        conteudo.add(titulo("NOVA DENÚNCIA"));  conteudo.add(rigido(20));
        conteudo.add(rotulo("Nome *"));         conteudo.add(rigido(5));
        conteudo.add(campoNomeDen);             conteudo.add(rigido(10));
        conteudo.add(rotulo("E-mail *"));       conteudo.add(rigido(5));
        conteudo.add(campoEmailDen);            conteudo.add(rigido(10));
        conteudo.add(rotulo("Telefone"));       conteudo.add(rigido(5));
        conteudo.add(campoTelDen);              conteudo.add(rigido(10));
        conteudo.add(rotulo("Local do Foco *")); conteudo.add(rigido(5));
        conteudo.add(campoLocalDen);            conteudo.add(rigido(10));
        conteudo.add(rotulo("Descrição *"));    conteudo.add(rigido(5));
        conteudo.add(scrollDesc);               conteudo.add(rigido(20));
        conteudo.add(btnRegistrar);             conteudo.add(rigido(10));
        conteudo.add(btnVoltar);
        conteudo.add(Box.createVerticalGlue());

        return emScrollPane(conteudo);
    }

    private void limparCamposDenuncia() {
        campoNomeDen.setText(""); campoEmailDen.setText("");
        campoTelDen.setText(""); campoLocalDen.setText(""); campoDescDen.setText("");
    }

    // ==================== TELA BUSCAR DENÚNCIA ====================

    private JPanel criarTelaBuscarDenuncia() {
        JPanel painel = painelBase(30, 100);

        campoEmailBusca = criarCampo();
        JButton btnBuscar = criarBotao("Buscar", COR_BOTAO);
        btnBuscar.setMaximumSize(new Dimension(200, 45));
        btnBuscar.setPreferredSize(new Dimension(200, 45));

        areaResultadoBusca = new JTextArea(10, 40);
        areaResultadoBusca.setEditable(false);
        areaResultadoBusca.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultadoBusca.setBackground(COR_AREA);
        areaResultadoBusca.setForeground(Color.WHITE);
        areaResultadoBusca.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollRes = new JScrollPane(areaResultadoBusca);
        scrollRes.setMaximumSize(new Dimension(600, 250));
        scrollRes.setPreferredSize(new Dimension(600, 250));
        scrollRes.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnVoltar = botaoVoltar();

        btnBuscar.addActionListener(e -> {
            String email = campoEmailBusca.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite seu e-mail!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Denuncia[] resultado = denService.buscarPorEmail(email);
            if (resultado.length == 0) {
                areaResultadoBusca.setText("Nenhuma denúncia encontrada para este e-mail.");
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
                areaResultadoBusca.setText(sb.toString());
                areaResultadoBusca.setCaretPosition(0);
            }
        });
        btnVoltar.addActionListener(e -> irPara("cidadao"));

        painel.add(Box.createVerticalGlue());
        painel.add(titulo("MINHAS DENÚNCIAS"));      painel.add(rigido(20));
        painel.add(rotulo("Seu E-mail"));            painel.add(rigido(5));
        painel.add(campoEmailBusca);                 painel.add(rigido(10));
        painel.add(btnBuscar);                       painel.add(rigido(15));
        painel.add(scrollRes);                       painel.add(rigido(15));
        painel.add(btnVoltar);
        painel.add(Box.createVerticalGlue());
        return painel;
    }

    // ==================== TELA LOGIN GESTOR ====================

    private JPanel criarTelaLoginGestor() {
        JPanel painel = painelBase(30, 50);

        campoEmailGestor = criarCampo();
        campoSenhaGestor = new JPasswordField(20);
        estilizarCampo(campoSenhaGestor);

        JButton btnEntrar = criarBotao("Entrar", COR_BOTAO);
        JButton btnVoltar = botaoVoltar();

        btnEntrar.addActionListener(e -> {
            String email = campoEmailGestor.getText().trim();
            String senha = new String(campoSenhaGestor.getPassword());
            if (funcService.loginGestor(email, senha)) {
                campoEmailGestor.setText(""); campoSenhaGestor.setText("");
                irParaGestor();
            } else {
                JOptionPane.showMessageDialog(this, "E-mail ou senha inválidos!", "Falha no Login", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnVoltar.addActionListener(e -> { campoEmailGestor.setText(""); campoSenhaGestor.setText(""); irPara("menu"); });

        painel.add(Box.createVerticalGlue());
        painel.add(titulo("LOGIN — GESTOR")); painel.add(rigido(30));
        painel.add(rotulo("E-mail"));         painel.add(rigido(5));
        painel.add(campoEmailGestor);         painel.add(rigido(15));
        painel.add(rotulo("Senha"));          painel.add(rigido(5));
        painel.add(campoSenhaGestor);         painel.add(rigido(25));
        painel.add(btnEntrar);                painel.add(rigido(10));
        painel.add(btnVoltar);
        painel.add(Box.createVerticalGlue());
        return painel;
    }

    // ==================== TELA GESTOR (dinâmica) ====================

    private void construirTelaGestor() {
        painelGestor.removeAll();
        painelGestor.setLayout(new BoxLayout(painelGestor, BoxLayout.Y_AXIS));
        painelGestor.setBackground(COR_FUNDO);
        painelGestor.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        int[] dash = denService.getDashboard();
        JLabel lblDash = new JLabel(String.format(
            "Total: %d  |  Pendentes: %d  |  Verídicas: %d  |  Falsas: %d",
            dash[0], dash[1], dash[2], dash[3]));
        lblDash.setFont(new Font("Arial", Font.BOLD, 16));
        lblDash.setForeground(new Color(255, 200, 100));
        lblDash.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnListar   = criarBotao("Listar Denúncias",   COR_BOTAO);
        JButton btnVistoria = criarBotao("Vistoriar Denúncia", COR_BOTAO);
        JButton btnVoltar   = botaoVoltar();
        btnVoltar.setText("← Sair");

        btnListar  .addActionListener(e -> irParaListarDenuncias("gestor"));
        btnVistoria.addActionListener(e -> irParaVistoriar());
        btnVoltar  .addActionListener(e -> irPara("menu"));

        painelGestor.add(Box.createVerticalGlue());
        painelGestor.add(titulo("PAINEL DO GESTOR")); painelGestor.add(rigido(15));
        painelGestor.add(lblDash);                    painelGestor.add(rigido(40));
        painelGestor.add(btnListar);                  painelGestor.add(rigido(12));
        painelGestor.add(btnVistoria);                painelGestor.add(rigido(30));
        painelGestor.add(btnVoltar);
        painelGestor.add(Box.createVerticalGlue());

        painelGestor.revalidate();
        painelGestor.repaint();
    }

    // ==================== TELA LISTAR DENÚNCIAS (dinâmica) ====================

    private void construirTelaListarDenuncias(String voltarCard) {
        painelListarDenuncias.removeAll();
        painelListarDenuncias.setLayout(new BorderLayout());
        painelListarDenuncias.setBackground(COR_FUNDO);

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topo.setBackground(COR_FUNDO);
        topo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        topo.add(titulo("LISTA DE DENÚNCIAS"));

        Denuncia[] lista = denService.listar();
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBackground(COR_AREA);
        area.setForeground(Color.WHITE);
        area.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        if (lista.length == 0) {
            area.setText("  Nenhuma denúncia registrada.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lista.length; i++) {
                Denuncia d = lista[i];
                sb.append("══════════════════════════════════════════════════════\n");
                sb.append(String.format("[%d]  %s  →  %s  |  %s\n", i + 1, d.descricao, d.status, d.dataHora));
                sb.append(String.format("     Nome: %s | E-mail: %s | Tel: %s\n", d.nome, d.email, d.telefone));
                sb.append(String.format("     Local: %s\n", d.local));
                if (d.observacao != null && !d.observacao.isEmpty()) {
                    sb.append(String.format("     Vistoria: %s | Obs: %s\n", d.dataHoraVistoria, d.observacao));
                }
            }
            area.setText(sb.toString());
            area.setCaretPosition(0);
        }

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBackground(COR_FUNDO);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        JButton btnVoltar = botaoVoltar();
        btnVoltar.addActionListener(e -> irPara(voltarCard));
        rodape.add(btnVoltar);

        painelListarDenuncias.add(topo,   BorderLayout.NORTH);
        painelListarDenuncias.add(scroll, BorderLayout.CENTER);
        painelListarDenuncias.add(rodape, BorderLayout.SOUTH);

        painelListarDenuncias.revalidate();
        painelListarDenuncias.repaint();
    }

    // ==================== TELA VISTORIAR (dinâmica) ====================

    private void construirTelaVistoriar() {
        painelVistoriar.removeAll();
        painelVistoriar.setLayout(new BoxLayout(painelVistoriar, BoxLayout.Y_AXIS));
        painelVistoriar.setBackground(COR_FUNDO);
        painelVistoriar.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        Denuncia[] lista = denService.listar();

        if (lista.length == 0) {
            JButton btnVoltar = botaoVoltar();
            btnVoltar.addActionListener(e -> irParaGestor());
            painelVistoriar.add(Box.createVerticalGlue());
            painelVistoriar.add(titulo("VISTORIAR DENÚNCIA")); painelVistoriar.add(rigido(20));
            painelVistoriar.add(mensagem("Nenhuma denúncia para vistoriar."));
            painelVistoriar.add(rigido(20));
            painelVistoriar.add(btnVoltar);
            painelVistoriar.add(Box.createVerticalGlue());
            painelVistoriar.revalidate(); painelVistoriar.repaint();
            return;
        }

        String[] opcoes = new String[lista.length];
        for (int i = 0; i < lista.length; i++) {
            opcoes[i] = String.format("[%d] %s  —  %s", i + 1, lista[i].descricao, lista[i].status);
        }
        JComboBox<String> combo = new JComboBox<>(opcoes);
        combo.setFont(new Font("Arial", Font.PLAIN, 14));
        combo.setMaximumSize(new Dimension(600, 40));
        combo.setPreferredSize(new Dimension(600, 40));
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea campoObs = new JTextArea(3, 30);
        campoObs.setFont(new Font("Arial", Font.PLAIN, 14));
        campoObs.setBackground(new Color(40, 40, 90));
        campoObs.setForeground(Color.WHITE);
        campoObs.setCaretColor(Color.WHITE);
        campoObs.setLineWrap(true);
        campoObs.setWrapStyleWord(true);
        JScrollPane scrollObs = new JScrollPane(campoObs);
        scrollObs.setMaximumSize(new Dimension(500, 90));
        scrollObs.setPreferredSize(new Dimension(500, 90));
        scrollObs.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollObs.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 180), 1));

        JButton btnVeridico = criarBotao("✓  Verídico", new Color(34, 139, 34));
        JButton btnFalsa    = criarBotao("✗  Falsa",    new Color(180, 30, 30));
        btnVeridico.setMaximumSize(new Dimension(200, 45)); btnVeridico.setPreferredSize(new Dimension(200, 45));
        btnFalsa   .setMaximumSize(new Dimension(200, 45)); btnFalsa   .setPreferredSize(new Dimension(200, 45));

        JButton btnVoltar = botaoVoltar();

        ActionListener acaoVistoria = e -> {
            String obs = campoObs.getText().trim();
            if (obs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite uma observação!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean veridico = (e.getSource() == btnVeridico);
            String msg = denService.vistoriar(combo.getSelectedIndex(), obs, veridico);
            JOptionPane.showMessageDialog(this, msg, "SALUS", JOptionPane.INFORMATION_MESSAGE);
            irParaGestor();
        };
        btnVeridico.addActionListener(acaoVistoria);
        btnFalsa   .addActionListener(acaoVistoria);
        btnVoltar  .addActionListener(e -> irParaGestor());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        painelBotoes.setBackground(COR_FUNDO);
        painelBotoes.setMaximumSize(new Dimension(500, 60));
        painelBotoes.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelBotoes.add(btnVeridico);
        painelBotoes.add(btnFalsa);

        painelVistoriar.add(Box.createVerticalGlue());
        painelVistoriar.add(titulo("VISTORIAR DENÚNCIA")); painelVistoriar.add(rigido(20));
        painelVistoriar.add(rotulo("Selecione a Denúncia")); painelVistoriar.add(rigido(8));
        painelVistoriar.add(combo);                          painelVistoriar.add(rigido(15));
        painelVistoriar.add(rotulo("Observação da Vistoria")); painelVistoriar.add(rigido(8));
        painelVistoriar.add(scrollObs);                      painelVistoriar.add(rigido(20));
        painelVistoriar.add(painelBotoes);                   painelVistoriar.add(rigido(15));
        painelVistoriar.add(btnVoltar);
        painelVistoriar.add(Box.createVerticalGlue());

        painelVistoriar.revalidate();
        painelVistoriar.repaint();
    }

    // ==================== TELA LOGIN ADMIN ====================

    private JPanel criarTelaLoginAdmin() {
        JPanel painel = painelBase(30, 50);

        campoEmailAdmin = criarCampo();
        campoSenhaAdmin = new JPasswordField(20);
        estilizarCampo(campoSenhaAdmin);

        JButton btnEntrar = criarBotao("Entrar", COR_BOTAO);
        JButton btnVoltar = botaoVoltar();

        btnEntrar.addActionListener(e -> {
            String email = campoEmailAdmin.getText().trim();
            String senha = new String(campoSenhaAdmin.getPassword());
            if (funcService.loginAdmin(email, senha)) {
                campoEmailAdmin.setText(""); campoSenhaAdmin.setText("");
                irParaAdmin();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciais inválidas!", "Falha no Login", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnVoltar.addActionListener(e -> { campoEmailAdmin.setText(""); campoSenhaAdmin.setText(""); irPara("menu"); });

        painel.add(Box.createVerticalGlue());
        painel.add(titulo("LOGIN — ADMINISTRADOR")); painel.add(rigido(30));
        painel.add(rotulo("E-mail"));                painel.add(rigido(5));
        painel.add(campoEmailAdmin);                 painel.add(rigido(15));
        painel.add(rotulo("Senha"));                 painel.add(rigido(5));
        painel.add(campoSenhaAdmin);                 painel.add(rigido(25));
        painel.add(btnEntrar);                       painel.add(rigido(10));
        painel.add(btnVoltar);
        painel.add(Box.createVerticalGlue());
        return painel;
    }

    // ==================== TELA ADMIN (dinâmica) ====================

    private void construirTelaAdmin() {
        painelAdmin.removeAll();
        painelAdmin.setLayout(new BoxLayout(painelAdmin, BoxLayout.Y_AXIS));
        painelAdmin.setBackground(COR_FUNDO);
        painelAdmin.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton btnAdicionar = criarBotao("Adicionar Funcionário",  COR_BOTAO);
        JButton btnListar    = criarBotao("Listar Funcionários",    COR_BOTAO);
        JButton btnDesativar = criarBotao("Desativar Funcionário",  COR_BOTAO);
        JButton btnAtivar    = criarBotao("Ativar Funcionário",     COR_BOTAO);
        JButton btnVoltar    = botaoVoltar();
        btnVoltar.setText("← Sair");

        btnAdicionar.addActionListener(e -> { limparCamposFuncionario(); irPara("add_funcionario"); });
        btnListar   .addActionListener(e -> irParaListarFuncs("admin"));
        btnDesativar.addActionListener(e -> irParaAlterarStatus(false));
        btnAtivar   .addActionListener(e -> irParaAlterarStatus(true));
        btnVoltar   .addActionListener(e -> irPara("menu"));

        painelAdmin.add(Box.createVerticalGlue());
        painelAdmin.add(titulo("PAINEL DO ADMINISTRADOR")); painelAdmin.add(rigido(40));
        painelAdmin.add(btnAdicionar); painelAdmin.add(rigido(12));
        painelAdmin.add(btnListar);    painelAdmin.add(rigido(12));
        painelAdmin.add(btnDesativar); painelAdmin.add(rigido(12));
        painelAdmin.add(btnAtivar);    painelAdmin.add(rigido(30));
        painelAdmin.add(btnVoltar);
        painelAdmin.add(Box.createVerticalGlue());

        painelAdmin.revalidate();
        painelAdmin.repaint();
    }

    // ==================== TELA ADICIONAR FUNCIONÁRIO ====================

    private JPanel criarTelaAddFuncionario() {
        JPanel conteudo = painelBase(30, 100);

        campoNomeFunc  = criarCampo();
        campoCPF       = criarCampo();
        campoEmailFunc = criarCampo();
        campoTelFunc   = criarCampo();
        campoSenhaFunc = criarCampo();
        campoCargoFunc = criarCampo();
        campoSetorFunc = criarCampo();

        JButton btnCadastrar = criarBotao("Cadastrar Funcionário", COR_BOTAO);
        JButton btnVoltar    = botaoVoltar();

        btnCadastrar.addActionListener(e -> {
            String nome  = campoNomeFunc.getText().trim();
            String cpf   = campoCPF.getText().trim();
            String email = campoEmailFunc.getText().trim();
            String tel   = campoTelFunc.getText().trim();
            String senha = campoSenhaFunc.getText().trim();
            String cargo = campoCargoFunc.getText().trim();
            String setor = campoSetorFunc.getText().trim();
            if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty() || cargo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios (*)!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String msg = funcService.adicionar(nome, cpf, email, tel, senha, cargo, setor);
            JOptionPane.showMessageDialog(this, msg, "SALUS", JOptionPane.INFORMATION_MESSAGE);
            limparCamposFuncionario();
            irParaAdmin();
        });
        btnVoltar.addActionListener(e -> irParaAdmin());

        conteudo.add(Box.createVerticalGlue());
        conteudo.add(titulo("NOVO FUNCIONÁRIO")); conteudo.add(rigido(20));
        conteudo.add(rotulo("Nome *"));          conteudo.add(rigido(5)); conteudo.add(campoNomeFunc);  conteudo.add(rigido(10));
        conteudo.add(rotulo("CPF *"));           conteudo.add(rigido(5)); conteudo.add(campoCPF);       conteudo.add(rigido(10));
        conteudo.add(rotulo("E-mail *"));        conteudo.add(rigido(5)); conteudo.add(campoEmailFunc); conteudo.add(rigido(10));
        conteudo.add(rotulo("Telefone"));        conteudo.add(rigido(5)); conteudo.add(campoTelFunc);   conteudo.add(rigido(10));
        conteudo.add(rotulo("Senha *"));         conteudo.add(rigido(5)); conteudo.add(campoSenhaFunc); conteudo.add(rigido(10));
        conteudo.add(rotulo("Cargo *"));         conteudo.add(rigido(5)); conteudo.add(campoCargoFunc); conteudo.add(rigido(10));
        conteudo.add(rotulo("Setor"));           conteudo.add(rigido(5)); conteudo.add(campoSetorFunc); conteudo.add(rigido(20));
        conteudo.add(btnCadastrar);              conteudo.add(rigido(10));
        conteudo.add(btnVoltar);
        conteudo.add(Box.createVerticalGlue());

        return emScrollPane(conteudo);
    }

    private void limparCamposFuncionario() {
        campoNomeFunc.setText(""); campoCPF.setText(""); campoEmailFunc.setText("");
        campoTelFunc.setText(""); campoSenhaFunc.setText(""); campoCargoFunc.setText(""); campoSetorFunc.setText("");
    }

    // ==================== TELA LISTAR FUNCIONÁRIOS (dinâmica) ====================

    private void construirTelaListarFuncs(String voltarCard) {
        painelListarFuncs.removeAll();
        painelListarFuncs.setLayout(new BorderLayout());
        painelListarFuncs.setBackground(COR_FUNDO);

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topo.setBackground(COR_FUNDO);
        topo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        topo.add(titulo("FUNCIONÁRIOS CADASTRADOS"));

        Funcionario[] lista = funcService.listar();
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBackground(COR_AREA);
        area.setForeground(Color.WHITE);
        area.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        if (lista.length == 0) {
            area.setText("  Nenhum funcionário cadastrado.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lista.length; i++) {
                Funcionario f = lista[i];
                sb.append("══════════════════════════════════════════════════════\n");
                sb.append(String.format("[%d]  %s  |  Cargo: %s  |  %s\n",
                    i + 1, f.nome, f.cargo, f.ativo ? "✔ ATIVO" : "✘ INATIVO"));
                sb.append(String.format("     CPF: %s | E-mail: %s | Tel: %s\n", f.cpf, f.email, f.telefone));
                sb.append(String.format("     Setor: %s | Cadastro: %s\n", f.setor, f.dataCadastro));
            }
            area.setText(sb.toString());
            area.setCaretPosition(0);
        }

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBackground(COR_FUNDO);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        JButton btnVoltar = botaoVoltar();
        btnVoltar.addActionListener(e -> irPara(voltarCard));
        rodape.add(btnVoltar);

        painelListarFuncs.add(topo,   BorderLayout.NORTH);
        painelListarFuncs.add(scroll, BorderLayout.CENTER);
        painelListarFuncs.add(rodape, BorderLayout.SOUTH);

        painelListarFuncs.revalidate();
        painelListarFuncs.repaint();
    }

    // ==================== TELA ALTERAR STATUS (dinâmica) ====================

    private void construirTelaAlterarStatus(boolean ativar) {
        painelAlterarStatus.removeAll();
        painelAlterarStatus.setLayout(new BoxLayout(painelAlterarStatus, BoxLayout.Y_AXIS));
        painelAlterarStatus.setBackground(COR_FUNDO);
        painelAlterarStatus.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        String acao = ativar ? "ATIVAR" : "DESATIVAR";
        Funcionario[] lista = funcService.listar();

        if (lista.length == 0) {
            JButton btnVoltar = botaoVoltar();
            btnVoltar.addActionListener(e -> irParaAdmin());
            painelAlterarStatus.add(Box.createVerticalGlue());
            painelAlterarStatus.add(titulo(acao + " FUNCIONÁRIO")); painelAlterarStatus.add(rigido(20));
            painelAlterarStatus.add(mensagem("Nenhum funcionário cadastrado."));
            painelAlterarStatus.add(rigido(20)); painelAlterarStatus.add(btnVoltar);
            painelAlterarStatus.add(Box.createVerticalGlue());
            painelAlterarStatus.revalidate(); painelAlterarStatus.repaint();
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
            irParaAdmin();
        });
        btnVoltar.addActionListener(e -> irParaAdmin());

        painelAlterarStatus.add(Box.createVerticalGlue());
        painelAlterarStatus.add(titulo(acao + " FUNCIONÁRIO")); painelAlterarStatus.add(rigido(20));
        painelAlterarStatus.add(rotulo("Selecione o Funcionário")); painelAlterarStatus.add(rigido(8));
        painelAlterarStatus.add(combo);           painelAlterarStatus.add(rigido(25));
        painelAlterarStatus.add(btnConfirmar);    painelAlterarStatus.add(rigido(12));
        painelAlterarStatus.add(btnVoltar);
        painelAlterarStatus.add(Box.createVerticalGlue());

        painelAlterarStatus.revalidate();
        painelAlterarStatus.repaint();
    }

    // ==================== HELPERS ====================

    private JPanel painelBase(int vertical, int horizontal) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(COR_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(vertical, horizontal, vertical, horizontal));
        return p;
    }

    private JPanel emScrollPane(JPanel conteudo) {
        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setBackground(COR_FUNDO);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(COR_FUNDO);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(COR_FUNDO);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    private JLabel titulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 28));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private JLabel rotulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setForeground(COR_SUBTXT);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private JLabel mensagem(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 16));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private Component rigido(int altura) {
        return Box.createRigidArea(new Dimension(0, altura));
    }

    private JTextField criarCampo() {
        JTextField campo = new JTextField(20);
        estilizarCampo(campo);
        return campo;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBackground(new Color(40, 40, 90));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 180), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setMaximumSize(new Dimension(500, 40));
        campo.setPreferredSize(new Dimension(500, 40));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private JButton botaoVoltar() {
        JButton btn = new JButton("← Voltar");
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setForeground(COR_SUBTXT);
        btn.setBackground(COR_FUNDO);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(Color.WHITE); }
            public void mouseExited(MouseEvent e)  { btn.setForeground(COR_SUBTXT); }
        });
        return btn;
    }

    private JLabel carregarLogo(int w, int h) {
        JLabel lbl = new JLabel();
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/logo.png"));
        if (icon.getIconWidth() > 0) {
            Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(img));
        } else {
            lbl.setText("SALUS");
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.BOLD, 32));
        }
        return lbl;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        botao.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        botao.setFont(new Font("Arial", Font.BOLD, 18));
        botao.setForeground(Color.WHITE);
        botao.setBackground(cor);
        botao.setOpaque(false);
        botao.setContentAreaFilled(false);
        botao.setMaximumSize(new Dimension(350, 50));
        botao.setPreferredSize(new Dimension(350, 50));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { botao.setBackground(cor.darker()); }
            public void mouseExited(MouseEvent e)  { botao.setBackground(cor); }
        });
        return botao;
    }

    // ==================== MAIN ====================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SalusSystem().setVisible(true));
    }
}
