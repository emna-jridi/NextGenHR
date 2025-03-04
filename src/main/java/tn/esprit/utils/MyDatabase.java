package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private static MyDatabase instance;
    private final String URL ="jdbc:mysql://127.0.0.1:3306/gestion ressources humaines";
    private final String USERNAME ="root";
    private final String PASSWORD = "";
    private Connection  cnx ;

    private MyDatabase() {
        try {
            cnx = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("connected ...");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static MyDatabase getInstance() {
        if (instance == null || instance.getCnx() == null) {
            instance = new MyDatabase();
        } else {
            try {
                if (instance.getCnx().isClosed()) {
                    instance = new MyDatabase(); // 🔄 Reconnexion automatique
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la vérification de la connexion : " + e.getMessage());
            }
        }
        return instance;
    }


    public Connection getCnx() {
        return cnx;
    }
}
