package devlab.hotel;

/**
 *
 * @author Equipo
 */
import java.util.Scanner;
public class DevLabHotel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DBConnection conn = new DBConnection();
        Scanner scanner = new Scanner(System.in);
        Login login = new Login(conn, scanner);

        boolean salir = false;
        while (!salir) {
            System.out.println("=== MENÚ DE LOGIN ===");
            System.out.println("1. Iniciar sesion como Cliente");
            System.out.println("2. Registrarse como Cliente");
            System.out.println("3. Iniciar sesion como Administrador");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opcion: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    login.loginUsuario("cliente");
                    break;
                case "2":
                    login.registrarUsuario();
                    break;
                case "3":
                    login.loginUsuario("administrador");
                    break;
                case "4":
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println(" Opción inválida.");
            }

            System.out.println();
        }

        scanner.close();
    }
    
}
