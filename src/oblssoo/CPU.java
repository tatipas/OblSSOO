package oblssoo;

public class CPU extends Recurso {

    private Instruccion[] instrucciones;

    public CPU() {
        this.setNombre("CPU");
        this.setTipo((byte) 0);
        instrucciones = new Instruccion[3];
    }

    public void setInstrucciones(Instruccion i1, Instruccion i2, Instruccion i3) {
        instrucciones[0] = i1;
        instrucciones[1] = i2;
        instrucciones[2] = i3;

    }

    public Instruccion[] getInstrucciones() {
        return instrucciones;
    }

}
