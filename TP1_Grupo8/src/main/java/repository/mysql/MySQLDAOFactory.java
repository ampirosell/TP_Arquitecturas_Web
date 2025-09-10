package repository.mysql;

import dao.ClienteDAO;
import dao.FacturaDAO;
import dao.FacturaProductoDAO;
import dao.ProductoDAO;
import factory.ConexionMySQL;
import factory.DAOFactory;
import util.CSVParser;
import util.DBCreationTables;

import java.sql.SQLException;

public class MySQLDAOFactory extends DAOFactory {

    public DBCreationTables getDBCreationTables() {
        return new DBCreationTables(ConexionMySQL.getInstance().getConnection());
    }
    @Override
    public FacturaDAO crearFacturaDAO() throws SQLException {
        return new MySQLFacturaDAO(ConexionMySQL.getInstance().getConnection(), this.getDBCreationTables());
    }
    @Override
    public ProductoDAO crearProductoDAO() throws SQLException {
        return new MySQLProductoDAO(ConexionMySQL.getInstance().getConnection(), this.crearFacturaProductoDAO(),this.getDBCreationTables());
    }
    @Override
    public FacturaProductoDAO crearFacturaProductoDAO() throws SQLException {
        return new MySQLFacturaProductoDAO(ConexionMySQL.getInstance().getConnection(),this.getDBCreationTables());
    }

    @Override
    public ClienteDAO crearClienteDAO() throws SQLException {
        return new MySQLClienteDAO(ConexionMySQL.getInstance().getConnection(), this.crearFacturaDAO(), this.crearFacturaProductoDAO(), this.crearProductoDAO(),this.getDBCreationTables());
    }

    @Override
    public CSVParser getCSVParser() throws SQLException {
        return new CSVParser(this.crearClienteDAO(),this.crearProductoDAO(), this.crearFacturaDAO(), this.crearFacturaProductoDAO());
    }
}
