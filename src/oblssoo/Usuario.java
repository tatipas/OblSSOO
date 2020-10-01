package oblssoo;

public class Usuario {
    private int id;

    public Usuario() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public boolean equals(Usuario u){
        return getId() == u.getId();
    }

    @Override
    public String toString() {
        return "Usuario " + id;
    }
    
}
