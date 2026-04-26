package view;

import dao.CommandeDAO;
import dao.PlatDAO;
import model.Plat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class PanneauCommandes extends JPanel {

    private JTable tableau;
    private DefaultTableModel modele;
    private PlatDAO platDAO;
    private CommandeDAO commandeDAO;
    private PanneauFactures panneauFactures;
    private PanneauTables panneauTables;

    public PanneauCommandes(PanneauFactures panneauFactures, PanneauTables panneauTables) {
        this.panneauFactures = panneauFactures;
        this.panneauTables = panneauTables;
        platDAO = new PlatDAO();
        commandeDAO = new CommandeDAO();

        setLayout(new BorderLayout(0, 0));
        setBackground(FenetrePrincipale.GRIS_CLAIR);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // En-tête
        JPanel entete = new JPanel(new BorderLayout());
        entete.setOpaque(false);
        entete.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JLabel titre = new JLabel("Gestion des commandes");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setForeground(FenetrePrincipale.BLEU_FONCE);

        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        droite.setOpaque(false);
        JButton btnNouvelle = creerBouton("+ Nouvelle commande", FenetrePrincipale.BLEU_FONCE);
        JButton btnTerminer = creerBouton("Terminer", new Color(39, 80, 10));
        JButton btnAnnuler  = creerBouton("Annuler",  new Color(163, 45, 45));
        droite.add(btnNouvelle); droite.add(btnTerminer); droite.add(btnAnnuler);
        entete.add(titre, BorderLayout.WEST);
        entete.add(droite, BorderLayout.EAST);
        add(entete, BorderLayout.NORTH);

        // Tableau
        String[] colonnes = {"#", "Client", "Téléphone", "Type", "Table",
                             "Plats", "Total (MAD)", "Paiement", "État"};
        modele = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modele);
        tableau.setRowHeight(30);
        tableau.setFont(new Font("Arial", Font.PLAIN, 13));
        tableau.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableau.getTableHeader().setBackground(new Color(230, 235, 245));
        tableau.setSelectionBackground(new Color(210, 225, 245));
        tableau.setGridColor(new Color(235, 238, 245));
        tableau.setFillsViewportHeight(true);

        // Colorier lignes selon état
        tableau.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) {
                    String etat = (String) modele.getValueAt(r, 8);
                    if ("Terminée".equals(etat))
                        comp.setBackground(new Color(235, 250, 235));
                    else if ("Annulée".equals(etat))
                        comp.setBackground(new Color(255, 235, 235));
                    else
                        comp.setBackground(Color.WHITE);
                }
                return comp;
            }
        });

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        add(scroll, BorderLayout.CENTER);

        // Charger les commandes depuis MySQL au démarrage
        chargerCommandes();

        // ── NOUVELLE COMMANDE ──
        btnNouvelle.addActionListener(e -> ouvrirFormulaireCommande());

        // ── TERMINER ──
        btnTerminer.addActionListener(e -> {
            int ligne = tableau.getSelectedRow();
            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une commande !");
                return;
            }
            if ("Terminée".equals(modele.getValueAt(ligne, 8))) {
                JOptionPane.showMessageDialog(this, "Commande déjà terminée.");
                return;
            }
            int id = Integer.parseInt(modele.getValueAt(ligne, 0).toString());
            modele.setValueAt("Terminée", ligne, 8);
            tableau.repaint();
            commandeDAO.modifierEtat(id, "Terminée"); // ← sauvegarder dans MySQL

            String type = (String) modele.getValueAt(ligne, 3);
            String tableStr = (String) modele.getValueAt(ligne, 4);
            if ("Sur place".equals(type) && !"—".equals(tableStr)) {
                int num = Integer.parseInt(tableStr.replace("Table ", "").trim());
                panneauTables.libererTable(num);
            }
        });

        // ── ANNULER ──
        btnAnnuler.addActionListener(e -> {
            int ligne = tableau.getSelectedRow();
            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une commande !");
                return;
            }
            int rep = JOptionPane.showConfirmDialog(this, "Annuler cette commande ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
            if (rep == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(modele.getValueAt(ligne, 0).toString());
                modele.setValueAt("Annulée", ligne, 8);
                tableau.repaint();
                commandeDAO.modifierEtat(id, "Annulée"); // ← sauvegarder dans MySQL

                String type = (String) modele.getValueAt(ligne, 3);
                String tableStr = (String) modele.getValueAt(ligne, 4);
                if ("Sur place".equals(type) && !"—".equals(tableStr)) {
                    int num = Integer.parseInt(tableStr.replace("Table ", "").trim());
                    panneauTables.libererTable(num);
                }
            }
        });
    }

    // Charger depuis MySQL
    private void chargerCommandes() {
        modele.setRowCount(0);
        List<Object[]> commandes = commandeDAO.getToutesCommandes();
        for (Object[] row : commandes) {
            modele.addRow(row);
        }
        tableau.repaint();
    }

    private void ouvrirFormulaireCommande() {
        JDialog dialog = new JDialog((Frame) null, "Nouvelle commande", true);
        dialog.setSize(500, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(20, 28, 10, 28));
        main.setBackground(Color.WHITE);

        main.add(creerSectionTitre("1. Informations du client"));
        JTextField champNom = new JTextField();
        JTextField champTel = new JTextField();
        main.add(creerLigne("Nom du client :", champNom));
        main.add(Box.createVerticalStrut(8));
        main.add(creerLigne("Téléphone :", champTel));
        main.add(Box.createVerticalStrut(16));

        main.add(creerSectionTitre("2. Type de commande"));
        JRadioButton rbSurPlace = new JRadioButton("Sur place");
        JRadioButton rbEmporter = new JRadioButton("À emporter");
        rbSurPlace.setBackground(Color.WHITE);
        rbEmporter.setBackground(Color.WHITE);
        rbSurPlace.setFont(new Font("Arial", Font.PLAIN, 13));
        rbEmporter.setFont(new Font("Arial", Font.PLAIN, 13));
        ButtonGroup groupType = new ButtonGroup();
        groupType.add(rbSurPlace); groupType.add(rbEmporter);
        rbSurPlace.setSelected(true);

        JPanel panType = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        panType.setBackground(Color.WHITE);
        panType.add(rbSurPlace); panType.add(rbEmporter);
        panType.setAlignmentX(Component.LEFT_ALIGNMENT);
        main.add(panType);
        main.add(Box.createVerticalStrut(8));

        JPanel panTable = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panTable.setBackground(Color.WHITE);
        panTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblTable = new JLabel("Table : ");
        lblTable.setFont(new Font("Arial", Font.PLAIN, 13));
        String[] tables = {"Table 1","Table 2","Table 3","Table 4",
                           "Table 5","Table 6","Table 7","Table 8"};
        JComboBox<String> comboTable = new JComboBox<>(tables);
        comboTable.setFont(new Font("Arial", Font.PLAIN, 13));
        panTable.add(lblTable); panTable.add(comboTable);
        main.add(panTable);
        main.add(Box.createVerticalStrut(16));

        rbEmporter.addActionListener(ev -> panTable.setVisible(false));
        rbSurPlace.addActionListener(ev -> panTable.setVisible(true));

        main.add(creerSectionTitre("3. Plats commandés"));
        List<Plat> tousPlats = platDAO.getTousLesPlats();
        JCheckBox[] checkboxes = new JCheckBox[tousPlats.size()];
        JPanel panPlats = new JPanel();
        panPlats.setLayout(new BoxLayout(panPlats, BoxLayout.Y_AXIS));
        panPlats.setBackground(Color.WHITE);

        for (int i = 0; i < tousPlats.size(); i++) {
            Plat p = tousPlats.get(i);
            checkboxes[i] = new JCheckBox(
                String.format("%-25s %6.1f MAD", p.getNom(), p.getPrix()));
            checkboxes[i].setFont(new Font("Monospaced", Font.PLAIN, 12));
            checkboxes[i].setBackground(Color.WHITE);
            panPlats.add(checkboxes[i]);
        }

        JScrollPane scrollPlats = new JScrollPane(panPlats);
        scrollPlats.setPreferredSize(new Dimension(440, 120));
        scrollPlats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        scrollPlats.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPlats.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        main.add(scrollPlats);
        main.add(Box.createVerticalStrut(16));

        main.add(creerSectionTitre("4. Mode de paiement"));
        JRadioButton rbEspeces = new JRadioButton("Espèces");
        JRadioButton rbCarte   = new JRadioButton("Carte bancaire");
        rbEspeces.setBackground(Color.WHITE);
        rbCarte.setBackground(Color.WHITE);
        rbEspeces.setFont(new Font("Arial", Font.PLAIN, 13));
        rbCarte.setFont(new Font("Arial", Font.PLAIN, 13));
        ButtonGroup groupPaiement = new ButtonGroup();
        groupPaiement.add(rbEspeces); groupPaiement.add(rbCarte);
        rbEspeces.setSelected(true);

        JLabel lblTotal = new JLabel("Total : 0.0 MAD");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotal.setForeground(FenetrePrincipale.BLEU_FONCE);

        for (int i = 0; i < checkboxes.length; i++) {
            final int idx = i;
            checkboxes[idx].addActionListener(ev -> {
                double total = 0;
                for (int j = 0; j < checkboxes.length; j++) {
                    if (checkboxes[j].isSelected())
                        total += tousPlats.get(j).getPrix();
                }
                lblTotal.setText(String.format("Total : %.1f MAD", total));
            });
        }

        JPanel panPaiement = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        panPaiement.setBackground(Color.WHITE);
        panPaiement.add(rbEspeces); panPaiement.add(rbCarte); panPaiement.add(lblTotal);
        panPaiement.setAlignmentX(Component.LEFT_ALIGNMENT);
        main.add(panPaiement);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btns.setBackground(new Color(245, 246, 248));
        btns.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 225, 235)));

        JButton btnAnn = new JButton("Annuler");
        btnAnn.setFocusPainted(false);
        btnAnn.addActionListener(ev -> dialog.dispose());

        JButton btnOk = creerBouton("Confirmer la commande", FenetrePrincipale.BLEU_FONCE);
        btnOk.addActionListener(ev -> {
            if (champNom.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Le nom du client est obligatoire !");
                return;
            }
            List<String> platsChoisis = new ArrayList<>();
            double total = 0;
            for (int i = 0; i < checkboxes.length; i++) {
                if (checkboxes[i].isSelected()) {
                    platsChoisis.add(tousPlats.get(i).getNom());
                    total += tousPlats.get(i).getPrix();
                }
            }
            if (platsChoisis.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Choisissez au moins un plat !");
                return;
            }

            String typeCommande = rbSurPlace.isSelected() ? "Sur place" : "Emporter";
            String tableChoisie = rbSurPlace.isSelected()
                ? (String) comboTable.getSelectedItem() : "—";
            String modePaiement = rbEspeces.isSelected() ? "Espèces" : "Carte";
            String platsStr = String.join(", ", platsChoisis);

            // Sauvegarder dans MySQL ← retourne l'ID réel
            int id = commandeDAO.sauvegarderCommande(
                champNom.getText().trim(),
                champTel.getText().trim(),
                typeCommande,
                tableChoisie,
                platsStr,
                total,
                modePaiement,
                "En cours"
            );

            // Ajouter dans le tableau
            modele.addRow(new Object[]{
                id,
                champNom.getText().trim(),
                champTel.getText().trim(),
                typeCommande,
                tableChoisie,
                platsStr,
                String.format("%.1f", total),
                modePaiement,
                "En cours"
            });
            tableau.repaint();

            // Ajouter dans Factures
            panneauFactures.ajouterFacture(
                id,
                champNom.getText().trim(),
                tableChoisie,
                platsStr,
                total,
                modePaiement
            );

            // Occuper la table
            if (rbSurPlace.isSelected()) {
                int numeroTable = Integer.parseInt(
                    tableChoisie.replace("Table ", "").trim()
                );
                panneauTables.occuperTable(numeroTable);
            }

            dialog.dispose();
            JOptionPane.showMessageDialog(this,
                "Commande #" + id + " confirmée !\n" +
                "Client : " + champNom.getText().trim() + "\n" +
                "Total  : " + String.format("%.1f", total) + " MAD\n" +
                "Paiement : " + modePaiement,
                "Commande créée", JOptionPane.INFORMATION_MESSAGE);
        });

        btns.add(btnAnn); btns.add(btnOk);

        JScrollPane scrollMain = new JScrollPane(main);
        scrollMain.setBorder(null);
        dialog.add(scrollMain, BorderLayout.CENTER);
        dialog.add(btns, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JLabel creerSectionTitre(String texte) {
        JLabel lbl = new JLabel(texte);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(FenetrePrincipale.BLEU_FONCE);
        lbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, FenetrePrincipale.BLEU_CLAIR),
            BorderFactory.createEmptyBorder(2, 8, 2, 0)
        ));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel creerLigne(String label, JTextField champ) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(Color.WHITE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setPreferredSize(new Dimension(130, 28));
        champ.setFont(new Font("Arial", Font.PLAIN, 13));
        p.add(lbl, BorderLayout.WEST);
        p.add(champ, BorderLayout.CENTER);
        return p;
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
        btn.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}