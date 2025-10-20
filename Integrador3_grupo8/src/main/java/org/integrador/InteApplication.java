package org.integrador;

import org.integrador.helper.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InteApplication implements CommandLineRunner {

    @Autowired
    private CSVReader csvReader;

    public static void main(String[] args) {
        SpringApplication.run(InteApplication.class, args);
    }

    @Override
    public void run(String[] args) {
        try {
            csvReader.populateDB();
        } catch (Exception e) {
            System.err.println("❌ Error populando la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
