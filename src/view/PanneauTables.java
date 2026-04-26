package view;

import dao.CommandeDAO;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanneauTables extends JPanel {

    private Map<Integer, String> etatsTable = new HashMap<>();
    private JPanel grille;
    private CommandeDAO commandeDAO;

    public PanneauTables() {
        commandeDAO = new CommandeDAO();
        setLayout(new BorderLayout());
        setBackground(FenetrePrincipale.GRIS_CLAIR);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // Initialiser toutes les tables comme LIBRES par défaut
        for (int i = 1; i <= 8; i++) {
            etatsTable.put(i, "libre");
        }

        // Lire depuis MySQL les commandes "En cours" pour occuper les bonnes tables
        chargerEtatDepuisDB();

        // En-tête
        JPanel entete = new JPanel(new BorderLayout());
        entete.setOpaque(false);
        entete.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JLabel titre = new JLabel("Plan des tables");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setForeground(FenetrePrincipale.BLEU_FONCE);

        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.setBackground(FenetrePrincipale.BLEU_FONCE);
        btnActualiser.setForeground(Color.WHITE);
        btnActualiser.setOpaque(true);
        btnActualiser.setContentAreaFilled(true);
        btnActualiser.setBorderPainted(false);
        btnActualiser.setFocusPainted(false);
        btnActualiser.setFont(new Font("Arial", Font.BOLD, 12));
        btnActualiser.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btnActualiser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnActualiser.addActionListener(e -> {
            // Réinitialiser et relire depuis MySQL
            for (int i = 1; i <= 8; i++) {
                etatsTable.put(i, "libre");
            }
            chargerEtatDepuisDB();
            rafraichirGrille();
        });

        entete.add(titre, BorderLayout.WEST);
        entete.add(btnActualiser, BorderLayout.EAST);
        add(entete, BorderLayout.NORTH);

        // Grille
        grille = new JPanel(new GridLayout(2, 4, 16, 16));
        grille.setOpaque(false);
        rafraichirGrille();

        JScrollPane scroll = new JScrollPane(grille);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }

    // Lire les commandes "En cours" depuis MySQL et occuper les tables
    private void chargerEtatDepuisDB() {
        List<Object[]> commandes = commandeDAO.getToutesCommandes();
        for (Object[] row : commandes) {
            String etat        = (String) row[8]; // colonne État
            String type        = (String) row[3]; // colonne Type
            String tableAssign = (String) row[4]; // colonne Table

            if ("En cours".equals(etat)
                    && "Sur place".equals(type)
                    && tableAssign != null
                    && !tableAssign.equals("—")) {
                try {
                    int num = Integer.parseInt(tableAssign.replace("Table ", "").trim());
                    etatsTable.put(num, "occupee");
                } catch (NumberFormatException ignored) {}
            }
        }
    }

    private void rafraichirGrille() {
        grille.removeAll();
        for (int i = 1; i <= 8; i++) {
            grille.add(creerCarteTable(i, 4, etatsTable.get(i)));
        }
        grille.revalidate();
        grille.repaint();
    }

    private JPanel creerCarteTable(int numero, int capacite, String etat) {
        boolean libre = etat.equals("libre");
        Color couleur = libre ? new Color(39, 110, 17) : new Color(163, 45, 45);
        Color bg      = libre ? new Color(234, 243, 222) : new Color(252, 235, 235);

        JPanel carte = new JPanel(new BorderLayout());
        carte.setBackground(bg);
        carte.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, couleur),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JPanel infos = new JPanel(new GridLayout(3, 1, 0, 4));
        infos.setOpaque(false);

        JLabel lblNum = new JLabel("Table " + numero, SwingConstants.CENTER);
        lblNum.setFont(new Font("Arial", Font.BOLD, 15));
        lblNum.setForeground(FenetrePrincipale.BLEU_FONCE);

        JLabel lblCap = new JLabel(capacite + " personnes", SwingConstants.CENTER);
        lblCap.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCap.setForeground(Color.GRAY);

        JLabel lblEtat = new JLabel(libre ? "LIBRE" : "OCCUPÉE", SwingConstants.CENTER);
        lblEtat.setFont(new Font("Arial", Font.BOLD, 12));
        lblEtat.setForeground(couleur);

        infos.add(lblNum);
        infos.add(lblCap);
        infos.add(lblEtat);
        carte.add(infos, BorderLayout.CENTER);

        JButton btnChanger = new JButton(libre ? "Occuper" : "Libérer");
        btnChanger.setBackground(couleur);
        btnChanger.setForeground(Color.WHITE);
        btnChanger.setOpaque(true);
        btnChanger.setContentAreaFilled(true);
        btnChanger.setBorderPainted(false);
        btnChanger.setFocusPainted(false);
        btnChanger.setFont(new Font("Arial", Font.BOLD, 11));
        btnChanger.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        btnChanger.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnChanger.addActionListener(e -> {
            etatsTable.put(numero, libre ? "occupee" : "libre");
            rafraichirGrille();
        });

        carte.add(btnChanger, BorderLayout.SOUTH);
        return carte;
    }

    public void occuperTable(int numero) {
        etatsTable.put(numero, "occupee");
        rafraichirGrille();
    }

    public void libererTable(int numero) {
        etatsTable.put(numero, "libre");
        rafraichirGrille();
    }
}