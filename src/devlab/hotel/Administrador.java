package devlab.hotel;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

/**
 * @author Equipo
 */
public class Administrador {
    
    private DBConnection conn;
    private Scanner scan;
    
    public Administrador(DBConnection conn, Scanner scan) {
        this.conn = conn;
        this.scan = scan;
    }
    
    public void AgregarAdministrador() {
        // Crear ventana de diálogo
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblDni = new JLabel("DNI del administrador:");
        JTextField txtDni = new JTextField(10);
        
        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField(10);
        
        panel.add(lblDni);
        panel.add(txtDni);
        panel.add(lblPassword);
        panel.add(txtPassword);

        int result = JOptionPane.showConfirmDialog(
            null, 
            panel, 
            "Agregar Administrador", 
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                int dni = Integer.parseInt(txtDni.getText());
                String contraseña = new String(txtPassword.getPassword());

                // Validar campos
                if (contraseña.isEmpty()) {
                    JOptionPane.showMessageDialog(null, 
                        "La contraseña no puede estar vacía", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Lógica según existencia y tipo
                String tipo = conn.ObtenerTipoUsuario(dni);

                if (tipo == null) {
                    conn.InsertarAdministrador(dni, contraseña);
                    JOptionPane.showMessageDialog(null, 
                        "Administrador agregado exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else if (tipo.equalsIgnoreCase("administrador")) {
                    JOptionPane.showMessageDialog(null, 
                        "Este usuario ya es un administrador", 
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (tipo.equalsIgnoreCase("cliente")) {
                    conn.ActualizarTipoAAdministrador(dni);
                    JOptionPane.showMessageDialog(null, 
                        "Cliente actualizado a administrador", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "Rol desconocido. No se pudo procesar", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, 
                    "DNI debe ser un número válido", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void EliminarAdministrador() {
        // Crear ventana de diálogo
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblDni = new JLabel("DNI del administrador a eliminar:");
        JTextField txtDni = new JTextField(10);
        
        panel.add(lblDni);
        panel.add(txtDni);

        int result = JOptionPane.showConfirmDialog(
            null, 
            panel, 
            "Eliminar Administrador", 
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                int dni = Integer.parseInt(txtDni.getText());
                String tipo = conn.ObtenerTipoUsuario(dni);

                if (tipo == null) {
                    JOptionPane.showMessageDialog(null, 
                        "No existe ningún usuario con ese DNI", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!tipo.equalsIgnoreCase("administrador")) {
                    JOptionPane.showMessageDialog(null, 
                        "El usuario no es un administrador", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Opciones para el usuario
                Object[] options = {"Convertir a Cliente", "Eliminar", "Cancelar"};
                int opcion = JOptionPane.showOptionDialog(
                    null,
                    "¿Qué acción desea realizar?",
                    "Opciones de Administrador",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
                );

                switch (opcion) {
                    case 0: // Convertir a cliente
                        conn.ActualizarTipoACliente(dni);
                        JOptionPane.showMessageDialog(null, 
                            "Administrador convertido a cliente", 
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case 1: // Eliminar
                        conn.EliminarUsuario(dni);
                        JOptionPane.showMessageDialog(null, 
                            "Usuario eliminado exitosamente", 
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    default: // Cancelar
                        break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, 
                    "DNI debe ser un número válido", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}