package dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeDAO {

    public boolean ajouter(Employe employe) {
        Connection conn = ConnexionDB.getConnection();
        if (conn == null) return false;
        try {
            String sql1 = "INSERT INTO personnes (nom, telephone, type) VALUES (?, ?, 'employe')";
            PreparedStatement ps1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, employe.getNom());
            ps1.setString(2, employe.getTelephone());
            ps1.executeUpdate();

            ResultSet rs = ps1.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                String sql2 = "INSERT INTO employes (id, role, salaire) VALUES (?, ?, ?)";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                ps2.setInt(1, id);
                ps2.setString(2, employe.getRole());
                ps2.setDouble(3, employe.getSalaire());
                ps2.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erreur ajout employé : " + e.getMessage());
        }
        return false;
    }

    public List<Employe> getTousLesEmployes() {
        Connection conn = ConnexionDB.getConnection();
        List<Employe> liste = new ArrayList<>();
        if (conn == null) return liste;
        try {
            String sql = "SELECT p.id, p.nom, p.telephone, e.role, e.salaire " +
                         "FROM personnes p JOIN employes e ON p.id = e.id";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                String role = rs.getString("role");
                Employe emp;
                switch (role) {
                    case "Administrateur":
                        emp = new Administrateur(rs.getInt("id"), rs.getString("nom"),
                              rs.getString("telephone"), rs.getDouble("salaire"));
                        break;
                    case "Caissier":
                        emp = new Caissier(rs.getInt("id"), rs.getString("nom"),
                              rs.getString("telephone"), rs.getDouble("salaire"));
                        break;
                    default:
                        emp = new Serveur(rs.getInt("id"), rs.getString("nom"),
                              rs.getString("telephone"), rs.getDouble("salaire"));
                }
                liste.add(emp);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lecture employés : " + e.getMessage());
        }
        return liste;
    }

    public boolean modifier(int id, String nom, String telephone, double salaire) {
        Connection conn = ConnexionDB.getConnection();
        if (conn == null) return false;
        try {
            String sql1 = "UPDATE personnes SET nom=?, telephone=? WHERE id=?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, nom); ps1.setString(2, telephone); ps1.setInt(3, id);
            ps1.executeUpdate();

            String sql2 = "UPDATE employes SET salaire=? WHERE id=?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setDouble(1, salaire); ps2.setInt(2, id);
            ps2.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur modification employé : " + e.getMessage());
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
            System.out.println("Erreur suppression employé : " + e.getMessage());
        }
        return false;
    }
}