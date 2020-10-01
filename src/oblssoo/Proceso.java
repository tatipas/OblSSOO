package oblssoo;

import java.time.Duration;
import static java.time.Duration.ZERO;
import java.util.LinkedList;
import java.util.Queue;

public class Proceso {

    private byte id;
    private Queue<Instruccion> colaInst;
    private byte estado;
    private Duration tiempoInst;
    private int cantI;
    private String instrucciones;

    public Proceso(String s) {
        this.estado = 0; //1-> listo // 2->enEjecucion // 3->Bloqueado;
        this.cantI = 0;
        this.tiempoInst = ZERO;
        this.instrucciones = s;
        this.colaInst = new LinkedList<>();
            encolarInstrucciones(s);
    }

    public byte getId() {
        return id;
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
        if (this.cantI == 0) {
            this.tiempoInst = i.getTiempoCPU();
        }
        colaInst.add(i);
        this.cantI ++;
    }

    public Duration getTiempoInst() {
        return tiempoInst;
    }

    public void setTiempoInst(Duration tiempoInst) {
        this.tiempoInst = tiempoInst;
    }

    public int getCantI() {
        return cantI;
    }

    public void setCantI(int cantI) {
        this.cantI = cantI;
    }

    public void despacharInstr() {
        this.colaInst.remove();
        this.cantI--;
        if (cantI > 0) {
            this.tiempoInst = this.colaInst.element().getTiempoCPU();
        } 
    }

    public void restarTiempo(long t) {
        tiempoInst.minusSeconds(1);
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
        return ("Proceso" + this.getId()) + "| Instrucciones: " + getInstrucciones();
    }

    public boolean equals(Proceso u) {
        return getId() == u.getId();
    }

    public final void encolarInstrucciones(String s) {
        for (int i = 0; i < s.length(); i++) {
            encolarInstr(Sistema.getInstByID(s.charAt(i)));
        }
    }
    

}
