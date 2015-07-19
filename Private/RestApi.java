package com.plastprod.plastprodapp;

import android.content.Context;
import android.widget.Switch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.*;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import sqlite.helper.Bon;
import sqlite.helper.Contact;
import sqlite.helper.Evenement;
import sqlite.helper.LigneCommande;
import sqlite.helper.Societe;

public class RESTApi {

	//pour les requetes HTTP
	AsyncHttpClient client;
	//pour serialiser / déserialiser les objets
	Gson gson;
	Context context;

    //les données à récupérer
    List<Societe> liste_clients;
    List<Contact> liste_contacts;
    List<Bon> liste_bons;
    List<LigneCommande> liste_articles;
    List<Evenement> liste_evenements;

	private static final String API_CLIENT			= "http://localhost/api/societes";
	private static final String API_CLIENT_AJT		= "http://localhost/api/societes/ajt";
	private static final String API_CLIENT_MAJ 		= "http://localhost/api/societes/maj";
	
	private static final String API_CONTACT 		= "http://localhost/api/contacts";
	private static final String API_CONTACT_AJT 	= "http://localhost/api/contacts/ajt";
	private static final String API_CONTACT_MAJ		= "http://localhost/api/contacts/maj";
	
	private static final String API_BON 			= "http://localhost/api/bons";
	private static final String API_BON_AJT 		= "http://localhost/api/bons/ajt";
	private static final String API_BON_MAJ 		= "http://localhost/api/bons/maj";
	
	private static final String API_LIGNES			= "http://localhost/api/lignes";
	private static final String API_LIGNES_AJT		= "http://localhost/api/lignes/ajt";
	private static final String API_LIGNES_MAJ		= "http://localhost/api/lignes/maj";
	
	private static final String API_EVENEMENT 		= "http://localhost/api/evenements";
	private static final String API_EVENEMENT_AJT 	= "http://localhost/api/evenements/ajt";
	private static final String API_EVENEMENT_MAJ 	= "http://localhost/api/evenements/maj";
	
	private static final String API_UTILISATEUR = "http://localhost/api/utilisateurs";
	private static final String API_PRODUIT 	= "http://localhost/api/produits";
	private static final String API_PARAMETRE 	= "http://localhost/api/parametres";
	private static final String API_OBJECTIF	= "http://localhost/api/objectifs";
	private static final String API_STOCK 		= "http://localhost/api/stocks";
	private static final String API_REPONSE		= "http://localhost/api/reponses";
	private static final String API_SATISF 		= "http://localhost/api/satisfactions";

	//constructeur
	public RESTApi(Context context) {
		client = new AsyncHttpClient();
		gson = new Gson();
		this.context = context;
	}

	public void recupererDepuisWS(String url, RequestParams parametres, final String type){

		int codeRetour = 200;
        JSONObject retour = null;

        client.get(url, parametres, new AsyncHttpResponseHandler() {

			//Réponse OK
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				try {

                    String reponse = new String(responseBody, StandardCharsets.UTF_8);

					// JSON Object
					JSONObject obj = new JSONObject(reponse);

					//Mise en place des données
                    switch (type) {

                        case "Societes":
                            setClients(obj);
                        default:

                    }
				} 
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			//Erreur
			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				//Erreur
			}

		});
	}
	
	public void envoyerVersWS(String url, String donneesJSON){

        StringEntity entite = null;
        RequestParams params = null;

        try {
            entite = new StringEntity(donneesJSON, "UTF-8");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        params = new RequestParams();
        params.put("data", entite);

		client.post(context, url, params, new AsyncHttpResponseHandler() {

            //Réponse 200 OK
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //Traiter
            }

            //Erreur
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Erreur
            }
        });
	}

	public int envoyerClients(int methode, List<Societe> liste_clients){
		
		try{
			
			Type type_liste = new TypeToken<ArrayList<Societe>>() {}.getType();
			String clients = gson.toJson(liste_clients, type_liste);
			
			//Ajout
			if( methode == 1){
				envoyerVersWS(API_CLIENT_AJT, clients);
			}
			//Modification
			else{
				envoyerVersWS(API_CLIENT_MAJ, clients);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return -1;
		}
		
		return 1;
	}

	public int envoyerContacts(int methode, List<Contact> liste_contacts){
		try{
			Type type_liste = new TypeToken<ArrayList<Contact>>() {}.getType();
			String contacts = gson.toJson(liste_contacts, type_liste);
			
			//Ajout
			if( methode == 1){
				envoyerVersWS(API_CONTACT_AJT, contacts);
			}
			//Modification
			else{
				envoyerVersWS(API_CONTACT_MAJ, contacts);
			}

		}
		catch(Exception e){
			e.printStackTrace();
			return -1;
		}
		
		return 1;		
	}

	public int envoyerBons(int methode, List<Bon> liste_bons){
		try{
			Type type_liste = new TypeToken<ArrayList<Bon>>() {}.getType();
			String bons = gson.toJson(liste_bons, type_liste);
			
			//Ajout
			if( methode == 1){
				envoyerVersWS(API_BON_AJT, bons);
			}
			//Modification
			else{
				envoyerVersWS(API_BON_MAJ, bons);
			}

		}
		catch(Exception e){
            e.printStackTrace();
            return -1;
		}
		
		return 1;		
	}

	public int envoyerLignes(int methode, List<LigneCommande> liste_lignes){
		try{
			Type type_liste = new TypeToken<ArrayList<LigneCommande>>() {}.getType();
			String lignes = gson.toJson(liste_lignes, type_liste);
			
			//Ajout
			if( methode == 1){
				envoyerVersWS(API_LIGNES_AJT, lignes);
			}
			//Modification
			else{
				envoyerVersWS(API_LIGNES_MAJ, lignes);
			}

		}
		catch(Exception e){
            e.printStackTrace();
            return -1;
		}
		
		return 1;		
	}

	public int envoyerEvenements(int methode, List<Evenement> liste_events){
		try{
			Type type_liste = new TypeToken<ArrayList<Evenement>>() {}.getType();
			String events = gson.toJson(liste_events, type_liste);
			
			//Ajout
			if( methode == 1){
				envoyerVersWS(API_EVENEMENT_AJT, events);
			}
			//Modification
			else{
				envoyerVersWS(API_EVENEMENT_MAJ, events);
			}

		}
		catch(Exception e){
            e.printStackTrace();
            return -1;
		}
		
		return 1;		
	}

    public void setClients(JSONObject donnees){

        Type type_liste = new TypeToken<ArrayList<Societe>>() {}.getType();
        liste_clients = gson.fromJson(donnees.toString(), type_liste);

    }

	public void recupererClients(){

		RequestParams parametres = new RequestParams();
		//on intérroge le web service
		recupererDepuisWS(API_CLIENT, parametres, "Societes");

	}
}