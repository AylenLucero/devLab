package devlab.hotel;

import java.util.Scanner;

/**
 *
 * @author Equipo
 */
public class Clientes {
    private DBConnection conn;
    private Scanner scan;

    public Clientes(DBConnection conn, Scanner scan) {
        this.conn = conn;
        this.scan = scan;
    }

    public int SolicitarDatosCliente() {
        System.out.println("Ingrese los siguientes datos para completar su registro:");
        
        System.out.print("DNI: ");
        int DNI = 0;
        while (true) {
            if (scan.hasNextInt()) {
                DNI = scan.nextInt();
                scan.nextLine();
                if (DNI > 1111111) break;
            } else {
                scan.nextLine(); // Limpiar basura
            }
            System.out.print("DNI inválido.");
        }
        
        System.out.print("Nombre: ");
        String nombre = scan.nextLine().trim();
        while (nombre.isEmpty()) {
            System.out.print("Nombre no puede estar vacío. Ingrese nuevamente: ");
            nombre = scan.nextLine().trim();
        }

        System.out.print("Apellido: ");
        String apellido = scan.nextLine().trim();
        while (apellido.isEmpty()) {
            System.out.print("Apellido no puede estar vacío. Ingrese nuevamente: ");
            apellido = scan.nextLine().trim();
        }

        System.out.print("Edad: ");
        int edad = 0;
        while (true) {
            if (scan.hasNextInt()) {
                edad = scan.nextInt();
                scan.nextLine();
                if (edad > 0 && edad < 120) break;
            } else {
                scan.nextLine(); // Limpiar basura
            }
            System.out.print("Edad inválida. Ingrese un número entre 1 y 120: ");
        }

        System.out.print("Teléfono: ");
        String telefono = scan.nextLine().trim();
        while (telefono.isEmpty()) {
            System.out.print("Teléfono no puede estar vacío. Ingrese nuevamente: ");
            telefono = scan.nextLine().trim();
        } 

        System.out.print("Email: ");
        String email = scan.nextLine().trim();
        while (!email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            System.out.print("Email inválido. Ingrese nuevamente: ");
            email = scan.nextLine().trim();
        }

        conn.InsertarCliente(DNI, nombre, apellido, edad, telefono, email);
        return DNI;
    }
}
