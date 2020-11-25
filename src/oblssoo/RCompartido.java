package oblssoo;


public class RCompartido extends Recurso {

    private Instruccion inst;

    public RCompartido(String nombre) {
        this.setNombre(nombre);
        this.setTipo((byte) 4);
    }
    
    public void setInstruccion(Instruccion i){
        inst = i;
    }

}
