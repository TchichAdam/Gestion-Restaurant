package dao;

import model.Facture;
import java.sql.*;

public class FactureDAO {

    private Connection conn = ConnexionDB.getConnection();

    public int creerFacture(Facture facture) {
        try {
            String sql = "INSERT INTO factures (montant_total, commande_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, facture.getMontantTotal());
            ps.setInt(2, facture.getCommande().getId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("✅ Facture #" + id + " créée. Montant : " + facture.getMontantTotal() + " MAD");
                return id;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur création facture : " + e.getMessage());
        }
        return -1;
    }

    public double getMontantParCommande(int commandeId) {
        try {
            String sql = "SELECT montant_total FROM factures WHERE commande_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, commandeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("montant_total");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lecture facture : " + e.getMessage());
        }
        return 0;
    }
}