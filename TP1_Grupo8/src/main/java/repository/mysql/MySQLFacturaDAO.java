package repository.mysql;

import dao.FacturaDAO;
import entity.Factura;
import entity.FacturaProducto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import util.DBCreationTables;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLFacturaDAO implements FacturaDAO {

    private final Connection conn;
    private DBCreationTables  dbCreationTables;

    public MySQLFacturaDAO(Connection connection, DBCreationTables dbCreationTables) throws SQLException {
        this.conn = connection;

        this.dbCreationTables = dbCreationTables;

        dbCreationTables.crearTablasSiNoExisten();

    }

    @Override
    public void crearFactura(int idFactura, int idCliente) {
        String sql = "INSERT INTO factura (idFactura, idCliente) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFactura);
            stmt.setInt(2, idCliente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en creación de factura: "+e.getMessage());
            e.printStackTrace();
            System.exit(4);
        }
    }
    public Factura getFacturaById(int id) {
        final String sql = "SELECT * FROM factura WHERE id = " + id + ";";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Factura(
                            rs.getInt("idFactura"),
                            rs.getInt("idCliente")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en obtención de factura por ID: "+e.getMessage());
            e.printStackTrace();
            System.exit(5);
        }
        return null;
    }

    @Override
    public List<Factura> listarTodos() throws SQLException {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT idFactura, idCliente FROM factura";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idFactura = rs.getInt("idFactura");
                int idCliente = rs.getInt("idCliente");
                facturas.add(new Factura(idFactura, idCliente));
            }
        }
        return facturas;
    }

    @Override
    public List<Factura> listarPorCliente(Integer idCliente) throws SQLException {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT idCliente, idFactura FROM factura where idCliente = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idFactura = rs.getInt("idFactura");
                    facturas.add(new Factura(idCliente,idFactura));
                }
            }
            return facturas;
        }catch (Exception e) {
            System.out.println("Error en listado de factura por idCliente: "+e.getMessage());
            e.printStackTrace();
            System.exit(5);
            return null;
        }


    }

}
