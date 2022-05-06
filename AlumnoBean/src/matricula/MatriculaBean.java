/*
 * TAREA AD07.
 * Crea un componente nuevo en el proyecto Alumno que para gestionar 
 * toda esta información.
 * Además del código necesario para gestionar las propiedades del 
 * componente y mantener la información de la base de datos en un 
 * vector interno, es preciso que incluyas los siguientes métodos:
 * seleccionarFila(i): recupera en las propiedades del componente 
 * el registro número i del vector.
 * RecargarDNI(): recarga la estructura interna del componente con 
 * las matrículas de un DNI en particular.
 * AddMatricula(): añade un registro nuevo a la base de datos con 
 * la información almacenada en las propiedades del componente.
 * Dado que el componente puede funcionar en dos modos diferentes 
 * (todos los alumnos o un alumno concreto) se generará un evento 
 * cada vez que se cambie de modo
 */
package matricula;

import java.beans.*;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventListener;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juang <juangmuelas@gmail.com>
 * @since 14/04/2022
 * @version 1
 */
public class MatriculaBean implements Serializable {
    
    /*****************************************************
     * Propiedades de conexión para los métodos.
     * Evitaremos así posibles errores, tanto en las 
     * llamadas a la BBDD como ante posibles cambios.
     * Si tu usuario o password son diferentes, recuerda modificarlos
     * y volver a compilar.
     * 
     */
    static final String url = "jdbc:mysql://localhost:3306/alumnos";
    static final String user="root";
    static final String pass="";

    /*****************************************************
     * Propiedades del Bean.
     * Crearemos una propiedad por cada campo de la tabla de
     * la base de datos del siguiente modo:
     *
     * DNI: String
     * NombreModulo: String
     * Curso: String
     * Nota: double
     */
    
        protected String DNI;

    /**
     * Get the value of DNI
     *
     * @return the value of DNI
     */
    public String getDNI() {
        return DNI;
    }

    /**
     * Set the value of DNI
     *
     * @param DNI new value of DNI
     */
    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    protected String NombreModulo;

    /**
     * Get the value of NombreModulo
     *
     * @return the value of NombreModulo
     */
    public String getNombreModulo() {
        return NombreModulo;
    }

    /**
     * Set the value of NombreModulo
     *
     * @param NombreModulo new value of NombreModulo
     */
    public void setNombreModulo(String NombreModulo) {
        this.NombreModulo = NombreModulo;
    }

    protected String Curso;

    /**
     * Get the value of Curso
     *
     * @return the value of Curso
     */
    public String getCurso() {
        return Curso;
    }

    /**
     * Set the value of Curso
     *
     * @param Curso new value of Curso
     */
    public void setCurso(String Curso) {
        this.Curso = Curso;
    }

    protected double Nota;

    /**
     * Get the value of Nota
     *
     * @return the value of Nota
     */
    public double getNota() {
        return Nota;
    }

    /**
     * Set the value of Nota
     *
     * @param Nota new value of Nota
     */
    public void setNota(double Nota) {
        this.Nota = Nota;
    }
    
    /**
     * Nuestro componente ya nos ha creado este acceso a la 
     * clase PropertyChangeSupport que manejará posibles
     * cambios en nuestras propiedades.
     * 
     */
    private PropertyChangeSupport propertySupport;
    
