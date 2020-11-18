package oblssoo;

import java.time.*;
import static java.time.temporal.ChronoUnit.SECONDS;

public class Instruccion {

    private char id;
    private Duration tiempoCPU;
    private Recurso recurso;
    private String descr;

    public Instruccion(char id, long tiempo, Recurso r, String s) {
        this.id = id;
        this.tiempoCPU = Duration.of(tiempo, SECONDS);
        this.recurso = r;
        this.descr = s;
    }

    public void setRecurso(Recurso r) {
        this.recurso = r;
    }

    public Recurso getRecurso() {
        return this.recurso;
    }

    public char getId() {
        return id;
    }

    public void setId(char id) {
        this.id = id;
    }

    public Duration getTiempoCPU() {
        return tiempoCPU;
    }

    public void setTiempoCPU(long tiempoCPU) {
        this.tiempoCPU = Duration.of(tiempoCPU, SECONDS);
    }

    public boolean equals(Instruccion u) {
        return getId() == u.getId();
    }

    public String getDescripcion() {
        return descr;
    }

    public boolean esPedir() {
        if (!recurso.esRSR()) {
            return false;
        }
        RSR rec = (RSR) this.recurso;
        return this.equals(rec.getPedir());
    }

    public boolean esDevolver() {
        if (!recurso.esRSR()) {
            return false;
        }
        RSR rec = (RSR) this.recurso;
        return this.equals(rec.getDevolver());
    }

    @Override
    public String toString() {
        return "Intruccion " + getId() + ":" + getDescripcion();
    }
}
