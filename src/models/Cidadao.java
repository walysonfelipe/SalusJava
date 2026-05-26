package models;

public class Cidadao extends Usuario {
    private String cpf;

    public Cidadao() {
        super();
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}
