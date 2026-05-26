package models;

import java.util.ArrayList;
import java.util.List;

public class Denuncia {
    private int idDenuncia;
    private Cidadao cidadao;
    private String enderecoCompleto;
    private double latitude;
    private double longitude;
    private String descricao;
    private List<String> midiaUrls;
    private StatusDenuncia status;
    private String protocoloEletronico;
    private String dataEnvio;
    private List<VistoriaLoteDenuncia> vistorias;

    public Denuncia() {
        this.status = StatusDenuncia.PENDENTE;
        this.midiaUrls = new ArrayList<>();
        this.vistorias = new ArrayList<>();
    }

    public int getIdDenuncia() { return idDenuncia; }
    public void setIdDenuncia(int idDenuncia) { this.idDenuncia = idDenuncia; }

    public Cidadao getCidadao() { return cidadao; }
    public void setCidadao(Cidadao cidadao) { this.cidadao = cidadao; }

    public String getEnderecoCompleto() { return enderecoCompleto; }
    public void setEnderecoCompleto(String enderecoCompleto) { this.enderecoCompleto = enderecoCompleto; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<String> getMidiaUrls() { return midiaUrls; }
    public void setMidiaUrls(List<String> midiaUrls) { this.midiaUrls = midiaUrls; }

    public StatusDenuncia getStatus() { return status; }
    public void setStatus(StatusDenuncia status) { this.status = status; }

    public String getProtocoloEletronico() { return protocoloEletronico; }
    public void setProtocoloEletronico(String protocoloEletronico) { this.protocoloEletronico = protocoloEletronico; }

    public String getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(String dataEnvio) { this.dataEnvio = dataEnvio; }

    public List<VistoriaLoteDenuncia> getVistorias() { return vistorias; }
    public void setVistorias(List<VistoriaLoteDenuncia> vistorias) { this.vistorias = vistorias; }
}
