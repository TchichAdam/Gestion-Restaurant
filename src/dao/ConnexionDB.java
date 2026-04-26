package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionDB {

    private static final String URL      = "jdbc:mysql://localhost:3306/restaurant_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion MySQL reussie !");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver MySQL introuvable : " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Erreur de connexion : " + e.getMessage());
            }
        }
        return connection;
    }

    public static void fermerConnexion() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.out.println("Erreur fermeture : " + e.getMessage());
            }
        }
    }
}