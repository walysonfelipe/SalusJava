package models;

import java.util.ArrayList;
import java.util.List;

public class VistoriaLote {
    private int idVistoriaLote;
    private Funcionario funcionario;
    private Denuncia denuncia;
    private String tipoAcaoFiscal;
    private String descricaoVistoria;
    private String dataVistoria;
    private List<VistoriaLoteDenuncia> itens;

    public VistoriaLote() {
        this.itens = new ArrayList<>();
    }

    public int getIdVistoriaLote() { return idVistoriaLote; }
    public void setIdVistoriaLote(int idVistoriaLote) { this.idVistoriaLote = idVistoriaLote; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public Denuncia getDenuncia() { return denuncia; }
    public void setDenuncia(Denuncia denuncia) { this.denuncia = denuncia; }

    public String getTipoAcaoFiscal() { return tipoAcaoFiscal; }
    public void setTipoAcaoFiscal(String tipoAcaoFiscal) { this.tipoAcaoFiscal = tipoAcaoFiscal; }

    public String getDescricaoVistoria() { return descricaoVistoria; }
    public void setDescricaoVistoria(String descricaoVistoria) { this.descricaoVistoria = descricaoVistoria; }

    public String getDataVistoria() { return dataVistoria; }
    public void setDataVistoria(String dataVistoria) { this.dataVistoria = dataVistoria; }

    public List<VistoriaLoteDenuncia> getItens() { return itens; }
    public void setItens(List<VistoriaLoteDenuncia> itens) { this.itens = itens; }
}
