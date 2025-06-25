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
public class AdminHabitaciones {
    private DBConnection conn;
    private Scanner scan;
    
    public AdminHabitaciones(DBConnection conn, Scanner scan) {
        this.conn = conn;
        this.scan = scan;
    }
    
    public void AgregarHabitacion(){
        
        int terminar;
        int cantidadPersonas;
        int camaDoble;
        int camaSimple;
        float precioNoche;
        
        
        do{
            System.out.println("Ingrese la cantidad de Personas:" );
            cantidadPersonas = this.scan.nextInt();
            this.scan.nextLine(); 
            System.out.println("Ingrese la cantidad de camas doble:" );
            camaDoble = this.scan.nextInt();
            this.scan.nextLine(); 
            
            System.out.println("Ingrese la cantidad de camas simple:" );
            camaSimple = this.scan.nextInt();
            this.scan.nextLine(); 
            
            System.out.println("Ingrese el precio por noche:" );
            precioNoche = this.scan.nextFloat();
            this.scan.nextLine(); 
            
            System.out.println("Deseas cargar otra habitacion (1=SI 0=NO):" );
            terminar = this.scan.nextInt();
            
            conn.InsertarHabitacion(cantidadPersonas, camaDoble, camaSimple, precioNoche);
            
        }while(terminar != 0);
    }
     
    public void ListarHabitaciones() {
        conn.MostrarHabitaciones();
    }
    
    public void EditarHabitacion() {
        System.out.println("Ingrese el ID de la habitacion que desea editar:");
        int idHabitacion = this.scan.nextInt();

        System.out.println("Ingrese la nueva cantidad de personas:");
        int cantidadPersonas = this.scan.nextInt();

        System.out.println("Ingrese la nueva cantidad de camas dobles:");
        int camaDoble = this.scan.nextInt();

        System.out.println("Ingrese la nueva cantidad de camas simples:");
        int camaSimple = this.scan.nextInt();

        System.out.println("Ingrese el nuevo precio por noche:");
        float precioNoche = this.scan.nextFloat();

        this.conn.EditarHabitacion(idHabitacion, cantidadPersonas, camaDoble, camaSimple, precioNoche);
    }
    
    public void EliminarHabitacion() {
        System.out.println("Ingrese el ID de la habitacion que desea eliminar:");
        int idHabitacion = this.scan.nextInt();
        this.scan.nextLine(); 

        System.out.println("¿Esta seguro que desea eliminar la habitacion con ID " + idHabitacion + "? (s/n)");
        String confirmacion = this.scan.nextLine();

        if (confirmacion.equalsIgnoreCase("s")) {
            conn.EliminarHabitacion(idHabitacion);
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

}
