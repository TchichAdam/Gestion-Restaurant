package model;

public abstract class Employe extends Personne {
    private String role;
    private double salaire;

    public Employe(int id, String nom, String telephone, String role, double salaire) {
        super(id, nom, telephone);
        this.role = role;
        this.salaire = salaire;
    }

    public String getRole() { return role; }
    public double getSalaire() { return salaire; }
    public void setSalaire(double salaire) { this.salaire = salaire; }

    public abstract void travailler(); // chaque sous-classe définit son travail
}