package devlab.hotel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VerificadorDeTerminacionReserva implements Runnable {

    private DBConnection conn;

    public VerificadorDeTerminacionReserva(DBConnection conn) {
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
        String reservas = conn.MostrarReservas();
        String[] lineas = reservas.split("\n");

        int idReserva = -1;
        String fechaFinStr = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate hoy = LocalDate.now();

        for (String linea : lineas) {
            if (linea.startsWith("ID Reserva: ")) {
                idReserva = Integer.parseInt(linea.replace("ID Reserva: ", "").trim());
            } else if (linea.startsWith("Fecha Fin: ")) {
                fechaFinStr = linea.replace("Fecha Fin: ", "").trim();

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
}
