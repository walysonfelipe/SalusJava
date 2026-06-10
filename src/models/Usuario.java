package models;

public abstract class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senhaHash;
    private String telefone;
    private String dataCadastro;
    private String statusConta;

    public Usuario() {
        this.statusConta = "ATIVO";
    }

    public boolean autenticar(String senhaInformada) {
        return senhaInformada != null && senhaInformada.equals(this.senhaHash);
    }

    public boolean contaAtiva() {
        return "ATIVO".equalsIgnoreCase(this.statusConta);
    }

    public boolean validarEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }

    public String getStatusConta() { return statusConta; }
    public void setStatusConta(String statusConta) { this.statusConta = statusConta; }

    @Override
    public String toString() {
        return String.format("[%d] %s <%s> — %s", id, nome, email, statusConta);
    }
}
