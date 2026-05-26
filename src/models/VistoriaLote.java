package models;

import java.util.ArrayList;
import java.util.List;

public class VistoriaLote {
    private int idVistoriaLote;
    private Gestor gestor;
    private Fiscal fiscal;
    private String tipoAcaoFiscal;
    private String dataProgramada;
    private String dataRealizacao;
    private String relatorioDetalhado;
    private int focosEncontrados;
    private String acoesTomadas;
    private String observacoes;
    private List<VistoriaLoteDenuncia> itens;

    public VistoriaLote() {
        this.itens = new ArrayList<>();
        this.focosEncontrados = 0;
    }

    public int getIdVistoriaLote() { return idVistoriaLote; }
    public void setIdVistoriaLote(int idVistoriaLote) { this.idVistoriaLote = idVistoriaLote; }

    public Gestor getGestor() { return gestor; }
    public void setGestor(Gestor gestor) { this.gestor = gestor; }

    public Fiscal getFiscal() { return fiscal; }
    public void setFiscal(Fiscal fiscal) { this.fiscal = fiscal; }

    public String getTipoAcaoFiscal() { return tipoAcaoFiscal; }
    public void setTipoAcaoFiscal(String tipoAcaoFiscal) { this.tipoAcaoFiscal = tipoAcaoFiscal; }

    public String getDataProgramada() { return dataProgramada; }
    public void setDataProgramada(String dataProgramada) { this.dataProgramada = dataProgramada; }

    public String getDataRealizacao() { return dataRealizacao; }
    public void setDataRealizacao(String dataRealizacao) { this.dataRealizacao = dataRealizacao; }

    public String getRelatorioDetalhado() { return relatorioDetalhado; }
    public void setRelatorioDetalhado(String relatorioDetalhado) { this.relatorioDetalhado = relatorioDetalhado; }

    public int getFocosEncontrados() { return focosEncontrados; }
    public void setFocosEncontrados(int focosEncontrados) { this.focosEncontrados = focosEncontrados; }

    public String getAcoesTomadas() { return acoesTomadas; }
    public void setAcoesTomadas(String acoesTomadas) { this.acoesTomadas = acoesTomadas; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public List<VistoriaLoteDenuncia> getItens() { return itens; }
    public void setItens(List<VistoriaLoteDenuncia> itens) { this.itens = itens; }
}
