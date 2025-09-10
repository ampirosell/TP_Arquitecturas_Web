package repository.mysql;

import dao.ClienteDAO;

import dao.FacturaDAO;
import dao.FacturaProductoDAO;
import dao.ProductoDAO;
import entity.Cliente;
import entity.Factura;
import entity.FacturaProducto;
import entity.Producto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import util.DBCreationTables;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLClienteDAO implements ClienteDAO {

    private final Connection conn;
    private FacturaDAO facturaDAO;
    private FacturaProductoDAO facturaProductoDAO;
    private ProductoDAO productoDAO;

    private DBCreationTables  dbCreationTables;

    public MySQLClienteDAO(Connection connection, FacturaDAO facturaDAO, FacturaProductoDAO facturaProductoDAO, ProductoDAO productoDAO, DBCreationTables dbCreationTables) throws SQLException {
        this.conn = connection;
        this.facturaDAO = facturaDAO;
        this.facturaProductoDAO = facturaProductoDAO;
        this.productoDAO = productoDAO;
        this.dbCreationTables = dbCreationTables;

        dbCreationTables.crearTablasSiNoExisten();
    }


    @Override
    public void crearCliente(int idCliente, String nombre, String email) {
        String sql = "INSERT INTO cliente (idCliente, nombre, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.setString(2, nombre);
            stmt.setString(3, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Cliente getPersonById(int id) {
        final String sql = "SELECT * FROM persons WHERE id = " + id + ";";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("idCliente"),
                            rs.getString("nombre"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT idCliente, nombre, email FROM cliente";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idCliente = rs.getInt("idCliente");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                clientes.add(new Cliente(idCliente, nombre, email));
            }
        }
        return clientes;
    }

    @Override
    public List<Cliente> listarOrdenadoPorRecaudacion()  throws SQLException {
        ArrayList<Map<String, Object>> lista = new ArrayList<>();

        List<Cliente> clientesOrdenado = new ArrayList<>();
        List<Cliente> clientes = this.listarTodos();

        for (Cliente cliente : clientes) {
            Double recaudacion = 0.0;
            List<Factura> facturasPorCliente = facturaDAO.listarPorCliente(cliente.getIdCliente());

            for (Factura factura : facturasPorCliente) {
                List<FacturaProducto> facturaProductosPorFactura = facturaProductoDAO.listarPorFactura(factura.getIdFactura());

                for (FacturaProducto facturaProducto : facturaProductosPorFactura) {
                    Producto p = productoDAO.getProductoById(facturaProducto.getIdProducto());
                    if(p!=null){
                        recaudacion += (p.getValor() * facturaProducto.getCantidad());
                    }


                }

            }
            Map<String, Object> map = new HashMap<>();
            map.put("cliente", cliente);
            map.put("recaudacion", recaudacion);
            lista.add(map);
        }

        // ordenar por recaudación descendente
        lista.sort((m1, m2) -> Double.compare(
                (Double) m2.get("recaudacion"),
                (Double) m1.get("recaudacion")
        ));

        // devolver solo clientes en orden
        List<Cliente> clientesOrdenados = new ArrayList<>();
        for (Map<String,Object> m : lista) {
            clientesOrdenados.add((Cliente) m.get("cliente"));
        }

        return clientesOrdenados;
    }


}
