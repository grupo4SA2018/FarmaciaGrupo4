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
import java.util.Iterator;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author gerson
 */
@WebService(serviceName = "TrasladoMedicamento")
public class TrasladoMedicamento {

    @WebMethod(operationName = "Traslado_Med")
    public String Traslado_Med(@WebParam(name = "Medicamentos") String meds) throws SQLException, NamingException {

        String sql = "";
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        ResultSet rs = null;
        boolean retorno = true;
        String sql1 = "";
        String signo = "+";
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/Farma");
            conn = ds.getConnection();
            stmt = conn.createStatement();

            JSONObject obj = new JSONObject(meds);
            String destino = (String) obj.get("Destino").toString();
            String origen = (String) obj.get("Origen").toString();
            JSONArray array = (JSONArray) obj.get("Medicamentos");

            if (origen.equals("4")) {
                signo = "-";
            }

            int leng = array.length();

            java.util.Date fecha = new java.util.Date();
            String fechaA = fecha.getYear() + "-" + fecha.getMonth() + "-" + fecha.getDay();
            sql = "insert into Solicitud(Fecha, Destino, Origen) values('" + fechaA + "'," + destino.trim() + "," + origen.trim() + ");";
            stmt.execute(sql);

            sql = "select MAX(idSolicitud) as id from Solicitud;";
            result = stmt.executeQuery(sql);
            String idS = "";
            if (result != null) {
                while (result.next()) {
                    idS = result.getString("id");
                }
            }
            for (int a = 0; a < leng; a++) {
                JSONObject obj1 = (JSONObject) array.get(a);
                String cant = (String) obj1.get("Cantidad");
                String cod = (String) obj1.get("Codigo");
                sql = "update Medicamento set Existencias = Existencias " + signo + cant + " where Codigo = '" + cod + "';";
                stmt.execute(sql);
                sql = "select idMedicamento as id from Medicamento where Codigo = '" + cod + "';";
                rs = stmt.executeQuery(sql);
                String idM = "";
                if (rs != null) {
                    while (rs.next()) {
                        idM = rs.getString("id");
                    }
                }
                sql1 = "insert into ListaMedicamentos(Medicamento, Solicitud, Cantidad) values(" + idM + "," + idS + "," + cant + ");";
                boolean st = stmt.execute(sql1);
            }
            /*while (keys.hasNext()) {
                String key = (String) keys.next();
                if (obj.get(key) instanceof JSONObject) {
                    JSONObject child = (JSONObject) obj.get(key);
                    String nombre = child.get("nombre").toString();
                    String c = child.get("cantidad").toString();
                    String id = child.get("id").toString();
                    sql = "update Medicamento set Existencias = Existencias -" + c.trim() + " where idMedicamento = " + id;
                    stmt.execute(sql);
                    
                }
            }*/
        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            //return "" + se;
            return "{\"error\"}";

        } finally {
            //finally block used to close resources
            if (stmt != null) {
                conn.close();
            } // do nothing
            if (conn != null) {
                conn.close();
            } //end finally try
        }
        if (retorno == true) {
            return "{\"exito\"}";
        } else {
            return "{\"error\"}";
        }

    }
}
