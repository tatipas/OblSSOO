package oblssoo;

import java.util.Scanner;

public class OblSSOO {

    public static Sistema s = new Sistema(10);
    public static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        init();
        menuLoggeo();
        menuProcesos();
        System.out.println("Desea iniciar la ejecucion? || 1 para SI, 0 para NO");
        
        byte opcion = in.nextByte();
        if(opcion==1){
            iniciarEjecucion();
        }
        
    }

    public static void init() {
        Usuario u = new Usuario();
        s.agregarUsuario(u);
        Instruccion i1 = new Instruccion('A', 2);
        Instruccion i2 = new Instruccion('B', 3);
        Instruccion i3 = new Instruccion('C', 1);
        Instruccion i4 = new Instruccion('D', 4);
        s.agregarInstruccion(i1);
        s.agregarInstruccion(i2);
        s.agregarInstruccion(i3);
        s.agregarInstruccion(i4);

        Proceso p1 = new Proceso("AABBCD");
        Proceso p2 = new Proceso("BABDCC");
        Proceso p3 = new Proceso("CDA");
        Proceso p4 = new Proceso("ABCDDB");

        s.agregarProceso(p1);
        s.agregarProceso(p2);
        s.agregarProceso(p3);
        s.agregarProceso(p4);
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
        s.imprimirProcesos();
        System.out.println("Ingrese cuantos procesos correra");
        byte l = in.nextByte();
        System.out.println("Ingrese la id de los procesos");
        Proceso[] a = new Proceso[l];
        for (int i = 0; i < l; i++) {
            byte id = in.nextByte();
            Proceso p = s.getProcesoByID(id);
            a[i] = p;
            s.encolar(p);
            System.out.println(p.toString() + " esta en la posicion1"
                    + " " + (i + 1) + " en la cola de ejecucion");
        }
        System.out.println("Cola de ejecucion: ");
        for (int i = 0; i < l; i++) {
            System.out.println(a[i].toString());
        }

        System.out.println("------------------------------------------------------");
    }
    
    public static void iniciarEjecucion() throws InterruptedException{
        s.setEnEjecucion();
        while(s.getCantCola() != 0)
        {
            s.siguienteInstante();
            
        }
    }
}
