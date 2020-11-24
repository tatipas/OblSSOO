package oblssoo;

import java.util.ArrayList;

public class Memoria extends Recurso {

    private boolean[] array;
    private ArrayList<Proceso> cola;
    private int espacioLibre;

    public Memoria(int d) {
        this.array = new boolean[d];
        this.cola = new ArrayList<>();
        this.espacioLibre = d;
    }

    public boolean addOrQueueProceso(Proceso p, Sistema s) { // si se carga en memoria, devuelve true, sino false.
        s.agregarProceso(p);
        if (p.getPeso() <= this.getEspacioLibre()) {
            addProceso(p, s);
            return true;
        } else {
            cola.add(p);
            s.agregarProcesoBloqueado(p);
            return false;
        }
    }

    public void quitarDeMemoria(Proceso p) {
        while (p.hayDir()) {
            array[p.pollDir()] = false;
            this.espacioLibre++;
        }
    }

    public void addQueuedProcesos(Sistema s) { //hago una lista de los que voy a agregar por una exception
        int peso = 0;
        ArrayList<Proceso> list = new ArrayList<>();
        for (Proceso p : cola) {
            if (p.getPeso() <= this.getEspacioLibre()) {
                restarEspacio(p);
                list.add(p);
            }
        }
        for (Proceso p : list) {
            peso = p.getPeso();
            for (int i = 0; 0 < peso; i++) {
                if (!array[i]) {
                    array[i] = true;
                    p.addDir(i);
                    peso--;
                }
            }
            cola.remove(p);
            s.desbloquear(p);
            System.out.println("Se cargo a " + p.toString() + " en memoria");
            imprimirMemoria();
        }
    }

    private int getEspacioLibre() {
        return espacioLibre;
    }

    private void restarEspacio(Proceso p) {
        this.espacioLibre -= p.getPeso();
        return;
    }

    private void addProceso(Proceso p, Sistema s) {
        int peso = p.getPeso();
        for (int i = 0; peso > 0; i++) {
            if (!array[i]) {
                array[i] = true;
                p.addDir(i);
                peso--;
            }
        }
        s.encolar(p);
        restarEspacio(p);
    }

    public void imprimirMemoria() {
        String r = "Espacio libre de Memoria: " + this.espacioLibre + "/" + array.length + " [";
        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                r += "*";
            } else {
                r += "-";
            }
        }
        r += "]";
        System.out.println(r);
    }
}
