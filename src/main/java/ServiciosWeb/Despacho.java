/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServiciosWeb;

import java.sql.Connection;
import java.sql.ResultSet;
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
@WebService(serviceName = "Despacho")
public class Despacho {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "DespachoMed")
    public String DespachoMed(@WebParam(name = "Meds") String meds) throws  SQLException {
        boolean result = false;
        String sql = "";
        String sql1 = "";
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;
        String json="";
        try { 
            
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:/Farmacia");
            conn =  ds.getConnection();
            stmt = conn.createStatement();
           
            sql = "select r.idReceta as id from Receta r inner join Cita c"
                    + " on c.idCita = r.Cita where c.Realizada = 0 and c.Paciente ="+meds+";";
            
            rs = stmt.executeQuery(sql);
            result=true;    
            if(rs != null){
                while(rs.next()){
                    json = rs.getString("id");
                    return rs.getString("id");
                }
            }else{
                return "{\"error\"}";
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

