package view;

import dao.ClientDAO;
import dao.PlatDAO;
import javax.swing.*;
import java.awt.*;

public class PanneauDashboard extends JPanel {

    public PanneauDashboard() {
        setLayout(new BorderLayout());
        setBackground(FenetrePrincipale.GRIS_CLAIR);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // Titre
        JLabel titre = new JLabel("Tableau de bord");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(FenetrePrincipale.BLEU_FONCE);
        titre.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        add(titre, BorderLayout.NORTH);

        // Panneau central
        JPanel centre = new JPanel(new BorderLayout(0, 16));
        centre.setOpaque(false);

        // Cartes statistiques
        JPanel cartes = new JPanel(new GridLayout(1, 4, 12, 0));
        cartes.setOpaque(false);
        cartes.add(creerCarte("Clients", obtenirNbClients(), new Color(74, 144, 217)));
        cartes.add(creerCarte("Plats au menu", obtenirNbPlats(), new Color(30, 158, 117)));
        cartes.add(creerCarte("Tables", "8", new Color(239, 159, 39)));
        cartes.add(creerCarte("Commandes", "12", new Color(216, 90, 48)));
        centre.add(cartes, BorderLayout.NORTH);

        // Message de bienvenue
        JPanel bienvenue = new JPanel(new BorderLayout());
        bienvenue.setBackground(FenetrePrincipale.BLANC);
        bienvenue.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
            BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));
        JLabel msg = new JLabel("<html><b>Bienvenue dans l'application de gestion !</b><br>" +
            "<span style='color:gray'>Utilisez le menu de gauche pour naviguer entre les modules.</span></html>");
        msg.setFont(new Font("Arial", Font.PLAIN, 14));
        bienvenue.add(msg, BorderLayout.CENTER);
        centre.add(bienvenue, BorderLayout.CENTER);

        add(centre, BorderLayout.CENTER);
    }

    private JPanel creerCarte(String label, String valeur, Color couleur) {
        JPanel carte = new JPanel(new BorderLayout());
        carte.setBackground(FenetrePrincipale.BLANC);
        carte.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, couleur), // barre colorée à gauche
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setForeground(Color.GRAY);

        JLabel val = new JLabel(valeur);
        val.setFont(new Font("Arial", Font.BOLD, 28));
        val.setForeground(couleur);

        carte.add(lbl, BorderLayout.NORTH);
        carte.add(val, BorderLayout.CENTER);
        return carte;
    }

    private String obtenirNbClients() {
        try {
            return String.valueOf(new ClientDAO().getTousLesClients().size());
        } catch (Exception e) { return "—"; }
    }

    private String obtenirNbPlats() {
        try {
            return String.valueOf(new PlatDAO().getTousLesPlats().size());
        } catch (Exception e) { return "—"; }
    }
}