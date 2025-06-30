package devlab.hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class Login {
    private DBConnection conn;
    private Scanner scanner; // Se mantiene pero no se usa en GUI
    private AdminHabitaciones adminHab;
    private AdminReservas adminRes;
    private Administrador admin;
    private Reservas ClRes;
    private Thread hilo;
    private Styles style;
    private static final Color COLOR_BEIGE = new Color(245, 245, 220);

    // Constructor se mantiene igual
    public Login(DBConnection conn, Scanner scanner, Thread hilo) {
        this.conn = conn;
        this.scanner = scanner; // Scanner queda pero no se usa
        this.hilo = hilo;
        this.adminHab = new AdminHabitaciones(conn, null); // Scanner como null
        this.adminRes = new AdminReservas(conn);
        this.admin = new Administrador(conn, null);
        this.ClRes = new Reservas(conn,hilo);
        this.style = new Styles();
    }

    // Versión GUI del loginUsuario()
    public void loginUsuarioGUI() {
        JFrame frame = new JFrame("Inicio de Sesión");
        frame.getContentPane().setBackground(COLOR_BEIGE);
        frame.setSize(350, 200);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(COLOR_BEIGE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblDni = new JLabel("DNI:");
        JTextField txtDni = new JTextField();
        JLabel lblPass = new JLabel("Contraseña:");
        JPasswordField txtPass = new JPasswordField();
        JButton btnLogin = style.crearBotonEstilizado("Ingresar");
        JButton btnBack = style.crearBotonEstilizado("Volver");

        btnLogin.addActionListener(e -> {
            try {
                int dni = Integer.parseInt(txtDni.getText());
                String pass = new String(txtPass.getPassword());
                String tipoUsuario = conn.BuscarUser(dni, pass);

                if (tipoUsuario != null) {
                    frame.dispose();
                    mostrarMenuUsuarioGUI(dni, tipoUsuario);
                    frame.setBackground(COLOR_BEIGE);
                } else {
                    JOptionPane.showMessageDialog(frame, 
                        "Credenciales incorrectas", "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    subMenuGUI("cliente", frame);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "DNI debe ser numérico", "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBack.addActionListener(e -> {
            frame.dispose();
            DevLabHotel.mostrarMenuPrincipal(this);
        });

        panel.add(lblDni);
        panel.add(txtDni);
        panel.add(lblPass);
        panel.add(txtPass);
        panel.add(btnLogin);
        panel.add(btnBack);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Versión GUI de registrarUsuario()
    public void registrarUsuarioGUI() {
        JFrame frame = new JFrame("Registro de Cliente");
        frame.setSize(350, 200);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblDni = new JLabel("DNI:");
        JTextField txtDni = new JTextField();
        JLabel lblPass = new JLabel("Contraseña:");
        JPasswordField txtPass = new JPasswordField();
        JButton btnRegistrar = style.crearBotonEstilizado("Registrar");
        JButton btnBack = style.crearBotonEstilizado("Volver");

        btnRegistrar.addActionListener(e -> {
            try {
                int dni = Integer.parseInt(txtDni.getText());
                String pass = new String(txtPass.getPassword());
                conn.InsrtarUsuario(dni, pass, "cliente");
                JOptionPane.showMessageDialog(frame, 
                    "Registro exitoso!", "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                DevLabHotel.mostrarMenuPrincipal(this);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "DNI debe ser numérico", "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBack.addActionListener(e -> {
            frame.dispose();
            DevLabHotel.mostrarMenuPrincipal(this);
        });

        panel.add(lblDni);
        panel.add(txtDni);
        panel.add(lblPass);
        panel.add(txtPass);
        panel.add(btnRegistrar);
        panel.add(btnBack);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void mostrarMenuUsuarioGUI(int dni, String tipoUsuario) {
        JFrame frame = new JFrame("Menú de " + tipoUsuario);
        frame.setSize(600, 400);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(COLOR_BEIGE);
        tabbedPane.setForeground(Color.BLACK);

        if (tipoUsuario.equalsIgnoreCase("administrador")) {
            JPanel adminPanel = crearPanelAdministrador(dni);
            tabbedPane.addTab("Bienvenido Administrador", adminPanel);
            tabbedPane.setBackground(COLOR_BEIGE);
            tabbedPane.setForeground(Color.BLACK);
        } else {
            JPanel clientePanel = crearPanelCliente();
            tabbedPane.addTab("Bienvenido Cliente", clientePanel);
        }

        JButton btnSalir = style.crearBotonEstilizado("Cerrar Sesión");
        btnSalir.addActionListener(e -> {
            frame.dispose();
            DevLabHotel.mostrarMenuPrincipal(this);
        });

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(btnSalir, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel crearPanelAdministrador(int dni) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBackground(new Color(230, 230, 210));

        seccionHabitacion(panel);
        seccionReserva(panel);
        agregarBoton(panel, "Agregar Admin", () -> admin.AgregarAdministrador());
        agregarBoton(panel, "Eliminar Admin", () -> admin.EliminarAdministrador());

        return panel;
    }
    
    private void seccionHabitacion (JPanel panel) {
        // Botón principal de Habitaciones con menú desplegable
        JButton btnHabitaciones = style.crearBotonEstilizado("Sección Habitaciones");

        // Crear el menú desplegable
        JPopupMenu menuHabitaciones = new JPopupMenu();
        menuHabitaciones.setBackground(new Color(230, 230, 210));
        
        // Añadir items al menú
        JMenuItem itemVer = new JMenuItem("Ver Habitaciones");
        itemVer.addActionListener(e -> adminHab.ListarHabitaciones());
        menuHabitaciones.add(itemVer);

        JMenuItem itemAgregar = new JMenuItem("Agregar Habitación");
        itemAgregar.addActionListener(e -> adminHab.AgregarHabitacion());
        menuHabitaciones.add(itemAgregar);

        JMenuItem itemEditar = new JMenuItem("Editar Habitación");
        itemEditar.addActionListener(e -> adminHab.EditarHabitacion());
        menuHabitaciones.add(itemEditar);

        JMenuItem itemEliminar = new JMenuItem("Eliminar Habitación");
        itemEliminar.addActionListener(e -> adminHab.EliminarHabitacion());
        menuHabitaciones.add(itemEliminar);

        // Configurar el botón para mostrar el menú
        btnHabitaciones.addActionListener(e -> {
            menuHabitaciones.show(btnHabitaciones, 0, btnHabitaciones.getHeight());
        });
        
        btnHabitaciones.addActionListener(e -> {
        // Mostrar el menú a la derecha del botón
            menuHabitaciones.show(btnHabitaciones, btnHabitaciones.getWidth(), 0);
        });

        // Aplicar estilos al menú (código existente)
        configurarEstiloMenu(menuHabitaciones);

        panel.add(btnHabitaciones);
    }
    
    private void seccionReserva (JPanel panel) {
        JButton btnHabitaciones = style.crearBotonEstilizado("Sección Reservas");

        // Crear el menú desplegable
        JPopupMenu menuHabitaciones = new JPopupMenu();
        menuHabitaciones.setBackground(new Color(230, 230, 210));
        
        // Añadir items al menú
        JMenuItem itemVer = new JMenuItem("Listar Reservas");
        itemVer.addActionListener(e -> adminRes.ListarReservas());
        menuHabitaciones.add(itemVer);

        JMenuItem itemAgregar = new JMenuItem("Agregar Reserva");
        itemAgregar.addActionListener(e -> ClRes.AgregarReserva());
        menuHabitaciones.add(itemAgregar);

        JMenuItem itemEditar = new JMenuItem("Editar Reserva");
        itemEditar.addActionListener(e -> adminRes.EditarReserva());
        menuHabitaciones.add(itemEditar);

        JMenuItem itemEliminar = new JMenuItem("Eliminar Reserva");
        itemEliminar.addActionListener(e -> adminRes.EliminarReserva());
        menuHabitaciones.add(itemEliminar);

        // Configurar el botón para mostrar el menú
        btnHabitaciones.addActionListener(e -> {
            menuHabitaciones.show(btnHabitaciones, 0, btnHabitaciones.getHeight());
        });
        
        btnHabitaciones.addActionListener(e -> {
        // Mostrar el menú a la derecha del botón
            menuHabitaciones.show(btnHabitaciones, btnHabitaciones.getWidth(), 0);
        });

        // Aplicar estilos al menú (código existente)
        configurarEstiloMenu(menuHabitaciones);

        panel.add(btnHabitaciones);
    }
    
    private void configurarEstiloMenu(JPopupMenu menu) {
        menu.setBackground(new Color(230, 230, 210));
        menu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 160)),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));

        for (Component comp : menu.getComponents()) {
            if (comp instanceof JMenuItem) {
                JMenuItem item = (JMenuItem) comp;
                item.setBackground(new Color(220, 220, 200));
                item.setForeground(Color.BLACK);
                item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                item.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

                item.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        item.setBackground(new Color(210, 210, 190));
                    }
                    public void mouseExited(MouseEvent e) {
                        item.setBackground(new Color(220, 220, 200));
                    }
                });
            }
        }
    }

    private JPanel crearPanelCliente() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBackground(new Color(230, 230, 210));
        
        agregarBoton(panel, "Agregar Reserva", () -> ClRes.AgregarReserva());
        agregarBoton(panel, "Editar Reserva", () -> adminRes.EditarReserva());
        agregarBoton(panel, "Eliminar Reserva", () -> adminRes.EliminarReserva());
        
        return panel;
    }

    private void agregarBoton(JPanel panel, String texto, Runnable accion) {
        JButton boton = style.crearBotonEstilizado(texto);
        boton.addActionListener(e -> accion.run());
        panel.add(boton);
    }

    private void subMenuGUI(String tipoUsuario, JFrame parent) {
        Object[] options = tipoUsuario.equals("cliente") ? 
            new Object[]{"Reintentar", "Registrarse", "Volver"} :
            new Object[]{"Reintentar", "Volver"};
        
        int opcion = JOptionPane.showOptionDialog(parent,
            "¿Qué desea hacer?",
            "Opción no válida",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        switch(opcion) {
            case 0: // Reintentar
                break;
            case 1:
                if (tipoUsuario.equals("cliente")) {
                    parent.dispose();
                    registrarUsuarioGUI();
                } else {
                    parent.dispose();
                    DevLabHotel.mostrarMenuPrincipal(this);
                }
                break;
            case 2:
                parent.dispose();
                DevLabHotel.mostrarMenuPrincipal(this);
                break;
        }
    }
}