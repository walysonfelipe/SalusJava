package models;

public class VistoriaLoteDenuncia {
    private int idVistoriaLoteDenuncia;
    private int idVistoriaLote;
    private Denuncia denuncia;
    private Funcionario funcionarioFiscal;
    private String constatadaProcedencia;
    private String fotoEvidencia;
    private String statusDenunciaPosVistoria;
    private String tipoImovel;
    private String moradores;
    private String acessibilidade;
    private String tipoReserv;
    private String condicReserv;

    public VistoriaLoteDenuncia() {}

    public int getIdVistoriaLoteDenuncia() { return idVistoriaLoteDenuncia; }
    public void setIdVistoriaLoteDenuncia(int idVistoriaLoteDenuncia) { this.idVistoriaLoteDenuncia = idVistoriaLoteDenuncia; }

    public int getIdVistoriaLote() { return idVistoriaLote; }
    public void setIdVistoriaLote(int idVistoriaLote) { this.idVistoriaLote = idVistoriaLote; }

    public Denuncia getDenuncia() { return denuncia; }
    public void setDenuncia(Denuncia denuncia) { this.denuncia = denuncia; }

    public Funcionario getFuncionarioFiscal() { return funcionarioFiscal; }
    public void setFuncionarioFiscal(Funcionario funcionarioFiscal) { this.funcionarioFiscal = funcionarioFiscal; }

    public String getConstatadaProcedencia() { return constatadaProcedencia; }
    public void setConstatadaProcedencia(String constatadaProcedencia) { this.constatadaProcedencia = constatadaProcedencia; }

    public String getFotoEvidencia() { return fotoEvidencia; }
    public void setFotoEvidencia(String fotoEvidencia) { this.fotoEvidencia = fotoEvidencia; }

    public String getStatusDenunciaPosVistoria() { return statusDenunciaPosVistoria; }
    public void setStatusDenunciaPosVistoria(String statusDenunciaPosVistoria) { this.statusDenunciaPosVistoria = statusDenunciaPosVistoria; }

    public String getTipoImovel() { return tipoImovel; }
    public void setTipoImovel(String tipoImovel) { this.tipoImovel = tipoImovel; }

    public String getMoradores() { return moradores; }
    public void setMoradores(String moradores) { this.moradores = moradores; }

    public String getAcessibilidade() { return acessibilidade; }
    public void setAcessibilidade(String acessibilidade) { this.acessibilidade = acessibilidade; }

    public String getTipoReserv() { return tipoReserv; }
    public void setTipoReserv(String tipoReserv) { this.tipoReserv = tipoReserv; }

    public String getCondicReserv() { return condicReserv; }
    public void setCondicReserv(String condicReserv) { this.condicReserv = condicReserv; }
}
