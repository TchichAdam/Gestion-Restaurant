package view;

import javax.swing.*;
import java.awt.*;

public class FenetreConnexion extends JFrame {

    private JTextField champUtilisateur;
    private JPasswordField champMotDePasse;
    private JButton btnConnexion;

    public FenetreConnexion() {
        setTitle("Restaurant App - Connexion");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centrer à l'écran
        setResizable(false);

        // Panneau principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);

        // Titre
        JLabel titre = new JLabel("Gestion Restaurant", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(new Color(26, 39, 68)); // bleu foncé
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titre, gbc);

        // Sous-titre
        JLabel sousTitre = new JLabel("Connectez-vous pour continuer", SwingConstants.CENTER);
        sousTitre.setFont(new Font("Arial", Font.PLAIN, 12));
        sousTitre.setForeground(Color.GRAY);
        gbc.gridy = 1;
        panel.add(sousTitre, gbc);

        // Séparateur
        gbc.gridy = 2;
        panel.add(new JSeparator(), gbc);

        // Champ utilisateur
        gbc.gridy = 3; gbc.gridwidth = 1;
        panel.add(new JLabel("Utilisateur :"), gbc);
        champUtilisateur = new JTextField(15);
        champUtilisateur.setText("admin");
        gbc.gridx = 1;
        panel.add(champUtilisateur, gbc);

        // Champ mot de passe
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Mot de passe :"), gbc);
        champMotDePasse = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(champMotDePasse, gbc);

        // Bouton connexion
        btnConnexion = new JButton("Se connecter");
        btnConnexion.setBackground(new Color(26, 39, 68));
        btnConnexion.setForeground(Color.WHITE);
        btnConnexion.setFont(new Font("Arial", Font.BOLD, 13));
        btnConnexion.setFocusPainted(false);
        btnConnexion.setOpaque(true);
        btnConnexion.setContentAreaFilled(true);
        btnConnexion.setBorderPainted(false);
        btnConnexion.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(16, 0, 0, 0);
        panel.add(btnConnexion, gbc);

        // Action du bouton
        btnConnexion.addActionListener(e -> {
            String user = champUtilisateur.getText();
            String pass = new String(champMotDePasse.getPassword());

            // Vérification simple (tu pourras connecter à la BDD plus tard)
            if (user.equals("admin") && pass.equals("admin")) {
                dispose(); // fermer la fenêtre de connexion
                new FenetrePrincipale().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Identifiants incorrects !",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Appuyer Entrée = connexion
        getRootPane().setDefaultButton(btnConnexion);

        add(panel);
        setVisible(true);
    }
}