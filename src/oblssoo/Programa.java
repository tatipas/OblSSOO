/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oblssoo;

/**
 *
 * @author Graziano
 */
public class Programa {

    private String instrucciones;
    private byte id;

    public Programa(String s) {
        this.instrucciones = s;
        //this.tiempoRequerido = calcularTiempoRequerido();
    }

    public String getInstrucciones() {
        return this.instrucciones;
    }

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public String toString() {
        String s = "Programa: " + id + " | Instrucciones: " + this.instrucciones;
        return s;
    }

    public boolean equals(Proceso u) {
        return getId() == u.getId();
    }

}
