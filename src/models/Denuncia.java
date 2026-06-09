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
    private Integer gestorResponsavelId;
    private String gestorResponsavelNome;
    private String observacaoVistoria;
    private String dataHoraVistoria;

    public Denuncia() {
        this.status = StatusDenuncia.PENDENTE;
        this.midiaUrls = new ArrayList<>();
    }

    public boolean validar() {
        return enderecoCompleto != null && !enderecoCompleto.isBlank()
                && descricao != null && !descricao.isBlank()
                && cidadao != null;
    }

    public boolean isPendente() {
        return status == StatusDenuncia.PENDENTE;
    }

    public void adicionarMidia(String url) {
        if (url != null && !url.isBlank()) {
            midiaUrls.add(url);
        }
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

    public Integer getGestorResponsavelId() { return gestorResponsavelId; }
    public void setGestorResponsavelId(Integer gestorResponsavelId) { this.gestorResponsavelId = gestorResponsavelId; }

    public String getGestorResponsavelNome() { return gestorResponsavelNome; }
    public void setGestorResponsavelNome(String gestorResponsavelNome) { this.gestorResponsavelNome = gestorResponsavelNome; }

    public String getObservacaoVistoria() { return observacaoVistoria; }
    public void setObservacaoVistoria(String observacaoVistoria) { this.observacaoVistoria = observacaoVistoria; }

    public String getDataHoraVistoria() { return dataHoraVistoria; }
    public void setDataHoraVistoria(String dataHoraVistoria) { this.dataHoraVistoria = dataHoraVistoria; }
}
