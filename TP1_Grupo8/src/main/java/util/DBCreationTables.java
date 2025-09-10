package util;

import dao.FacturaDAO;
import entity.Factura;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBCreationTables {

    private final Connection conn;

    public DBCreationTables(Connection conn) {
        this.conn = conn;
    }

    public void crearTablasSiNoExisten(){
        //fuerzo creación en el orden que yo quiero.
        this.crearTablaClienteSiNoExiste();
        this.crearTablaProductoSiNoExiste();
        this.crearTablaFacturaSiNoExiste();
        this.crearTablaFacturaProductoSiNoExiste();


    }

    private void crearTablaClienteSiNoExiste() {
        final String sql = "CREATE TABLE IF NOT EXISTS cliente (" +
                "idCliente INT AUTO_INCREMENT PRIMARY KEY," +
                "nombre VARCHAR(500) NOT NULL," +
                "email VARCHAR(150) NOT NULL" +
                ");";

        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void crearTablaFacturaSiNoExiste() {

        final String sql = "CREATE TABLE IF NOT EXISTS factura (" +
                "idFactura INT AUTO_INCREMENT PRIMARY KEY," +
                "idCliente INT NOT NULL," +
                "FOREIGN KEY (idCliente) REFERENCES cliente(idCliente)" +
                ");";


        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (Exception e) {
            System.out.println("Error en creación de tabla para factura: "+e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }

    private void crearTablaProductoSiNoExiste() {
        final String sql = "CREATE TABLE IF NOT EXISTS producto (" +
                "idProducto INT AUTO_INCREMENT PRIMARY KEY," +
                "nombre VARCHAR(45) NOT NULL," +
                "valor FLOAT NOT NULL" +
                ");";

        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void crearTablaFacturaProductoSiNoExiste() {
        final String sql = "CREATE TABLE IF NOT EXISTS factura_producto (" +
                "idFactura INT NOT NULL," +
                "idProducto INT NOT NULL," +
                "cantidad INT NOT NULL," +
                "FOREIGN KEY (idFactura) REFERENCES factura(idFactura),"+
                "FOREIGN KEY (idProducto) REFERENCES producto(idProducto)"+
                ");";


        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (Exception e) {
            System.out.println("Error en creación de tabla para FacturaProducto: "+e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }

}
