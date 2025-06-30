package devlab.hotel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class AdminReservas {
    private DBConnection conn;
    private static final Color COLOR_BEIGE = new Color(245, 245, 220);
    private static final Color COLOR_BOTON = new Color(220, 220, 200);
    private static final Color COLOR_BOTON_HOVER = new Color(210, 210, 190);

    public AdminReservas(DBConnection conn) {
        this.conn = conn;
    }

    public void ListarReservas() {
        JFrame frame = new JFrame("Listado de Reservas");
        frame.setSize(800, 600);
        frame.getContentPane().setBackground(COLOR_BEIGE);

        JTextArea textArea = new JTextArea(conn.MostrarReservas());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(COLOR_BEIGE);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnCerrar = crearBotonEstilizado("Cerrar");
        btnCerrar.addActionListener(e -> frame.dispose());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BEIGE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnCerrar, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void EditarReserva() {
        JFrame frame = new JFrame("Editar Reserva");
        frame.setSize(400, 350);
        frame.getContentPane().setBackground(COLOR_BEIGE);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBackground(COLOR_BEIGE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblDni = new JLabel("DNI del cliente:");
        JTextField txtDni = new JTextField();
        JLabel lblHabitacion = new JLabel("Nuevo ID de habitación:");
        JTextField txtHabitacion = new JTextField();
        JLabel lblInicio = new JLabel("Nueva fecha de inicio (YYYY-MM-DD):");
        JTextField txtInicio = new JTextField();
        JLabel lblFin = new JLabel("Nueva fecha de fin (YYYY-MM-DD):");
        JTextField txtFin = new JTextField();

        JButton btnEditar = crearBotonEstilizado("Editar Reserva");
        JButton btnCancelar = crearBotonEstilizado("Cancelar");

        btnEditar.addActionListener(e -> {
            try {
                int dniCliente = Integer.parseInt(txtDni.getText());
                
                if (!conn.ExisteReservaPorDNI(dniCliente)) {
                    JOptionPane.showMessageDialog(frame, "No se encontró reserva para ese DNI", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int idReserva = conn.ObtenerReservaPorDNI(dniCliente);
                if (idReserva == -1) {
                    JOptionPane.showMessageDialog(frame, "Error al obtener la reserva", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int idHabitacion = Integer.parseInt(txtHabitacion.getText());
                LocalDate fechaInicio = LocalDate.parse(txtInicio.getText());
                LocalDate fechaFin = LocalDate.parse(txtFin.getText());

                long cantidadDias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
                if (cantidadDias <= 0) {
                    JOptionPane.showMessageDialog(frame, "La fecha fin debe ser posterior", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                float precioNoche = conn.ObtenerPrecioHabitacion(idHabitacion);
                if (precioNoche == -1f) {
                    JOptionPane.showMessageDialog(frame, "Habitación no encontrada", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (conn.ExisteSuperposicionReservaExcluyendo(idHabitacion, fechaInicio, fechaFin, idReserva)) {
                    JOptionPane.showMessageDialog(frame, "Habitación ya reservada en esas fechas", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                float precioTotal = cantidadDias * precioNoche;
                boolean exito = conn.EditarReserva(idReserva, idHabitacion, 
                    txtInicio.getText(), txtFin.getText(), (int) cantidadDias, precioTotal);

                if (exito) {
                    JOptionPane.showMessageDialog(frame, "Reserva editada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Error al editar reserva", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "DNI y ID deben ser números", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de fecha inválido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> frame.dispose());

        panel.add(lblDni);
        panel.add(txtDni);
        panel.add(lblHabitacion);
        panel.add(txtHabitacion);
        panel.add(lblInicio);
        panel.add(txtInicio);
        panel.add(lblFin);
        panel.add(txtFin);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 10));
        panelBotones.setBackground(COLOR_BEIGE);
        panelBotones.add(btnEditar);
        panelBotones.add(btnCancelar);

        panel.add(panelBotones);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void EliminarReserva() {
        JFrame frame = new JFrame("Eliminar Reserva");
        frame.setSize(400, 200);
        frame.getContentPane().setBackground(COLOR_BEIGE);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBackground(COLOR_BEIGE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblId = new JLabel("ID de la reserva a eliminar:");
        JTextField txtId = new JTextField();

        JButton btnEliminar = crearBotonEstilizado("Eliminar");
        JButton btnCancelar = crearBotonEstilizado("Cancelar");

        btnEliminar.addActionListener(e -> {
            try {
                int idReserva = Integer.parseInt(txtId.getText());
                int opcion = JOptionPane.showConfirmDialog(frame, 
                    "¿Está seguro de eliminar la reserva " + idReserva + "?", 
                    "Confirmar", JOptionPane.YES_NO_OPTION);

                if (opcion == JOptionPane.YES_OPTION) {
                    int idHabitacion = conn.ObtenerIdHabitacionDeReserva(idReserva);
                    if (idHabitacion == -1) {
                        JOptionPane.showMessageDialog(frame, "Reserva no encontrada", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean exito = conn.EliminarReserva(idReserva);
                    if (!exito) {
                        JOptionPane.showMessageDialog(frame, "Error al eliminar reserva", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!conn.HabitacionTieneReservasActivas(idHabitacion)) {
                        conn.ActualizarDisponibilidadHabitacion(idHabitacion, "Disponible");
                    }

                    JOptionPane.showMessageDialog(frame, "Reserva eliminada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "ID debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> frame.dispose());

        panel.add(lblId);
        panel.add(txtId);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 10));
        panelBotones.setBackground(COLOR_BEIGE);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCancelar);

        panel.add(panelBotones);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(COLOR_BOTON);
        boton.setForeground(Color.BLACK);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 160)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_BOTON_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_BOTON);
            }
        });

        return boton;
    }
}