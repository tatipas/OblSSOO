package oblssoo;

import java.time.*;
import static java.time.temporal.ChronoUnit.SECONDS;

public class Instruccion {

    private char id;
    private Duration tiempoCPU;

    public Instruccion(char id, long tiempoCPU) {
        this.id = id;
        this.tiempoCPU = Duration.of(tiempoCPU, SECONDS);
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

    public boolean equals(Instruccion u){
        return getId() == u.getId();
    }
    
    @Override
    public String toString(){
        return "Intruccion "+getId()+" | Duracion: " + getTiempoCPU().toString();
    }
}
