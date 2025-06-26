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
    private AdminHabitaciones adminHab;
    private AdminReservas adminRes;
    private Administrador admin;
    private Reservas ClRes;
    private Thread hilo;

    public Login(DBConnection conn, Scanner scanner, Thread hilo) {
        this.conn = conn;
        this.scanner = scanner;
        adminHab = new AdminHabitaciones(conn, scanner);
        adminRes = new AdminReservas(conn, scanner);
        admin = new Administrador(conn, scanner);

        ClRes = new Reservas(conn, scanner,hilo);
        hilo = new Thread(new VerificadorDeOcupacion(conn));
    }
    
    public void loginUsuario() {
    boolean encontrado = false;
    String input = "";

    do {
        System.out.println("Ingrese DNI de usuario:");
        int usuario = this.scanner.nextInt();
        this.scanner.nextLine();

        System.out.println("Ingrese contraseña:");
        String contrasena = this.scanner.nextLine();

        // Pedir tipo desde la base
        String tipoUsuario = conn.BuscarUser(usuario, contrasena);

        if (tipoUsuario != null) {
            encontrado = true;

            while (!input.equals("0")) {
                if (tipoUsuario.equalsIgnoreCase("administrador")) {
                    System.out.println("-------- MENU ADMINISTRADOR --------");
                    System.out.println("Administracion de Habitaciones");
                    System.out.println("1. Ver Habitaciones");
                    System.out.println("2. Agregar Habitacion");
                    System.out.println("3. Editar Habitacion");
                    System.out.println("4. Eliminar Habitacion");
                    System.out.println("Administracion de Reservas");
                    System.out.println("5. Ver Reservas");
                    System.out.println("6. Agregar Reserva");
                    System.out.println("7. Editar Reserva");
                    System.out.println("8. Eliminar Reserva");
                    if (usuario == 40653615) {
                        System.out.println("Administracion de Permisos");
                        System.out.println("9. Agregar Administrador");
                        System.out.println("10. Eliminar Administrador");
                    }
                    System.out.println("0. Salir");
                    System.out.print("Opcion: ");
                    input = this.scanner.nextLine();

                    switch (input) {
                        case "1":
                            adminHab.ListarHabitaciones();
                            break;
                        case "2":
                            adminHab.AgregarHabitacion();
                            break;
                        case "3":
                            adminHab.EditarHabitacion();
                            break;
                        case "4":
                            adminHab.EliminarHabitacion();
                            break;
                        case "5":
                            adminRes.ListarReservas();
                            break;
                        case "6":
                            ClRes.AgregarReserva();
                            break;
                        case "7":
                            adminRes.EditarReserva();
                            break;
                        case "8":
                            adminRes.EliminarReserva();
                            break;
                        case "9":
                            if (usuario == 40653615) admin.AgregarAdministrador();
                            break;
                        case "10":
                            if (usuario == 40653615) admin.EliminarAdministrador();
                            break;
                        default:
                            if (!input.equals("0")) System.out.println("Opcion invalida.");
                    }
                } else if (tipoUsuario.equalsIgnoreCase("cliente")) {
                    System.out.println("-------- MENU CLIENTE --------");
                    System.out.println("1. Agregar Reserva");
                    System.out.println("2. Editar Reserva");
                    System.out.println("3. Eliminar Reserva");
                    System.out.println("0. Salir");
                    System.out.print("Opcion: ");
                    input = this.scanner.nextLine();

                    switch (input) {
                        case "1":
                            ClRes.AgregarReserva();
                            break;
                        case "2":
                            adminRes.EditarReserva();
                            break;
                        case "3":
                            adminRes.EliminarReserva();
                            break;
                        default:
                            if (!input.equals("0")) System.out.println("Opción inválida.");
                    }
                } else {
                    System.out.println("Tipo de usuario desconocido. Abortando.");
                    break;
                }
            }

        } else {
            System.out.println("Usuario o contraseña incorrectos.");
            encontrado = subMenu("cliente"); // o simplemente: encontrado = false;
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
