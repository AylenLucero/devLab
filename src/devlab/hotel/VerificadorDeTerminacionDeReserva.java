package devlab.hotel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class VerificadorDeTerminacionDeReserva implements Runnable {

    private DBConnection conn;

    public VerificadorDeTerminacionDeReserva(DBConnection conn) {
        this.conn = conn;
    }

    @Override
    public void run() {
        while (true) {
            try {
                verificarYEliminar();
                Thread.sleep(1000 * 60 * 60); // cada 1 hora
            } catch (InterruptedException e) {
                System.out.println("Hilo interrumpido: " + e.getMessage());
                break;
            }
        }
    }

    private void verificarYEliminar() {
        List<Map<String, Object>> reservas = conn.mostrarReservas();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate hoy = LocalDate.now();

        for (Map<String, Object> reserva : reservas) {
            int idReserva = (int) reserva.get("id_reserva");
            String fechaFinStr = (String) reserva.get("Fecha_fin");

            try {
                LocalDate fechaFin = LocalDate.parse(fechaFinStr, formatter);

                if (fechaFin.isBefore(hoy)) {
                    boolean eliminado = conn.EliminarReserva(idReserva);
                    if (eliminado) {
                        System.out.println("Reserva vencida eliminada: ID " + idReserva);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error al parsear fecha: " + fechaFinStr + " | " + e.getMessage());
            }
        }
    }
}