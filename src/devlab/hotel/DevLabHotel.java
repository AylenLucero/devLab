package devlab.hotel;

import javax.swing.*;
import java.awt.*;

public class DevLabHotel {
    public static void main(String[] args) {
        DBConnection conn = new DBConnection();
        Thread hilo = new Thread(new VerificadorDeOcupacion(conn));
        Thread hiloRes = new Thread(new VerificadorDeTerminacionReserva(conn));
        hilo.start();
        hiloRes.start();
        SwingUtilities.invokeLater(() -> {
            Login login = new Login(conn, null, hilo);
            mostrarMenuPrincipal(login);
        });
    } 

    public static void mostrarMenuPrincipal(Login login) {
        JFrame frame = new JFrame("Hotel DevLab");
        Styles style = new Styles();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanelWithBackground panel = new JPanelWithBackground();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Para que se vea el fondo
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JLabel label = new JLabel("=== DEVLAB HOTEL ===", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnLogin = style.crearBotonEstilizado("Iniciar sesión");
        JButton btnRegistro = style.crearBotonEstilizado("Registrarse");
        JButton btnSalir = style.crearBotonEstilizado("Salir");

        Dimension buttonSize = new Dimension(150, 40);
        btnLogin.setPreferredSize(buttonSize);
        btnRegistro.setPreferredSize(buttonSize);
        btnSalir.setPreferredSize(buttonSize);
        
        // Listeners
        btnLogin.addActionListener(e -> {
            frame.dispose();
            login.loginUsuarioGUI();
        });

        btnRegistro.addActionListener(e -> {
            frame.dispose();
            login.registrarUsuarioGUI();
        });

        btnSalir.addActionListener(e -> System.exit(0));

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegistro);
        buttonPanel.add(btnSalir);

        panel.add(label, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    
}