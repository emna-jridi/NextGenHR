package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public final String USERNAME = "root";
    public final String PWD = "";
    public final String URL = "jdbc:mysql://localhost:3306/gestion_rh";

    private Connection con;

    public Connection getCon() {
        return con;
    }

    //3 creer une variable static pour stocker l'instance
    public static DBConnection instance;

    //1 rendre le constructeur privee
    private DBConnection() {
        try {
            con = DriverManager.getConnection(URL, USERNAME, PWD);
            System.out.println("Connected to DB");
        } catch (SQLException e) {
            System.out.println("Not connected to DB: " + e.getMessage());
        }
    }

    //2 creer une methode intermediaire
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
}