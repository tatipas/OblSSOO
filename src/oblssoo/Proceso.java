package oblssoo;

import java.time.Duration;
import static java.time.Duration.ZERO;
import java.util.LinkedList;
import java.util.Queue;

public class Proceso {

    private byte id;
    private Programa instancia;
    private Queue<Instruccion> colaInst;
    private byte estado;
    private Duration tRequeridoInst;
    private Duration tiempoInst;
    private String instrucciones;
    private int pos;
    //private Duration tiempoRequerido;

    public Proceso(Programa instancia) {
        this.estado = 0; //1-> listo // 2->enEjecucion // 3->Bloqueado;
        this.tiempoInst = ZERO;
        this.instancia = instancia;
        this.instrucciones = instancia.getInstrucciones();
        this.colaInst = new LinkedList<>();
        encolarInstrucciones(instrucciones);
        this.tRequeridoInst = Sistema.getInstByID(instrucciones.charAt(0)).getTiempoCPU();
        this.pos = 0;
        //this.tiempoRequerido = calcularTiempoRequerido();
    }

    /*public Duration getTiempoRequerido() {
        return tiempoRequerido;
    }*/
    
    public Duration calcularTiempoRequerido(){
        Duration res=ZERO;
        for (int i = 0; i < instrucciones.length(); i++) {
            res= res.plus(Sistema.getInstByID(instrucciones.charAt(i)).getTiempoCPU());
        }
        return res;
    }

    public byte getId() {
        return id;
    }

    public Duration gettRequeridoInst() {
        return tRequeridoInst;
    }

    public void settRequeridoInst(Duration tRequeridoInst) {
        this.tRequeridoInst = tRequeridoInst;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public Queue<Instruccion> getColaInst() {
        return colaInst;
    }

    public void setColaInst(Queue<Instruccion> colaInst) {
        this.colaInst = colaInst;
    }

    public byte getEstado() {
        return estado;
    }

    public void setEstadoListo() {
        this.estado = 1;
    }

    public void setEstadoEnEjecucion() {
        this.estado = 2;
    }

    public void setEstadoBloqueado() {
        this.estado = 3;
    }

    public void encolarInstr(Instruccion i) {
        if (getCantI() == 0) {
            this.tiempoInst = i.getTiempoCPU();
        }
        colaInst.add(i);

    }

    public Duration getTiempoInst() {
        return tiempoInst;
    }

    public void setTiempoInst(Duration tiempoInst) {
        this.tiempoInst = tiempoInst;
    }

    public int getCantI() {
        return colaInst.size();
    }
    
    public void despacharInstr() {
        //System.out.println("Se completó: "+ this.colaInst.element().toString());
        this.colaInst.remove();
        this.pos++;
        if (getCantI() > 0) {
            this.tiempoInst = this.colaInst.element().getTiempoCPU();
            this.tRequeridoInst = this.colaInst.element().getTiempoCPU();
        }
    }

    public void restarTiempo(long t) {
        tiempoInst = tiempoInst.minusSeconds(1);
        if (tiempoInst.isZero()) {
            despacharInstr();
        }

    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    @Override
    public String toString() {
        return ("Proceso " + this.getId()) + " | Instancia de: " + instancia.toString();
    }

    public Programa getInstancia() {
        return instancia;
    }

    public int getPos() {
        return pos;
    }

    public boolean equals(Proceso u) {
        return getId() == u.getId();
    }

    public final void encolarInstrucciones(String s) {
        for (int i = 0; i < s.length(); i++) {
            encolarInstr(Sistema.getInstByID(s.charAt(i)));
        }
    }

    public Instruccion getInstEnEjecucion(){
        return colaInst.element();
    }
}
