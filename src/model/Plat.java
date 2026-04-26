package model;

public class Plat {
    private int id;
    private String nom;
    private String categorie;
    private double prix;
    private boolean disponible;

    public Plat(int id, String nom, String categorie, double prix) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.prix = prix;
        this.disponible = true; // disponible par défaut
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getCategorie() { return categorie; }
    public double getPrix() { return prix; }
    public boolean isDisponible() { return disponible; }

    public void setPrix(double prix) { this.prix = prix; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return nom + " (" + categorie + ") - " + prix + " MAD" + (disponible ? "" : " [Indisponible]");
    }
}