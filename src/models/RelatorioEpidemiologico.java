package models;

public class RelatorioEpidemiologico {
    private int idRelatorio;
    private String periodo;
    private String regiao;
    private int totalDenuncias;
    private int totalVistorias;
    private int totalFocosEncontrados;
    private int totalFocosEliminados;
    private double indiceInfestacao;
    private Usuario geradoPor;
    private String dataGeracao;

    public RelatorioEpidemiologico() {}

    public int getIdRelatorio() { return idRelatorio; }
    public void setIdRelatorio(int idRelatorio) { this.idRelatorio = idRelatorio; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public String getRegiao() { return regiao; }
    public void setRegiao(String regiao) { this.regiao = regiao; }

    public int getTotalDenuncias() { return totalDenuncias; }
    public void setTotalDenuncias(int totalDenuncias) { this.totalDenuncias = totalDenuncias; }

    public int getTotalVistorias() { return totalVistorias; }
    public void setTotalVistorias(int totalVistorias) { this.totalVistorias = totalVistorias; }

    public int getTotalFocosEncontrados() { return totalFocosEncontrados; }
    public void setTotalFocosEncontrados(int totalFocosEncontrados) { this.totalFocosEncontrados = totalFocosEncontrados; }

    public int getTotalFocosEliminados() { return totalFocosEliminados; }
    public void setTotalFocosEliminados(int totalFocosEliminados) { this.totalFocosEliminados = totalFocosEliminados; }

    public double getIndiceInfestacao() { return indiceInfestacao; }
    public void setIndiceInfestacao(double indiceInfestacao) { this.indiceInfestacao = indiceInfestacao; }

    public Usuario getGeradoPor() { return geradoPor; }
    public void setGeradoPor(Usuario geradoPor) { this.geradoPor = geradoPor; }

    public String getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(String dataGeracao) { this.dataGeracao = dataGeracao; }
}
