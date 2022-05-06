/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebaalumnobean;

/**
 *
 * @author usuario
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException {
        // TODO code application logic here
        AccedeBD gestion = new AccedeBD();
//        gestion.listado();
//        gestion.anade();

        AccedeBDMat gestionMat = new AccedeBDMat();
//        gestionMat.listadoM();
//        gestionMat.listadoDNI("12345678A");
        gestionMat.anadeM();
    }

}
