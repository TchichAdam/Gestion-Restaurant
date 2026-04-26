package view;

import javax.swing.*;
import java.awt.*;

public class FenetrePrincipale extends JFrame {

    private JPanel panneauContenu;
    private CardLayout cardLayout;
    private PanneauFactures panneauFactures; // ← référence partagée

    public static final Color BLEU_FONCE = new Color(26, 39, 68);
    public static final Color BLEU_CLAIR = new Color(74, 144, 217);
    public static final Color BLANC      = Color.WHITE;
    public static final Color GRIS_CLAIR = new Color(245, 246, 248);

    public FenetrePrincipale() {
        setTitle("Gestion Restaurant - EMSI");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = creerSidebar();
        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panneauContenu = new JPanel(cardLayout);
        panneauContenu.setBackground(GRIS_CLAIR);

        panneauFactures = new PanneauFactures();
        // Crée PanneauTables en premier
        PanneauTables panneauTables = new PanneauTables();


        panneauContenu.add(new PanneauDashboard(),                        "dashboard");
        panneauContenu.add(new PanneauCommandes(panneauFactures, panneauTables), "commandes");
        panneauContenu.add(new PanneauPlats(),                            "plats");
        panneauContenu.add(panneauTables,                                 "tables");
        panneauContenu.add(new PanneauEmployes(),                         "employes");
        panneauContenu.add(panneauFactures,                               "factures");

        add(panneauContenu, BorderLayout.CENTER);
        cardLayout.show(panneauContenu, "dashboard");
    }

    private JPanel creerSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(BLEU_FONCE);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("Restaurant App");
        logo.setForeground(BLANC);
        logo.setFont(new Font("Arial", Font.BOLD, 15));
        logo.setBorder(BorderFactory.createEmptyBorder(20, 16, 5, 16));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);

        JLabel emsi = new JLabel("EMSI - 3IIR16");
        emsi.setForeground(new Color(150, 160, 180));
        emsi.setFont(new Font("Arial", Font.PLAIN, 11));
        emsi.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        emsi.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(emsi);

        sidebar.add(new JSeparator());
        sidebar.add(Box.createVerticalStrut(8));

        // ← Plus de "Clients" ici
        ajouterBoutonNav(sidebar, "Tableau de bord", "dashboard");
        ajouterBoutonNav(sidebar, "Commandes",        "commandes");
        ajouterBoutonNav(sidebar, "Menu / Plats",     "plats");
        ajouterBoutonNav(sidebar, "Tables",           "tables");
        ajouterBoutonNav(sidebar, "Employés",         "employes");
        ajouterBoutonNav(sidebar, "Factures",         "factures");

        sidebar.add(Box.createVerticalGlue());

        JButton btnQuitter = new JButton("Deconnexion");
        btnQuitter.setForeground(new Color(200, 100, 100));
        btnQuitter.setBackground(BLEU_FONCE);
        btnQuitter.setBorderPainted(false);
        btnQuitter.setFocusPainted(false);
        btnQuitter.setFont(new Font("Arial", Font.PLAIN, 13));
        btnQuitter.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnQuitter.setMaximumSize(new Dimension(200, 40));
        btnQuitter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnQuitter.addActionListener(e -> { dispose(); new FenetreConnexion(); });
        sidebar.add(btnQuitter);
        sidebar.add(Box.createVerticalStrut(10));

        return sidebar;
    }

    private void ajouterBoutonNav(JPanel sidebar, String texte, String cle) {
        JButton btn = new JButton("  " + texte);
        btn.setForeground(new Color(180, 190, 210));
        btn.setBackground(BLEU_FONCE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(40, 55, 90));
                btn.setForeground(BLANC);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(BLEU_FONCE);
                btn.setForeground(new Color(180, 190, 210));
            }
        });

        btn.addActionListener(e -> cardLayout.show(panneauContenu, cle));
        sidebar.add(btn);
    }
}