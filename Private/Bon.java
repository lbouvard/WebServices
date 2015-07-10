package sqlite.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Laurent on 10/06/2015.
 */
public class Bon {

    private int id;
    private String date_commande;
    private String etat_commande;
    private String type;
    private String suivi;
    private String transporteur;
    private String auteur;
    private String date_changement;
    private List<LigneCommande> lignesBon;
    private Societe client;
    private Contact commercial;
	
	private int ajoute = 0;
	private int supprime = 0;
	

    public Bon(String type) {
        this.type = type;
        this.lignesBon = new ArrayList<LigneCommande>();
    }

	public void setId(int id) {
        this.id = id;
    }
	
    public int getId() {
        return id;
    }

    public String getDate_commande() {
        return date_commande;
    }

    public String getEtat_commande() {
        return etat_commande;
    }

    public String getType() {
        return type;
    }

    public String getSuivi() {
        return suivi;
    }

    public String getTransporteur() {
        return transporteur;
    }

    public String getDate_changement() {
        return date_changement;
    }

    public void setDate_commande(String date_commande) {
        this.date_commande = date_commande;
    }

    public void setEtat_commande(String etat_commande) {
        this.etat_commande = etat_commande;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSuivi(String suivi) {
        this.suivi = suivi;
    }

    public void setTransporteur(String transporteur) {
        this.transporteur = transporteur;
    }

    public void setDate_changement(String date_changement) {
        this.date_changement = date_changement;
    }

    public List<LigneCommande> getLignesBon() {
        return lignesBon;
    }

    public void setLignesBon(List<LigneCommande> lignesBon) {
        this.lignesBon = lignesBon;
    }

    public Societe getClient() {
        return client;
    }

    public void setClient(Societe client) {
        this.client = client;
    }

    public Contact getCommercial() {
        return commercial;
    }

    public void setCommercial(Contact commercial) {
        this.commercial = commercial;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }
	
	public void isAjoute(int ajt) {
        this.ajoute = ajt;
    }
	
    public int isSupprime() {
        return this.supprime;
    }
	
}