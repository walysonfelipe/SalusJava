package models;

public class Logs {
    private int idLog;
    private Cidadao cidadao;
    private Funcionario funcionario;
    private Admin admin;
    private String tipoUsuario;
    private String descricaoAcao;
    private String ipOrigemAcao;
    private String dataLogAcao;

    public Logs() {}

    public int getIdLog() { return idLog; }
    public void setIdLog(int idLog) { this.idLog = idLog; }

    public Cidadao getCidadao() { return cidadao; }
    public void setCidadao(Cidadao cidadao) { this.cidadao = cidadao; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public Admin getAdmin() { return admin; }
    public void setAdmin(Admin admin) { this.admin = admin; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public String getDescricaoAcao() { return descricaoAcao; }
    public void setDescricaoAcao(String descricaoAcao) { this.descricaoAcao = descricaoAcao; }

    public String getIpOrigemAcao() { return ipOrigemAcao; }
    public void setIpOrigemAcao(String ipOrigemAcao) { this.ipOrigemAcao = ipOrigemAcao; }

    public String getDataLogAcao() { return dataLogAcao; }
    public void setDataLogAcao(String dataLogAcao) { this.dataLogAcao = dataLogAcao; }
}
