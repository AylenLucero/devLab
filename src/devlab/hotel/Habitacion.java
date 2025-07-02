/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devlab.hotel;

/**
 *
 * @author Rodrigo
 */
public class Habitacion {
    private int id;
    private int cantidadPersonas;
    private int camaDoble;
    private int camaSimple;
    private String disponibilidad;
    private float precioPorNoche;

    public Habitacion(int id, int cantidadPersonas, int camaDoble, int camaSimple, String disponibilidad, float precioPorNoche) {
        this.id = id;
        this.cantidadPersonas = cantidadPersonas;
        this.camaDoble = camaDoble;
        this.camaSimple = camaSimple;
        this.disponibilidad = disponibilidad;
        this.precioPorNoche = precioPorNoche;
    }

    public int getId() {
        return id;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public int getCamaDoble() {
        return camaDoble;
    }

    public int getCamaSimple() {
        return camaSimple;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public float getPrecioPorNoche() {
        return precioPorNoche;
    }

    @Override
    public String toString() {
        return "id: " + id +
               " - Personas: " + cantidadPersonas +
               " - Cama Doble: " + camaDoble +
               " - Cama Simple: " + camaSimple +
               " - Disponibilidad: " + disponibilidad +
               " - Precio: $" + precioPorNoche;
    }
}