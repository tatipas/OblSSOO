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

        Programa p1 = new Programa("AABBCD");
        Programa p2 = new Programa("BABDCC");
        Programa p3 = new Programa("CDA");
        Programa p4 = new Programa("ABCDDB");

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
        s.imprimirProgramas();
        System.out.println("Ingrese cuantos programas correra");
        byte l = in.nextByte();
        System.out.println("Ingrese la id de los programas");
        Proceso[] a = new Proceso[l];
        for (int i = 0; i < l; i++) {
            byte id = in.nextByte();
            Programa p = s.getProgramaByID(id);
            Proceso proc = new Proceso(p);
            a[i] = proc;
            s.encolar(proc);
            System.out.println(proc.toString() + " esta en la posicion"
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
