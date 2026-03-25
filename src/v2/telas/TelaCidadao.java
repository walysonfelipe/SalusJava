package v2.telas;

import javax.swing.*;

import static v2.ui.UIFactory.*;
import static v2.ui.SalusTheme.*;

/**
 * Sub-menu do Cidadão com opções de denúncia.
 */
public class TelaCidadao extends JPanel {

    private final Navegador navegador;
    private final TelaFazerDenuncia telaFazerDenuncia;
    private final TelaBuscarDenuncia telaBuscarDenuncia;

    public TelaCidadao(Navegador navegador, TelaFazerDenuncia telaFazerDenuncia,
                       TelaBuscarDenuncia telaBuscarDenuncia) {
        this.navegador = navegador;
        this.telaFazerDenuncia = telaFazerDenuncia;
        this.telaBuscarDenuncia = telaBuscarDenuncia;
        construir();
    }

    private void construir() {
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton btnDenuncia = criarBotao("Fazer Denúncia",       COR_BOTAO);
        JButton btnBuscar   = criarBotao("Ver Minhas Denúncias", COR_BOTAO);
        JButton btnVoltar   = botaoVoltar();

        btnDenuncia.addActionListener(e -> { telaFazerDenuncia.limparCampos(); navegador.irPara("fazer_denuncia"); });
        btnBuscar  .addActionListener(e -> { telaBuscarDenuncia.limparCampos(); navegador.irPara("buscar_denuncia"); });
        btnVoltar  .addActionListener(e -> navegador.irPara("menu"));

        add(javax.swing.Box.createVerticalGlue());
        add(titulo("CIDADÃO"));
        add(rigido(40));
        add(btnDenuncia); add(rigido(12));
        add(btnBuscar);   add(rigido(30));
        add(btnVoltar);
        add(javax.swing.Box.createVerticalGlue());
    }
}
