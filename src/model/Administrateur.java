package model;

public class Administrateur extends Employe {

    public Administrateur(int id, String nom, String telephone, double salaire) {
        super(id, nom, telephone, "Administrateur", salaire);
    }

    @Override
    public void travailler() {
        System.out.println(getNom() + " gère le système.");
    }

    public void gererMenu() { System.out.println("Gestion du menu..."); }
    public void gererTables() { System.out.println("Gestion des tables..."); }
    public void gererEmployes() { System.out.println("Gestion des employés..."); }
}