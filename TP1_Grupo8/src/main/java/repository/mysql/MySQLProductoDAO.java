package repository.mysql;

import dao.FacturaProductoDAO;
import dao.ProductoDAO;
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

public class MySQLProductoDAO implements ProductoDAO {

    private final Connection conn;

    private FacturaProductoDAO facturaProductoDAO;
    private DBCreationTables dbCreationTables;

    public MySQLProductoDAO(Connection connection, FacturaProductoDAO facturaProductoDAO, DBCreationTables dbCreationTables) throws SQLException {
        this.conn = connection;
        this.facturaProductoDAO = facturaProductoDAO;

        this.dbCreationTables = dbCreationTables;

        dbCreationTables.crearTablasSiNoExisten();
    }


    @Override
    public Map<String, Object> obtenerRecaudacionMaxima() throws SQLException {

        Map<String,Object> map = new HashMap<>();

        String nombre="";
        Double valorMaximo=0.0;
        Double valorParcial;

        //obtengo todos los productos
        List<Producto> productos = this.listarTodos();
        for(Producto producto: productos){
            valorParcial=this.obtenerRecaudacion(producto);
            if(valorParcial>valorMaximo){
                valorMaximo=valorParcial;
                nombre=producto.getNombre();
            }

        }

        map.put("Nombre producto", nombre);
        map.put("Recaudación", valorMaximo);

        return map;
    }

    private Double obtenerRecaudacion(Producto producto) throws SQLException {
        Double valor = 0.0;
        //por cada uno recorro y obtengo sus facturas de productos
        List<FacturaProducto> facturaProductos = facturaProductoDAO.listarPorProducto(producto.getIdProducto());

        for(FacturaProducto facturaProducto: facturaProductos) {
            //por cada una me guardo la suma de valor
            valor += (facturaProducto.getCantidad() * producto.getValor());

        }
        return valor;
    }

    @Override
    public void crearProducto(int idProducto, String nombre, Double valor) {
        String sql = "INSERT INTO producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            stmt.setString(2, nombre);
            stmt.setDouble(3, valor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Producto getProductoById(Integer id) {
        if(id<=0){
            return null;
        }
        final String sql = "SELECT * FROM producto WHERE idProducto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("idProducto"),
                            rs.getString("nombre"),
                            rs.getDouble("valor")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en obtener producto por id: " + e.getMessage());
            e.printStackTrace();
            System.exit(5);

        }
        return null;
    }

    @Override
    public List<Producto> listarTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT idProducto, nombre, valor FROM producto";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idProducto = rs.getInt("idProducto");
                String nombre = rs.getString("nombre");
                Double valor = rs.getDouble("valor");
                productos.add(new Producto(idProducto, nombre, valor));
            }
        }
        return productos;
    }


}
