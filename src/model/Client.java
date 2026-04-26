package model;

public class Client extends Personne {
    private String adresse;

    public Client(int id, String nom, String telephone, String adresse) {
        super(id, nom, telephone); // appel du constructeur parent
        this.adresse = adresse;
    }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public void passerCommande() {
        System.out.println(getNom() + " passe une commande.");
    }

    @Override
    public String toString() {
        return super.toString() + " | Adresse: " + adresse;
    }
}