package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {

    private Connection conn = ConnexionDB.getConnection();

    // Sauvegarder une commande complète
    public int sauvegarderCommande(String nomClient, String telephone,
                                    String type, String table,
                                    String plats, double total,
                                    String modePaiement, String etat) {
        if (conn == null) return -1;
        try {
            String sql = "INSERT INTO commandes_app " +
                         "(nom_client, telephone, type_commande, table_assignee, " +
                         "plats, total, mode_paiement, etat) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nomClient);
            ps.setString(2, telephone);
            ps.setString(3, type);
            ps.setString(4, table);
            ps.setString(5, plats);
            ps.setDouble(6, total);
            ps.setString(7, modePaiement);
            ps.setString(8, etat);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Erreur sauvegarde commande : " + e.getMessage());
        }
        return -1;
    }

    // Charger toutes les commandes
    public List<Object[]> getToutesCommandes() {
        List<Object[]> liste = new ArrayList<>();
        if (conn == null) return liste;
        try {
            String sql = "SELECT id, nom_client, telephone, type_commande, " +
                         "table_assignee, plats, total, mode_paiement, etat " +
                         "FROM commandes_app ORDER BY id";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                liste.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nom_client"),
                    rs.getString("telephone"),
                    rs.getString("type_commande"),
                    rs.getString("table_assignee"),
                    rs.getString("plats"),
                    rs.getString("total"),
                    rs.getString("mode_paiement"),
                    rs.getString("etat")
                });
            }
        } catch (SQLException e) {
            System.out.println("Erreur chargement commandes : " + e.getMessage());
        }
        return liste;
    }

    // Modifier l'état d'une commande
    public void modifierEtat(int id, String nouvelEtat) {
        if (conn == null) return;
        try {
            String sql = "UPDATE commandes_app SET etat=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nouvelEtat);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur modification état : " + e.getMessage());
        }
    }
}