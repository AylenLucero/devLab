package devlab.hotel;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Equipo
 */
public class Reservas {
    private DBConnection conn;
    private Scanner scan;

    public Reservas(DBConnection conn, Scanner scan, Thread hilo) {
        this.conn = conn;
        this.scan = scan;
    }
    
    public void AgregarReserva() {
        Clientes CL = new Clientes(conn, scan);
        int terminar;
        float precioTotal = 0;
        
        do {
            
            System.out.println("Ingrese la cantidad de personas:");
            int cantidadPersonas = scan.nextInt();
            scan.nextLine();

            System.out.println("Ingrese la fecha de inicio (formato YYYY-MM-DD):");
            String fechaInicioStr = scan.nextLine();

            System.out.println("Ingrese la fecha de fin (formato YYYY-MM-DD):");
            String fechaFinStr = scan.nextLine();

            try {
                LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
                LocalDate fechaFin = LocalDate.parse(fechaFinStr);

                long cantidadDias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
                if (cantidadDias <= 0) {
                    System.out.println("La fecha de fin debe ser posterior a la de inicio.");
                    return;
                }

                List<Map<String, Object>> disponibles = conn.ObtenerHabitacionesDisponiblesPorCapacidadYFecha(cantidadPersonas, fechaInicio, fechaFin);
                if (disponibles.isEmpty()) {
                    System.out.println("No hay habitaciones disponibles para esa fecha y cantidad de personas.");
                    return;
                }

                System.out.println("Habitaciones disponibles:");
                for (Map<String, Object> hab : disponibles) {
                    float precioNoche = ((Float) hab.get("Precio_noche")).floatValue();
                    precioTotal = (int) (cantidadDias * precioNoche);
                    System.out.println(
                        "ID: " + hab.get("id") + 
                        " | Capacidad: " + hab.get("Cantidad_personas") + 
                        " | Precio por noche: " + precioNoche +
                        " | Precio total: " + precioTotal +
                        " | Cantidad de camas dobles: " + hab.get("C_doble")+
                        " | Cantidad de camas simples: " + hab.get("C_simple")
                    );
                }
                System.out.println("Desea reservar alguna de estas habitaciones? (1=SI, 0=NO):");
                int reservar = scan.nextInt();
                scan.nextLine();
                if(reservar != 1) {
                    System.out.println("Retornando...");
                    return;
                } else {
                    System.out.println("Ingrese el ID de la habitacion que desea reservar:");
                    int idHabitacion = scan.nextInt();
                    scan.nextLine();
                    int DNI = CL.SolicitarDatosCliente();
                    conn.InsertarReserva(idHabitacion, fechaInicioStr, fechaFinStr, (int)cantidadDias, precioTotal, DNI);
                }

            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha invalido. Use YYYY-MM-DD.");
                return;
            }

            System.out.println("Desea reservar otra habitacion? (1=SI, 0=NO):");
            terminar = scan.nextInt();
            scan.nextLine();

        } while (terminar != 0);
    }
}
