package models;

public class Admin {
    private int idAdmin;
    private String nomeAdmin;
    private String senhaAdmin;
    private String emailAdmin;
    private boolean notifHabilitado;
    private String permissoesAdmin;
    private boolean restricaoIp;

    public Admin() {}

    public int getIdAdmin() { return idAdmin; }
    public void setIdAdmin(int idAdmin) { this.idAdmin = idAdmin; }

    public String getNomeAdmin() { return nomeAdmin; }
    public void setNomeAdmin(String nomeAdmin) { this.nomeAdmin = nomeAdmin; }

    public String getSenhaAdmin() { return senhaAdmin; }
    public void setSenhaAdmin(String senhaAdmin) { this.senhaAdmin = senhaAdmin; }

    public String getEmailAdmin() { return emailAdmin; }
    public void setEmailAdmin(String emailAdmin) { this.emailAdmin = emailAdmin; }

    public boolean isNotifHabilitado() { return notifHabilitado; }
    public void setNotifHabilitado(boolean notifHabilitado) { this.notifHabilitado = notifHabilitado; }

    public String getPermissoesAdmin() { return permissoesAdmin; }
    public void setPermissoesAdmin(String permissoesAdmin) { this.permissoesAdmin = permissoesAdmin; }

    public boolean isRestricaoIp() { return restricaoIp; }
    public void setRestricaoIp(boolean restricaoIp) { this.restricaoIp = restricaoIp; }
}
