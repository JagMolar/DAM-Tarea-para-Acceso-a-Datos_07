/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaalumnobean;


import java.util.logging.Level;
import java.util.logging.Logger;
import matricula.MatriculaBean;
import matricula.MatriculaBean.BDMatModificadaListener;

/**
 *
 * @author Juan A. García Muelas <juangmuelas@gmail.com>
 */
public class AccedeBDMat implements BDMatModificadaListener{
    
    MatriculaBean matriculas;
    
    AccedeBDMat(){
        matriculas = new MatriculaBean();
        matriculas.addBDModificadaListener((BDMatModificadaListener)this );
    }
    
    /**
     * Método para listar todas las matrículas de nuestra BD.
     * Recorremos mediante un for que hemos ajustado al tamaño
     * de nuestro array.
     */
    
    public void listadoM()
    {
        System.out.println("\033[36m***** LISTADO GENERAL DE MATRÍCULAS *****");
        for(int i=0; i< matriculas.getSize(); i++)
        {
            matriculas.seleccionarFila(i);
            System.out.println("Alumno con DNI:" + matriculas.getDNI() + " matriculado en:");
            System.out.println("\tNombre Modulo: " + matriculas.getNombreModulo());
            System.out.println("\tCurso: " + matriculas.getCurso());
            System.out.println("\tNota: " + matriculas.getNota());
        } 
    }
    
    /**
     * Método para listar por un dni que le indicaremos desde 
     * la llamada en el main.
     * Recorremos mediante un for que hemos ajustado al tamaño
     * de nuestro array.
     * @param DNI
     * @throws ClassNotFoundException 
     */
    public void listadoDNI(String DNI) throws ClassNotFoundException
    {
        matriculas.recargarDNI(DNI);
        System.out.println("\033[36m***** LISTADO DE MATRÍCULAS PARA EL DNI "+ matriculas.getDNI()+" *****");
        for(int i= 0; i < matriculas.getSize(); i++){
            matriculas.seleccionarFila(i);
            System.out.println("\tNombre Modulo: " + matriculas.getNombreModulo());
            System.out.println("\tCurso: " + matriculas.getCurso());
            System.out.println("\tNota: " + matriculas.getNota());
        }    
    }
    
    /**
     * Método para añadir una nueva matrícula.
     */
    void anadeM()
    {
        matriculas.setDNI("98765432B");
        matriculas.setNombreModulo("Programación");
        matriculas.setCurso("21-22");
        matriculas.setNota(7.5);

        try {
            matriculas.addMatricula();
            System.out.println("\033[36m***** LISTADO GENERAL DE MATRÍCULAS *****");
            for(int i=0; i< matriculas.getSize(); i++)
            {
                matriculas.seleccionarFila(i);
                System.out.println("Alumno con DNI:" + matriculas.getDNI() + " matriculado en:");
                System.out.println("\tNombre Modulo: " + matriculas.getNombreModulo());
                System.out.println("\tCurso: " + matriculas.getCurso());
                System.out.println("\tNota: " + matriculas.getNota());
            } 
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AccedeBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void capturarBDModificada(MatriculaBean.BDMatModificadaEvent ev)
    {
        if(ev.modoDni){
            System.out.println("\033[33mSe ha pedido el listado con  DNI a la base de datos");
        }else{
            System.out.println("\033[33mSe ha añadido una nueva matrícula a la base de datos");
        }
    }
    
}//fin clase AccedeBDMat
