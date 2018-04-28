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
 * @author joe
 */
@WebService(serviceName = "RegistrarMedicamento")
public class RegistrarMedicamento {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "Carga")
    public String Carga(@WebParam(name = "Nombre") String nombre) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/Farma");
            System.out.println("Connecting to a selected database...");
            conn = ds.getConnection();
            System.out.println("Connected database successfully...");
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();
            LocalDate localDate = LocalDate.now();
            String line[] = nombre.split("\n");
            String sql = "";
            sql="insert into Medicamento(nombre, descripcion, fabricante,"
                            + " precio, existencias,bajo_prescripcion, codigo) values";
            for (int a = 0; a < line.length; a++) {
                
                    String[] array2 = line[a].split(",");
                    //RegistrarMedicamento_Service rgs = new RegistrarMedicamento_Service();
                    //RegistrarMedicamento rm = rgs.getRegistrarMedicamentoPort();
                    //Nombre0,Descripcion1,Fabricante2,Precio3,Existencias4,Bajo_Prescripcion5,Codigo6
                    
                    if (array2.length == 7) {

                        //rm.registroMedicamento(array2[1], array2[2], array2[5], Float.parseFloat(array2[6]), (array2[3]), Integer.parseInt(array2[4]), array2[0]);
                        
                        sql += "('" + array2[1] + "', '" + array2[2] + "', '" + array2[5]
                            + "', " + Float.parseFloat(array2[6]) + ", " + array2[3] + "," + Integer.parseInt(array2[4]) + ",'" + array2[0] + "'),";
                        
                    } else {
                        //rm.registroMedicamento(array2[1], array2[2], array2[6], Float.parseFloat(array2[7]), (array2[3].replace("\"", "") + array2[4].replace("\"", "")), Integer.parseInt(array2[5]), array2[0]);
                        
                        sql += "('" + array2[1] + "', '" + array2[2] + "', '" + array2[6]
                            + "', " + Float.parseFloat(array2[7]) + ", " + (array2[3].replace("\"", "") + array2[4].replace("\"", "")) + "," + Integer.parseInt(array2[5]) + ",'" + array2[0] + "'),";
                        
                    }
                    
                    //verificar que el tamaño de array2 sea el adecuado

                    //insertar result, que sería el nombre de cada medicamento
                    // algo asi como insertarmedicamento(array2[0], array2[1], array2[2]);
                
            }
            sql = sql.substring(0,sql.length()-1);
            stmt.execute(sql);
            System.out.println(sql);
            return "{\"exito\"}";
        } catch (SQLException | NamingException se) {
            //Handle errors for JDBC
            System.out.println(se.getMessage());
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

    @WebMethod(operationName = "RegistroMedicamento")
    public String RegistroMedicamento(@WebParam(name = "Nombre") String nombre,
            @WebParam(name = "Descripcion") String descripcion,
            @WebParam(name = "Fabricante") String fabricante,
            @WebParam(name = "Precio") float precio,
            @WebParam(name = "Existencias") String existencias,
            @WebParam(name = "Bajo_Prescripcion") int bajo,
            @WebParam(name = "Codigo") String codigo) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/Farmacia");
            System.out.println("Connecting to a selected database...");
            conn = ds.getConnection();
            System.out.println("Connected database successfully...");
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            String sql = "insert into Medicamento(nombre, descripcion, fabricante,"
                    + " precio, existencias,bajo_prescripcion, codigo) "
                    + "values ('" + nombre + "', '" + descripcion + "', '" + fabricante
                    + "', " + precio + ", " + existencias + "," + bajo + ",'" + codigo + "')";

            stmt.execute(sql);
            return "{\"exito\"}";
        } catch (SQLException | NamingException se) {
            //Handle errors for JDBC
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

    @WebMethod(operationName = "consultar_Medicamento")
    public String consultar_Medicamento(@WebParam(name = "idMed") String idMed) throws SQLException {

        String sql = "";
        Connection conn = null;
        Statement stmt = null;
        ResultSet result;

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/Farmacia");
            conn = ds.getConnection();
            stmt = conn.createStatement();

            sql = "select nombre, descripcion, fabricante, precio, existencias, bajo_prescripcion "
                    + "from Medicamento Where idMedicamento = " + idMed;
            result = stmt.executeQuery(sql);
            if (result != null) {
                while (result.next()) {
                    String nombre = result.getString("nombre");
                    String fecha_nac = result.getString("descripcion");
                    String Genero = result.getString("fabricante");
                    String direccion = result.getString("precio");
                    String telefono = result.getString("existencias");
                    String estado = result.getString("bajo_prescripcion");
                    return "{\n"
                            + "\"nombre\": \"" + nombre + "\",\n"
                            + "\"descripcion\": \"" + fecha_nac + "\",\n"
                            + "\"fabricante\": \"" + Genero + "\",\n"
                            + "\"precio\": \"" + direccion + "\",\n"
                            + "\"existencias\": \"" + telefono + "\",\n"
                            + "\"bajo_prescripcion\": \"" + estado + "\",\n"
                            + "}";
                }
            } else {
                return "{\"error\"}";
            }

            System.out.println(sql);

        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            return "" + se;
        } finally {
            //finally block used to close resources
            if (stmt != null) {
                conn.close();
            } // do nothing
            if (conn != null) {
                conn.close();
            } //end finally try
        };
        return "{\"error\"}";
    }

    @WebMethod(operationName = "Medicamentos")
    public String Medicamentos() throws SQLException {

        String sql = "";
        Connection conn = null;
        Statement stmt = null;
        ResultSet result;

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/Farmacia");
            conn = ds.getConnection();
            stmt = conn.createStatement();

            sql = "select Nombre,Existencias, idMedicamento from Medicamento";
            String json = "{\n";
            int inicio = 0;
            result = stmt.executeQuery(sql);
            if (result != null) {
                while (result.next()) {
                    String nombre = result.getString("Nombre");
                    String fecha_nac = result.getString("idMedicamento");
                    String cantidad = result.getString("Existencias");

                    if (inicio == 0) {
                        json += "\"med_" + fecha_nac + "\":{\n";
                        inicio = 1;
                    } else {
                        json += ",\"med_" + fecha_nac + "\":{\n";
                    }
                    json += "\"nombre\":\"" + nombre + "\",";
                    json += "\"cantidad\":\"" + cantidad + "\",";
                    json += "\"id\":\"" + fecha_nac + "\"\n}\n";

                }
                json += "}";
                return json;
            } else {
                return "{\"error\"}";
            }
        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            return "" + se;
        } finally {
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
