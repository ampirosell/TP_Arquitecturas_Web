package util;

import dao.ClienteDAO;
import dao.FacturaDAO;
import dao.FacturaProductoDAO;
import dao.ProductoDAO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CSVParser {

    private FacturaDAO facturaDAO;

    private ClienteDAO clienteDAO;

    private ProductoDAO productoDAO;

    private FacturaProductoDAO facturaProductoDAO;


    public CSVParser(ClienteDAO clienteDAO,ProductoDAO productoDAO, FacturaDAO facturaDAO, FacturaProductoDAO facturaProductoDAO) {
        this.clienteDAO = clienteDAO;
        this.productoDAO = productoDAO;
        this.facturaDAO = facturaDAO;
        this.facturaProductoDAO = facturaProductoDAO;
    }

    public void parseoCsvFacturas() {
        // Abrimos y procesamos el CSV
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("csvFiles/facturas.csv")) {
            // Verificamos que el archivo exista
            if (inputStream == null) {
                throw new FileNotFoundException("No se encontró facturas.csv en resources");
            }
            // Creamos reader y parser dentro de try-with-resources para cerrarlos automáticamente y que no gasten recursos innecesarios
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                 org.apache.commons.csv.CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
                // Iteramos por cada fila del CSV

                for(CSVRecord row: parser) {
                    facturaDAO.crearFactura(Integer.parseInt(row.get("idFactura")), Integer.parseInt(row.get("idCliente")));
                }
            }
        } catch (Exception e) {
            System.out.println("Error en parseo de CSV para facturas: " + e.getMessage());
            e.printStackTrace();
            System.exit(4);
        }
    }

    public void parseoCsvClientes() {
        // Abrimos y procesamos el CSV
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("csvFiles/clientes.csv")) {
            // Verificamos que el archivo exista
            if (inputStream == null) {
                throw new FileNotFoundException("No se encontró clientes.csv en resources");
            }
            // Creamos reader y parser dentro de try-with-resources para cerrarlos automáticamente y que no gasten recursos innecesarios
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                 org.apache.commons.csv.CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

                // Iteramos por cada fila del CSV
                for (CSVRecord row : parser) {
                    // Creamos el cliente usando los datos de cada fila
                    clienteDAO.crearCliente(
                            Integer.parseInt(row.get("idCliente")),
                            row.get("nombre"),
                            row.get("email")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error en parseo de CSV para clientes: " + e.getMessage());
            e.printStackTrace();
            System.exit(3);
        }
    }

    public void parseoCsvProductos() {
        // Abrimos y procesamos el CSV
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("csvFiles/productos.csv")) {
            // Verificamos que el archivo exista
            if (inputStream == null) {
                throw new FileNotFoundException("No se encontró productos.csv en resources");
            }
            // Creamos reader y parser dentro de try-with-resources para cerrarlos automáticamente y que no gasten recursos innecesarios
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                 org.apache.commons.csv.CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
                // Iteramos por cada fila del CSV
                for(CSVRecord row: parser) {
                    productoDAO.crearProducto(Integer.parseInt(row.get("idProducto")), row.get("nombre"), Double.valueOf(row.get("valor")));
                }
            }
        } catch (Exception e) {
            System.out.println("Error en parseo de CSV para productos: " + e.getMessage());
            e.printStackTrace();
            System.exit(5);
        }
    }

    public void parseoCsvFacturaProductos() {
        // Abrimos y procesamos el CSV
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("csvFiles/facturas-productos.csv")) {
            // Verificamos que el archivo exista
            if (inputStream == null) {
                throw new FileNotFoundException("No se encontró facturas-productos.csv en resources");
            }
            // Creamos reader y parser dentro de try-with-resources para cerrarlos automáticamente y que no gasten recursos innecesarios
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                 org.apache.commons.csv.CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
                // Iteramos por cada fila del CSV
                for(CSVRecord row: parser) {
                    facturaProductoDAO.crearFacturaProducto(Integer.parseInt(row.get("idProducto")), Integer.parseInt(row.get("cantidad")),Integer.parseInt(row.get("idFactura")));
                }
            }
        } catch (Exception e) {
            System.out.println("Error en parseo de CSV para facturas: " + e.getMessage());
            e.printStackTrace();
            System.exit(4);
        }
    }

}
