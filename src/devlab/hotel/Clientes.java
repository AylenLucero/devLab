package devlab.hotel;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class Clientes {
    private DBConnection conn;
    private static final Color COLOR_BEIGE = new Color(245, 245, 220);
    private static final Color COLOR_BOTON = new Color(220, 220, 200);
    private static final Color COLOR_BOTON_HOVER = new Color(210, 210, 190);
    private static final Color COLOR_CAMPO = new Color(255, 255, 255);

    public Clientes(DBConnection conn) {
        this.conn = conn;
    }

    public int SolicitarDatosClienteGUI() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Registro de Cliente");
        dialog.setSize(500, 450);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(COLOR_BEIGE);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(COLOR_BEIGE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Campos del formulario
        JLabel lblDni = new JLabel("DNI:");
        JTextField txtDni = new JTextField();
        txtDni.setBackground(COLOR_CAMPO);

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();
        txtNombre.setBackground(COLOR_CAMPO);

        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellido = new JTextField();
        txtApellido.setBackground(COLOR_CAMPO);

        JLabel lblEdad = new JLabel("Edad:");
        JSpinner spinnerEdad = new JSpinner(new SpinnerNumberModel(18, 1, 120, 1));
        ((JSpinner.DefaultEditor) spinnerEdad.getEditor()).getTextField().setBackground(COLOR_CAMPO);

        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField();
        txtTelefono.setBackground(COLOR_CAMPO);

        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();
        txtEmail.setBackground(COLOR_CAMPO);

        // Botones
        JButton btnRegistrar = crearBotonEstilizado("Registrar");
        JButton btnCancelar = crearBotonEstilizado("Cancelar");

        formPanel.add(lblDni);
        formPanel.add(txtDni);
        formPanel.add(lblNombre);
        formPanel.add(txtNombre);
        formPanel.add(lblApellido);
        formPanel.add(txtApellido);
        formPanel.add(lblEdad);
        formPanel.add(spinnerEdad);
        formPanel.add(lblTelefono);
        formPanel.add(txtTelefono);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);
        formPanel.add(btnRegistrar);
        formPanel.add(btnCancelar);

        // Variable para almacenar el DNI
        final int[] dniResult = {-1};

        btnRegistrar.addActionListener(e -> {
            try {
                // Validar DNI
                int dni = Integer.parseInt(txtDni.getText());
                if (dni <= 1111111) {
                    JOptionPane.showMessageDialog(dialog, "DNI debe ser mayor a 1111111", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar nombre
                String nombre = txtNombre.getText().trim();
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Nombre no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar apellido
                String apellido = txtApellido.getText().trim();
                if (apellido.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Apellido no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Obtener edad
                int edad = (Integer) spinnerEdad.getValue();

                // Validar teléfono
                String telefono = txtTelefono.getText().trim();
                if (telefono.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Teléfono no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar email
                String email = txtEmail.getText().trim();
                if (!Pattern.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$", email)) {
                    JOptionPane.showMessageDialog(dialog, "Email inválido", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insertar cliente
                conn.InsertarCliente(dni, nombre, apellido, edad, telefono, email);
                dniResult[0] = dni;
                dialog.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "DNI debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> {
            dniResult[0] = -1;
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setVisible(true);

        return dniResult[0];
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