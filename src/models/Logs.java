package models;

public class Logs {
    private int idLog;
    private Usuario usuario;
    private String acaoRealizada;
    private String timestamp;
    private String ipOrigem;

    public Logs() {}

    public int getIdLog() { return idLog; }
    public void setIdLog(int idLog) { this.idLog = idLog; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getAcaoRealizada() { return acaoRealizada; }
    public void setAcaoRealizada(String acaoRealizada) { this.acaoRealizada = acaoRealizada; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getIpOrigem() { return ipOrigem; }
    public void setIpOrigem(String ipOrigem) { this.ipOrigem = ipOrigem; }
}
