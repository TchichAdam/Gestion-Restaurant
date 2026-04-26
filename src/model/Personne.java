package model;

public abstract class Personne {
    private int id;
    private String nom;
    private String telephone;

    // Constructeur
    public Personne(int id, String nom, String telephone) {
        this.id = id;
        this.nom = nom;
        this.telephone = telephone;
    }

    // Getters et Setters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getTelephone() { return telephone; }

    public void setNom(String nom) { this.nom = nom; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    @Override
    public String toString() {
        return "ID: " + id + " | Nom: " + nom + " | Tel: " + telephone;
    }
}