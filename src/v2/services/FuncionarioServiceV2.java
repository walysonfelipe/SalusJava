package v2.services;

import models.Funcionario;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FuncionarioServiceV2 {

    private final Funcionario[] funcionarios = new Funcionario[200];
    private int total = 0;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public String adicionar(String nome, String cpf, String email, String telefone,
                            String senha, String cargo, String setor) {
        if (total >= 200) return "Limite de funcionários atingido!";
        Funcionario f = new Funcionario();
        f.nome = nome;
        f.cpf = cpf;
        f.email = email;
        f.telefone = telefone;
        f.senha = senha;
        f.cargo = cargo;
        f.setor = setor;
        f.dataCadastro = LocalDateTime.now().format(formatter);
        funcionarios[total++] = f;
        return "Funcionário cadastrado com sucesso!";
    }

    public Funcionario[] listar() {
        Funcionario[] resultado = new Funcionario[total];
        for (int i = 0; i < total; i++) resultado[i] = funcionarios[i];
        return resultado;
    }

    public String alterarStatus(int indice, boolean ativar) {
        if (indice < 0 || indice >= total) return "Funcionário inválido!";
        funcionarios[indice].ativo = ativar;
        return "Funcionário " + (ativar ? "ativado" : "desativado") + " com sucesso!";
    }

    public boolean loginAdmin(String email, String senha) {
        return email.equals("admin@prefeitura.gov") && senha.equals("123456");
    }

    public boolean loginGestor(String email, String senha) {
        for (int i = 0; i < total; i++) {
            if (funcionarios[i].email.equals(email)
                    && funcionarios[i].senha.equals(senha)
                    && funcionarios[i].cargo.equalsIgnoreCase("gestor")
                    && funcionarios[i].ativo) {
                return true;
            }
        }
        return false;
    }
}
