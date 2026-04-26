package model;

import java.util.Date;

public class Facture {
    private int id;
    private Date date;
    private double montantTotal;
    private Commande commande;

    public Facture(int id, Commande commande) {
        this.id = id;
        this.commande = commande;
        this.date = new Date();
        this.montantTotal = commande.calculerTotal();
    }

    public double calculerMontant() { return montantTotal; }

    public void imprimerFacture() {
        System.out.println("========== FACTURE #" + id + " ==========");
        System.out.println("Date : " + date);
        System.out.println("Client : " + commande.getClient().getNom());
        System.out.println("Table : " + commande.getTable().getNumero());
        System.out.println("--- Plats ---");
        for (Plat p : commande.getPlats()) {
            System.out.println("  " + p.getNom() + " : " + p.getPrix() + " MAD");
        }
        System.out.println("--- TOTAL : " + montantTotal + " MAD ---");
    }

    public int getId() { return id; }
    public double getMontantTotal() { return montantTotal; }
    public Commande getCommande() { return commande; }
}