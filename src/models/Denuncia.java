package models;

import java.util.ArrayList;
import java.util.List;

public class Denuncia {
    private int idDenuncia;
    private Cidadao cidadao;
    private String enderecoDenuncia;
    private String descricaoDenuncia;
    private String dataEnvio;
    private String tipoAcaoFiscal;
    private String statusDenuncia;
    private String protocolo;
    private List<VistoriaLoteDenuncia> vistorias;

    public Denuncia() {
        this.statusDenuncia = "PENDENTE";
        this.vistorias = new ArrayList<>();
    }

    public int getIdDenuncia() { return idDenuncia; }
    public void setIdDenuncia(int idDenuncia) { this.idDenuncia = idDenuncia; }

    public Cidadao getCidadao() { return cidadao; }
    public void setCidadao(Cidadao cidadao) { this.cidadao = cidadao; }

    public String getEnderecoDenuncia() { return enderecoDenuncia; }
    public void setEnderecoDenuncia(String enderecoDenuncia) { this.enderecoDenuncia = enderecoDenuncia; }

    public String getDescricaoDenuncia() { return descricaoDenuncia; }
    public void setDescricaoDenuncia(String descricaoDenuncia) { this.descricaoDenuncia = descricaoDenuncia; }

    public String getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(String dataEnvio) { this.dataEnvio = dataEnvio; }

    public String getTipoAcaoFiscal() { return tipoAcaoFiscal; }
    public void setTipoAcaoFiscal(String tipoAcaoFiscal) { this.tipoAcaoFiscal = tipoAcaoFiscal; }

    public String getStatusDenuncia() { return statusDenuncia; }
    public void setStatusDenuncia(String statusDenuncia) { this.statusDenuncia = statusDenuncia; }

    public String getProtocolo() { return protocolo; }
    public void setProtocolo(String protocolo) { this.protocolo = protocolo; }

    public List<VistoriaLoteDenuncia> getVistorias() { return vistorias; }
    public void setVistorias(List<VistoriaLoteDenuncia> vistorias) { this.vistorias = vistorias; }
}
