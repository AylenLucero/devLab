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
import java.sql.PreparedStatement;
public class DBConnection {
    private Connection conn;
    
    public DBConnection(){
        conn = null;
        this.connectar();
    }
    
    public void connectar(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://206.189.237.186:14111;databaseName=Equipo8-Progra:DevLab;encrypt=false;user=alucero;password=");
            System.out.println("Se extablecio la conneccion con l BD");
        } catch(SQLException e) {
            System.out.println("La BD no se pudo conectar" + e.getMessage());
        }
    }
    
    public boolean BuscarUser(int usuario, String contrasena, String tipo) {
        ResultSet rs;
        try {
            Statement statement = this.conn.createStatement();
            rs = statement.executeQuery("SELECT * FROM dbo.Login");
            while(rs.next()) {
                int DNI = rs.getInt("DNI");
                String contraseña = rs.getString("Contrasena");
                String tipoUsuario = rs.getString("Tipo");

                if (usuario == DNI && contrasena.equals(contraseña) && tipoUsuario.equals(tipo)) {
                    return true;
                } 
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }
    
    public void InsrtarUsuario(int DNI, String contrasena, String tipo) {
        try { 
            String sql = "INSERT INTO dbo.Login (Contrasena, Tipo, DNI) VALUES (?, ?, ?)";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, contrasena);
            stmt.setString(2, tipo);
            stmt.setInt(3, DNI);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                System.out.println("Usuario insertado correctamente.");
            } else {
                System.out.println("No se insertó ningún usuario.");
            }
        } catch (SQLException e) {
            System.out.println("La BD no se pudo insertar en el Login " + e.getMessage());
        }
    }
}
