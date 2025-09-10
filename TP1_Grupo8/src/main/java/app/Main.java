package app;
import dao.ClienteDAO;

import dao.FacturaDAO;
import dao.FacturaProductoDAO;
import dao.ProductoDAO;
import factory.DAOFactory;
import factory.DBType;
import util.CSVParser;
import util.DBCreationTables;


import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Creamos la factory de MySQL
            DAOFactory factory = DAOFactory.getInstance(DBType.MYSQL);

            DBCreationTables dbCreationTables = factory.getDBCreationTables();

            CSVParser csvParser = factory.getCSVParser();

            // 2. Obtenemos el DAO

            ClienteDAO clienteDAO = factory.crearClienteDAO();
            FacturaDAO facturaDAO = factory.crearFacturaDAO();
            ProductoDAO productoDAO = factory.crearProductoDAO();
            FacturaProductoDAO facturaProductoDAO = factory.crearFacturaProductoDAO();

            //si mi tabla esta vacía, parseo los datos
            if(clienteDAO.listarTodos().size()==0){
                csvParser.parseoCsvClientes();
            }
            if(facturaDAO.listarTodos().size()==0){
                csvParser.parseoCsvFacturas();
            }
            if(productoDAO.listarTodos().size()==0){
                csvParser.parseoCsvProductos();
            }

            if(facturaProductoDAO.listarTodos().size()==0){
                csvParser.parseoCsvFacturaProductos();
            }

            // 4. Listamos todas las personas
            /*clienteDAO.listarTodos().forEach(System.out::println);

            facturaDAO.listarTodos().forEach(System.out::println);
            productoDAO.listarTodos().forEach(System.out::println);
            facturaProductoDAO.listarTodos().forEach(System.out::println);*/


            System.out.println(productoDAO.obtenerRecaudacionMaxima());

            clienteDAO.listarOrdenadoPorRecaudacion().forEach(System.out::println);

        } catch (SQLException e) {
            System.out.println("Error en main: "+e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

