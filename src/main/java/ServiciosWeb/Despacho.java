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
import java.util.logging.Level;
import java.util.logging.Logger;
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
            st = conn.createStatement();
            String jsonR = "";
            JSONObject obj = new JSONObject(meds);
            
            String nombre = (String) obj.get("nombre");
            String receta = (String) obj.get("receta");
            
            java.util.Date fecha = new java.util.Date();
            String f = fecha.getYear() + "-" + fecha.getMonth() + "-" + fecha.getDay();
            sql = "insert into Factura(Nombre,Fecha,NIT, Receta) values('" + nombre + "','" + f + "','C/F',"+receta+");";
            stmt.execute(sql); //Ingreso la factura en la BD
            sql = "select MAX(idFactura) as id from Factura;";//Obtener el ID de la Factura Actual
            resul = st.executeQuery(sql);
            String idF = "";
            if (resul != null) {
                while (resul.next()) {
                    idF = resul.getString("id");
                }
            }
            Iterator<?> keys = obj.keys();
            int z = 0;
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (obj.get(key) instanceof JSONObject) {
                    JSONObject child = (JSONObject) obj.get(key);
                    String n = child.get("codigo").toString();
                    String c = child.get("cantidad").toString();
                    sql = "select idMedicamento, Precio from Medicamento where Codigo='" + n.trim() + "';";
                    resul1 = st.executeQuery(sql);//Seleccionar id medicamento, Precio
                    String id = "";//declaracion de ID
                    int prec = 0;//declaracion de variable para precio
                    if (resul1 != null) {
                        while (resul1.next()) {
                            id = resul1.getString("idMedicamento");
                            prec = Integer.parseInt(resul1.getString("Precio"));
                        }
                    }
                    System.out.println("ERROR");
                    int holi = Integer.parseInt(c.trim()) * prec;
                    String hola = holi + "";
                    System.out.println(hola);
                    sql = "insert into DETALLE(Cantidad, SubTotal, Medicamento, Factura)"
                            + " values(" + c + "," + Integer.parseInt(c.trim()) * prec + "," + id + "," + idF + ");";
                    stmt.execute(sql);
                    sql = "update Medicamento set Existencias = Existencias -" + c + " where Codigo = " + n;
                    stmt.execute(sql);
                    if (z == 0) {
                        jsonR += "\"med_" + id + "\":{\n";
                        z = 1;
                    } else {
                        jsonR += ",\"med_" + id + "\":{\n";
                    }

                    jsonR += "\"subtotal\":\"" + holi + "\",\n";
                    jsonR += "\"medicamento\":\"" + n.trim() + "\",\n";
                    jsonR += "\"cantidad\":\"" + c + "\",\n";
                    jsonR += "\"precio\":\"" + prec + "\"\n";
                    jsonR += "}\n";
                }
            }
            sql = "select SUM(SubTotal) as total from DETALLE where Factura = " + idF;
            resul1 = stmt.executeQuery(sql);
            String retorno = "{\n";
            if (resul1 != null) {
                while (resul1.next()) {
                    retorno += "\"total\":\"" + resul1.getString("total") + "\",";
                }
            }
            retorno += jsonR + "}";
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

    @WebMethod(operationName = "ReporteDespacho")
    public String ReporteDespacho(@WebParam(name = "Receta") String receta) throws SQLException {
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
            st = conn.createStatement();
            String jsonR = "";
            JSONObject obj = new JSONObject(receta);
            String id_receta = (String) obj.get("Receta");
            jsonR = "{\"Despachos\":[\n";

            if (id_receta.equals("-1")) {
                sql = "select * from Factura";
                rs = st.executeQuery(sql);
                if (rs == null) {
                    jsonR += "]}";
                    return rs.getRow() + "";
                }
                int cnt = 0;

                while (rs.next()) {
                    //return "dafsadf";
                    id_receta = resul1.getString("Receta");
                    String Fecha = resul1.getString("Fecha");
                    String DPI = resul1.getString("NIT");
                    
                    sql = "select d.cantidad cantidad, m.Nombre nombre, m.idMedicamento id from Factura f";
                    sql += " inner join DETALLE d on f.idFactura = d.Factura inner join Medicamento m";
                    sql += " on m.idMedicamento = d.Medicamento where f.Receta = " + id_receta;

                    resul = stmt.executeQuery(sql);

                    if (resul == null) {
                        jsonR += "]}";
                        return jsonR;
                    }

                    if (cnt > 0) {
                        jsonR += ",";
                    };
                    cnt++;

                    jsonR += "{\"Receta\":\"" + id_receta + "\",";
                    jsonR += "\"Fecha\":\"" + Fecha + "\",";
                    jsonR += "\"DPI\":\"" + DPI + "\",";

                    String medicamentos = "\"Medicamentos\":[";

                    int cont = 0;
                    while (resul.next()) {
                        //return "dfaf";    
                        String codigo = resul.getString("id");
                        String nombre = resul.getString("nombre");
                        String cantidad = resul.getString("cantidad");

                        String json = "{";
                        json += "\"Codigo\":\"" + codigo + "\",";
                        json += "\"Nombre\":\"" + nombre + "\",";
                        json += "\"Cantidad\":\"" + cantidad + "\"";
                        json += "}";

                        if (cont > 0) {
                            medicamentos += ",";
                        }

                        medicamentos += json;
                        cont++;
                    }

                    medicamentos += "]";

                    jsonR += medicamentos;

                    jsonR += "}";

                }
            } else {
                sql = "select NIT, Fecha from Factura where Receta = " + id_receta;
                String Fecha = "";
                String DPI = "";
                ResultSet rs1 = stmt.executeQuery(sql);
                String var = "";
                var = "funciona";
                if (rs1 != null) {
                    while (rs1.next()) {
                        Fecha = rs1.getString("Fecha");
                        DPI = rs1.getString("NIT");
                    }
                } else {
                    return "{ \"Despacho\":[] }";
                }

                sql = "select d.cantidad cantidad, m.Nombre nombre, m.idMedicamento id from Factura f";
                sql += " inner join DETALLE d on f.idFactura = d.Factura inner join Medicamento m";
                sql += " on m.idMedicamento = d.Medicamento where f.Receta = " + id_receta;

                resul1 = stmt.executeQuery(sql);

                if (resul1 == null) {
                    jsonR += "]}";
                    return jsonR;
                }

                jsonR += "{\"Receta\":\"" + id_receta + "\",";
                jsonR += "\"Fecha\":\"" + Fecha + "\",";
                jsonR += "\"DPI\":\"" + DPI + "\",";

                String medicamentos = "\"Medicamentos\":[";

                int cont = 0;
                while (resul1.next()) {

                    String codigo = resul1.getString("id");
                    String nombre = resul1.getString("nombre");
                    String cantidad = resul1.getString("cantidad");

                    String json = "{";
                    json += "\"Codigo\":\"" + codigo + "\",";
                    json += "\"Nombre\":\"" + nombre + "\",";
                    json += "\"Cantidad\":\"" + cantidad + "\"";
                    json += "}";

                    if (cont > 0) {
                        medicamentos += ",";
                    }

                    medicamentos += json;
                    cont++;
                }

                medicamentos += "]";

                jsonR += medicamentos;

                jsonR += "}";

            }

            jsonR += "]}";

            return jsonR;

        } catch (NamingException ex) {
            Logger.getLogger(Despacho.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