    /**
     * Constuctor que manteniendo la escucha de eventos,
     * llama al método recargar filas.
     * 
     */
    public MatriculaBean() {
        propertySupport = new PropertyChangeSupport(this);
        
        try {
            recargarFilas();
        } catch (ClassNotFoundException ex) {
            this.DNI = "";
            this.NombreModulo = "";
            this.Curso = "";
            this.Nota = 0.0;
            Logger.getLogger(MatriculaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }// fin constructor
    
    /*******************************************************
     * Tras varios errores en la salida del ejemplo,
     * decido generar una nueva propiedad con la que poder 
     * manejar el tamaño de nuestro array de datos
     * cuando se maneje desde otro proyecto.
     *
     */
    
    protected int size;

    /**
     * Get the value of size
     *
     * @return the value of size
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the value of size
     *
     * @param size new value of size
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * @param modoDni boolean discrimina según petición
     */
    boolean modoDni;

    public boolean isModoDni() {
        return modoDni;
    }

    public void setModoDni(boolean modoDni) {
        this.modoDni = modoDni;
    }


    /*****************************************************
     * Clase auxiliar que usaremos para crear un vector privado
     * de matrículas de alumnos.
     */
    
    private class Matricula{
        /**
         * Recogemos las propiedades para nuestra clase auxiliar.
         * 
         * @param DNI String
         * @param NombreModulo String
         * @param Curso String 
         * @param Nota double
         */
        String DNI;
        String NombreModulo;
        String Curso;
        double Nota;

        //constructor vacío
        public Matricula() {
        }

        //Constructor con argumentos
        public Matricula(String DNI, String NombreModulo, String Curso, double Nota) {
            this.DNI = DNI;
            this.NombreModulo = NombreModulo;
            this.Curso = Curso;
            this.Nota = Nota;
        }
  
    }//fin clase Matricula
    
    /******************************************************
     * Usaremos un vector auxiliar para cargar la información de la
     * tabla de forma que tengamos acceso a los datos sin necesidad
     * de estar conectados constantemente
     */
    private Vector Matriculas = new Vector();

    /*******************************************************
     * Actualiza el contenido de la tabla en el vector de alumnos
     * Las propiedades contienen el valor del primer elementos de la tabla
     */
    private void recargarFilas() throws ClassNotFoundException{
        /**
         * Aseguro la estricta recogida de datos en el vector.
         */
        if(!Matriculas.isEmpty())
        {
            Matriculas.removeAllElements();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);
            java.sql.Statement s = con.createStatement();
            ResultSet rs = s.executeQuery ("select * from matriculas");
            while (rs.next())
            {
                Matricula a = new Matricula(rs.getString("DNI"),
                                      rs.getString("NombreModulo"),
                                      rs.getString("Curso"),
                                      rs.getDouble("Nota"));
                                      

                Matriculas.add(a);
            }
            Matricula mat = new Matricula();
            mat = (Matricula) Matriculas.elementAt(1);
            this.DNI = mat.DNI;
            this.NombreModulo = mat.NombreModulo;
            this.Curso = mat.Curso;
            this.Nota = mat.Nota;
            /**
             * Tras recoger los datos recibidos recojo el 
             * tamaño final y cierro conexiones.
             */
            size = Matriculas.size();
            rs.close();
            con.close();
        } catch (SQLException ex) {
            this.DNI = "";
            this.NombreModulo = "";
            this.Curso = "";
            this.Nota = 0;
            Logger.getLogger(MatriculaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//fin recargarFilas()
    
    /********************************************************
     *
     * @param i numero de la fila a cargar en las propiedades del componente
     */
    public void seleccionarFila(int i)
    {
        if(i<=Matriculas.size())
        {
            Matricula mat = new Matricula();
            mat = (Matricula) Matriculas.elementAt(i);
            this.DNI = mat.DNI;
            this.NombreModulo = mat.NombreModulo;
            this.Curso = mat.Curso;
            this.Nota = mat.Nota;
        }else{
            this.DNI = "";
            this.NombreModulo = "";
            this.Curso = "";
            this.Nota = 0.0;
        }
    }//Fin seleccionarFila(i)
    
    /********************************************************
     *
     * @param DNI DNI A buscar, se carga en las propiedades del componente
     */
    public void recargarDNI(String DNI) throws ClassNotFoundException
    {
        /**
         * Aseguro la estricta recogida de datos en el vector.
         */
        if(!Matriculas.isEmpty())
        {
            Matriculas.removeAllElements();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);
            PreparedStatement s = con.prepareStatement("select * from matriculas where DNI = ?");
            s.setString(1, DNI);
            ResultSet rs = s.executeQuery ();
            while (rs.next())
            {
                Matricula a = new Matricula(rs.getString("DNI"),
                                    rs.getString("NombreModulo"),
                                    rs.getString("Curso"),
                                    rs.getDouble("Nota"));
                                      
                Matriculas.add(a);
            }
            Matricula mat = new Matricula();
            mat = (Matricula) Matriculas.elementAt(1);
            this.DNI = mat.DNI;
            this.NombreModulo = mat.NombreModulo;
            this.Curso = mat.Curso;
            this.Nota = mat.Nota;
            modoDni = true;
            receptor.capturarBDModificada(new BDMatModificadaEvent(this, modoDni));
            /**
             * Tras recoger los datos recibidos recojo el 
             * tamaño final y cierro conexiones.
             */
            size = Matriculas.size();
            rs.close();
            con.close();
        } catch (SQLException ex) {
            this.DNI = "";
            this.NombreModulo = "";
            this.Curso = "";
            this.Nota = 0;
            Logger.getLogger(MatriculaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }// Fin recargarDNI()
    
    /*********************************************************************
     * Código para añadir una nueva matrícula la base de datos.
     * cada vez que se modifica el estado de la BD se genera un evento para
     * que se recargue el componente.
     */

    private BDMatModificadaListener receptor;

    public class BDMatModificadaEvent extends java.util.EventObject
    {
        // constructor
        public boolean modoDni;
        public BDMatModificadaEvent(Object source, boolean modoDNI)
        {
            super(source);
            modoDni = modoDNI;
        }
    }
    

    //Define la interfaz para el nuevo tipo de evento
    public interface BDMatModificadaListener extends EventListener
    {
        public void capturarBDModificada(BDMatModificadaEvent ev);
    }

    public void addBDModificadaListener(BDMatModificadaListener receptor)
    {
        this.receptor = receptor;
    }
    public void removeBDModificadaListener(BDMatModificadaListener receptor)
    {
        this.receptor=null;
    }
    
    
    /*******************************************************
     * Método que añade un alumno a la base de datos
     * añade un registro a la base de datos formado a partir
     * de los valores de las propiedades del componente.
     *
     * Se presupone que se han usado los métodos set para configurar
     * adecuadamente las propiedades con los datos del nuevo registro.
     */
    public void addMatricula() throws ClassNotFoundException
    {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection con = DriverManager.getConnection(url, user, pass);
            PreparedStatement s = con.prepareStatement("insert into matriculas values (?,?,?,?)");

            s.setString(1, DNI);
            s.setString(2, NombreModulo);
            s.setString(3, Curso);
            s.setDouble(4, Nota);

            s.executeUpdate ();
            recargarFilas();
            modoDni =false;
            receptor.capturarBDModificada( new BDMatModificadaEvent(this, modoDni));
        }
        catch(SQLException ex)
        {
            Logger.getLogger(MatriculaBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }// fin addMatricula()
       
    
    /*******************************************************
     *
     * @param listener
     */
   
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
   
}//Fin clase MatriculaBean
