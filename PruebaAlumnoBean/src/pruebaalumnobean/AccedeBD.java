/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebaalumnobean;
import Alumno.AlumnoBean;
import Alumno.AlumnoBean.BDModificadaEvent;
import Alumno.AlumnoBean.BDModificadaListener;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author usuario
 */
public class AccedeBD implements BDModificadaListener{

    AlumnoBean alumnos;

    AccedeBD()
    {
         alumnos = new AlumnoBean();
         alumnos.addBDModificadaListener( (BDModificadaListener)this );
    }

    public void listado()
    {
        for(int i=0; i<4; i++)
        {
            alumnos.seleccionarFila(i);
            System.out.println("Alumno " + i + "\n\tDNI:" + alumnos.getDNI());
            System.out.println("\tNombre: " + alumnos.getNombre());
            System.out.println("\tApellidos: " + alumnos.getApellidos());
            System.out.println("\tDireccion: " + alumnos.getDireccion());
            System.out.println("\tFecha de nacimiento: " + alumnos.getFechaNac());
        }
    }

    void anade()
    {
        alumnos.setDNI("98765432B");
        alumnos.setNombre("Asunción M");
        alumnos.setApellidos("de las Heras López");
        alumnos.setDireccion("C/Felix Rodríguez de la Fuente, nº27");
        alumnos.setFechaNac(Date.valueOf("1987-06-05"));
        try {
            alumnos.addAlumno();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AccedeBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    /**
     * Método para sobreescribir y lanzar un mensaje por consola al 
     * producirse el evento.
     * @param ev 
     */

    public void capturarBDModificada(BDModificadaEvent ev)
    {     
            System.out.println("\033[33mSe ha añadido un elemento a la base de datos");
    }
}
