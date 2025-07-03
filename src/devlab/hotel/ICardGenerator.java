package devlab.hotel;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public interface ICardGenerator {
    JPanel crearCard(Map<String, Object> datos, boolean conBoton, String textoBoton);
    
    // Método default con implementación básica
    default JPanel crearCard(Map<String, Object> datos) {
        return crearCard(datos, false, ""); // Llama al método principal con valores por defecto
    }
    
    default void containerCards(List<Map<String, Object>> datos, JPanel contenedor, boolean conBoton, String textoBoton) {
        contenedor.removeAll();

        if (datos.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "No hay datos disponibles.");
        } else {
            for (Map<String, Object> item : datos) {
                contenedor.add(crearCard(item, conBoton, textoBoton));
            }
        }

        contenedor.setPreferredSize(new java.awt.Dimension(
            contenedor.getWidth(), 
            contenedor.getComponentCount() * 140
        ));
        contenedor.revalidate();
        contenedor.repaint();
    }
    
    default JPanel crearBaseCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5),
            javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY)
        ));
        card.setPreferredSize(new java.awt.Dimension(500, 130));
        
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("img/habitacion.jpg"));
            java.awt.Image scaledImg = icon.getImage().getScaledInstance(120, 130, java.awt.Image.SCALE_SMOOTH); 
            javax.swing.JLabel imgLabel = new javax.swing.JLabel(new javax.swing.ImageIcon(scaledImg));
            card.add(imgLabel, BorderLayout.WEST);
        } catch (Exception ex) {
            System.err.println("No se pudo cargar la imagen: " + ex.getMessage());
        }
        
        return card;
    }
}