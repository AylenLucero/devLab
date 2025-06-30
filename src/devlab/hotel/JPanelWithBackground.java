package devlab.hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JPanelWithBackground extends JPanel {
    private List<Image> imagenes = new ArrayList<>();
    private int imagenActual = 0;
    private Timer timer;
    private int delay = 2000; // Cambio cada 5 segundos

    public JPanelWithBackground() {
        cargarImagenes();
        iniciarCarrusel();
        setLayout(new BorderLayout()); // Mantener tu layout actual
    }

    private void cargarImagenes() {
        // Nombres de tus imágenes de fondo
        String[] rutas = {
            "../resourses/images/fondo1.jpg",
            "../resourses/images/fondo2.jpg", 
            "../resourses/images/fondo3.jpg"
        };
        
        for (String ruta : rutas) {
            try (InputStream is = getClass().getResourceAsStream(ruta)) {
                if (is != null) {
                    imagenes.add(ImageIO.read(is));
                } else {
                    System.err.println("No se encontró: " + ruta);
                }
            } catch (Exception e) {
                System.err.println("Error al cargar " + ruta + ": " + e.getMessage());
            }
        }
        
        if (imagenes.isEmpty()) {
            // Fondo de respaldo si no hay imágenes
            setBackground(new Color(245, 245, 220)); // Tu color beige
        }
    }

    private void iniciarCarrusel() {
        timer = new Timer(delay, e -> {
            imagenActual = (imagenActual + 1) % imagenes.size();
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!imagenes.isEmpty()) {
            Image img = imagenes.get(imagenActual);
            // Escalar manteniendo relación de aspecto
            int ancho = getWidth();
            int alto = getHeight();
            float relacionImg = (float)img.getWidth(null)/img.getHeight(null);
            float relacionPanel = (float)ancho/alto;
            
            int x = (getWidth() - ancho)/2;
            int y = (getHeight() - alto)/2;
            
            g.drawImage(img, x, y, ancho, alto, this);
        }
    }

    public void detenerCarrusel() {
        if (timer != null) {
            timer.stop();
        }
    }
}