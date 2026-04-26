package view;

import dao.PlatDAO;
import model.Plat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class PanneauPlats extends JPanel {

    private JTable tableau;
    private DefaultTableModel modele;
    private PlatDAO platDAO;

    public PanneauPlats() {
        platDAO = new PlatDAO();
        setLayout(new BorderLayout(0, 0));
        setBackground(FenetrePrincipale.GRIS_CLAIR);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // En-tête
        JPanel entete = new JPanel(new BorderLayout());
        entete.setOpaque(false);
        entete.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        JLabel titre = new JLabel("Menu / Gestion des plats");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setForeground(FenetrePrincipale.BLEU_FONCE);

        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        droite.setOpaque(false);
        JButton btnAjouter  = creerBouton("+ Ajouter",  FenetrePrincipale.BLEU_FONCE);
        JButton btnModifier = creerBouton("Modifier",   new Color(39, 80, 10));
        JButton btnSupprimer= creerBouton("Supprimer",  new Color(163, 45, 45));
        droite.add(btnAjouter); droite.add(btnModifier); droite.add(btnSupprimer);
        entete.add(titre, BorderLayout.WEST);
        entete.add(droite, BorderLayout.EAST);
        add(entete, BorderLayout.NORTH);

        // Tableau
        String[] colonnes = {"ID", "Nom du plat", "Catégorie", "Prix (MAD)", "Disponible"};
        modele = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
            public Class<?> getColumnClass(int c) {
                return c == 4 ? Boolean.class : Object.class;
            }
        };
        tableau = new JTable(modele);
        tableau.setRowHeight(28);
        tableau.setFont(new Font("Arial", Font.PLAIN, 13));
        tableau.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableau.getTableHeader().setBackground(new Color(230, 235, 245));
        tableau.setSelectionBackground(new Color(210, 225, 245));
        tableau.setGridColor(new Color(235, 238, 245));

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        add(scroll, BorderLayout.CENTER);

        chargerPlats();

        btnAjouter.addActionListener(e -> {
            String nom = JOptionPane.showInputDialog(this, "Nom du plat :");
            if (nom == null || nom.trim().isEmpty()) return;
            String cat = JOptionPane.showInputDialog(this, "Catégorie :");
            String prixStr = JOptionPane.showInputDialog(this, "Prix (MAD) :");
            try {
                double prix = Double.parseDouble(prixStr);
                Plat p = new Plat(0, nom.trim(), cat.trim(), prix);
                platDAO.ajouter(p);
                chargerPlats();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Prix invalide !");
            }
        });

        btnModifier.addActionListener(e -> {
            int ligne = tableau.getSelectedRow();
            if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un plat !"); return; }
            int id = (int) modele.getValueAt(ligne, 0);
            String nom = JOptionPane.showInputDialog(this, "Nouveau nom :", modele.getValueAt(ligne, 1));
            if (nom == null) return;
            String cat = JOptionPane.showInputDialog(this, "Catégorie :", modele.getValueAt(ligne, 2));
            String prixStr = JOptionPane.showInputDialog(this, "Prix :", modele.getValueAt(ligne, 3));
            try {
                double prix = Double.parseDouble(prixStr);
                Plat p = new Plat(id, nom.trim(), cat.trim(), prix);
                platDAO.modifier(p);
                chargerPlats();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Prix invalide !");
            }
        });

        btnSupprimer.addActionListener(e -> {
            int ligne = tableau.getSelectedRow();
            if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un plat !"); return; }
            int rep = JOptionPane.showConfirmDialog(this, "Supprimer ce plat ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (rep == JOptionPane.YES_OPTION) {
                platDAO.supprimer((int) modele.getValueAt(ligne, 0));
                chargerPlats();
            }
        });
    }

    private void chargerPlats() {
        modele.setRowCount(0);
        for (Plat p : platDAO.getTousLesPlats()) {
            modele.addRow(new Object[]{p.getId(), p.getNom(), p.getCategorie(), p.getPrix(), p.isDisponible()});
        }
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);   // ← important
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
}