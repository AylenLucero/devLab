package devlab.hotel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Reservas {
    private DBConnection conn;
    private static final Color COLOR_BEIGE = new Color(245, 245, 220);
    private static final Color COLOR_BOTON = new Color(220, 220, 200);
    private static final Color COLOR_BOTON_HOVER = new Color(210, 210, 190);

    public Reservas(DBConnection conn, Thread hilo) {
        this.conn = conn;
    }
    
    public void AgregarReserva() {
        JFrame frame = new JFrame("Nueva Reserva");
        frame.setSize(700, 600);
        frame.getContentPane().setBackground(COLOR_BEIGE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(COLOR_BEIGE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel de entrada de datos
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(COLOR_BEIGE);

        JLabel lblPersonas = new JLabel("Cantidad de personas:");
        JSpinner spinnerPersonas = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        
        JLabel lblInicio = new JLabel("Fecha de inicio (YYYY-MM-DD):");
        JTextField txtInicio = new JTextField();
        
        JLabel lblFin = new JLabel("Fecha de fin (YYYY-MM-DD):");
        JTextField txtFin = new JTextField();

        inputPanel.add(lblPersonas);
        inputPanel.add(spinnerPersonas);
        inputPanel.add(lblInicio);
        inputPanel.add(txtInicio);
        inputPanel.add(lblFin);
        inputPanel.add(txtFin);

        // Panel de resultados
        JTextArea txtResultados = new JTextArea();
        txtResultados.setEditable(false);
        txtResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtResultados.setBackground(COLOR_BEIGE);
        JScrollPane scrollPane = new JScrollPane(txtResultados);

        // Panel de botones (ahora solo con Buscar y Cancelar)
        JButton btnBuscar = crearBotonEstilizado("Buscar Habitaciones");
        JButton btnCancelar = crearBotonEstilizado("Cancelar");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(COLOR_BEIGE);
        buttonPanel.add(btnBuscar);
        buttonPanel.add(btnCancelar);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Acción del botón Buscar
        btnBuscar.addActionListener(e -> {
            try {
                int cantidadPersonas = (Integer) spinnerPersonas.getValue();
                LocalDate fechaInicio = LocalDate.parse(txtInicio.getText());
                LocalDate fechaFin = LocalDate.parse(txtFin.getText());

                long cantidadDias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
                if (cantidadDias <= 0) {
                    JOptionPane.showMessageDialog(frame, 
                        "La fecha de fin debe ser posterior a la de inicio.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<Map<String, Object>> disponibles = conn.ObtenerHabitacionesDisponiblesPorCapacidadYFecha(
                    cantidadPersonas, fechaInicio, fechaFin);

                if (disponibles.isEmpty()) {
                    txtResultados.setText("No hay habitaciones disponibles para esas fechas y cantidad de personas.");
                    return;
                }

                JPanel cardsContainer = new JPanel();
                cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
                cardsContainer.setBackground(COLOR_BEIGE);

                mainPanel.remove(scrollPane); 
                JScrollPane scrollCards = new JScrollPane(cardsContainer);
                scrollCards.setBorder(BorderFactory.createEmptyBorder());
                scrollCards.getViewport().setBackground(COLOR_BEIGE);

                mainPanel.add(scrollCards, BorderLayout.CENTER);

                // Generar cards para cada habitación
                cardsContainer.removeAll();

                for (Map<String, Object> hab : disponibles) {
                    int idHabitacion = (Integer) hab.get("id");
                    float precioNoche = ((Number) hab.get("Precio_noche")).floatValue();
                    float precioTotal = (float) (cantidadDias * precioNoche);

                    // Crear y agregar la card con todos los parámetros necesarios
                    cardsContainer.add(crearCardHabitacion(
                        idHabitacion,
                        (Integer) hab.get("Cantidad_personas"),
                        precioNoche,
                        precioTotal,
                        (Integer) hab.get("C_doble"),
                        (Integer) hab.get("C_simple"),
                        txtInicio.getText(),
                        txtFin.getText(),
                        frame
                    ));

                    // Espacio entre cards
                    cardsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
                }

                // Actualizar la interfaz
                cardsContainer.revalidate();
                cardsContainer.repaint();
                frame.pack();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Formato de fecha inválido. Use YYYY-MM-DD.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> frame.dispose());

        frame.add(mainPanel);
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
    
    private JPanel crearCardHabitacion(int id, int capacidad, float precioNoche, float precioTotal, 
                                     int camasDobles, int camasSimples, 
                                     String fechaInicio, String fechaFin,
                                     JFrame parentFrame) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(new Color(230, 230, 210));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 160)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Panel de imagen
        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(150, 130));
        lblImagen.setOpaque(true);
        lblImagen.setBackground(new Color(200, 200, 200));
        lblImagen.setHorizontalAlignment(JLabel.CENTER);
        lblImagen.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        lblImagen.setText("Habitación " + id);

        // Panel de información
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 2, 5, 5));
        infoPanel.setBackground(new Color(230, 230, 210));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        infoPanel.add(crearEtiqueta("ID:"));
        infoPanel.add(crearValor(String.valueOf(id)));
        infoPanel.add(crearEtiqueta("Capacidad:"));
        infoPanel.add(crearValor(capacidad + " personas"));
        infoPanel.add(crearEtiqueta("Precio/noche:"));
        infoPanel.add(crearValor(String.format("$%.2f", precioNoche)));
        infoPanel.add(crearEtiqueta("Precio total:"));
        infoPanel.add(crearValor(String.format("$%.2f", precioTotal)));
        infoPanel.add(crearEtiqueta("Camas dobles:"));
        infoPanel.add(crearValor(String.valueOf(camasDobles)));
        infoPanel.add(crearEtiqueta("Camas simples:"));
        infoPanel.add(crearValor(String.valueOf(camasSimples)));

        // Botón de selección (ahora con la lógica de reserva)
        JButton btnSeleccionar = new JButton("Reservar");
        btnSeleccionar.setBackground(COLOR_BOTON);
        btnSeleccionar.setForeground(Color.BLACK);
        
        // ActionListener para el botón Seleccionar
        btnSeleccionar.addActionListener(e -> {
            try {
                LocalDate inicio = LocalDate.parse(fechaInicio);
                LocalDate fin = LocalDate.parse(fechaFin);
                long dias = ChronoUnit.DAYS.between(inicio, fin);
                
                Clientes CL = new Clientes(conn);
                int DNI = CL.SolicitarDatosClienteGUI();
                
                if (DNI != -1) {
                    boolean exito = conn.InsertarReserva(
                        id, 
                        fechaInicio, 
                        fechaFin, 
                        (int) dias, 
                        precioTotal, 
                        DNI);
                    
                    if (exito) {
                        JOptionPane.showMessageDialog(parentFrame, 
                            "Reserva realizada con éxito!", 
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        parentFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(parentFrame, 
                            "Error al realizar la reserva", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Formato de fecha inválido", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Efecto hover para el botón
        btnSeleccionar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSeleccionar.setBackground(COLOR_BOTON_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSeleccionar.setBackground(COLOR_BOTON);
            }
        });

        // Panel derecho (información + botón)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(230, 230, 210));
        rightPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(230, 230, 210));
        buttonPanel.add(btnSeleccionar);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        card.add(lblImagen, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.CENTER);

        return card;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
    }

    private JLabel crearValor(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return label;
    }
    
    private JLabel crearImagenHabitacion(int idHabitacion) {
        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(150, 130));

        try {
            String rutaImagen = "../resorses/images/fondo1.jpg";
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaImagen));
            Image img = icono.getImage().getScaledInstance(150, 130, Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblImagen.setOpaque(true);
            lblImagen.setBackground(new Color(200, 200, 200));
            lblImagen.setHorizontalAlignment(JLabel.CENTER);
            lblImagen.setText("Habitación " + idHabitacion);
        }

        return lblImagen;
    }
}