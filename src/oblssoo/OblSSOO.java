package oblssoo;

import java.util.Scanner;

public class OblSSOO {

    public static Sistema s = new Sistema(5);
    public static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        menuLoggeo();
        menuProcesos();
        System.out.println("Desea iniciar la ejecucion? || 1 para SI, 0 para NO");

        byte opcion = in.nextByte();
        if (opcion == 1) {
            iniciarEjecucion();
        }

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
        s.imprimirRecursos();
        s.imprimirInstrucciones();
        s.imprimirProgramas();
        Memoria m = s.getMemoria();
        System.out.println("Ingrese cuantos programas correra");
        byte l = in.nextByte();
        System.out.println("Ingrese la id de los programas");
        Proceso[] a = new Proceso[l];
        for (int i = 0; i < l; i++) {
            byte id = in.nextByte();
            Programa p = s.getProgramaByID(id);
            if (s.puedeCorrerProceso(p)) {
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
        for (Proceso p : s.getColaDeEjecucion()) {
            System.out.println(p.toString());
        }

        System.out.println("------------------------------------------------------");
    }

    public static byte pedirOtroId() {
        System.out.println("El usuario " + s.getEnSesion() + " no tiene los permisos para correr el programa ingresado. Seleccione otro.");
        byte id = in.nextByte();
        Programa p = s.getProgramaByID(id);
        if (s.puedeCorrerProceso(p)) {
            return id;
        } else {
            return pedirOtroId();
        }
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
}
