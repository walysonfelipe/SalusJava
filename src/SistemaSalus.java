import models.Admin;
import models.Cidadao;
import models.Funcionario;
import services.*;
import utils.MenuUtil;

import java.util.Scanner;

public class SistemaSalus {

    private final Scanner scanner = new Scanner(System.in);
    private final CidadaoService cidadaoService = new CidadaoService();
    private final AdminService adminService = new AdminService();
    private final FuncionarioService funcService = new FuncionarioService();
    private final DenunciaService denService = new DenunciaService();
    private final VistoriaService vistoriaService = new VistoriaService();
    private final LogService logService = new LogService();

    private void menuCidadao(Cidadao cidadao) {
    }

    private void menuPreCidadao() {
    }

    private void menuAdmin(Admin admin) {
    }

    private void menuGestor(Funcionario gestor) {
    }

    private void run() {
    }

    public static void main(String[] args) {
        SistemaSalus sistema = new SistemaSalus();
        sistema.run();
    }
}
