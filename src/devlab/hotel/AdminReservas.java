/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devlab.hotel;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author Rodrigo
 */
public class AdminReservas {
    private DBConnection conn;
    private Scanner scan;

    public AdminReservas(DBConnection conn, Scanner scan) {
        this.conn = conn;
        this.scan = scan;
    }
    
    /*
    public void AgregarReserva() {
        Clientes CL = new Clientes(conn, scan);
        int terminar;

        do {
            System.out.println("Ingrese el ID de la habitacion:");
            int idHabitacion = scan.nextInt();
            scan.nextLine();

            // Buscar el precio por noche 
            float precioNoche = conn.ObtenerPrecioHabitacion(idHabitacion);
            if (precioNoche == -1f) {
                System.out.println("No se encontro una habitacion con ese ID.");
                return;
            }

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

                 // Verificar si hay superposicion con otra reserva
                boolean ocupado = conn.ExisteSuperposicionReserva(idHabitacion, fechaInicio, fechaFin);
                if (ocupado) {
                    System.out.println("La habitacion ya esta reservada en ese rango de fechas. No se puede crear la reserva.");
                    return;
                }

                float precioTotal = cantidadDias * precioNoche;

                int DNI_cliente = CL.SolicitarDatosCliente();
                conn.InsertarReserva(idHabitacion, fechaInicioStr, fechaFinStr, (int)cantidadDias, precioTotal, DNI_cliente);

                //conn.ActualizarDisponibilidadHabitacion(idHabitacion, "Ocupado");

            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha invalido. Use YYYY-MM-DD.");
                return;
            }

            System.out.println("Desea cargar otra reserva? (1=SI, 0=NO):");
            terminar = scan.nextInt();
            scan.nextLine();

        } while (terminar != 0);
    }
    */

    public void ListarReservas() {
        conn.MostrarReservas();
    }

  public void EditarReserva() {
        System.out.println("Ingrese el DNI del cliente:");
        int dniCliente = scan.nextInt();
        scan.nextLine();

        // Verificar si existe una reserva con ese DNI
        if (!conn.ExisteReservaPorDNI(dniCliente)) {
            System.out.println("No se encontro ninguna reserva registrada para ese DNI.");
            return;
        }

        // Obtener la última reserva de ese cliente
        int idReserva = conn.ObtenerReservaPorDNI(dniCliente);

        if (idReserva == -1) {
            System.out.println("No se pudo obtener la reserva del cliente.");
            return;
        }

        System.out.println("Ingrese el nuevo ID de habitacion:");
        int idHabitacion = scan.nextInt();
        scan.nextLine();

        System.out.println("Ingrese la nueva fecha de inicio (YYYY-MM-DD):");
        String fechaInicioStr = scan.nextLine();

        System.out.println("Ingrese la nueva fecha de fin (YYYY-MM-DD):");
        String fechaFinStr = scan.nextLine();

        try {
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);

            long cantidadDias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
            if (cantidadDias <= 0) {
                System.out.println("La fecha de fin debe ser posterior a la de inicio.");
                return;
            }

            float precioNoche = conn.ObtenerPrecioHabitacion(idHabitacion);
            if (precioNoche == -1f) {
                System.out.println("No se encontro una Habitacion con ese ID.");
                return;
            }

            boolean ocupado = conn.ExisteSuperposicionReservaExcluyendo(idHabitacion, fechaInicio, fechaFin, idReserva);
            if (ocupado) {
                System.out.println("La Habitacion ya esta reservada en ese rango de fechas.");
                return;
            }

            float precioTotal = cantidadDias * precioNoche;

            conn.EditarReserva(idReserva, idHabitacion, fechaInicioStr, fechaFinStr, (int) cantidadDias, precioTotal);
            conn.ActualizarDisponibilidadHabitacion(idHabitacion, "Ocupado");

        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha invalido. Use YYYY-MM-DD.");
        }
    }

    public void EliminarReserva() {
        System.out.println("Ingrese el ID de la reserva que desea eliminar:");
        int idReserva = scan.nextInt();
        scan.nextLine();

        System.out.println("Esta seguro que desea eliminar la reserva con ID " + idReserva + "? (s/n)");
        String confirmacion = scan.nextLine();

        if (confirmacion.equalsIgnoreCase("s")) {
            int idHabitacion = conn.ObtenerIdHabitacionDeReserva(idReserva);
            if (idHabitacion == -1) {
                System.out.println("No se encontro la reserva.");
                return;
            }

            conn.EliminarReserva(idReserva);

            // Verificar si quedan reservas activas
            boolean tieneReservas = conn.HabitacionTieneReservasActivas(idHabitacion);
            if (!tieneReservas) {
                conn.ActualizarDisponibilidadHabitacion(idHabitacion, "Disponible");
            }

        } else {
            System.out.println("Operacion cancelada.");
        }
    }
    
    
}

