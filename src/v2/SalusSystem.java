package v2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import v2.services.DenunciaServiceV2;
import v2.services.FuncionarioServiceV2;
import v2.telas.*;

/**
 * Ponto de entrada do sistema SALUS.
 * Gerencia a navegação entre telas via CardLayout.
 */
public class SalusSystem extends JFrame implements Navegador {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    // ===== Services =====
    private final DenunciaServiceV2 denService  = new DenunciaServiceV2();
    private final FuncionarioServiceV2 funcService = new FuncionarioServiceV2();

    // ===== Telas dinâmicas (reconstruídas a cada navegação) =====
    private final TelaGestor             telaGestor;
    private final TelaListarDenuncias    telaListarDenuncias;
    private final TelaVistoriar          telaVistoriar;
    private final TelaAdmin             telaAdmin;
    private final TelaListarFuncionarios telaListarFuncs;
    private final TelaAlterarStatus      telaAlterarStatus;
    private final TelaCarregamento       telaCarregamento;
    private final TelaCreditos           telaCreditos;

    public SalusSystem() {
        setTitle("SALUS - Sistema de Saúde");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());

        // Instanciar telas
        telaCarregamento       = new TelaCarregamento(this);
        telaCreditos           = new TelaCreditos(() -> irPara("menu"));
        TelaFazerDenuncia  telaFazerDen  = new TelaFazerDenuncia(this, denService);
        TelaBuscarDenuncia telaBuscarDen = new TelaBuscarDenuncia(this, denService);
        TelaAddFuncionario telaAddFunc   = new TelaAddFuncionario(this, funcService);

        telaGestor          = new TelaGestor(this, denService);
        telaListarDenuncias = new TelaListarDenuncias(this, denService);
        telaVistoriar       = new TelaVistoriar(this, denService);
        telaAdmin           = new TelaAdmin(this, telaAddFunc);
        telaListarFuncs     = new TelaListarFuncionarios(this, funcService);
        telaAlterarStatus   = new TelaAlterarStatus(this, funcService);

        // Registrar telas no CardLayout
        mainPanel.add(telaCarregamento,                      "loading");
        mainPanel.add(new TelaMenu(this, telaCreditos),      "menu");
        mainPanel.add(new TelaCidadao(this, telaFazerDen, telaBuscarDen), "cidadao");
        mainPanel.add(telaFazerDen,                          "fazer_denuncia");
        mainPanel.add(telaBuscarDen,                         "buscar_denuncia");
        mainPanel.add(new TelaLoginGestor(this, funcService),"login_gestor");
        mainPanel.add(new TelaLoginAdmin(this, funcService), "login_admin");
        mainPanel.add(telaAddFunc,                           "add_funcionario");
        mainPanel.add(telaCreditos,                          "creditos");
        mainPanel.add(telaGestor,                            "gestor");
        mainPanel.add(telaListarDenuncias,                   "listar_denuncias");
        mainPanel.add(telaVistoriar,                         "vistoriar");
        mainPanel.add(telaAdmin,                             "admin");
        mainPanel.add(telaListarFuncs,                       "listar_funcs");
        mainPanel.add(telaAlterarStatus,                     "alterar_status");

        add(mainPanel);
        cardLayout.show(mainPanel, "loading");
        telaCarregamento.iniciarCarregamento();

        // ESC para sair
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "sair");
        getRootPane().getActionMap().put("sair", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { dispose(); System.exit(0); }
        });
    }

    // ==================== NAVEGAÇÃO ====================

    @Override
    public void irPara(String tela) {
        // Telas dinâmicas: reconstruir antes de exibir
        switch (tela) {
            case "gestor" -> telaGestor.construir();
            case "listar_denuncias_gestor" -> {
                telaListarDenuncias.construir("gestor");
                tela = "listar_denuncias";
            }
            case "listar_denuncias_admin" -> {
                telaListarDenuncias.construir("admin");
                tela = "listar_denuncias";
            }
            case "vistoriar" -> telaVistoriar.construir();
            case "admin" -> telaAdmin.construir();
            case "listar_funcs_admin" -> {
                telaListarFuncs.construir("admin");
                tela = "listar_funcs";
            }
            case "ativar_func" -> {
                telaAlterarStatus.construir(true);
                tela = "alterar_status";
            }
            case "desativar_func" -> {
                telaAlterarStatus.construir(false);
                tela = "alterar_status";
            }
        }
        cardLayout.show(mainPanel, tela);
    }

    // ==================== MAIN ====================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SalusSystem().setVisible(true));
    }
}
