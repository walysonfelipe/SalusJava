package models;

public class VistoriaLoteDenuncia {
    private int idVistoriaLoteDenuncia;
    private int idVistoriaLote;
    private Denuncia denuncia;
    private Fiscal fiscal;
    private String constatadaProcedencia;
    private String fotoEvidencia;
    private String statusDenunciaPosVistoria;

    public VistoriaLoteDenuncia() {}

    public int getIdVistoriaLoteDenuncia() { return idVistoriaLoteDenuncia; }
    public void setIdVistoriaLoteDenuncia(int idVistoriaLoteDenuncia) { this.idVistoriaLoteDenuncia = idVistoriaLoteDenuncia; }

    public int getIdVistoriaLote() { return idVistoriaLote; }
    public void setIdVistoriaLote(int idVistoriaLote) { this.idVistoriaLote = idVistoriaLote; }

    public Denuncia getDenuncia() { return denuncia; }
    public void setDenuncia(Denuncia denuncia) { this.denuncia = denuncia; }

    public Fiscal getFiscal() { return fiscal; }
    public void setFiscal(Fiscal fiscal) { this.fiscal = fiscal; }

    public String getConstatadaProcedencia() { return constatadaProcedencia; }
    public void setConstatadaProcedencia(String constatadaProcedencia) { this.constatadaProcedencia = constatadaProcedencia; }

    public String getFotoEvidencia() { return fotoEvidencia; }
    public void setFotoEvidencia(String fotoEvidencia) { this.fotoEvidencia = fotoEvidencia; }

    public String getStatusDenunciaPosVistoria() { return statusDenunciaPosVistoria; }
    public void setStatusDenunciaPosVistoria(String s) { this.statusDenunciaPosVistoria = s; }
}
