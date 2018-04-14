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
import java.util.ArrayList;
import java.util.Iterator;
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
@WebService(serviceName = "Despacho")
public class Despacho {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "DespachoMed")
    public String DespachoMed(@WebParam(name = "Meds") String meds) throws SQLException {
        String sql = "";
        Connection conn = null;
        ResultSet rs = null;
        ResultSet resul1 = null;
        ResultSet resul = null;
        Statement stmt = null;
        Statement st = null;
        try {

            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/Farmacia");
            conn = ds.getConnection();
            stmt = conn.createStatement();
            st =  conn.createStatement();
            String jsonR = "";
            JSONObject obj = new JSONObject(meds);
            String nombre = (String) obj.get("nombre");
            java.util.Date fecha =  new java.util.Date();
            String f = fecha.getYear() + "-"+fecha.getMonth()+"-"+fecha.getDay();
            sql = "insert into Factura(Nombre,Fecha,NIT) values('"+nombre+"','"+f+"','C/F');";
            stmt.execute(sql); //Ingreso la factura en la BD
            sql = "select MAX(idFactura) as id from Factura;";//Obtener el ID de la Factura Actual
            resul = st.executeQuery(sql);
            String idF = "";
            if(resul != null){
                while(resul.next()){
                    idF = resul.getString("id");
                }
                
            }            
            Iterator<?> keys = obj.keys();
            int z=0;
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (obj.get(key) instanceof JSONObject) {
                    JSONObject child = (JSONObject) obj.get(key);
                    String n = child.get("nombre").toString();
                    String c = child.get("cantidad").toString();
                    sql = "select idMedicamento, Precio from Medicamento where Nombre='"+n.trim()+"';";
                    resul1 = st.executeQuery(sql);//Seleccionar id medicamento, Precio
                    String id = "";
                    int prec = 0;
                    if(resul1 != null){
                        while(resul1.next()){
                            id = resul1.getString("idMedicamento");
                            prec = Integer.parseInt(resul1.getString("Precio"));
                        }                        
                    }
                    System.out.println("ERROR");
                    int holi = Integer.parseInt(c.trim())*prec;
                    String hola = holi+"";
                    System.out.println(hola);
                    sql = "insert into DETALLE(Cantidad, SubTotal, Medicamento, Factura)"
                            + " values("+c+","+Integer.parseInt(c.trim())*prec+","+id+","+idF+");";
                    stmt.execute(sql);
                    sql = "update Medicamento set Existencias = Existencias -"+c+" where idMedicamento = "+id;
                    stmt.execute(sql);
                    if(z==0){
                        jsonR+="\"med_"+id+"\":{\n";
                        z=1;
                    }else{
                        jsonR+=",\"med_"+id+"\":{\n";
                    }
                    
                    jsonR += "\"subtotal\":\""+holi+"\",\n";
                    jsonR += "\"medicamento\":\""+n.trim()+"\",\n";
                    jsonR += "\"cantidad\":\""+c+"\",\n";
                    jsonR += "\"precio\":\""+prec+"\"\n";
                    jsonR += "}\n";
                }
            }
            sql = "select SUM(SubTotal) as total from DETALLE where Factura = "+idF;
            resul1 = stmt.executeQuery(sql);
            String retorno = "{\n";
            if(resul1!= null){
                while(resul1.next()){
                    retorno += "\"total\":\""+resul1.getString("total") +"\",";                    
                }
            }
            retorno+= jsonR +"}";           
            return retorno;
        } catch (SQLException | NamingException se) {
            //Handle errors for JDBC
            //return se.getMessage();
            return "{\"error\"}";
        } //Handle errors for Class.forName
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
