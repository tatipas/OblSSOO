package oblssoo;

import java.util.Scanner;

public class OblSSOO {

    public static Sistema s = new Sistema(5);
    public static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        init();
        menuLoggeo();
        menuProcesos();
        System.out.println("Desea iniciar la ejecucion? || 1 para SI, 0 para NO");

        byte opcion = in.nextByte();
        if (opcion == 1) {
            iniciarEjecucion();
        }

    }

    public static void init() {
        crearUsuarios(); //Crea y agrega 5 usuarios
        crearRecursos(); //Crea y agrega 5 recursos y la CPU con sus instrucciones

        Programa p1 = new Programa("ADCFJKLM");
        Programa p2 = new Programa("BABDEFAAM");
        Programa p3 = new Programa("DBAGEHFIBA");
        Programa p4 = new Programa("GADHFIBCDDB");

        s.agregarPrograma(p1);
        s.agregarPrograma(p2);
        s.agregarPrograma(p3);
        s.agregarPrograma(p4);
    }

    public static void menuLoggeo() {
        System.out.println("------------------------------------------------------");
        s.imprimirUsuarios();
        System.out.println("Pon el numero de usuario para iniciar sesion");
        byte opcion = in.nextByte();
        s.setEnSesion(s.getUsuarioByID(opcion));
        System.out.println(s.getEnSesion().toString() + " inicio sesion");
        System.out.println("------------------------------------------------------");
    }

    public static void menuProcesos() {
        imprimirRecursos();
        imprimirInstrucciones();
        s.imprimirProgramas();
        Memoria m = s.getMemoria();
        System.out.println("Ingrese cuantos programas correra");
        byte l = in.nextByte();
        System.out.println("Ingrese la id de los programas");
        Proceso[] a = new Proceso[l];
        for (int i = 0; i < l; i++) {
            byte id = in.nextByte();
            Programa p = s.getProgramaByID(id);
            if (puedeCorrerProceso(s.getEnSesion(), p)) {
                Proceso proc = new Proceso(p);
                a[i] = proc;
                if (m.addOrQueueProceso(proc, s)) {
                    System.out.println(proc.toString() + " se cargo en memoria y se agrego a la cola de ejecucion");
                    m.imprimirMemoria();
                } else {
                    System.out.println("No hay espacio suficiente para " + proc.toString() + " || Se cargara cuando se libere memoria");
                }

            } else {
                id = pedirOtroId();
                Proceso proc = new Proceso(p);
                a[i] = proc;
                if (m.addOrQueueProceso(proc, s)) {
                    System.out.println(proc.toString() + " se cargo en memoria y se agrego a la cola de ejecucion");
                                        m.imprimirMemoria();

                } else {
                    System.out.println("No hay espacio suficiente para " + proc.toString() + " || Se cargara cuando se libere memoria");
                }
            }

        }
        System.out.println("Cola de ejecucion: ");
        for (Proceso p: s.getColaDeEjecucion()) {
            System.out.println(p.toString());
        }

        System.out.println("------------------------------------------------------");
    }

    public static byte pedirOtroId() {
        System.out.println("El usuario " + s.getEnSesion() + " no tiene los permisos para correr el programa ingresado. Seleccione otro.");
        byte id = in.nextByte();
        Programa p = s.getProgramaByID(id);
        if (puedeCorrerProceso(s.getEnSesion(), p)) {
            return id;
        } else {
            return pedirOtroId();
        }
    }

    public static boolean puedeCorrerProceso(Usuario u, Programa p) {
        Instruccion a;
        for (int i = 0; i < p.getInstrucciones().length(); i++) {
            a = s.getInstByID(p.getInstrucciones().charAt(i));
            if (!s.tienePermiso(u, a.getRecurso())) {
                return false;
            }
        }
        return true;
    }

    public static void iniciarEjecucion() throws InterruptedException {
        s.setEnEjecucion();

        while (s.getCantCola() != 0) {
            s.siguienteInstante();
        }
        if (s.getListaBloqueados().size() > 0) {
            System.out.println("DEADLOCK");
        }
    }

    public static void crearUsuarios() {
        Usuario u1 = new Usuario();
        Usuario u2 = new Usuario();
        Usuario u3 = new Usuario();
        Usuario u4 = new Usuario();
        Usuario u5 = new Usuario();

        s.agregarUsuario(u1);
        s.agregarUsuario(u2);
        s.agregarUsuario(u3);
        s.agregarUsuario(u4);
        s.agregarUsuario(u5);
    }

    public static void crearRecursos() {
        crearCPU();
        crearMemoria(30);
        crearRSR("Impresora 1");
        crearRSR("Impresora 2");
        crearRSR("Variable Global");
        crearRCompartido("Red Wifi");

    }

    public static void crearCPU() {
        CPU cpu = new CPU();
        s.agregarRecurso(cpu);
        Instruccion i1 = new Instruccion((char) (65 + s.getListaInstrucciones().size()), 2, cpu, "Calculo Numerico");
        s.agregarInstruccion(i1);
        Instruccion i2 = new Instruccion((char) (65 + s.getListaInstrucciones().size()), 3, cpu, "Calculo Numerico");
        s.agregarInstruccion(i2);
        Instruccion i3 = new Instruccion((char) (65 + s.getListaInstrucciones().size()), 4, cpu, "Calculo Numerico");
        s.agregarInstruccion(i3);
        cpu.setInstrucciones(i1, i2, i3);

    }

    public static void crearRSR(String nombre) {
        RSR r = new RSR(nombre);
        s.agregarRecurso(r);
        Instruccion i1 = new Instruccion((char) (65 + s.getListaInstrucciones().size()), 1, r, "Pedir " + r.getNombre());
        s.agregarInstruccion(i1);
        Instruccion i2 = new Instruccion((char) (65 + s.getListaInstrucciones().size()), 3, r, "Usar " + r.getNombre());
        s.agregarInstruccion(i2);
        Instruccion i3 = new Instruccion((char) (65 + s.getListaInstrucciones().size()), 1, r, "Devolver " + r.getNombre());
        s.agregarInstruccion(i3);

        r.setInstrucciones(i1, i2, i3);
    }

    public static void crearRCompartido(String nombre) {
        RCompartido r = new RCompartido(nombre);
        s.agregarRecurso(r);
        Instruccion i = new Instruccion((char) (65 + s.getListaInstrucciones().size()), 2, r, "Usar " + r.getNombre());
        s.agregarInstruccion(i);
        r.setInstruccion(i);
    }

    public static void imprimirRecursos() {
        System.out.println("Recursos:");
        for (Recurso recurso : s.getListaRecursos()) {
            System.out.println("........." + recurso.toString());
        }
    }

    public static void imprimirInstrucciones() {
        System.out.println("Instrucciones:");
        for (Instruccion i : s.getListaInstrucciones()) {
            System.out.println("........." + i.toString());
        }
    }

    private static void crearMemoria(int i) {
        Memoria m = new Memoria(i);
        s.agregarRecurso(m);
    }
}
