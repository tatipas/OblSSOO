package ui;
import oblssoo.*;
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Sistema sistema = new Sistema(5);
        logIn ventLogin = new logIn(sistema);
        ventLogin.setVisible(true);
        

    }
    
}
