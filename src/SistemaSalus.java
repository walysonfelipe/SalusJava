import services.*;
import java.util.Scanner;

public class SistemaSalus {

    private  Scanner scanner = new Scanner(System.in);
    private  CidadaoService cidadaoService = new CidadaoService();
    private  AdminService adminService = new AdminService();
    private  FuncionarioService funcService = new FuncionarioService();
    private  DenunciaService denService = new DenunciaService();
    private  VistoriaService vistoriaService = new VistoriaService();
    private  LogService logService = new LogService();

    private void menuCidadao() {
    }

    private void menuPreCidadao() {
    }

    private void menuAdmin() {
    }

    private void menuGestor() {
    }

    private void run() {
    }

    public static void main(String[] args) {
        SistemaSalus sistema = new SistemaSalus();
        sistema.run();
    }
}
