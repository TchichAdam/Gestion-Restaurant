package model;

public class Serveur extends Employe {

    public Serveur(int id, String nom, String telephone, double salaire) {
        super(id, nom, telephone, "Serveur", salaire);
    }

    @Override
    public void travailler() {
        System.out.println(getNom() + " sert les clients.");
    }

    public void prendreCommande() { System.out.println("Commande prise."); }
    public void modifierCommande() { System.out.println("Commande modifiée."); }
}