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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
public class DBConnection {
    private Connection conn;
    
    public DBConnection(){
        conn = null;
        this.connectar();
    }
    
    public void connectar(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://206.189.237.186:14111;"
                    + "databaseName=Equipo8-Progra:DevLab;"
                    + "encrypt=false;"
                    + "user=rribes;"
                    + "password=Abc.123456");
            System.out.println("Se extablecio la conneccion con l BD");
        } catch(SQLException e) {
            System.out.println("La BD no se pudo conectar" + e.getMessage());
        }
    }
    
    // -------------  LOGIN
    
   public String BuscarUser(int dni, String contrasena) {
        try {
            String sql = "SELECT tipo FROM Login WHERE DNI = ? AND Contrasena = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, dni);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("tipo"); 
            }
        } catch (SQLException e) {
            System.out.println("Error al autenticar usuario: " + e.getMessage());
        }
        return null; // no se encontró usuario
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
                System.out.println("No se inserto ningun usuario.");
            }
        } catch (SQLException e) {
            System.out.println("La BD no se pudo insertar en el Login " + e.getMessage());
        }
    }
    
    // -------------  ADMIN HABITACION
    
    public void InsertarHabitacion(int cantidadPersonas, int camaDoble, int camaSimple, float precioNoche){
        try { 
            String sql = "INSERT INTO dbo.Habitaciones "
                    + "(Cantidad_personas, C_doble, C_simple, Disponibilidad, Precio_noche) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, cantidadPersonas);
            stmt.setInt(2, camaDoble);
            stmt.setInt(3, camaSimple);
            stmt.setString(4, "Disponible");
            stmt.setFloat(5, precioNoche);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                System.out.println("Habitacion cargada correctamente...");
            } else {
                System.out.println("No se pudo cargar la habitacion.");
            }
        } catch (SQLException e) {
            System.out.println("Hubo un error" + e.getMessage());
        }
    }
    
    public void EditarHabitacion(int idHabitacion, int cantidadPersonas, int camaDoble, int camaSimple, float precioNoche) {
        try {
            String sql = "UPDATE dbo.Habitaciones SET "
                       + "Cantidad_personas = ?, "
                       + "C_doble = ?, "
                       + "C_simple = ?, "
                       + "Precio_noche = ? "
                       + "WHERE ID_habitacion = ?";

            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, cantidadPersonas);
            stmt.setInt(2, camaDoble);
            stmt.setInt(3, camaSimple);
            stmt.setFloat(4, precioNoche);
            stmt.setInt(5, idHabitacion);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                System.out.println("habitacion actualizada correctamente...");
            } else {
                System.out.println("No se encontró la habitacion con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar la habitacion: " + e.getMessage());
        }
    }
    
    public void MostrarHabitaciones() {
        try {
            String sql = "SELECT ID_habitacion, Cantidad_personas, C_doble, C_simple, Disponibilidad, Precio_noche FROM dbo.Habitaciones";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            System.out.println("----- LISTADO DE HABITACIONES -----");
            while (rs.next()) {
                int id = rs.getInt("ID_habitacion");
                int personas = rs.getInt("Cantidad_personas");
                int dobles = rs.getInt("C_doble");
                int simples = rs.getInt("C_simple");
                String disponibilidad = rs.getString("Disponibilidad");
                float precio = rs.getFloat("Precio_noche");

                System.out.println("ID: " + id);
                System.out.println("Cantidad de personas: " + personas);
                System.out.println("Camas dobles: " + dobles);
                System.out.println("Camas simples: " + simples);
                System.out.println("Disponibilidad: " + disponibilidad);
                System.out.println("Precio por noche: $" + precio);
                System.out.println("------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar habitaciones: " + e.getMessage());
        }
    }
    
    public void EliminarHabitacion(int idHabitacion) {
        try {
            String sql = "DELETE FROM dbo.Habitaciones WHERE ID_habitacion = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, idHabitacion);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                System.out.println("habitacion eliminada correctamente.");
            } else {
                System.out.println("No se encontro ninguna habitacion con ese ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la habitacion: " + e.getMessage());
        }
    }

    // -------------  ADMIN RESERVA
    
    public void InsertarReserva(int idHabitacion, String fechaInicio, String fechaFin, int cantidadDias, float precioTotal, int DNI) {
        try {
            String sql = "INSERT INTO dbo.Reservas (id_habitacion, Fecha_inicio, Fecha_fin, Cantidad_dias, Precio_total, DNI_cliente) VALUES (?, ?, ?, ?, ?,?)";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, idHabitacion);
            stmt.setString(2, fechaInicio);
            stmt.setString(3, fechaFin);
            stmt.setInt(4, cantidadDias);
            stmt.setFloat(5, precioTotal);
            stmt.setInt(6, DNI);
            
            int filas = stmt.executeUpdate();
            System.out.println(filas > 0 ? "Reserva registrada correctamente." : "No se pudo registrar la reserva.");
        } catch (SQLException e) {
            System.out.println("Error al insertar reserva: " + e.getMessage());
        }
    }

    public void MostrarReservas() {
        try {
            String sql = "SELECT * FROM dbo.Reservas";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            System.out.println("----- LISTADO DE RESERVAS -----");
            while (rs.next()) {
                System.out.println("ID Reserva: " + rs.getInt("id_reserva"));
                System.out.println("ID Habitacion: " + rs.getInt("id_habitacion"));
                System.out.println("Fecha Inicio: " + rs.getString("Fecha_inicio"));
                System.out.println("Fecha Fin: " + rs.getString("Fecha_fin"));
                System.out.println("Cantidad de dias: " + rs.getInt("Cantidad_dias"));
                System.out.println("Precio total: $" + rs.getFloat("Precio_total"));
                System.out.println("--------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar reservas: " + e.getMessage());
        }
    }
    
    public boolean ExisteReservaPorDNI(int dniCliente) {
        try {
            String sql = "SELECT COUNT(*) FROM dbo.Reservas WHERE DNI_cliente = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, dniCliente);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar existencia de reserva por DNI: " + e.getMessage());
        }
        return false;
    }
    
    public int ObtenerReservaPorDNI(int dniCliente) {
        try {
            String sql = "SELECT Id_reserva, Id_habitacion, Fecha_inicio, Fecha_fin FROM dbo.Reservas WHERE DNI_cliente = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, dniCliente);
            ResultSet rs = stmt.executeQuery();

            List<Integer> idsDisponibles = new ArrayList<>();
            System.out.println("Reservas encontradas para el cliente con DNI " + dniCliente + ":");

            while (rs.next()) {
                int idReserva = rs.getInt("Id_reserva");
                int idHabitacion = rs.getInt("Id_habitacion");
                String fechaInicio = rs.getString("Fecha_inicio");
                String fechaFin = rs.getString("Fecha_fin");

                System.out.println("- ID Reserva: " + idReserva + " | Habitacion: " + idHabitacion +
                                   " | Desde: " + fechaInicio + " Hasta: " + fechaFin);

                idsDisponibles.add(idReserva);
            }

            if (idsDisponibles.isEmpty()) {
                return -1; // No hay reservas
            }

            Scanner scan = new Scanner(System.in);
            System.out.println("Ingrese el ID de la reserva que desea editar:");
            int seleccion = scan.nextInt();
            scan.nextLine();

            if (idsDisponibles.contains(seleccion)) {
                return seleccion;
            } else {
                System.out.println("ID ingresado invalido.");
                return -1;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar reservas por DNI: " + e.getMessage());
        }
        return -1;
    }
    
    public void EditarReserva(int idReserva, int idHabitacion, String fechaInicio, String fechaFin, int cantidadDias, float precioTotal) {
        try {
            String sql = "UPDATE dbo.Reservas SET id_habitacion = ?, Fecha_inicio = ?, Fecha_fin = ?, Cantidad_dias = ?, Precio_total = ? WHERE id_reserva = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, idHabitacion);
            stmt.setString(2, fechaInicio);
            stmt.setString(3, fechaFin);
            stmt.setInt(4, cantidadDias);
            stmt.setFloat(5, precioTotal);
            stmt.setInt(6, idReserva);

            int filas = stmt.executeUpdate();
            System.out.println(filas > 0 ? "Reserva actualizada correctamente." : "No se encontro la reserva con ese ID.");
        } catch (SQLException e) {
            System.out.println("Error al editar reserva: " + e.getMessage());
        }
    }

    public void EliminarReserva(int idReserva) {
        try {
            String sql = "DELETE FROM dbo.Reservas WHERE id_reserva = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, idReserva);

            int filas = stmt.executeUpdate();
            System.out.println(filas > 0 ? "Reserva eliminada correctamente." : "No se encontro ninguna reserva con ese ID.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar reserva: " + e.getMessage());
        }
    }
    
    public float ObtenerPrecioHabitacion(int idHabitacion) {
        try {
            String sql = "SELECT Precio_noche FROM dbo.Habitaciones WHERE ID_habitacion = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, idHabitacion);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getFloat("Precio_noche");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener precio: " + e.getMessage());
        }
        return -1f; // -1 si no se encuentra
    }
    
    public boolean ExisteSuperposicionReserva(int idHabitacion, LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            String sql = "SELECT 1 FROM dbo.Reservas " +
                         "WHERE id_habitacion = ? " +
                         "AND Fecha_inicio <= ? AND Fecha_fin >= ?";

            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, idHabitacion);
            stmt.setString(2, fechaFin.toString());   
            stmt.setString(3, fechaInicio.toString());

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error al verificar superposicion: " + e.getMessage());
            return true; 
        }
    }
    
    public boolean ExisteSuperposicionReservaExcluyendo(int idHabitacion, LocalDate fechaInicio, LocalDate fechaFin, int idReservaExcluir) {
        try {
            String sql = "SELECT 1 FROM dbo.Reservas " +
                         "WHERE id_habitacion = ? AND id_reserva != ? " +
                         "AND Fecha_inicio <= ? AND Fecha_fin >= ?";

            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, idHabitacion);
            stmt.setInt(2, idReservaExcluir);
            stmt.setString(3, fechaFin.toString());
            stmt.setString(4, fechaInicio.toString());

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error al verificar superposicion (edicion): " + e.getMessage());
            return true;
        }
    }

    
    public void ActualizarDisponibilidadHabitacion(int idHabitacion, String estado) {
        try {
            String sql = "UPDATE dbo.Habitaciones SET Disponibilidad = ? WHERE ID_habitacion = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, estado);
            stmt.setInt(2, idHabitacion);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar disponibilidad: " + e.getMessage());
        }
    }
    
    public int ObtenerIdHabitacionDeReserva(int idReserva) {
        try {
            String sql = "SELECT id_habitacion FROM dbo.Reservas WHERE id_reserva = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, idReserva);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_habitacion");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la habitacion de la reserva: " + e.getMessage());
        }
        return -1;
    }

    public boolean HabitacionTieneReservasActivas(int idHabitacion) {
        try {
            String sql = "SELECT 1 FROM dbo.Reservas WHERE id_habitacion = ? AND Fecha_fin >= CONVERT(date, GETDATE())";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, idHabitacion);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            System.out.println("Error al verificar reservas activas: " + e.getMessage());
        }
        return false;
    }
    
    // ADMINISTRADOR
    
   public void InsertarAdministrador(int dni, String contraseña) {
        try {
            String sql = "INSERT INTO Login (DNI, Contrasena, Tipo) VALUES (?, ?, 'administrador')";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, dni);
            stmt.setString(2, contraseña);

            int filas = stmt.executeUpdate();
            System.out.println(filas > 0 ? "Administrador agregado correctamente." : "No se pudo agregar el administrador.");
        } catch (SQLException e) {
            System.out.println("Error al insertar administrador: " + e.getMessage());
        }
    }
    
        public void ActualizarTipoAAdministrador(int dni) {
         try {
             String sql = "UPDATE Login SET Tipo = 'administrador' WHERE dni = ?";
             PreparedStatement stmt = this.conn.prepareStatement(sql);
             stmt.setInt(1, dni);

             int filas = stmt.executeUpdate();
             System.out.println(filas > 0 ? "El cliente ahora es administrador." : "No se pudo actualizar el rol.");
         } catch (SQLException e) {
             System.out.println("Error al actualizar rol: " + e.getMessage());
         }
     }
   public String ObtenerTipoUsuario(int dni) {
        try {
            String sql = "SELECT Tipo FROM Login WHERE dni = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Tipo");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener tipo de usuario: " + e.getMessage());
        }
        return null;
    }
    
    public void ActualizarTipoACliente(int dni) {
        try {
            String sql = "UPDATE Login SET Tipo = 'cliente' WHERE dni = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, dni);

            int filas = stmt.executeUpdate();
            System.out.println(filas > 0 ? "Administrador convertido a cliente correctamente." : "No se pudo actualizar.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar tipo: " + e.getMessage());
        }
    }
    
    public void EliminarUsuario(int dni) {
        try {
            String sql = "DELETE FROM Login WHERE dni = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, dni);

            int filas = stmt.executeUpdate();
            System.out.println(filas > 0 ? "Administrador eliminado." : "No se pudo eliminar el usuario.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
        }
    }
   
    // --- CLIENTE
    
    
    public List<Map<String, Object>> ObtenerHabitacionesDisponiblesPorCapacidadYFecha(int cantidadPersonas, LocalDate fechaInicio, LocalDate fechaFin) {
    List<Map<String, Object>> disponibles = new ArrayList<>();

        String sql = "SELECT * FROM Habitaciones WHERE cantidad_personas >= ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cantidadPersonas);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID_habitacion"); 
                float precio = rs.getFloat("Precio_noche");
                int C_doble = rs.getInt("C_doble"); 
                int C_simple = rs.getInt("C_simple"); 

                boolean ocupado = ExisteSuperposicionReserva(id, fechaInicio, fechaFin);
                if (!ocupado) {
                    Map<String, Object> hab = new HashMap<>();
                    hab.put("id", id);
                    hab.put("Cantidad_personas", rs.getInt("Cantidad_personas"));
                    hab.put("Precio_noche", precio);
                    hab.put("C_doble", C_doble);
                    hab.put("C_simple", C_simple);
                    disponibles.add(hab);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener habitaciones disponibles: " + e.getMessage());
        }

        return disponibles;
    }

    public List<Integer> ObtenerTodosLosIdHabitaciones() {
        List<Integer> ids = new ArrayList<>();
        try {
            String sql = "SELECT ID_habitacion FROM dbo.Habitaciones";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ids.add(rs.getInt("ID_habitacion"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener IDs de habitaciones: " + e.getMessage());
        }
        return ids;
    }
    
    public void InsertarCliente(int dni, String nombre, String apellido, int edad, String telefono, String email) {
        if (ClienteExiste(dni)) {
            System.out.println("El cliente ya existe. No se insertó nada.");
            return;
        }
        
        try {
            String sql = "INSERT INTO dbo.Clientes (DNI, Nombre, Apellido, Edad, Telefono, Email) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, dni);
            stmt.setString(2, nombre);
            stmt.setString(3, apellido);
            stmt.setInt(4, edad);
            stmt.setString(5, telefono);
            stmt.setString(6, email);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                System.out.println("Cliente insertado correctamente.");
            } else {
                System.out.println("No se pudo insertar el cliente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
        }
    }
    
    public boolean ClienteExiste(int dni) {
        try {
            String sql = "SELECT 1 FROM dbo.Clientes WHERE DNI = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Devuelve true si existe
        } catch (SQLException e) {
            System.out.println("Error al verificar si el cliente existe: " + e.getMessage());
            return false; // En caso de error, se asume que no existe para evitar insertar mal
        }
    }
}
