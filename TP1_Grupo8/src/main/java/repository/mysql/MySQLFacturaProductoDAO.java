package repository.mysql;

import dao.FacturaProductoDAO;
import entity.FacturaProducto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import util.DBCreationTables;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLFacturaProductoDAO implements FacturaProductoDAO {

    private final Connection conn;

    private DBCreationTables dbCreationTables;

    public MySQLFacturaProductoDAO(Connection connection, DBCreationTables dbCreationTables) throws SQLException {
        this.conn = connection;

        this.dbCreationTables = dbCreationTables;

        dbCreationTables.crearTablasSiNoExisten();

    }

    @Override
    public void crearFacturaProducto(Integer idProducto, Integer cantidad, Integer idFactura) {
        String sql = "INSERT INTO factura_producto(idProducto, cantidad, idFactura) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            stmt.setInt(2, cantidad);
            stmt.setInt(3, idFactura);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en creación de factura_producto: "+e.getMessage());
            e.printStackTrace();
            System.exit(4);
        }
    }


    @Override
    public List<FacturaProducto> listarTodos() throws SQLException {
        List<FacturaProducto> facturasProductos = new ArrayList<>();
        String sql = "SELECT idProducto, cantidad, idFactura FROM factura_producto";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idFactura = rs.getInt("idFactura");
                int idProducto = rs.getInt("idProducto");
                int cantidad = rs.getInt("cantidad");
                facturasProductos.add(new FacturaProducto(idProducto, cantidad,idFactura));
            }
        }
        return facturasProductos;
    }


    @Override
    public List<FacturaProducto> listarPorProducto(Integer idProducto) throws SQLException {
        List<FacturaProducto> facturasProductos = new ArrayList<>();
        String sql = "SELECT idProducto, cantidad, idFactura FROM factura_producto where idProducto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idFactura = rs.getInt("idFactura");
                    int cantidad = rs.getInt("cantidad");
                    facturasProductos.add(new FacturaProducto(idProducto, cantidad,idFactura));
                }
            }
            return facturasProductos;
        }catch (Exception e) {
            System.out.println("Error en listado de factura_producto por idProducto: "+e.getMessage());
            e.printStackTrace();
            System.exit(5);
            return null;
        }
    }


    @Override
    public List<FacturaProducto> listarPorFactura(Integer idFactura) throws SQLException {
        List<FacturaProducto> facturasProductos = new ArrayList<>();
        String sql = "SELECT idProducto, cantidad, idFactura FROM factura_producto where idFactura = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFactura);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idProducto = rs.getInt("idProducto");
                    int cantidad = rs.getInt("cantidad");
                    facturasProductos.add(new FacturaProducto(idProducto, cantidad,idFactura));
                }
            }
            return facturasProductos;
        }catch (Exception e) {
            System.out.println("Error en listado de factura_producto por idFactura: "+e.getMessage());
            e.printStackTrace();
            System.exit(5);
            return null;
        }
    }

}
