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

/**
 *
 * @author gerson
 */
@WebService(serviceName = "RegFarma")
public class RegFarma {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "RegistroFarmacia")
    public String RegistroFarmacia(@WebParam(name = "Nombre") String nombre
            , @WebParam(name = "Direccion") String direccion) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try { 
           InitialContext ctx = new InitialContext(); 
           DataSource ds = (DataSource)ctx.lookup("java:/Farmacia");
            System.out.println("Connecting to a selected database...");
           conn =  ds.getConnection();
            System.out.println("Connected database successfully...");
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();    
            String sql = "insert into Farmacia(nombre, direccion) "
                    + "values ('" + nombre +  "', '" + direccion + "')";
     
            stmt.execute(sql);
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
