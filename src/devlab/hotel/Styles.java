/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devlab.hotel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 *
 * @author Equipo
 */
public class Styles {
    public static JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto) {
            private int arcWidth = 15;
            private int arcHeight = 15;
            // Solución clave: Sobreescribimos paintComponent para control total
            @Override
            protected void paintComponent(Graphics g) {
                // Fondo beige sólido (sin transparencia)
                g.setColor(new Color(255, 230, 200, 200));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

                // Pintar el texto
                super.paintComponent(g);
            }

            // Mejorar renderizado del borde
            @Override
            protected void paintBorder(Graphics g) {
                g.setColor(new Color(150, 150, 100));
                g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
            }
        };

        // Configuración básica
        boton.setOpaque(false); // Importante para nuestra implementación personalizada
        boton.setContentAreaFilled(false);
        boton.setForeground(Color.BLACK);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false); // Elimina el recuadro de enfoque

        // Efecto hover mejorado
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            private final Color BEIGE = new Color(245, 245, 220);
            private final Color BEIGE_HOVER = new Color(230, 230, 200);
            private final Color BORDE_HOVER = new Color(100, 100, 70);
            
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(BEIGE_HOVER);
                
                // 2. Efecto de elevación (sombra)
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDE_HOVER, 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(BEIGE);
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return boton;
    }
}
