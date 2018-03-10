/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServiciosWeb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import consumir.Receta;
import consumir.Receta_Service;
import consumir.SQLException_Exception;


/**
 *
 * @author gerson
 */
@WebService(serviceName = "Despacho")
public class Despacho {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "DespachoMed")
    public String DespachoMed(@WebParam(name = "idReceta") String idReceta) throws SQLException_Exception, SQLException {
        Connection conn = null;
        Statement stmt = null;
        try { 
            Receta_Service cd  =  new Receta_Service();
            final Receta pr = cd.getRecetaPort();
            String rjson = pr.consultarReceta(idReceta);
            
            InitialContext ctx = new InitialContext(); 
            DataSource ds = (DataSource)ctx.lookup("java:/Farmacia");
            conn =  ds.getConnection();
            stmt = conn.createStatement();   
            String sql = "";
            
            String[] parts = rjson.split(";");
            int recursor = parts.length-2;
            /*String[] partes = parts[recursor].split(",");
            String n = partes[0];
            String c = partes[1];
            sql = "update Medicamento set Existencias = Existencias-"+c+" where Nombre = '"+n+"'";*/
            
            while(recursor>=0){
                String[] partes = parts[recursor].split(",");
                String n = partes[0];
                String c = partes[1];
                n = n.replace("\n", "");
                
                sql = "update Medicamento set Existencias = Existencias-"+c+" where Nombre = '"+n+"'";
                stmt.execute (sql);
                recursor--;
            }                       
            //stmt.execute(sql);
            
            return "{\"exito\"}";
     } catch (SQLException | NamingException se) {
            //Handle errors for JDBC
            return "{\"error\"}";
        }
        //Handle errors for Class.forName
         finally {
            //finally block used to close resources
            if (stmt != null) {
                conn.close();
            } // do nothing
            if (conn != null) {
                conn.close();
            } //end finally try
        }
    }
}

