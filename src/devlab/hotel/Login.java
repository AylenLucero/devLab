package devlab.hotel;

/**
 *
 * @author Equipo
 */
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Login {
    public void loginUsuario(DBConnection conn, Scanner scanner, String tipoUsuario) {
        boolean encontrado = false;
        do {
            System.out.println("Ingrese usuario de " + tipoUsuario + ": ");
            int usuario = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Ingrese contrasena: ");
            String contrasena = scanner.nextLine();

            if(conn.BuscarUser(usuario, contrasena, tipoUsuario)) {
                System.out.println("Bienvenido!");
                encontrado = true;
            } else {
                System.out.println("El usuario no fue encontrado! ");
                encontrado = subMenu(conn,scanner,tipoUsuario);
            }
        } while (!encontrado);
    }
    
    public boolean subMenu(DBConnection conn, Scanner scanner, String tipoUsuario) {
        System.out.println("1)Reintentar");
        if(tipoUsuario == "cliente") {
            System.out.println("2)Registrarse");
            System.out.println("3)Volver al menu principal");
        } else {
            System.out.println("2)Volver al menu principal");
        }
        int opcionNoEncontrado = scanner.nextInt();
        switch(opcionNoEncontrado) {
            case 1:
                return false;
            case 2:
                //FALTA LLAMAR A LA FUTURA FUNCIION DEL REGISTRO
                if(tipoUsuario == "cliente")
                    registrarUsuario(conn,scanner);
                return true;
            case 3:
                if(tipoUsuario == "administrador")
                    System.out.println("Opcion incorrecta");
                return true;
            default:
                System.out.println("Opcion incorrecta");
                subMenu(conn,scanner,tipoUsuario);
                break;
        }
        return false;
    }
    
    public void registrarUsuario(DBConnection conn,Scanner scanner) {
        int dni = 0;
        String contrasena;

        while (true) {
            System.out.println("Ingrese su DNI (solo numeros): ");
            String input = scanner.nextLine();
            scanner.nextLine();
            try {
                dni = Integer.parseInt(input);
                break; 
            } catch (NumberFormatException e) {
                System.out.println("DNI invalido. Debe contener solo numeros.");
            }
        }

        System.out.println("Ingrese su contrasena: ");
        contrasena = scanner.nextLine();
        
        conn.InsrtarUsuario(dni, contrasena, "cliente");
    }
    

}
