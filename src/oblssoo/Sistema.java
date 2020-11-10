package oblssoo;

import java.util.ArrayList;
import java.util.Queue;
import java.time.*;
import static java.time.Duration.ZERO;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.LinkedList;

public class Sistema {

    private static ArrayList<Proceso> listaProcesos;
    private static ArrayList<Programa> listaProgramas;
    private static ArrayList<Instruccion> listaInstrucciones;
    private static ArrayList<Recurso> listaRecursos;
    private ArrayList<Usuario> listaUsuarios;
    private ArrayList<Proceso> listaBloqueados;
    private static Queue<Proceso> colaDeEjecucion;
    private Proceso enEjecucion;
    private Usuario enSesion;
    private Duration quantum;
    private Duration tiempoEjec;

    public Sistema(long q) {
        this.listaProcesos = new ArrayList<>();
        this.listaProgramas = new ArrayList<>();
        this.listaRecursos = new ArrayList<>();
        this.listaInstrucciones = new ArrayList<>();
        this.listaUsuarios = new ArrayList<>();
        this.listaBloqueados = new ArrayList<>();
        this.colaDeEjecucion = new LinkedList<>();
        this.enEjecucion = null;
        this.enSesion = null;
        this.quantum = Duration.of(q, SECONDS);
        this.tiempoEjec = ZERO;
    }

    public Duration getQuantum() {
        return quantum;
    }

    public void setQuantum(long quantum) {
        this.quantum = Duration.of(quantum, SECONDS);
    }

    public byte getCantCola() {
        return (byte) colaDeEjecucion.size();
    }

    public Proceso getEnEjecucion() {
        return enEjecucion;
    }

    public void setEnEjecucion() {
        this.enEjecucion = colaDeEjecucion.element();
        colaDeEjecucion.element().setEstadoEnEjecucion();
        System.out.println(enEjecucion.toString() + " ha tomado el procesador");

    }

    public static ArrayList<Recurso> getListaRecursos() {
        return listaRecursos;
    }

    public void agregarRecurso(Recurso p) {
        this.getListaRecursos().add(p);
        p.setId((byte) listaRecursos.size());
    }

    public Usuario getEnSesion() {
        return enSesion;
    }

    public void setEnSesion(Usuario u) {
        this.enSesion = u;
    }

    public void encolar(Proceso p) {
        this.colaDeEjecucion.add(p);
        p.setId((byte) colaDeEjecucion.size());
    }

    public void desencolar() {
        System.out.println(this.colaDeEjecucion.element().toString() + " ha finalizado su ejecucion");
        this.colaDeEjecucion.remove();
        if (getCantCola() != 0) {
            setEnEjecucion();
        }
        this.tiempoEjec = ZERO;
    }

    public void timeout() {
        Proceso p = this.colaDeEjecucion.poll();
        this.colaDeEjecucion.add(p);
        p.setEstadoListo();
        this.colaDeEjecucion.element().setEstadoEnEjecucion();
        this.enEjecucion = this.colaDeEjecucion.element();
    }

    public void agregarProceso(Proceso p) {
        this.listaProcesos.add(p);
        p.setId((byte) listaProcesos.size());
    }

    public void agregarPrograma(Programa p) {
        this.listaProgramas.add(p);
        p.setId((byte) listaProgramas.size());
    }

    public void agregarInstruccion(Instruccion p) {
        this.listaInstrucciones.add(p);
    }

    public void agregarUsuario(Usuario p) {
        this.listaUsuarios.add(p);
        p.setId((byte) listaUsuarios.size());
    }

    public void bloquear(Proceso p) {
        p.setEstadoBloqueado();
        this.desencolar();
        this.encolar(p);
    }

    public void siguienteInstante() throws InterruptedException {

        if (enEjecucion.getTiempoInst().equals(enEjecucion.gettRequeridoInst())) {
            System.out.println("En ejecución: " + enEjecucion.getInstEnEjecucion().toString() + " en la posicion: " + enEjecucion.getPos());
        }
        Thread.sleep(1000);
        agregarTiempoEjec(1);
        enEjecucion.restarTiempo(1); // se resta al tiempo de la instruccion, y si llega a 0, se pasa a la siguiente instruccion
        if (enEjecucion.getCantI() == 0) {
            desencolar();

        } else {
            if (tiempoEjec.equals(quantum)) {
                timeout();
                tiempoEjec = ZERO;
                System.out.println("El " + getEnEjecucion().toString() + " tomo el procesador por Timeout");
            }
        }
    }

    public void agregarTiempoEjec(int t) {
        tiempoEjec = tiempoEjec.plusSeconds(1);
    }

    public static ArrayList<Instruccion> getListaInstrucciones() {
        return listaInstrucciones;
    }

    public static ArrayList<Proceso> getListaProcesos() {
        return listaProcesos;
    }

    public ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public ArrayList<Proceso> getListaBloqueados() {
        return listaBloqueados;
    }

    public Queue<Proceso> getColaDeEjecucion() {
        return colaDeEjecucion;
    }

    public Duration getTiempoEjec() {
        return tiempoEjec;
    }

    public static Instruccion getInstByID(char c) {
        for (int i = 0; i < listaInstrucciones.size(); i++) {
            if (listaInstrucciones.get(i).getId() == c) {
                return listaInstrucciones.get(i);
            }
        }
        return null;
    }

    public Programa getProgramaByID(byte id) {
        for (int i = 0; i < listaProgramas.size(); i++) {
            if (listaProgramas.get(i).getId() == id) {
                return listaProgramas.get(i);
            }
        }
        return null;
    }

    public Usuario getUsuarioByID(byte c) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getId() == c) {
                return listaUsuarios.get(i);
            }
        }
        return null;
    }

    public static ArrayList<Programa> getListaProgramas() {
        return listaProgramas;
    }

    public void imprimirProgramas() {
        for (int i = 0; i < getListaProgramas().size(); i++) {
            System.out.println(getListaProgramas().get(i).toString());
        }
    }

    public void imprimirUsuarios() {
        for (int i = 0; i < getListaUsuarios().size(); i++) {
            System.out.println(getListaUsuarios().get(i).toString());
        }
    }

}
