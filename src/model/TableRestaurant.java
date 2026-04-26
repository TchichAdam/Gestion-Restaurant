package model;

public class TableRestaurant {
    private int numero;
    private int capacite;
    private String etat; // "libre" ou "occupee"

    public TableRestaurant(int numero, int capacite) {
        this.numero = numero;
        this.capacite = capacite;
        this.etat = "libre";
    }

    public int getNumero() { return numero; }
    public int getCapacite() { return capacite; }
    public String getEtat() { return etat; }

    public void occuper() { this.etat = "occupee"; }
    public void liberer() { this.etat = "libre"; }
    public boolean isLibre() { return etat.equals("libre"); }

    @Override
    public String toString() {
        return "Table " + numero + " (cap: " + capacite + ") - " + etat;
    }
}