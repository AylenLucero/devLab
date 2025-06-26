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
        Thread hilo = new Thread(new VerificadorDeOcupacion(conn));
        Scanner scanner = new Scanner(System.in);
        Login login = new Login(conn, scanner, hilo);
        
        hilo.start();

        boolean salir = false;
        while (!salir) {
            System.out.println("=== MENU DE LOGIN ===");
            System.out.println("1. Iniciar sesion");
            System.out.println("2. Registrarse como Cliente");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    login.loginUsuario();
                    break;
                case "2":
                    login.registrarUsuario();
                    break;
         
                case "3":
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println(" Opcion invalida.");
            }

            System.out.println();
        }

        scanner.close();
    }
    
}
