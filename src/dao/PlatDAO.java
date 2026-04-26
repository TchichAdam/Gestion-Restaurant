package dao;

import model.Plat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatDAO {

    private Connection conn = ConnexionDB.getConnection();

    public boolean ajouter(Plat plat) {
        try {
            String sql = "INSERT INTO plats (nom, categorie, prix, disponible) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, plat.getNom());
            ps.setString(2, plat.getCategorie());
            ps.setDouble(3, plat.getPrix());
            ps.setBoolean(4, plat.isDisponible());
            ps.executeUpdate();
            System.out.println("✅ Plat ajouté.");
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout plat : " + e.getMessage());
        }
        return false;
    }

    public List<Plat> getTousLesPlats() {
        List<Plat> liste = new ArrayList<>();
        try {
            String sql = "SELECT * FROM plats";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Plat p = new Plat(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("categorie"),
                    rs.getDouble("prix")
                );
                p.setDisponible(rs.getBoolean("disponible"));
                liste.add(p);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lecture plats : " + e.getMessage());
        }
        return liste;
    }

    public List<Plat> getPlatsDisponibles() {
        List<Plat> liste = new ArrayList<>();
        try {
            String sql = "SELECT * FROM plats WHERE disponible = TRUE";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Plat p = new Plat(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("categorie"),
                    rs.getDouble("prix")
                );
                liste.add(p);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        }
        return liste;
    }

    public boolean modifier(Plat plat) {
        try {
            String sql = "UPDATE plats SET nom=?, categorie=?, prix=?, disponible=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, plat.getNom());
            ps.setString(2, plat.getCategorie());
            ps.setDouble(3, plat.getPrix());
            ps.setBoolean(4, plat.isDisponible());
            ps.setInt(5, plat.getId());
            ps.executeUpdate();
            System.out.println("✅ Plat modifié.");
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur modification plat : " + e.getMessage());
        }
        return false;
    }

    public boolean supprimer(int id) {
        try {
            String sql = "DELETE FROM plats WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("✅ Plat supprimé.");
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression plat : " + e.getMessage());
        }
        return false;
    }
}