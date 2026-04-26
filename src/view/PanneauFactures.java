package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PanneauFactures extends JPanel {

    private JTable tableau;
    private DefaultTableModel modele;
    private int compteurFacture = 4;

    public PanneauFactures() {
        setLayout(new BorderLayout(0, 0));
        setBackground(FenetrePrincipale.GRIS_CLAIR);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // En-tête
        JPanel entete = new JPanel(new BorderLayout());
        entete.setOpaque(false);
        entete.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JLabel titre = new JLabel("Gestion des factures");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setForeground(FenetrePrincipale.BLEU_FONCE);

        JButton btnVoir = creerBouton("Voir détails", FenetrePrincipale.BLEU_FONCE);

        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        droite.setOpaque(false);
        droite.add(btnVoir);
        entete.add(titre, BorderLayout.WEST);
        entete.add(droite, BorderLayout.EAST);
        add(entete, BorderLayout.NORTH);

        // Tableau
        String[] colonnes = {"# Fact.", "# Cmd", "Client",
                             "Table", "Plats", "Total (MAD)", "Mode paiement", "Statut"};
        modele = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modele);
        tableau.setRowHeight(28);
        tableau.setFont(new Font("Arial", Font.PLAIN, 13));
        tableau.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableau.getTableHeader().setBackground(new Color(230, 235, 245));
        tableau.setSelectionBackground(new Color(210, 225, 245));
        tableau.setGridColor(new Color(235, 238, 245));
        tableau.setFillsViewportHeight(true);

        // Colorier selon statut
        tableau.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) {
                    String statut = (String) modele.getValueAt(r, 7);
                    comp.setBackground("Payée".equals(statut)
                        ? new Color(235, 250, 235) : new Color(255, 248, 220));
                }
                return comp;
            }
        });

        // Données de démo
        modele.addRow(new Object[]{"1","1","Youssef Alami","Table 3",
            "Tajine, Salade, Thé","110.0","Espèces","Payée"});
        modele.addRow(new Object[]{"2","2","Fatima Zahra","Table 1",
            "Couscous, Jus","115.0","Carte","Payée"});
        modele.addRow(new Object[]{"3","3","Karim Bennis","—",
            "Harira, Pain","45.0","Espèces","Payée"});

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        add(scroll, BorderLayout.CENTER);

        // Voir détails
        btnVoir.addActionListener(e -> {
            int ligne = tableau.getSelectedRow();
            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une facture !");
                return;
            }
            String msg =
                "╔══════════════════════════════╗\n" +
                "       FACTURE #" + modele.getValueAt(ligne, 0) + "\n" +
                "╚══════════════════════════════╝\n\n" +
                "Commande  : #" + modele.getValueAt(ligne, 1) + "\n" +
                "Client    : "  + modele.getValueAt(ligne, 2) + "\n" +
                "Table     : "  + modele.getValueAt(ligne, 3) + "\n\n" +
                "Plats     : "  + modele.getValueAt(ligne, 4) + "\n\n" +
                "──────────────────────────────\n" +
                "TOTAL     : "  + modele.getValueAt(ligne, 5) + " MAD\n" +
                "Paiement  : "  + modele.getValueAt(ligne, 6) + "\n" +
                "Statut    : "  + modele.getValueAt(ligne, 7);
            JOptionPane.showMessageDialog(this, msg,
                "Détail facture #" + modele.getValueAt(ligne, 0),
                JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // ← Appelée automatiquement depuis PanneauCommandes
    public void ajouterFacture(int idCommande, String client, String table,
                                String plats, double total, String modePaiement) {
        modele.addRow(new Object[]{
            compteurFacture++,
            idCommande,
            client,
            table,
            plats,
            String.format("%.1f", total),
            modePaiement,
            "Payée"
        });
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}