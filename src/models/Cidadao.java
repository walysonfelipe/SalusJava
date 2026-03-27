package models;

public class Cidadao {
    private int idCidadao;
    private String nomeCidadao;
    private String cpfCidadao;
    private String telefoneCidadao;
    private String emailCidadao;
    private String senhaCidadao;
    private String dataCadastro;

    public Cidadao() {}

    public int getIdCidadao() { return idCidadao; }
    public void setIdCidadao(int idCidadao) { this.idCidadao = idCidadao; }

    public String getNomeCidadao() { return nomeCidadao; }
    public void setNomeCidadao(String nomeCidadao) { this.nomeCidadao = nomeCidadao; }

    public String getCpfCidadao() { return cpfCidadao; }
    public void setCpfCidadao(String cpfCidadao) { this.cpfCidadao = cpfCidadao; }

    public String getTelefoneCidadao() { return telefoneCidadao; }
    public void setTelefoneCidadao(String telefoneCidadao) { this.telefoneCidadao = telefoneCidadao; }

    public String getEmailCidadao() { return emailCidadao; }
    public void setEmailCidadao(String emailCidadao) { this.emailCidadao = emailCidadao; }

    public String getSenhaCidadao() { return senhaCidadao; }
    public void setSenhaCidadao(String senhaCidadao) { this.senhaCidadao = senhaCidadao; }

    public String getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }
}
