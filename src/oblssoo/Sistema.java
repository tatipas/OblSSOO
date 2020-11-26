package oblssoo;

import java.util.ArrayList;
import java.util.Queue;
import java.time.*;
import static java.time.Duration.ZERO;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.LinkedList;
import ui.ejecucion;

public class Sistema {

    private static ArrayList<Proceso> listaProcesos;
    private static ArrayList<Programa> listaProgramas;
    private static ArrayList<Instruccion> listaInstrucciones;
    private static ArrayList<Recurso> listaRecursos;
    private static ArrayList<Usuario> listaUsuarios;
    private static ArrayList<Proceso> listaBloqueados;
    private static Queue<Proceso> colaDeEjecucion;
    private Proceso enEjecucion;
    private static Usuario enSesion;
    private Duration quantum;
    private Duration tiempoEjec;
    private static boolean[][] permisos;
    private CPU cpu;
    private Memoria memoria;
    private ejecucion ejec;

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
        this.permisos = new boolean[6][6];//6 para acceder directo con el id de los recursos/ usuarios
        initialize();
        this.permisos[1] = new boolean[]{false, true, true, true, true};
        this.permisos[2] = new boolean[]{false, false, true, true, true};
        this.permisos[3] = new boolean[]{false, true, false, true, true};
        this.permisos[4] = new boolean[]{false, true, true, false, true};
        this.permisos[5] = new boolean[]{false, true, true, true, false};
    }

    public boolean tienePermiso(Usuario u, Recurso r) {
        return permisos[u.getId()][r.getId()];
    }

    public void darPermiso(Usuario u, Recurso r) {
        permisos[u.getId()][r.getId()] = true;
    }

    public void quitarPermiso(Usuario u, Recurso r) {
        permisos[u.getId()][r.getId()] = false;
    }

    public Duration getQuantum() {
        return quantum;
    }

    public void setQuantum(long quantum) {
        this.quantum = Duration.of(quantum, SECONDS);
    }

    public ejecucion getEjec() {
        return ejec;
    }

    public void setEjec(ejecucion ejec) {
        this.ejec = ejec;
    }

    public byte getCantCola() {
        return (byte) colaDeEjecucion.size();
    }

    public Programa getProgramaByInstr(String insrucciones) {
        Programa p = new Programa("");
        for (int i = 0; i < listaProgramas.size(); i++) {
            if (listaProgramas.get(i).getInstrucciones().equals(insrucciones)) {
                p = listaProgramas.get(i);
            }
        }
        return p;
    }

    public void iniciarEjecucion() {
        this.setEnEjecucion();

        while (this.getCantCola() != 0) {
            this.siguienteInstante();
        }
        if (this.getListaBloqueados().size() > 0) {
            System.out.println("DEADLOCK");
            ejec.mostrar("DEADLOCK");
        }
    }

    public Proceso getEnEjecucion() {
        return enEjecucion;
    }

    public void setEnEjecucion() {
        this.enEjecucion = colaDeEjecucion.element();
        if (!enEjecucion.estaBloqueado()) {
            System.out.println(enEjecucion.toString() + " ha tomado el procesador");
            ejec.mostrar(enEjecucion.toString() + " ha tomado el procesador");
        }
    }

    public static ArrayList<Recurso> getListaRecursos() {
        return listaRecursos;
    }

    public void agregarRecurso(Recurso p) {
        this.getListaRecursos().add(p);
        p.setId(listaRecursos.size());
    }

    public Usuario getEnSesion() {
        return enSesion;
    }

    public void setEnSesion(Usuario u) {
        this.enSesion = u;
    }

    public void encolar(Proceso p) {
        this.colaDeEjecucion.add(p);
        p.setEstadoListo();

    }

    public void despacharProceso() {
        System.out.println(this.colaDeEjecucion.element().toString() + " ha finalizado su ejecucion");
        ejec.mostrar(this.colaDeEjecucion.element().toString() + " ha finalizado su ejecucion");
        getMemoria().quitarDeMemoria(enEjecucion);
        desencolar();
        getMemoria().imprimirMemoria(this);
        getMemoria().addQueuedProcesos(this);
    }

    public void desencolar() {
        this.colaDeEjecucion.remove();
        if (getCantCola() != 0) {
            setEnEjecucion();
        }
        this.tiempoEjec = ZERO;
    }

    public static boolean[][] getPermisos() {
        return permisos;
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
        if (p.getId() == 0) {
            p.setId((byte) this.listaProcesos.size());
        }
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
        p.setId(listaUsuarios.size());
    }

    public void bloquearProceso() {
        Proceso p = enEjecucion;
        agregarProcesoBloqueado(p);
        this.desencolar();
    }

    public void agregarProcesoBloqueado(Proceso p) {
        
        p.setEstadoBloqueado(this);
        listaBloqueados.add(p);
    }

    public void siguienteInstante() {
        Instruccion i = enEjecucion.getInstEnEjecucion();
        System.out.println("..........En ejecución: " + i.toString() + " en la posicion: " + enEjecucion.getPos());
        ejec.mostrar("..........En ejecución: " + i.toString() + " en la posicion: " + enEjecucion.getPos());
        if (i.esPedir()) {
            RSR rec = (RSR) i.getRecurso();
            rec.encolar(enEjecucion, this);

            if (!rec.getAtendido().equals(enEjecucion)) {
                enEjecucion.despacharInstr();
                bloquearProceso();
                return;
            }
        } else {
            if (i.esDevolver()) {
                RSR rec = (RSR) i.getRecurso();
                System.out.println(enEjecucion.toString() + " devolvio el " + rec.getNombre());
                ejec.mostrar(enEjecucion.toString() + " devolvio el " + rec.getNombre());
                rec.desencolar(this);
                if (!rec.colaVacia()) {
                    desbloquear(rec.getAtendido());
                }
            }
        }
        //Thread.sleep(1000);
        agregarTiempoEjec(1);
        enEjecucion.restarTiempo(1); // se resta al tiempo de la instruccion, y si llega a 0, se pasa a la siguiente instruccion
        if (enEjecucion.getCantI() == 0) {
            despacharProceso();
        } else {
            if (tiempoEjec.equals(quantum)) {
                timeout();
                tiempoEjec = ZERO;
                System.out.println("El " + getEnEjecucion().toString() + " tomo el procesador por Timeout");
                ejec.mostrar("El " + getEnEjecucion().toString() + " tomo el procesador por Timeout");
            }
        }
    }

    /*
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
     */
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

    public static ArrayList<Proceso> getListaBloqueados() {
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
            Programa p = getListaProgramas().get(i);
            System.out.println(p.toString() + "| Intrucciones: " + p.getInstrucciones());
            ejec.mostrar(p.toString() + "| Intrucciones: " + p.getInstrucciones());
        }
    }

    public void imprimirUsuarios() {
        for (int i = 0; i < getListaUsuarios().size(); i++) {
            System.out.println(getListaUsuarios().get(i).toString());
            ejec.mostrar(getListaUsuarios().get(i).toString());
        }
    }

    public void desbloquear(Proceso p) {
        encolar(p);
        listaBloqueados.remove(p);
    }

    public Memoria getMemoria() {
        return this.memoria;
    }

    public void initialize() {
        crearUsuarios(); //Crea y agrega 5 usuarios
        crearRecursos(); //Crea y agrega 5 recursos y la CPU con sus instrucciones

        Programa p1 = new Programa("ADCFJKLM");
        Programa p2 = new Programa("BABDEFAAM");
        Programa p3 = new Programa("DBAGEHFIBA");
        Programa p4 = new Programa("GADHFIBCDDB");
                Programa p5 = new Programa("DAJEFKGHLAI");
        Programa p6 = new Programa("DEGHFIJAKL");
        Programa p7 = new Programa("JGKHIL");

        agregarPrograma(p1);
        agregarPrograma(p2);
        agregarPrograma(p3);
        agregarPrograma(p4);
                agregarPrograma(p5);
        agregarPrograma(p6);
        agregarPrograma(p7);
    }

    public void crearUsuarios() {
        Usuario u1 = new Usuario();
        Usuario u2 = new Usuario();
        Usuario u3 = new Usuario();
        Usuario u4 = new Usuario();
        Usuario u5 = new Usuario();

        agregarUsuario(u1);
        agregarUsuario(u2);
        agregarUsuario(u3);
        agregarUsuario(u4);
        agregarUsuario(u5);
    }

    public void crearRecursos() {
        crearCPU();
        crearMemoria(30);
        crearRSR("Impresora 1");
        crearRSR("Impresora 2");
        crearRSR("Variable Global");
        crearRCompartido("Red Wifi");

    }

    public void crearCPU() {
        CPU cpu = new CPU();
        setCPU(cpu);
        Instruccion i1 = new Instruccion((char) (65 + getListaInstrucciones().size()), 2, cpu, "Calculo Numerico");
        agregarInstruccion(i1);
        Instruccion i2 = new Instruccion((char) (65 + getListaInstrucciones().size()), 3, cpu, "Calculo Numerico");
        agregarInstruccion(i2);
        Instruccion i3 = new Instruccion((char) (65 + getListaInstrucciones().size()), 4, cpu, "Calculo Numerico");
        agregarInstruccion(i3);
        cpu.setInstrucciones(i1, i2, i3);

    }

    public void crearRSR(String nombre) {
        RSR r = new RSR(nombre);
        agregarRecurso(r);
        Instruccion i1 = new Instruccion((char) (65 + getListaInstrucciones().size()), 1, r, "Pedir " + r.getNombre());
        agregarInstruccion(i1);
        Instruccion i2 = new Instruccion((char) (65 + getListaInstrucciones().size()), 3, r, "Usar " + r.getNombre());
        agregarInstruccion(i2);
        Instruccion i3 = new Instruccion((char) (65 + getListaInstrucciones().size()), 1, r, "Devolver " + r.getNombre());
        agregarInstruccion(i3);

        r.setInstrucciones(i1, i2, i3);
    }

    public void crearRCompartido(String nombre) {
        RCompartido r = new RCompartido(nombre);
        agregarRecurso(r);
        Instruccion i = new Instruccion((char) (65 + getListaInstrucciones().size()), 2, r, "Usar " + r.getNombre());
        agregarInstruccion(i);
        r.setInstruccion(i);
    }

    public static void imprimirRecursos() {
        System.out.println("Recursos:");
        for (Recurso recurso : getListaRecursos()) {
            System.out.println("........." + recurso.toString());
        }
    }

    public static void imprimirInstrucciones() {
        System.out.println("Instrucciones:");
        for (Instruccion i : getListaInstrucciones()) {
            System.out.println("........." + i.toString());
        }
    }

    public void setMemoria(Memoria memoria) {
        this.memoria = memoria;
    }

    private void crearMemoria(int i) {
        Memoria m = new Memoria(i);
        setMemoria(m);
    }

    public boolean puedeCorrerProceso(Programa p) {
        Instruccion a;
        for (int i = 0; i < p.getInstrucciones().length(); i++) {
            a = getInstByID(p.getInstrucciones().charAt(i));
            if (!a.getRecurso().esCPU() && !a.getRecurso().esMemoria()) {
                if (!tienePermiso(getEnSesion(), a.getRecurso())) {
                    return false;
                }
            }
        }
        return true;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCPU(CPU cpu) {
        this.cpu = cpu;
    }

    public static void limpiarSistema() {
        getListaBloqueados().clear();
        getListaProcesos().clear();
        colaDeEjecucion.clear();
    }

    public static void imprimirPermisos() {
        System.out.println("------------------------------------------------------");

        if (enSesion.getId() == 1) {
            for (Usuario u : listaUsuarios) {
                System.out.println(u.toString());
                for (Recurso r : listaRecursos) {
                    System.out.println(r.toString() + ": " + permisos[u.getId()][r.getId()]);
                }
            }
        } else {
            System.out.println(enSesion.toString());
            for (Recurso r : listaRecursos) {
                System.out.println(r.toString() + ": " + permisos[enSesion.getId()][r.getId()]);
            }
        }
        System.out.println("------------------------------------------------------");

    }

}
