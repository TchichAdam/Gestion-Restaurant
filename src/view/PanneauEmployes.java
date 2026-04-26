package view;

import dao.EmployeDAO;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanneauEmployes extends JPanel {

    private JTable tableau;
    private DefaultTableModel modele;
    private EmployeDAO employeDAO;

    public PanneauEmployes() {
        employeDAO = new EmployeDAO();
        setLayout(new BorderLayout(0, 0));
        setBackground(FenetrePrincipale.GRIS_CLAIR);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // En-tête
        JPanel entete = new JPanel(new BorderLayout());
        entete.setOpaque(false);
        entete.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JLabel titre = new JLabel("Gestion des employés");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setForeground(FenetrePrincipale.BLEU_FONCE);

        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        droite.setOpaque(false);
        JButton btnAjouter   = creerBouton("+ Ajouter",  FenetrePrincipale.BLEU_FONCE);
        JButton btnModifier  = creerBouton("Modifier",   new Color(39, 80, 10));
        JButton btnSupprimer = creerBouton("Supprimer",  new Color(163, 45, 45));
        droite.add(btnAjouter); droite.add(btnModifier); droite.add(btnSupprimer);
        entete.add(titre, BorderLayout.WEST);
        entete.add(droite, BorderLayout.EAST);
        add(entete, BorderLayout.NORTH);

        // Tableau
        String[] colonnes = {"ID", "Nom", "Téléphone", "Rôle", "Salaire (MAD)"};
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

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        add(scroll, BorderLayout.CENTER);

        chargerEmployes();

        // Ajouter
        btnAjouter.addActionListener(e -> {
            JTextField champNom  = new JTextField();
            JTextField champTel  = new JTextField();
            JTextField champSal  = new JTextField();
            String[] roles = {"Administrateur", "Serveur", "Caissier"};
            JComboBox<String> comboRole = new JComboBox<>(roles);

            JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
            form.add(new JLabel("Nom :")); form.add(champNom);
            form.add(new JLabel("Téléphone :")); form.add(champTel);
            form.add(new JLabel("Rôle :")); form.add(comboRole);
            form.add(new JLabel("Salaire (MAD) :")); form.add(champSal);

            int res = JOptionPane.showConfirmDialog(this, form,
                "Ajouter un employé", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    double salaire = Double.parseDouble(champSal.getText().trim());
                    String role = (String) comboRole.getSelectedItem();
                    Employe emp;
                    switch (role) {
                        case "Administrateur":
                            emp = new Administrateur(0, champNom.getText().trim(),
                                  champTel.getText().trim(), salaire); break;
                        case "Caissier":
                            emp = new Caissier(0, champNom.getText().trim(),
                                  champTel.getText().trim(), salaire); break;
                        default:
                            emp = new Serveur(0, champNom.getText().trim(),
                                  champTel.getText().trim(), salaire);
                    }
                    employeDAO.ajouter(emp);
                    chargerEmployes();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Salaire invalide !");
                }
            }
        });

        // Modifier
        btnModifier.addActionListener(e -> {
            int ligne = tableau.getSelectedRow();
            if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un employé !"); return; }

            int id = (int) modele.getValueAt(ligne, 0);
            JTextField champNom = new JTextField(modele.getValueAt(ligne, 1).toString());
            JTextField champTel = new JTextField(modele.getValueAt(ligne, 2).toString());
            JTextField champSal = new JTextField(modele.getValueAt(ligne, 4).toString());

            JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
            form.add(new JLabel("Nom :")); form.add(champNom);
            form.add(new JLabel("Téléphone :")); form.add(champTel);
            form.add(new JLabel("Salaire (MAD) :")); form.add(champSal);

            int res = JOptionPane.showConfirmDialog(this, form,
                "Modifier l'employé", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    double salaire = Double.parseDouble(champSal.getText().trim());
                    employeDAO.modifier(id, champNom.getText().trim(),
                        champTel.getText().trim(), salaire);
                    chargerEmployes();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Salaire invalide !");
                }
            }
        });

        // Supprimer
        btnSupprimer.addActionListener(e -> {
            int ligne = tableau.getSelectedRow();
            if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un employé !"); return; }
            int rep = JOptionPane.showConfirmDialog(this, "Supprimer cet employé ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
            if (rep == JOptionPane.YES_OPTION) {
                employeDAO.supprimer((int) modele.getValueAt(ligne, 0));
                chargerEmployes();
            }
        });
    }

    private void chargerEmployes() {
        modele.setRowCount(0);
        for (Employe emp : employeDAO.getTousLesEmployes()) {
            modele.addRow(new Object[]{
                emp.getId(), emp.getNom(), emp.getTelephone(),
                emp.getRole(), emp.getSalaire()
            });
        }
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