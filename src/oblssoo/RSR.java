package oblssoo;

import java.util.LinkedList;
import java.util.Queue;

public class RSR extends Recurso {

    private Queue<Proceso> colaDeEspera;
    private Instruccion[] instrucciones;

    public RSR(String nombre) {
        this.setNombre(nombre);
        this.setTipo((byte) 1);
        this.colaDeEspera = new LinkedList<>();
        instrucciones = new Instruccion[3];

    }

    public void setInstrucciones(Instruccion pedir, Instruccion usar, Instruccion devolver) {
        instrucciones[0] = pedir;
        instrucciones[1] = usar;
        instrucciones[2] = devolver;

    }

    public Instruccion getPedir() {
        return instrucciones[0];
    }

    public Instruccion getUsar() {
        return instrucciones[1];
    }

    public Instruccion getDevolver() {
        return instrucciones[2];
    }

    public void encolar(Proceso p) {
        this.colaDeEspera.add(p);
        if (getAtendido().equals(p)) {
            System.out.println(p.toString() + "tomo" + this.getNombre());
        }
    }

    public void desencolar() {
        this.colaDeEspera.remove();
        if (colaDeEspera.size() > 0) {
            System.out.println(getAtendido() + "tomo" + this.getNombre());
        }
    }
    
    public boolean colaVacia(){
        return this.colaDeEspera.isEmpty();
    }

    public Proceso getAtendido() {
        return this.colaDeEspera.peek();
    }

}
