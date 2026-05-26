package models;

public class Admin extends Usuario {
    private int nivelAcesso;

    public Admin() {
        super();
    }

    public int getNivelAcesso() { return nivelAcesso; }
    public void setNivelAcesso(int nivelAcesso) { this.nivelAcesso = nivelAcesso; }
}
