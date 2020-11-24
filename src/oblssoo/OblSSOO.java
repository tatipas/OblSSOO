package oblssoo;

import java.util.Scanner;

public class OblSSOO {

    public static Sistema s = new Sistema(5);
    public static Scanner in = new Scanner(System.in);

   // public static void main(String[] args) throws InterruptedException {
    //    System.out.println("-------------------C A F F A  O S---------------------");
      //  menuLoggeo();
        //menuOpciones();

    //}

    public static void menuOpciones() throws InterruptedException {
        System.out.println("Seleccione una opcion:");
        System.out.println("1- Ejecutar procesos");
        System.out.println("2- Ver programas cargados");
        System.out.println("3- Ver permisos");
        System.out.println("4- Cargar un programa");
        System.out.println("5- Cambiar de usuario");
        System.out.println("6- SALIR");

        byte opcion = in.nextByte();
        switch (opcion) {
            case 1:
                menuProcesos();
                s.limpiarSistema();
                menuOpciones();
                break;
            case 2:
                s.imprimirProgramas();
                menuOpciones();
                break;
            case 3:
                s.imprimirPermisos();
                menuOpciones();
                break;
            case 4:
                cargarPrograma();
                menuOpciones();
                break;
            case 5: menuLoggeo();
                    menuOpciones();
                   break;
            case 6: 
                break;
                
            default:
                System.err.println("Opcion inexistente");
                menuOpciones();
                break;
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

    public static void menuProcesos() throws InterruptedException {
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
        System.out.println("Desea iniciar la ejecucion? || 1 para SI, otro para volver al Menu");
        byte opcion = in.nextByte();
        switch (opcion) {
            case 1:
                iniciarEjecucion();
                break;
            default:
                menuOpciones();
                break;
        }
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

    private static void cargarPrograma() throws InterruptedException {
        s.imprimirRecursos();
        s.imprimirInstrucciones();
        System.out.println("Escriba las instrucciones del programa con el lenguaje dado");
        System.out.println("Si utiliza un RSR, asegurese  de pedirlo, antes de usarlo, y devolverlo despues de usarlo");
        Programa p = new Programa(in.next().toUpperCase());
        System.out.println("Desea cargar al sistema "+ p.toString()+ " || " + p.getInstrucciones() + "? 1 para SI");
                byte opcion = in.nextByte();
        switch (opcion) {
            case 1:
                s.agregarPrograma(p);
                System.out.println(p.toString()+" cargado con exito");
                break;
            default:
                menuOpciones();
                break;
        }
        
        
    }
}
