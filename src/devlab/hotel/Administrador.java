/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devlab.hotel;

import java.util.Scanner;

/**
 *
 * @author Rodrigo
 */
public class Administrador {
    
     private DBConnection conn;
    private Scanner scan;
    
    public Administrador(DBConnection conn, Scanner scan) {
        this.conn = conn;
        this.scan = scan;
    }
    
    public void AgregarAdministrador() {


        System.out.println("Ingrese el DNI del administrador:");
        int dni = scan.nextInt();
        scan.nextLine();

        System.out.println("Ingrese la contraseña:");
        String contraseña = scan.nextLine();

        // Lógica según existencia y tipo
        String tipo = conn.ObtenerTipoUsuario(dni);

        if (tipo == null) {
            // No existe en la tabla, lo agregamos como nuevo administrador
            conn.InsertarAdministrador(dni, contraseña);
        } else if (tipo.equalsIgnoreCase("administrador")) {
            System.out.println("Este usuario ya es un administrador.");
        } else if (tipo.equalsIgnoreCase("cliente")) {
            conn.ActualizarTipoAAdministrador(dni);
        } else {
            System.out.println("Rol desconocido. No se pudo procesar.");
        }
    }
    
    public void EliminarAdministrador() {

        System.out.println("Ingrese el DNI del administrador que desea eliminar:");
        int dni = scan.nextInt();
        scan.nextLine(); // limpiar buffer

        String tipo = conn.ObtenerTipoUsuario(dni);

        if (tipo == null) {
            System.out.println("No existe ningun usuario con ese DNI.");
            return;
        }

        if (!tipo.equalsIgnoreCase("administrador")) {
            System.out.println("El usuario con ese DNI no es un administrador.");
            return;
        }

        System.out.println("¿Desea convertirlo en cliente (c) o eliminarlo completamente (e)?");
        String opcion = scan.nextLine().trim().toLowerCase();

        switch (opcion) {
            case "c":
                conn.ActualizarTipoACliente(dni);
                break;
            case "e":
                conn.EliminarUsuario(dni);
                break;
            default:
                System.out.println("Opción no válida. Cancelado.");
        }
    }
}
