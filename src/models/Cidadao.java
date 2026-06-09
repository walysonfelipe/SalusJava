package models;

import util.ValidadorBR;
import java.util.ArrayList;
import java.util.List;

public class Cidadao extends Usuario {
    private String cpf;
    private List<Denuncia> denuncias;

    public Cidadao() {
        super();
        this.denuncias = new ArrayList<>();
    }

    public void registrarDenuncia(Denuncia denuncia) {
        if (denuncia != null) {
            denuncia.setCidadao(this);
            this.denuncias.add(denuncia);
        }
    }

    public boolean validarCpf() {
        return ValidadorBR.validarCpf(cpf);
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public List<Denuncia> getDenuncias() { return denuncias; }
    public void setDenuncias(List<Denuncia> denuncias) { this.denuncias = denuncias; }
}
