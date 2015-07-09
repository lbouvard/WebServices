package com.plastprod.plastprodapp;

import com.loopj.android.http.*;

public class RESTApi {

	//pour les requetes HTTP
	AsyncHttpClient client;
	//pour serialiser / déserialiser les objets
	Gson gson;
	Context context;
	
	private static final string API_CLIENT			= "http://localhost/api/societes"
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
	private static final String API_EVENEMENT_AJT 	= "http://localhost/api/evenements/alt";
	private static final String API_EVENEMENT_MAJ 	= "http://localhost/api/evenements/maj";
	
	private static final String API_UTILISATEUR = "http://localhost/api/utilisateurs";
	private static final String API_PRODUIT 	= "http://localhost/api/produits";
	private static final String API_PARAMETRE 	= "http://localhost/api/parametres";
	private static final String API_OBJECTIF	= "http://localhost/api/objectifs";
	private static final String API_STOCK 		= "http://localhost/api/stocks";
	private static final String API_REPONSE		= "http://localhost/api/reponses";
	private static final String API_SATISF 		= "http://localhost/api/satisfactions";

	//constructeur
	public RestApi(Context context) {		
		client = new AsyncHttpClient();
		gson = new Gson();
		this.context = context;
	}

	public JSONObject recupererDepuisWS(String url, RequestParams parametres){
		
		JSONObject resultat = null;
		int codeRetour = 200;
		
		client.get(url, parametres, new AsyncHttpResponseHandler() {
			
			//Réponse OK
			@Override
			public void onSuccess(String reponse) {

				try {
					// JSON Object
					JSONObject obj = new JSONObject(reponse);
					
					//TODO : test de la réponse
				
					resultat = obj;
				} 
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			//Erreur
			@Override
			public void onFailure(int statusCode, Throwable error, String content) {
				
				//TODO tracer l'erreur
			}
		});

		return resultat;
	}
	
	public int envoyerVersWS(String url, String donneesJSON){
		
		int Etat = 0;
		StringEntity entite = new StringEntity(donneesJSON);
		
		client.post(context, url, entite, new AsyncHttpResponseHandler() {
			
			//Réponse 200 OK
			@Override
			public void onSuccess(String response) {
				Etat = 1;
			}
			
			//Erreur
			@Override
			public void onFailure(int statusCode, Throwable error, String content) {
				Etat = statusCode;
			}
		});

		return Etat;
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
			printStackTrace();
			return -1
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
			printStackTrace();
			return -1
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
			printStackTrace();
			return -1
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
			printStackTrace();
			return -1
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
			printStackTrace();
			return -1
		}
		
		return 1;		
	}
			
	public List<Societe> recupererClients(String auteur){
		
		List<Societe> liste_clients;
		
		RequestParams parametres = new RequestParams();
		//parametres.put("auteur", auteur);
		
		//on recupère les données JSON
		JSONObject obj = recupererDepuisWS(API_CLIENT, parametres)
		
		Type type_liste = new TypeToken<ArrayList<Societe>>() {}.getType();
		liste_clients = gson.fromJson(obj, type_liste);
		
		return liste_clients;
	}
}