package model;

public class Caissier extends Employe {

    public Caissier(int id, String nom, String telephone, double salaire) {
        super(id, nom, telephone, "Caissier", salaire);
    }

    @Override
    public void travailler() {
        System.out.println(getNom() + " encaisse les paiements.");
    }

    public void creerFacture() { System.out.println("Facture créée."); }
    public void encaisserPaiement() { System.out.println("Paiement encaissé."); }
}