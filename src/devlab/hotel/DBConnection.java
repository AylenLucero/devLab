package devlab.hotel;

/**
 *
 * @author Equipo
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
public class DBConnection {
    private Connection conn;
    
    public DBConnection(){
        conn = null;
        this.connectar();
    }
    
    public void connectar(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://206.189.237.186:14111;databaseName=Equipo8-Progra:DevLab;encrypt=false;user=alucero;password=Sanclemente1");
            System.out.println("Se extablecio la conneccion con l BD");
        } catch(SQLException e) {
            System.out.println("La BD no se pudo conectar" + e.getMessage());
        }
    }
}
