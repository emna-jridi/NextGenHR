package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private static MyDatabase instance;
    private final String URL = "jdbc:mysql://127.0.0.1:3306/Gestion_ressources_humaines";
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private Connection cnx;

    private MyDatabase() {
        try {
            // Charger explicitement le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected...");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}
