package com.plastprod.plastprodapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Date;
import sqlite.helper.Contact;
import sqlite.helper.DatabaseHelper;

public class AccueilActivity extends ActionBarActivity {

    //pour la base de donn�e locale
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void onResume(){
        super.onResume();

        //on affiche le nom d'utilisateur ou son mail si il s'est d�j� connect�
        EditText edt_utilisateur = (EditText)findViewById(R.id.edt_nomutilisateur);
        EditText edt_motdepasse = (EditText)findViewById(R.id.edt_motdepasse);

        final Global jeton = (Global) getApplicationContext();

        if( jeton.getNom_utilisateur().equals("") )
            edt_utilisateur.setText(jeton.getNom_utilisateur());
        if( jeton.getMail_utilisateur().equals("") )
            edt_utilisateur.setText(jeton.getMail_utilisateur());

        //on enl�ve le mot de passe
        edt_motdepasse.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void ByPass(View vue){

        db = new DatabaseHelper(getApplicationContext());

        //V�rification des donn�es d'identification
        Contact commercial = db.getCommercial(4);

        //R�ussite, on va au menu principal
        if( commercial != null ) {

            final Global jeton = (Global) getApplicationContext();
            jeton.setNom_utilisateur("dupond.jean");
            jeton.setUtilisateur(commercial);
            jeton.setDate_connexion(new Date());

            //nouvelle activit�
            Intent activite = new Intent(this, MenuActivity.class);
            //on d�marre la nouvelle activit�
            startActivity(activite);
        }
    }

    public void Authentifier(View vue){

        String identifiant;
        String motDePasse;
        Boolean connexion_ok = false;
        CharSequence message = "";

        try {

            EditText edt_utilisateur = (EditText)findViewById(R.id.edt_nomutilisateur);
            identifiant = edt_utilisateur.getText().toString();
            EditText edt_motdepasse = (EditText)findViewById(R.id.edt_motdepasse);
            motDePasse = edt_motdepasse.getText().toString();

            //acc�s base
            db = new DatabaseHelper(getApplicationContext());

            String salt = db.getSalt(identifiant);

            if( !salt.equals("") ) {
                Cryptage securite = new Cryptage(motDePasse, salt);
                String mdpEncode = securite.CrypterDonnees();

                Log.d("Info", mdpEncode);

                //V�rification des donn�es d'identification
                Contact commercial = db.verifierIdentifiantCommercial(identifiant, mdpEncode);

                //R�ussite, on va au menu principal
                if( commercial != null ){

                    connexion_ok = true;
                    final Global jeton = (Global) getApplicationContext();

                    if( identifiant.contains("@") )
                        jeton.setMail_utilisateur(identifiant);
                    else
                        jeton.setNom_utilisateur(identifiant);

                    jeton.setUtilisateur(commercial);

                    jeton.setDate_connexion(new Date());

                    //nouvelle activit�
                    Intent activite = new Intent(this, MenuActivity.class);

                    //on d�marre la nouvelle activit�
                    startActivity(activite);
                }
                else{
                    message = "Identifiant et/ou mot de passe incorrect";
                }
            }
            else{
                message = "Identifiant inconnu";
            }


            if( !connexion_ok) {
                //Message d'erreur � afficher
                Outils.afficherToast(getApplicationContext(), message.toString());
                /*Context context = getApplicationContext();
                int duree = Toast.LENGTH_LONG;

                Toast notification = Toast.makeText(context, message, duree);
                notification.show();*/
            }

            db.close();

        }
        catch (Exception e){
            Log.d("Erreur", "Message : " + e.getMessage());
        }
    }

	public int SynchroniserApp(){
		
		List<Societe> liste_client = new ArrayList<Societe>();
		List<Contact> liste_contact = new ArrayList<Contact>();
		List<Bon> liste_bon = new ArrayList<Bon>();
		List<LigneCommande> liste_lignecommande = new ArrayList<LigneCommande>();
		List<Evenement> liste_evenement = new ArrayList<Evenement>();
		List<Parametre> liste_parametre = new ArrayList<Parametre>();
		
		//Appels au WebServices
		RestApi controleur = new RestApi();
		
		//acc�s base
		db = new DatabaseHelper(getApplicationContext());
		
		/*********************************************
		**											**
		**              ETAPE 1                     **
		**		R�cup�ration donn�es locale			**
		**											**
		*********************************************/
		
		/** A : Donn�es � ajouter **/
		//Societe
		liste_client = db.getSyncClient("AJOUT");
		//Contact
		liste_contact = db.getSyncContact("AJOUT");
		//Bon
		liste_bon = db.getSyncBon("AJOUT");
		//LigneCommande
		liste_lignecommande = db.getSyncLigne("AJOUT");
		//Evenement
		liste_evenement = db.getSyncEvenement("AJOUT");

		//Envoi des donn�es
		controleur.envoyerDonneesAAjouter(liste_client, liste_contact, liste_bon, liste_lignecommande, liste_evenement);
		
		/** B : Donn�es � modifier **/
		liste_client = db.getSyncClient("MAJ");
		liste_contact = db.getSyncContact("MAJ");
		liste_bon = db.getSyncBon("MAJ");
		liste_lignecommande = db.getSyncLigne("MAJ");
		liste_evenement = db.getSyncEvenement("MAJ");

		//Envoi des donn�es
		controleur.envoyerDonneesMaj(liste_client, liste_contact, liste_bon, liste_lignecommande, liste_evenement);		
		
		/** C : Donn�es � supprimer **/
		liste_client = db.getSyncClient("SUPP");
		liste_contact = db.getSyncContact("SUPP");
		liste_bon = db.getSyncBon("SUPP");
		liste_lignecommande = db.getSyncLigne("SUPP");
		liste_evenement = db.getSyncEvenement("SUPP");

		//Envoi des donn�es
		controleur.envoyerDonneesMaj(liste_client, liste_contact, liste_bon, liste_lignecommande, liste_evenement);			
		
		/*********************************************
		**											**
		**              ETAPE 2                    	**
		**		Vider les donn�es looales			**
		**											**
		*********************************************/		
		db.tronquerTable();
		
		/*********************************************
		**											**
		**              ETAPE 3                    	**
		**		Importer les donn�es serveurs		**
		**											**
		*********************************************/			
		
		
		
		
		
	}
}