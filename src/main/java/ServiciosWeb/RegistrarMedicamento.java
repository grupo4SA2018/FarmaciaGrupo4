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
 * @author joe
 */
@WebService(serviceName = "RegistrarMedicamento")
public class RegistrarMedicamento {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "RegistroMedicamento")
    public String RegistroMedicamento(@WebParam(name = "Nombre") String nombre
            , @WebParam(name = "Descripcion") String descripcion,
            @WebParam(name = "Fabricante") String fabricante
            , @WebParam(name = "Precio") float precio
            , @WebParam(name = "Existencias") String existencias) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        String codigoFact="";
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
            String sql = "insert into medicamento(nombre, descripcion, fabricante, precio, existencias) "
                    + "values ('" + nombre +  "', '" + descripcion + "', '" + fabricante  + "', " + precio + ", " + existencias + ")";
     
            stmt.executeQuery(sql);
            return "Exito";
     } catch (SQLException | NamingException se) {
            //Handle errors for JDBC
            return "Error we";
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
