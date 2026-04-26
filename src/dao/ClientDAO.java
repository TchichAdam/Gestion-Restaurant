package dao;

import model.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    // ⚠️ Ne pas stocker conn comme attribut fixe
    // À la place, on l'obtient à chaque méthode

    public boolean ajouter(Client client) {
        Connection conn = ConnexionDB.getConnection(); // ← ici
        if (conn == null) return false;               // ← vérification
        try {
            String sql1 = "INSERT INTO personnes (nom, telephone, type) VALUES (?, ?, 'client')";
            PreparedStatement ps1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, client.getNom());
            ps1.setString(2, client.getTelephone());
            ps1.executeUpdate();

            ResultSet rs = ps1.getGeneratedKeys();
            if (rs.next()) {
                int idGenere = rs.getInt(1);
                String sql2 = "INSERT INTO clients (id, adresse) VALUES (?, ?)";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                ps2.setInt(1, idGenere);
                ps2.setString(2, client.getAdresse());
                ps2.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erreur ajout client : " + e.getMessage());
        }
        return false;
    }

    public List<Client> getTousLesClients() {
        Connection conn = ConnexionDB.getConnection(); // ← ici
        List<Client> liste = new ArrayList<>();
        if (conn == null) return liste;               // ← vérification
        try {
            String sql = "SELECT p.id, p.nom, p.telephone, c.adresse " +
                         "FROM personnes p JOIN clients c ON p.id = c.id";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                liste.add(new Client(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("telephone"),
                    rs.getString("adresse")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lecture clients : " + e.getMessage());
        }
        return liste;
    }

    public Client getClientParId(int id) {
        Connection conn = ConnexionDB.getConnection();
        if (conn == null) return null;
        try {
            String sql = "SELECT p.id, p.nom, p.telephone, c.adresse " +
                         "FROM personnes p JOIN clients c ON p.id = c.id WHERE p.id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Client(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("telephone"),
                    rs.getString("adresse")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur recherche client : " + e.getMessage());
        }
        return null;
    }

    public boolean modifier(Client client) {
        Connection conn = ConnexionDB.getConnection();
        if (conn == null) return false;
        try {
            String sql1 = "UPDATE personnes SET nom=?, telephone=? WHERE id=?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, client.getNom());
            ps1.setString(2, client.getTelephone());
            ps1.setInt(3, client.getId());
            ps1.executeUpdate();

            String sql2 = "UPDATE clients SET adresse=? WHERE id=?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setString(1, client.getAdresse());
            ps2.setInt(2, client.getId());
            ps2.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur modification client : " + e.getMessage());
        }
        return false;
    }

    public boolean supprimer(int id) {
        Connection conn = ConnexionDB.getConnection();
        if (conn == null) return false;
        try {
            String sql = "DELETE FROM personnes WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur suppression client : " + e.getMessage());
        }
        return false;
    }

    public List<Client> rechercherParNom(String nom) {
        Connection conn = ConnexionDB.getConnection();
        List<Client> liste = new ArrayList<>();
        if (conn == null) return liste;
        try {
            String sql = "SELECT p.id, p.nom, p.telephone, c.adresse " +
                         "FROM personnes p JOIN clients c ON p.id = c.id " +
                         "WHERE p.nom LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + nom + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                liste.add(new Client(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("telephone"),
                    rs.getString("adresse")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur recherche : " + e.getMessage());
        }
        return liste;
    }
}