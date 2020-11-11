package oblssoo;

public class Recurso {

    private String nombre;
    private int id;
    private byte tipo;

    public boolean isCPU() {
        return tipo == 0;
    }

    public boolean isRSR() {
        return tipo == 1;
    }

    public boolean isOneTime() {
        return tipo == 2;
    }

    public boolean isRComp() {
        return tipo == 3;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getTipo() {
        return tipo;
    }

    public void setTipo(byte tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return  "Recurso " + id + ": "+nombre;
    }

}
