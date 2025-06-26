package devlab.hotel;

import java.time.LocalDate;
import java.util.List;

public class VerificadorDeOcupacion implements Runnable {
    private DBConnection conn;

    public VerificadorDeOcupacion(DBConnection conn) {
        this.conn = conn;
    }

    @Override
    public void run() {
        while (true) {
            LocalDate hoy = LocalDate.now();

            List<Integer> idsHabitaciones = conn.ObtenerTodosLosIdHabitaciones();

            for (int id : idsHabitaciones) {
                boolean ocupada = conn.ExisteSuperposicionReserva(id, hoy, hoy);
                if (ocupada) {
                    conn.ActualizarDisponibilidadHabitacion(id, "Ocupado");
                } else {
                    conn.ActualizarDisponibilidadHabitacion(id, "Disponible");
                }
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                System.out.println("El hilo VerificadorDeOcupacion fue interrumpido.");
                return; // salir del hilo si fue interrumpido
            }
        }
    }
}
