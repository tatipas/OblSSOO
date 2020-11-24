/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oblssoo;
import java.util.ArrayList;

public class Programa {

    private String instrucciones;
    private byte id;
    private ArrayList<Recurso> listaRecursos;
    

    public Programa(String s) {
        this.instrucciones = s;

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

    @Override
    public String toString() {
        String s = "Programa: " + id ;
        return s;
    }

    public boolean equals(Proceso u) {
        return getId() == u.getId();
    }
    
    public int getPeso(){
        return instrucciones.length();
    }

}
