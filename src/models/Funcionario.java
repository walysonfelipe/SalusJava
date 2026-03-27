package models;

public class Funcionario {
    private String idFuncionario;
    private String nomeFuncionario;
    private String cpfFuncionario;
    private String emailFuncionario;
    private String telefoneFuncionario;
    private String senhaHash;
    private String cargoFuncionario;
    private String salarioFuncionario;
    private String certificado;
    private boolean ativoFuncionario;
    private String dataCadastro;

    public Funcionario() {
        this.ativoFuncionario = true;
    }

    public String getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(String idFuncionario) { this.idFuncionario = idFuncionario; }

    public String getNomeFuncionario() { return nomeFuncionario; }
    public void setNomeFuncionario(String nomeFuncionario) { this.nomeFuncionario = nomeFuncionario; }

    public String getCpfFuncionario() { return cpfFuncionario; }
    public void setCpfFuncionario(String cpfFuncionario) { this.cpfFuncionario = cpfFuncionario; }

    public String getEmailFuncionario() { return emailFuncionario; }
    public void setEmailFuncionario(String emailFuncionario) { this.emailFuncionario = emailFuncionario; }

    public String getTelefoneFuncionario() { return telefoneFuncionario; }
    public void setTelefoneFuncionario(String telefoneFuncionario) { this.telefoneFuncionario = telefoneFuncionario; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getCargoFuncionario() { return cargoFuncionario; }
    public void setCargoFuncionario(String cargoFuncionario) { this.cargoFuncionario = cargoFuncionario; }

    public String getSalarioFuncionario() { return salarioFuncionario; }
    public void setSalarioFuncionario(String salarioFuncionario) { this.salarioFuncionario = salarioFuncionario; }

    public String getCertificado() { return certificado; }
    public void setCertificado(String certificado) { this.certificado = certificado; }

    public boolean isAtivoFuncionario() { return ativoFuncionario; }
    public void setAtivoFuncionario(boolean ativoFuncionario) { this.ativoFuncionario = ativoFuncionario; }

    public String getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }
}
