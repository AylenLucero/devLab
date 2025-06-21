package devlab.hotel;

/**
 *
 * @author Equipo
 */
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Login {
    private DBConnection conn;
    private Scanner scanner;

    public Login(DBConnection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }
    
    public void loginUsuario(String tipoUsuario) {
        boolean encontrado = false;
        do {
            System.out.println("Ingrese usuario de " + tipoUsuario + ": ");
            int usuario = this.scanner.nextInt();
            this.scanner.nextLine();
            System.out.println("Ingrese contrasena: ");
            String contrasena = this.scanner.nextLine();

            if(conn.BuscarUser(usuario, contrasena, tipoUsuario)) {
                System.out.println("Bienvenido!");
                encontrado = true;
            } else {
                System.out.println("El usuario no fue encontrado! ");
                encontrado = subMenu(tipoUsuario);
            }
        } while (!encontrado);
    }
    
    public boolean subMenu(String tipoUsuario) {
        System.out.println("1)Reintentar");
        if(tipoUsuario == "cliente") {
            System.out.println("2)Registrarse");
            System.out.println("3)Volver al menu principal");
        } else {
            System.out.println("2)Volver al menu principal");
        }
        int opcionNoEncontrado = this.scanner.nextInt();
        switch(opcionNoEncontrado) {
            case 1:
                return false;
            case 2:
                if(tipoUsuario == "cliente")
                    registrarUsuario();
                return true;
            case 3:
                if(tipoUsuario == "administrador")
                    System.out.println("Opcion incorrecta");
                return true;
            default:
                System.out.println("Opcion incorrecta");
                subMenu(tipoUsuario);
                break;
        }
        return false;
    }
    
    public void registrarUsuario() {
        int dni = 0;
        String contrasena;

        while (true) {
            System.out.println("Ingrese su DNI (solo numeros): ");
            String input = this.scanner.nextLine();
            this.scanner.nextLine();
            try {
                dni = Integer.parseInt(input);
                break; 
            } catch (NumberFormatException e) {
                System.out.println("DNI invalido. Debe contener solo numeros.");
            }
        }

        System.out.println("Ingrese su contrasena: ");
        contrasena = this.scanner.nextLine();
        
        conn.InsrtarUsuario(dni, contrasena, "cliente");
    }
    

}
