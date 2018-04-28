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
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.json.*;

/**
 *
 * @author gerson
 */
@WebService(serviceName = "ConsultarExistencias")
public class ConsultarExistencias {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "Existencias")
    public String Existencias(@WebParam(name = "Codigo") String Codigo) throws SQLException {
        
        String sql = "";
        Connection conn = null;
        boolean result=true;
        ResultSet rs = null;
        Statement stmt = null;
        String ret = "";
        try {
           InitialContext ctx = new InitialContext();
           DataSource ds = (DataSource)ctx.lookup("java:/Farmacia");
           conn =  ds.getConnection();
            stmt = conn.createStatement();
            JSONObject obj = new JSONObject(Codigo);
            String codigo = (String) obj.get("Codigo");
            
            
            sql = "select Existencias from Medicamento where Codigo ='"+codigo+"'";
            String cant = "";
            rs = stmt.executeQuery(sql);
            if(rs != null){
                while(rs.next()){
                    cant = rs.getString("Existencias");                    
                }
            }
            result=true;
            ret = "{\n\"Cantidad\":\""+cant+"\"\n}";
            
            if(cant.isEmpty() == true){
                result = false;
            }else{
                return ret;
            }
            
        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            result = false;
        }
        finally {
            //finally block used to close resources
            if (stmt != null) {
                conn.close();
            } // do nothing
            if (conn != null) {
                conn.close();
            } //end finally try
        };
        
        
        if(result)
            return "{\"Exito\":\"1\"}";
        return "{\"Cantidad\":\"0\"}";
        
    }
}
