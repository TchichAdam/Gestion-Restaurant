package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commande {
    private int id;
    private Date date;
    private String etat; // "en cours", "terminee", "annulee"
    private Client client;
    private Serveur serveur;
    private TableRestaurant table;
    private List<Plat> plats;

    public Commande(int id, Client client, Serveur serveur, TableRestaurant table) {
        this.id = id;
        this.client = client;
        this.serveur = serveur;
        this.table = table;
        this.date = new Date();
        this.etat = "en cours";
        this.plats = new ArrayList<>();
        table.occuper(); // la table devient occupée
    }

    public void ajouterPlat(Plat plat) {
        plats.add(plat);
        System.out.println(plat.getNom() + " ajouté à la commande.");
    }

    public void supprimerPlat(Plat plat) {
        plats.remove(plat);
        System.out.println(plat.getNom() + " retiré de la commande.");
    }

    public double calculerTotal() {
        double total = 0;
        for (Plat p : plats) {
            total += p.getPrix();
        }
        return total;
    }

    public void terminer() {
        this.etat = "terminee";
        table.liberer(); // libérer la table
    }

    // Getters
    public int getId() { return id; }
    public Date getDate() { return date; }
    public String getEtat() { return etat; }
    public Client getClient() { return client; }
    public Serveur getServeur() { return serveur; }
    public TableRestaurant getTable() { return table; }
    public List<Plat> getPlats() { return plats; }

    @Override
    public String toString() {
        return "Commande #" + id + " | Client: " + client.getNom() 
             + " | Table: " + table.getNumero() 
             + " | Total: " + calculerTotal() + " MAD"
             + " | Etat: " + etat;
    }
}