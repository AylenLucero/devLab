package devlab.hotel;

/**
 *
 * @author Equipo
 */
public class DevLabHotel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DBConnection conn = new DBConnection();
        Thread hilo = new Thread(new VerificadorDeOcupacion(conn));
        Thread hiloRes = new Thread(new VerificadorDeTerminacionDeReserva(conn));
                
        LoginFrame vs = new LoginFrame();
        vs.setVisible(true);
        
        hilo.start();
        hiloRes.start();
    } 
}
