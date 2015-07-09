package sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Laurent on 10/06/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //logcat tag
    private static final String log = "DatabaseHelper";

    //version de la base
    private static final int DATABASE_VERSION = 32;

    //nom de la base
    private static final String DATABASE_NAME = "DB_PLASTPROD";

    //nom des tables
    private static final String TABLE_BON = "Bon";
    private static final String TABLE_COM = "Commentaire";
    private static final String TABLE_COMPTE = "Compte";
    private static final String TABLE_CONTACT = "Contact";
    private static final String TABLE_EVENT = "Evenement";
    private static final String TABLE_LIGNE = "LigneCommande";
    private static final String TABLE_OBJ = "Objectif";
    private static final String TABLE_PARAM = "Parametre";
    private static final String TABLE_PRODUIT = "Produit";
    private static final String TABLE_REPONSE = "Reponse";
    private static final String TABLE_SATISF = "SatisfactionQ";
    private static final String TABLE_SOCIETE = "Societe";
    private static final String TABLE_STOCK = "Stock";
    private static final String TABLE_CORRESP_COULEUR = "CorrespCouleur";

    private static final String CREATE_TABLE_SOCIETE = "CREATE TABLE Societe (IdtSociete INTEGER PRIMARY KEY, Nom TEXT NOT NULL,"
            + "Adresse1 TEXT NOT NULL, Adresse2 TEXT, CodePostal TEXT NOT NULL, Ville TEXT NOT NULL , Pays TEXT NOT NULL, Type TEXT NOT NULL, Commentaire  TEXT,"
            + "Auteur TEXT NOT NULL, BitAjout INTEGER NOT NULL, BitModif INTEGER NOT NULL, BitSup INTEGER NOT NULL)";

    private static final String CREATE_TABLE_CONTACT = "CREATE TABLE Contact(IdtContact INTEGER PRIMARY KEY, Nom TEXT NOT NULL,"
            + "Prenom TEXT NOT NULL, Poste TEXT, TelFixe TEXT, Fax TEXT, TelMobile TEXT, Mail  TEXT, Adresse TEXT, CodePostal  TEXT, Ville TEXT, Pays  TEXT,"
            + "Commentaire TEXT, Auteur TEXT NOT NULL, BitAjout INTEGER NOT NULL, BitModif INTEGER NOT NULL, BitSup INTEGER NOT NULL, IdtSociete INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete))";

    private static final String CREATE_TABLE_COMPTE= "CREATE TABLE Compte(IdtCompte INTEGER PRIMARY KEY, Nom TEXT NOT NULL,"
            + "MotDePasse  TEXT NOT NULL, Mail TEXT NOT NULL, Salt TEXT NOT NULL, Actif INTEGER NOT NULL, IdtContact INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtContact) REFERENCES Contact(IdtContact))";

    private static final String CREATE_TABLE_BON = "CREATE TABLE Bon(IdtBon INTEGER PRIMARY KEY, DateCommande TEXT NOT NULL,"
            + "EtatCommande TEXT, Type TEXT, Suivi TEXT, Transporteur TEXT, Auteur TEXT NOT NULL, DateChg TEXT, BitChg  INTEGER NOT NULL,"
            + "BitAjout INTEGER NOT NULL, BitModif INTEGER NOT NULL, BitSup INTEGER NOT NULL, IdtSociete INTEGER NOT NULL, IdtContact INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete),"
            + "FOREIGN KEY (IdtContact) REFERENCES Contact(IdtContact))";

    private static final String CREATE_TABLE_STOCK = "CREATE TABLE Stock(IdtEntree INTEGER PRIMARY KEY,"
            + "Quantite INTEGER NOT NULL, DelaisMoy INTEGER NOT NULL, Delais INTEGER NOT NULL)";

    private static final String CREATE_TABLE_PRODUIT = "CREATE TABLE Produit(IdtProduit INTEGER PRIMARY KEY,"
            + "Nom TEXT NOT NULL, Description TEXT NOT NULL, Categorie TEXT NOT NULL, Code TEXT NOT NULL,"
            + "Prix REAL NOT NULL, IdtEntree INTEGER NOT NULL,"
            + "FOREIGN KEY(IdtEntree) REFERENCES Stock(IdtEntree))";

    private static final String CREATE_TABLE_OBJ = "CREATE TABLE Objectif(IdtObjectif INTEGER PRIMARY KEY,"
            + "Annee TEXT NOT NULL, Type  TEXT NOT NULL, Libelle TEXT NOT NULL, Valeur TEXT NOT NULL, IdtCompte INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_PARAM = "CREATE TABLE Parametre(IdtParam INTEGER PRIMARY KEY,"
            + "Nom TEXT NOT NULL, Type TEXT NOT NULL, Libelle TEXT NOT NULL, Valeur TEXT NOT NULL, BitAjout INTEGER NOT NULL,"
            + "BitModif INTEGER NOT NULL, IdtCompte INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_REPONSE = "CREATE TABLE Reponse(IdtQuestion INTEGER PRIMARY KEY,"
            + "Question TEXT NOT NULL, Reponse TEXT NOT NULL, Categorie TEXT NOT NULL, Type TEXT NOT NULL, IdtSatisfaction INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSatisfaction) REFERENCES SatisfactionQ(IdtSatisfaction))";

    private static final String CREATE_TABLE_SATISF = "CREATE TABLE SatisfactionQ(IdtSatisfaction INTEGER PRIMARY KEY,"
            + "Nom TEXT, DateEnvoi TEXT NOT NULL, DateRecu TEXT, IdtSociete INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtSociete) REFERENCES Societe(IdtSociete))";

    private static final String CREATE_TABLE_EVENT = "CREATE TABLE Evenement(IdtEvent INTEGER PRIMARY KEY, DateDeb TEXT NOT NULL,"
            + "DateFin TEXT NOT NULL, Recurrent TEXT, Frequence TEXT, Titre TEXT NOT NULL, Emplacement TEXT NOT NULL, Commentaire TEXT NOT NULL,"
            + "Disponibilite TEXT NOT NULL, EstPrive INTEGER NOT NULL, BitAjout INTEGER NOT NULL, BitModif INTEGER NOT NULL, IdtCompte  INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtCompte) REFERENCES Compte(IdtCompte))";

    private static final String CREATE_TABLE_LIGNE = "CREATE TABLE LigneCommande(Idt INTEGER PRIMARY KEY, Quantite INTEGER NOT NULL,"
            + "Code TEXT, Nom TEXT NOT NULL, Description TEXT, PrixUnitaire REAL NOT NULL, Remise REAL NOT NULL,"
            + "IdtBon INTEGER NOT NULL,"
            + "FOREIGN KEY (IdtBon) REFERENCES Bon(IdtBon))";

    private static final String CREATE_TABLE_CORRESP_COULEUR = "CREATE TABLE CorrespCouleur (IdtLigne INTEGER PRIMARY KEY, Nom TEXT NOT NULL,"
            + "Couleur TEXT NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_BON);
        db.execSQL(CREATE_TABLE_COM);
        db.execSQL(CREATE_TABLE_COMPTE);
        db.execSQL(CREATE_TABLE_CONTACT);
        db.execSQL(CREATE_TABLE_EVENT);
        db.execSQL(CREATE_TABLE_LIGNE);
        db.execSQL(CREATE_TABLE_OBJ);
        db.execSQL(CREATE_TABLE_PARAM);
        db.execSQL(CREATE_TABLE_PRODUIT);
        db.execSQL(CREATE_TABLE_SATISF);
        db.execSQL(CREATE_TABLE_STOCK);
        db.execSQL(CREATE_TABLE_SOCIETE);
        db.execSQL(CREATE_TABLE_CORRESP_COULEUR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIGNE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBJ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARAM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUIT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPONSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SATISF);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOCIETE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CORRESP_COULEUR);

        // create new tables
        onCreate(db);

        //populate tables
        chargerTables(db);
    }

    /***********************
     * AJOUTER
     ***********************/

    //Ajouter un devis
    public void ajouterDevis(Bon devis, LigneCommande[] lignes){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("DateCommande", devis.getDate_commande());
        valeurs.put("EtatCommande", devis.getEtat_commande());
        valeurs.put("Type", "DE");
        valeurs.put("Suivi", "");
        valeurs.put("Transporteur", "");
        valeurs.put("Auteur", devis.getAuteur());
        valeurs.put("DateChg", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date()));
        valeurs.put("BitChg", 0);
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);
        valeurs.put("IdtSociete", devis.getClient().getId());
        valeurs.put("IdtContact", devis.getCommercial().getId());

        //insertion du devis
        long devis_id = db.insert(TABLE_BON, null, valeurs);

        //ajout des articles du devis
        for(LigneCommande ligne : lignes){
            ajouterLigne(ligne, devis_id);
        }

        db.close();
    }

    //Ajouter un bon de commande
    public long ajouterBonCommande(Bon bon, LigneCommande[] lignes){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("DateCommande", bon.getDate_commande());
        valeurs.put("EtatCommande", bon.getEtat_commande());
        valeurs.put("Type", "DE");
        valeurs.put("Suivi", "");
        valeurs.put("Transporteur", "");
        valeurs.put("Auteur", bon.getAuteur());
        valeurs.put("DateChg", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date()));
        valeurs.put("BitChg", 0);
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);
        valeurs.put("IdtSociete", bon.getClient().getId());
        valeurs.put("IdtContact", bon.getCommercial().getId());

        //insertion du bon de commande
        long bon_id = db.insert(TABLE_BON, null, valeurs);

        //ajout des articles du bon de commande
        for(LigneCommande ligne : lignes){
            ajouterLigne(ligne, bon_id);
        }

        return bon_id;
    }

    //Ajouter une ligne d'article
    public long ajouterLigne(LigneCommande ligne, long bon_id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Quantite", ligne.getQuantite());
        valeurs.put("Code", ligne.getCode());
        valeurs.put("Nom", ligne.getNom());
        valeurs.put("Description", ligne.getDescription());
        valeurs.put("Remise", ligne.getRemise());
        valeurs.put("PrixUnitaire", ligne.getPrixUnitaire());
        valeurs.put("IdtBon", bon_id);

        return db.insert(TABLE_LIGNE, null, valeurs);
    }

    //Ajouter un client
    public void ajouterClient(Societe client, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", client.getNom());
        valeurs.put("Adresse1", client.getAdresse1());
        valeurs.put("Adresse2", client.getAdresse2());
        valeurs.put("CodePostal", client.getCode_postal());
        valeurs.put("Ville", client.getVille());
        valeurs.put("Pays", client.getPays());
        valeurs.put("Type", type);
        valeurs.put("Commentaire", client.getCommentaire());
        valeurs.put("Auteur", client.getAuteur());
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);

        db.insert(TABLE_SOCIETE, null, valeurs);
        db.close();
    }

    //Ajouter un contact
    public long ajouterContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", contact.getNom());
        valeurs.put("Prenom", contact.getPrenom());
        valeurs.put("Poste", contact.getPoste());
        valeurs.put("TelFixe", contact.getTel_fixe());
        valeurs.put("Fax", contact.getFax());
        valeurs.put("TelMobile", contact.getTel_mobile());
        valeurs.put("Mail", contact.getEmail());
        valeurs.put("Adresse", contact.getAdresse());
        valeurs.put("CodePostal", contact.getCode_postal());
        valeurs.put("Ville", contact.getVille());
        valeurs.put("Pays", contact.getPays());
        valeurs.put("Commentaire", contact.getCommentaire());
        valeurs.put("Auteur", contact.getAuteur());
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);
        valeurs.put("IdtSociete", contact.getSociete().getId());

        return db.insert(TABLE_CONTACT, null, valeurs);
    }

    //Ajouter un parametre
    public long ajouterParametre(Parametre param){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", param.getNom());
        valeurs.put("Type", param.getType());
        valeurs.put("Libelle", param.getLibelle());
        valeurs.put("Valeur", param.getValeur());
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);
        valeurs.put("IdtCompte", param.getCompte().getId());

        return db.insert(TABLE_PARAM, null, valeurs);
    }

    //Ajouter un événement
    public long ajouterEvenement(Evenement e) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("DateDeb", e.getDate_debut());
        valeurs.put("DateFin", e.getDate_fin());
        valeurs.put("Recurrent", e.getReccurent());
        valeurs.put("Frequence", e.getFrequence());
        valeurs.put("Titre", e.getTitre());
        valeurs.put("Emplacement", e.getEmplacement());
        valeurs.put("Commentaire", e.getCommentaire());
        valeurs.put("Disponibilite", e.getDisponibilite());
        valeurs.put("EstPrive", e.getEst_prive());
        valeurs.put("BitAjout", 1);
        valeurs.put("BitModif", 0);
        valeurs.put("IdtCompte", e.getCompte().getId());

        return db.insert(TABLE_EVENT, null, valeurs);
    }

    //Ajouter un compte (seulement IT)
    public long ajouterCompte(Compte cp){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", cp.getNom());
        valeurs.put("MotDePasse", cp.getMdp());
        valeurs.put("Mail", cp.getEmail());
        valeurs.put("Salt", cp.getSalt());
        valeurs.put("Actif", cp.getActif());
        valeurs.put("IdtContact", cp.getContact_id());

        return db.insert(TABLE_COMPTE, null, valeurs);
    }

    /***********************
     * LIRE
     ***********************/

    public List<Bon> getBons(String type) {

        List<Bon> bons = new ArrayList<Bon>();

        String requete = "SELECT IdtBon, DateCommande, EtatCommande, Suivi, Transporteur, IdtContact, IdtSociete"
                        + "FROM " + TABLE_BON
                        + "WHERE BitChg = 0 AND BitSup = 0";

        Log.d("Requete", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        //On parcours toutes les commandes pour récupérer les articles.
        if( c != null ) {
            if (c.moveToFirst()) {
                do {
                    Bon ligne = new Bon(type);
                    ligne.setId(c.getInt(c.getColumnIndex("IdtBon")));
                    ligne.setDate_commande(c.getString(c.getColumnIndex("DateCommande")));
                    ligne.setEtat_commande(c.getString(c.getColumnIndex("EtatCommande")));
                    ligne.setSuivi(c.getString(c.getColumnIndex("Suivi")));
                    ligne.setTransporteur(c.getString(c.getColumnIndex("Transporteur")));
                    ligne.setCommercial(getCommercial(c.getInt(c.getColumnIndex("IdtContact"))));
                    ligne.setClient(getClient(c.getInt(c.getColumnIndex("IdtSociete"))));
                    ligne.setLignesBon(getLignesCommande(c.getInt(c.getColumnIndex("IdtBon"))));

                    //On ajoute la commande
                    bons.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return bons;
    }

    public List<LigneCommande> getLignesCommande(long id_bon){

        List<LigneCommande> lignes = new ArrayList<LigneCommande>();
        String requete = "SELECT Idt, Quantite, Code, Nom, Description, PrixUnitaire, Remise, IdtBon FROM Ligne_commande"
                            + "WHERE IdtBon = " + id_bon ;

        Log.d("Requete", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        //On parcours toutes les lignes d'articles.
        if( c != null ) {
            if (c.moveToFirst()) {
                do {
                    LigneCommande ligne = new LigneCommande();
                    ligne.setId(c.getInt(c.getColumnIndex("Idt")));
                    ligne.setCode(c.getString(c.getColumnIndex("Code")));
                    ligne.setDescription(c.getString(c.getColumnIndex("Description")));
                    ligne.setId_bon(c.getInt(c.getColumnIndex("IdtBon")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setPrixUnitaire(c.getDouble(c.getColumnIndex("PrixUnitaire")));
                    ligne.setQuantite(c.getInt(c.getColumnIndex("Quantite")));
                    ligne.setRemise(c.getDouble(c.getColumnIndex("Remise")));

                    ligne.calculerPrixRemise();
                    ligne.calculerPrixTotal();

                    lignes.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return lignes;
    }

    public Societe getClient(int id_client){

        Societe client = new Societe();
        String requete = "SELECT IdtSociete, Nom, Adresse1, Adresse2, CodePostal, Ville, Pays, Type, Commentaire, Auteur FROM Societe"
                        + "WHERE IdtSociete = " + id_client + " AND BitSup = 0 AND BitModif = 0";

        Log.d("Requete", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            c.moveToFirst();

            client.setId(c.getInt(c.getColumnIndex("IdtSociete")));
            client.setNom(c.getString(c.getColumnIndex("Nom")));
            client.setAdresse1(c.getString(c.getColumnIndex("Adresse1")));
            client.setAdresse2(c.getString(c.getColumnIndex("Adresse2")));
            client.setCode_postal(c.getString(c.getColumnIndex("CodePostal")));
            client.setVille(c.getString(c.getColumnIndex("Ville")));
            client.setPays(c.getString(c.getColumnIndex("Pays")));
            client.setType(c.getString(c.getColumnIndex("Type")));
            client.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
            client.setAuteur(c.getString(c.getColumnIndex("Auteur")));

            c.close();
        }
        else
            client = null;

        return client;
    }

    public Contact getCommercial(int id_commercial){

        Contact commercial = new Contact();
        String requete = "SELECT IdtContact, Nom, Prenom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays,"
                        + "Commentaire, Auteur FROM Contact "
                        + "WHERE IdtContact = " + id_commercial + " AND BitModif = 0 AND BitSup = 0";

        Log.d("Requete", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            c.moveToFirst();

            commercial.setId(id_commercial);
            commercial.setNom(c.getString(c.getColumnIndex("Nom")));
            commercial.setPrenom(c.getString(c.getColumnIndex("Prenom")));
            commercial.setPoste(c.getString(c.getColumnIndex("Poste")));
            commercial.setTel_fixe(c.getString(c.getColumnIndex("TelFixe")));
            commercial.setFax(c.getString(c.getColumnIndex("Fax")));
            commercial.setTel_mobile(c.getString(c.getColumnIndex("TelMobile")));
            commercial.setEmail(c.getString(c.getColumnIndex("Mail")));
            commercial.setAdresse(c.getString(c.getColumnIndex("Adresse")));
            commercial.setCode_postal(c.getString(c.getColumnIndex("CodePostal")));
            commercial.setVille(c.getString(c.getColumnIndex("Ville")));
            commercial.setPays(c.getString(c.getColumnIndex("Pays")));
            commercial.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
            commercial.setAuteur(c.getString(c.getColumnIndex("Auteur")));

            c.close();
        }
        else
            commercial = null;

        return commercial;
    }

    public List<Societe> getSocietes(String clauseWhere){

        List<Societe> societes = new ArrayList<Societe>();
        String requete = "";

        requete = "SELECT soc.IdtSociete, soc.Nom, soc.Adresse1, soc.Adresse2, soc.CodePostal, soc.Ville, soc.Pays, soc.Type, soc.Commentaire, " +
                "soc.Auteur, cc.Couleur, ifnull(COUNT(con.IdtContact),0) AS Nb " +
                "FROM Societe soc LEFT JOIN Contact con ON soc.IdtSociete =  con.IdtSociete " +
                "INNER JOIN CorrespCouleur cc ON soc.Auteur = cc.Nom " +
                "WHERE soc.Type = 'C' AND soc.BitAjout = 0 AND soc.BitSup = 0 GROUP BY con.IdtContact, soc.IdtSociete ORDER BY soc.IdtSociete";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les sociétés pour ajouter un objet à la liste.
            if (c.moveToFirst()) {
                do {

                    Societe ligne = new Societe();
                    ligne.setId(c.getInt(c.getColumnIndex("IdtSociete")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setAdresse1(c.getString(c.getColumnIndex("Adresse1")));
                    ligne.setAdresse2(c.getString(c.getColumnIndex("Adresse2")));
                    ligne.setCode_postal(c.getString(c.getColumnIndex("CodePostal")));
                    ligne.setVille(c.getString(c.getColumnIndex("Ville")));
                    ligne.setPays(c.getString(c.getColumnIndex("Pays")));
                    ligne.setType(c.getString(c.getColumnIndex("Type")));
                    ligne.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
                    ligne.setAuteur(c.getString(c.getColumnIndex("Auteur")));
                    ligne.setNb_contact(c.getInt(c.getColumnIndex("Nb")));
                    ligne.setCouleur(c.getString(c.getColumnIndex("Couleur")));

                    //On ajoute la société
                    societes.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return societes;
    }

    public List<Contact> getContacts(int id_societe, String clauseWhere){

        List<Contact> contacts = new ArrayList<Contact>();
        String requete = "";

        if( clauseWhere.isEmpty() ) {
            requete = "SELECT IdtContact, Nom, Prenom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays,"
                    + "Commentaire, Auteur FROM Contact "
                    + "WHERE IdtSociete = " + id_societe + " AND BitModif = 0 AND BitSup = 0";
        }
        else{
            requete = "SELECT IdtContact, Nom, Prenom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays,"
                    + "Commentaire, Auteur FROM Contact " + clauseWhere;
        }

        Log.d("Requete", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les sociétés pour ajouter un objet à la liste.
            if (c.moveToFirst()) {
                do {

                    Contact ligne = new Contact();
                    ligne.setId(c.getInt(c.getColumnIndex("IdtContact")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setPrenom(c.getString(c.getColumnIndex("Prenom")));
                    ligne.setPoste(c.getString(c.getColumnIndex("Poste")));
                    ligne.setTel_fixe(c.getString(c.getColumnIndex("TelFixe")));
                    ligne.setFax(c.getString(c.getColumnIndex("Fax")));
                    ligne.setTel_mobile(c.getString(c.getColumnIndex("TelMobile")));
                    ligne.setEmail(c.getString(c.getColumnIndex("Mail")));
                    ligne.setAdresse(c.getString(c.getColumnIndex("Adresse")));
                    ligne.setCode_postal(c.getString(c.getColumnIndex("CodePostal")));
                    ligne.setVille(c.getString(c.getColumnIndex("Ville")));
                    ligne.setPays(c.getString(c.getColumnIndex("Pays")));
                    ligne.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
                    ligne.setAuteur(c.getString(c.getColumnIndex("Auteur")));

                    //On ajoute la commande
                    contacts.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return contacts;
    }

    public List<Parametre> getParametres(int id_commercial){

        List<Parametre> params = new ArrayList<Parametre>();
        String requete = "";

        requete = "SELECT IdtParam, Nom, Type, Libelle, Valeur FROM Parametre"
                + "WHERE IdtCompte = " + id_commercial + " AND BitModif = 0";

        Log.d("Requete", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les paramètres pour ajouter un objet à la liste.
            if (c.moveToFirst()) {
                do {
                    Parametre ligne = new Parametre();
                    ligne.setId(c.getInt(c.getColumnIndex("IdtParam")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setType(c.getString(c.getColumnIndex("Type")));
                    ligne.setLibelle(c.getString(c.getColumnIndex("Libelle")));
                    ligne.setValeur(c.getString(c.getColumnIndex("Valeur")));

                    //On ajoute la commande
                    params.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return params;
    }

    public List<Objectif> getObjectifs(int id_commercial){

        List<Objectif> objectifs = new ArrayList<Objectif>();
        String requete = "";

        requete = "SELECT IdtObjectif, Annee, Type, Libelle, Valeur FROM Objectif"
                + "WHERE IdtCompte = " + id_commercial;

        Log.d("Requete", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les objectifs pour ajouter un objet à la liste.
            if (c.moveToFirst()) {
                do {
                    Objectif ligne = new Objectif();
                    ligne.setId(c.getInt(c.getColumnIndex("IdtObjectif")));
                    ligne.setAnnee(c.getString(c.getColumnIndex("Annee")));
                    ligne.setType(c.getString(c.getColumnIndex("Type")));
                    ligne.setLibelle(c.getString(c.getColumnIndex("Libelle")));
                    ligne.setValeur(c.getString(c.getColumnIndex("Valeur")));

                    //On ajoute un élément des objectifs
                    objectifs.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return objectifs;
    }

    public List<Produit> getProduits(){

        List<Produit> produits = new ArrayList<Produit>();
        String requete = "";

        requete = "SELECT IdtProduit, Nom, Description, Categorie, Code, Prix, IdtEntree FROM Produit";

        Log.d("Requete", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les produits pour ajouter un objet à la liste.
            if (c.moveToFirst()) {
                do {
                    Produit ligne = new Produit();

                    ligne.setId(c.getInt(c.getColumnIndex("IdtProduit")));
                    ligne.setNom(c.getString(c.getColumnIndex("Nom")));
                    ligne.setDescription(c.getString(c.getColumnIndex("Description")));
                    ligne.setCategorie(c.getString(c.getColumnIndex("Categorie")));
                    ligne.setCode(c.getString(c.getColumnIndex("Code")));
                    ligne.setPrix(c.getDouble(c.getColumnIndex("Prix")));
                    ligne.setEntree(getInfoStock(c.getInt(c.getColumnIndex("IdtEntree"))));

                    //On ajoute un élément des objectifs
                    produits.add(ligne);

                } while (c.moveToNext());
            }

            c.close();
        }

        return produits;
    }

    public Stock getInfoStock(int id_entree){

        Stock info = new Stock();
        String requete = "";

        requete = "SELECT Quantite, DelaisMoy, Delais FROM Stock"
                + "WHERE IdtEntree = " + id_entree;

        Log.d("Requete", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les produits pour ajouter un objet à la liste.
            if (c.moveToFirst()) {

                info.setId(id_entree);
                info.setQuantite(c.getInt(c.getColumnIndex("Quantite")));
                info.setDelaisMoy(c.getInt(c.getColumnIndex("DelaisMoy")));
                info.setDelais(c.getInt(c.getColumnIndex("Delais")));
            }

            c.close();
        }
        else
            info = null;

        return info;
    }

    /* Pour l'utilisateur de l'application. */

    public String getSalt(String login){

        String salt = "";
        String requete = "SELECT Salt FROM Compte "
                + "WHERE Nom = '" + login + "' OR Mail = '" + login + "'";

        Log.d("Requete", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c.getCount() > 0 ) {
            c.moveToFirst();

            salt = c.getString(c.getColumnIndex("Salt"));

            c.close();
        }

        return salt;
    }

    public Contact verifierIdentifiantCommercial(String login, String motDePasse){

        Contact info = new Contact();
        int id_contact = -1;

        String requete = "SELECT IdtContact FROM Compte "
                        + "WHERE Nom = '" + login + "' And MotDePasse = '" + motDePasse + "'";

        Log.d("Requete", requete);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c.getCount() > 0) {
            c.moveToFirst();
            id_contact = c.getInt(c.getColumnIndex("IdtContact"));
            c.close();

            //Authentification réussie
            info = getCommercial(id_contact);
        }
        else
            info = null;

        return info;
    }

    /*Pour la satisfaction cliente : A faire.*/

    /***********************
     * METTRE A JOUR
     ***********************/

    public int majSociete(Societe client){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", client.getNom());
        valeurs.put("Adresse1", client.getAdresse1());
        valeurs.put("Adresse2", client.getAdresse2());
        valeurs.put("CodePostal", client.getCode_postal());
        valeurs.put("Ville", client.getVille());
        valeurs.put("Pays", client.getPays());
        valeurs.put("Commentaire", client.getCommentaire());
        valeurs.put("Auteur", client.getAuteur());
        valeurs.put("BitAjout", 0);
        valeurs.put("BitModif", 1);

        // updating row
        db.update(TABLE_SOCIETE, valeurs, "IdtSociete = ?",
                new String[] { String.valueOf(client.getId()) });

        return 0;
    }

    public void majContact(Contact contact, boolean modif_nouveau){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", contact.getNom());
        valeurs.put("Prenom", contact.getPrenom());
        valeurs.put("Poste", contact.getPoste());
        valeurs.put("TelFixe", contact.getTel_fixe());
        valeurs.put("Fax", contact.getFax());
        valeurs.put("TelMobile", contact.getTel_mobile());
        valeurs.put("Mail", contact.getEmail());
        valeurs.put("Adresse", contact.getAdresse());
        valeurs.put("CodePostal", contact.getCode_postal());
        valeurs.put("Ville", contact.getVille());
        valeurs.put("Pays", contact.getPays());
        valeurs.put("Commentaire", contact.getCommentaire());
        valeurs.put("Auteur", contact.getAuteur());
        valeurs.put("BitAjout", modif_nouveau);
        valeurs.put("BitModif", 1);
        valeurs.put("IdtSociete", contact.getSociete().getId());

        // updating row
        db.update(TABLE_CONTACT, valeurs, "IdtContact = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    public void majParametre(Parametre param, boolean modif_nouveau){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Nom", param.getNom());
        valeurs.put("Type", param.getType());
        valeurs.put("Libelle", param.getLibelle());
        valeurs.put("Valeur", param.getValeur());
        valeurs.put("BitAjout", modif_nouveau);
        valeurs.put("BitModif", 1);

        // updating row
        db.update(TABLE_PARAM, valeurs, "IdtParam = ?",
                new String[] { String.valueOf(param.getId()) });
    }

    public void majEvenement(Evenement e, boolean modif_nouveau){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("DateDeb", e.getDate_debut());
        valeurs.put("DateFin", e.getDate_fin());
        valeurs.put("Recurrent", e.getReccurent());
        valeurs.put("Frequence", e.getFrequence());
        valeurs.put("Titre", e.getTitre());
        valeurs.put("Emplacement", e.getEmplacement());
        valeurs.put("Commentaire", e.getCommentaire());
        valeurs.put("Disponibilite", e.getDisponibilite());
        valeurs.put("EstPrive", e.getEst_prive());
        valeurs.put("BitAjout", modif_nouveau);
        valeurs.put("BitModif", 1);

        // updating row
        db.update(TABLE_EVENT, valeurs, "IdtEvent = ?",
                new String[] { String.valueOf(e.getId()) });
    }

    public void majBon(Bon bon, List<LigneCommande> liste_article, boolean modif_nouveau){

        //mise à jour des articles
        Iterator iterator = liste_article.iterator();
        while(iterator.hasNext()){
            LigneCommande element = (LigneCommande) iterator.next();
            majLigneBon(element);
        }

        //mise à jour du bon
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("DateCommande", bon.getDate_commande());
        valeurs.put("EtatCommande", bon.getEtat_commande());
        valeurs.put("Type", bon.getType());
        valeurs.put("Suivi", bon.getSuivi());
        valeurs.put("Transporteur", bon.getTransporteur());
        valeurs.put("Auteur", bon.getAuteur());
        valeurs.put("DateChg", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date()));
        valeurs.put("BitChg", 1);
        valeurs.put("BitAjout", modif_nouveau);
        valeurs.put("BitModif", 1);
        valeurs.put("IdtSociete", bon.getClient().getId());
        valeurs.put("IdtContact", bon.getCommercial().getId());

        // updating row
        db.update(TABLE_BON, valeurs, "IdtBon = ?",
                new String[] { String.valueOf(bon.getId()) });
    }

    public void majLigneBon(LigneCommande ligne){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put("Quantite", ligne.getQuantite());
        valeurs.put("Code", ligne.getCode() );
        valeurs.put("Nom", ligne.getNom() );
        valeurs.put("Description", ligne.getDescription() );
        valeurs.put("Remise", ligne.getRemise() );
        valeurs.put("PrixUnitaire", ligne.getPrixUnitaire() );

        // updating row
        db.update(TABLE_LIGNE, valeurs, "Idt = ?",
                new String[] { String.valueOf(ligne.getId()) });
    }

    /***********************
     * SUPPRIMER
     ***********************/

    public void suppprimerSociete(Societe client, boolean supprimer_contact){

        SQLiteDatabase db = this.getWritableDatabase();

        if( supprimer_contact ){
            db.delete(TABLE_CONTACT, "IdtSociete = ?",
                    new String[] { String.valueOf(client.getId()) });
        }

        db.delete(TABLE_SOCIETE, "IdtSociete = ?",
                new String[]{String.valueOf(client.getId())});
    }

    public void supprimerContact(Contact contact){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CONTACT, "IdtContact = ?",
                new String[]{String.valueOf(contact.getId())} );
    }

    public void supprimerEvenement(Evenement event){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_EVENT, "IdtEvenement = ?",
                new String[]{String.valueOf(event.getId())});
    }

    public void supprimerBon(Bon bon){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_BON, "IdtBon = ?",
                new String[]{String.valueOf(bon.getId())});
    }

    public void supprimerLigneBon(LigneCommande ligne){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_LIGNE, "Idt = ?",
                new String[]{String.valueOf(ligne.getId())});
    }

	/*****************************
	*     Synchronisation		
	******************************/
	//des clients
	public List<Societe> getSyncClients(int type){
		
		List<Societe> clients = new ArrayList<Societe>();
		Societe client = new Societe();
        String requete = "";

		if( type == 1){
			//données ajoutées
			requete = "SELECT IdtSociete, Nom, Adresse1, Adresse2, CodePostal, Ville, Pays, Type, Commentaire, Auteur "
				+ "FROM Societe"
                + "WHERE BitAjout = 1";
		}
		else if( type == 2){
			//données modifiéss
			requete = "SELECT IdtSociete, Nom, Adresse1, Adresse2, CodePostal, Ville, Pays, Type, Commentaire, Auteur "
				+ "FROM Societe"
                + "WHERE BitModif = 1";		
		}
		else if( type == 3){
			//données supprimées
			requete = "SELECT IdtSociete, Nom, Adresse1, Adresse2, CodePostal, Ville, Pays, Type, Commentaire, Auteur "
				+ "FROM Societe"
                + "WHERE BitSup = 1";
		}

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les produits pour ajouter un objet à la liste.
            if (c.moveToFirst()) {

				client.setId(c.getInt(c.getColumnIndex("IdtSociete")));
				client.setNom(c.getString(c.getColumnIndex("Nom")));
				client.setAdresse1(c.getString(c.getColumnIndex("Adresse1")));
				client.setAdresse2(c.getString(c.getColumnIndex("Adresse2")));
				client.setCode_postal(c.getString(c.getColumnIndex("CodePostal")));
				client.setVille(c.getString(c.getColumnIndex("Ville")));
				client.setPays(c.getString(c.getColumnIndex("Pays")));
				client.setType(c.getString(c.getColumnIndex("Type")));
				client.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
				client.setAuteur(c.getString(c.getColumnIndex("Auteur")));

				//On ajoute la commande
				clients.add(client);
            }

            c.close();
        }

        return clients;
	}
	
	//des contacts
	public List<Contact> getSyncContacts(String type){
		
		List<Contact> contacts = new ArrayList<Contact>();
		Contact contact = new Contact();
		Societe client = new Societe();
        String requete = "";

		if( type.equals("AJOUT")){
			requete = "SELECT IdtContact, Nom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays, "
				+ "Commentaire, Auteur, IdtSociete "
				+ "FROM Contact "
                + "WHERE BitAjout = 1 AND BitSup = 0";
		}
		else if( type.equals("MAJ")){
			requete = "SELECT IdtContact, Nom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays, "
				+ "Commentaire, Auteur, IdtSociete "
				+ "FROM Contact "
                + "WHERE BitModif = 1 AND BitAjout = 0";		
		}
		else{
			//données supprimés
			requete = "SELECT IdtContact, Nom, Poste, TelFixe, Fax, TelMobile, Mail, Adresse, CodePostal, Ville, Pays, "
				+ "Commentaire, Auteur, IdtSociete "
				+ "FROM Contact "
                + "WHERE BitSup = 1 AND BitAjout = 0 AND BitModif = 0";
		}

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les contact pour ajouter un objet à la liste.
            if (c.moveToFirst()) {

				contact.setId(c.getInt(c.getColumnIndex("IdtContact")));
				contact.setNom(c.getString(c.getColumnIndex("Nom")));
				contact.setPoste(c.getString(c.getColumnIndex("Poste")));
				contact.setTel_fixe(c.getString(c.getColumnIndex("TelFixe")));
				contact.setTel_mobile(c.getString(c.getColumnIndex("TelMobile")));
				contact.setFax(c.getString(c.getColumnIndex("Fax")));
				contact.setEmail(c.getString(c.getColumnIndex("Mail")));
				contact.setAdresse(c.getString(c.getColumnIndex("Adresse")));
				contact.setCode_postal(c.getString(c.getColumnIndex("CodePostal")));
				contact.setVille(c.getString(c.getColumnIndex("Ville")));
				contact.setPays(c.getString(c.getColumnIndex("Pays")));
				contact.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
				contact.setAuteur(c.getString(c.getColumnIndex("Auteur")));
				
				client.setId(c.getInt(c.getColumnIndex("IdtSociete")));
				contact.setSociete(client);
				
				//On ajoute le contact
				contacts.add(contact);
            }
            c.close();
        }

        return contacts;
	}

	//des bons (devis, commandes)
	public List<Bon> getSyncBons(String type){
		
		List<Bon> bons = new ArrayList<Bon>();
		Bon bon = new Bon();
		
		//client
		Societe client = new Societe();
		//commercial
		Contact commercial = new Contact();
		
        String requete = "";

		if( type.equals("AJOUT")){
			requete = "SELECT IdtBon, DateCommande, EtatCommande, Type, Suivi, Transporteur, Auteur, DateChg, BitChg "
				+ "IdtSociete, IdtContact "
				+ "FROM Bon "
                + "WHERE BitAjout = 1 AND BitSup = 0";
		}
		else if( type.equals("MAJ")){
			requete = "SELECT IdtBon, DateCommande, EtatCommande, Type, Suivi, Transporteur, Auteur, DateChg, BitChg "
				+ "IdtSociete, IdtContact "
				+ "FROM Bon "
                + "WHERE BitModif = 1 AND BitAjout = 0";		
		}
		else{
			//données supprimés
			requete = "SELECT IdtBon, DateCommande, EtatCommande, Type, Suivi, Transporteur, Auteur, DateChg "
				+ "IdtSociete, IdtContact "
				+ "FROM Bon "
                + "WHERE BitSup = 1 AND BitAjout = 0 AND BitModif = 0";
		}

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les bons pour ajouter un objet à la liste.
            if (c.moveToFirst()) {

				bon.setId(c.getInt(c.getColumnIndex("IdtBon")));
				bon.setDate_commande(c.getString(c.getColumnIndex("DateCommande")));
				bon.setEtat_commande(c.getString(c.getColumnIndex("EtatCommande")));
				bon.setType(c.getString(c.getColumnIndex("Type")));
				bon.setSuivi(c.getString(c.getColumnIndex("Suivi")));
				bon.setTransporteur(c.getString(c.getColumnIndex("Transporteur")));
				bon.setAuteur(c.getString(c.getColumnIndex("Auteur")));
				bon.setDate_changement(c.getString(c.getColumnIndex("DateChg")));
				
				client.setId(c.getInt(c.getColumnIndex("IdtSociete")));
				bon.setClient(client);
				commercial.setId(c.getInt(c.getColumnIndex("IdtContact")));
				bon.setCommercial(commercial);
				
				//On ajoute le bon
				bons.add(bon);
            }
            c.close();
        }

        return bons;
	}
	
	//des produits des bons
	public List<Bon> getSyncLignes(String type){
		
		List<LigneCommande> lignes = new ArrayList<LigneCommande>();
		LigneCommande ligne = new LigneCommande();
		Bon bon = new Bon();
		
        String requete = "";

		if( type.equals("AJOUT")){
			requete = "SELECT Idt, Quantite, Code, Nom, Description, PrixUnitaire, Remise, IdtBon "
				+ "FROM LigneCommande "
                + "WHERE BitAjout = 1 AND BitSup = 0";
		}
		else if( type.equals("MAJ")){
			requete = "SELECT Idt, Quantite, Code, Nom, Description, PrixUnitaire, Remise, IdtBon "
				+ "FROM LigneCommande "
                + "WHERE BitModif = 1 AND BitAjout = 0";		
		}
		else{
			//données supprimés
			requete = "SELECT Idt, Quantite, Code, Nom, Description, PrixUnitaire, Remise, IdtBon "
				+ "FROM LigneCommande "
                + "WHERE BitSup = 1 AND BitAjout = 0 AND BitModif = 0";
		}

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les bons pour ajouter un objet à la liste.
            if (c.moveToFirst()) {

				ligne.setId(c.getInt(c.getColumnIndex("Idt")));
				ligne.setQuantite(c.getInt(c.getColumnIndex("Quantite")));
				ligne.setCode(c.getString(c.getColumnIndex("Code")));
				ligne.setNom(c.getString(c.getColumnIndex("Nom")));
				ligne.setDescription(c.getString(c.getColumnIndex("Description")));
				ligne.setPrixUnitaire(c.getDouble(c.getColumnIndex("PrixUnitaire")));
				ligne.setRemise(c.getDouble(c.getColumnIndex("Remise")));
				ligne.setId_bon(c.getInt(c.getColumnIndex("IdtBon")));
				
				//On ajoute une ligne pour un article
				lignes.add(ligne);
            }
            c.close();
        }

        return lignes;
	}
	
	//des événements du calendrier
	public List<Evenement> getSyncEvenements(String type){
		
		List<Evenement> events = new ArrayList<Evenement>();
		Evenement event = new Evenement();

        String requete = "";

		if( type.equals("AJOUT")){
			requete = "SELECT IdtEvent, DateDeb, DateFin, Reccurent, Frequence, Titre, Emplacement, "
				+ "Commentaire, Disponibilite, EstPrive, IdtCompte "
				+ "FROM Evenement "
                + "WHERE BitAjout = 1 AND BitSup = 0";
		}
		else if( type.equals("MAJ")){
			requete = "SELECT IdtEvent, DateDeb, DateFin, Reccurent, Frequence, Titre, Emplacement, "
				+ "Commentaire, Disponibilite, EstPrive, IdtCompte "
				+ "FROM Evenement "
                + "WHERE BitModif = 1 AND BitAjout = 0";		
		}
		else{
			//données supprimés
			requete = "SELECT IdtEvent, DateDeb, DateFin, Reccurent, Frequence, Titre, Emplacement, "
				+ "Commentaire, Disponibilite, EstPrive, IdtCompte "
				+ "FROM Evenement "
                + "WHERE BitSup = 1 AND BitAjout = 0 AND BitModif = 0";
		}

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if( c != null) {
            //On parcours les bons pour ajouter un objet à la liste.
            if (c.moveToFirst()) {

				event.setId(c.getInt(c.getColumnIndex("IdtEvent")));
				event.setDate_debut(c.getString(c.getColumnIndex("DateDeb")));
				event.setDate_fin(c.getString(c.getColumnIndex("DateFin")));
				event.setRecurrent(c.getString(c.getColumnIndex("Reccurent")));
				event.setFrequence(c.getString(c.getColumnIndex("Frequence")));
				event.setTitre(c.getString(c.getColumnIndex("Titre")));
				event.setEmplacement(c.getString(c.getColumnIndex("Emplacement")));
				event.setCommentaire(c.getString(c.getColumnIndex("Commentaire")));
				event.setDisponibilite(c.getString(c.getColumnIndex("Disponibilite")));
				event.setEst_prive(c.getInt(c.getColumnIndex("EstPrive")));
				
				Compte compte = new Compte();
				compte.setId(c.getInt(c.getInt(c.getColumnIndex("IdtCompte"))));
				
				event.setCompte(compte);

				//On ajoute l'événement du calendrier
				events.add(event);
            }
            c.close();
        }

        return events;
	}
		
	//pour vider les tables
	public void tronquerTables(){
		
		//Client
		db.execSQL("TRUNCATE TABLE Societe");
		//Contact
		db.execSQL("TRUNCATE TABLE Contact");
		//Stock
		db.execSQL("TRUNCATE TABLE Stock");
		//Produit
		db.execSQL("TRUNCATE TABLE Compte");
		//Compte
		db.execSQL("TRUNCATE TABLE Produit");
		//Bon
		db.execSQL("TRUNCATE TABLE Bon");
		//Article d'une commande
		db.execSQL("TRUNCATE TABLE LigneCommande");
		//Objectif
		db.execSQL("TRUNCATE TABLE Objectif");		
		//Evenement
		db.execSQL("TRUNCATE TABLE Evenement");
		//Réponses questionnaire
		db.execSQL("TRUNCATE TABLE Reponse");		
		//Questionnaire
		db.execSQL("TRUNCATE TABLE SatisfactionQ");		
		
	}
	
    //Populate tables
    public void chargerTables(SQLiteDatabase db){

        db.execSQL("INSERT INTO Societe (IdtSociete, Nom, Adresse1, Adresse2, CodePostal, Ville, Pays, Type, Commentaire, Auteur, BitAjout, BitModif) VALUES (1, 'PlastProd', '1 rue du comodo', '', '54600', 'Villers-Lès-Nancy', 'France', 'M', 'Société mère', '', 0, 0),	(5, 'Societe1', '25 rue du ponant', 'ZI Fessel', '69000', 'Lyon', 'France', 'C', '', 'Bouvard Laurent', 0, 0),(6, 'Societe2', '16 rue du clos', '', '93600', 'Bondy', 'France', 'C', '', 'Dupond Jean', 0, 0),(7, 'Boite3', '20 avenue du général de Gaulle', '', '78000', 'Versailles', 'France', 'P', '', 'Bouvard Laurent', 0, 0)");

        db.execSQL("INSERT INTO Contact (IdtContact, Nom, Prenom, Poste, TelFixe, TelMobile, Fax, Mail, Adresse, CodePostal, Ville, Pays, Commentaire, Auteur, BitAjout, BitModif, IdtSociete) VALUES (2, 'Bouvard', 'Laurent', 'Commercial', '+33383256594', '+33645986147', NULL, 'laurent.bouvard@plastprod.fr', '1 rue du comodo', '54600', 'Villers-Lès-Nancy', 'France', '', '', 0, 0, 1),(3, 'Convenant', 'Claude', 'Commercial', '+33145328564', '+33610447251', NULL, 'convenant.claude@boite3.fr', '20 avenue du général de Gaulle', '78000', 'Versailles', 'France', '','Bouvard Laurent', 0, 0, 7),	(4, 'Dupond', 'Jean', 'Commercial', '+33383256598', '+33612356898', NULL, 'jean.dupond@plastprod.fr', '1 rue du comodo', '54600', 'Villers-Lès-Nancy', 'France', 'Commentaire', '', 0, 0, 1),	(6, 'Lemoine', 'Alain', 'Commercial', '+33133443002', '+33610447251', NULL, 'alain.lemoine@societe2.fr', '16 rue du clos', '93600', 'Bondy', 'France', '', 'Dupond Jean', 0, 0, 6)");
        db.execSQL("INSERT INTO Contact (IdtContact, Nom, Prenom, Poste, TelFixe, TelMobile, Fax, Mail, Adresse, CodePostal, Ville, Pays, Commentaire, Auteur, BitAjout, BitModif, IdtSociete) VALUES (7, 'Morandi', 'Pierre', 'Directeur technique', '+33445238590', '+33645464724', NULL, 'pierre.morandi@societe1.fr', 'ZI Fessel', '69000', 'Lyon', 'France', '', 'Bouvard Laurent', 0, 0, 5)");

        db.execSQL("INSERT INTO Compte (IdtCompte, Nom, MotDePasse, Mail, Salt, Actif, IdtContact) VALUES (2, 'bouvard.laurent', 'ZQGi8N+qt7Rt0o1Z/4hFodTwaXrj8BIYtj5zCbXMtXg2j5CKpoaoveoKPQodBS1oTs3XC+0bjwGLfj9mHjiX6Q==', 'laurent.bouvard@plastprod.fr', '5703c8599affced67f20c76ff6ec0116', 1, 2),(4, 'dupond.jean', 'xs2y6GqgDMuy1G+jJxelOTeouwIeVwdad1/vUJi3U87fDNfpNiiNkFoLcGmt/pYHIVvjgs0Xb48Fys2zFjaAxQ==', 'jean.dupond@plasprod.fr', '5703c8599affgku67f20c76ff6ec0116', 1, 4),(9, 'super.admin', 'whqoSMIHm68/KSh1L4mvV/aWen4c4VlIQ9RBPYzCAFkDBwtJBgZcraI9at0uzqXyjda7n5LiJn5Nybd9NlP8Iw==', 'admin@plastprod.fr', '0575f5b5602389cf17daf9bbbc5b4e7a', 1, 9)");

        db.execSQL("INSERT INTO Bon (IdtBon, DateCommande ,EtatCommande, Type, Suivi, Transporteur, Auteur, DateChg, BitChg  ,BitAjout, BitModif, IdtSociete, IdtContact) VALUES (1, '2015-03-17 18:59:05', 'Validée', 'CD', NULL, NULL, '', NULL, 0, 0, 0, 5, 4),(2, '2015-03-17 19:38:52', 'Validée', 'CD', NULL, NULL,'', NULL, 0, 0, 0,  7, 2),(3, '2015-03-23 23:38:45', 'Validée', 'CD', NULL, NULL, '', NULL, 0, 0, 0, 5, 2),(4, '2015-05-15 15:25:10', 'En cours de préparation', 'CD', NULL, NULL, '','2015-05-17 16:14:15', 1, 0, 0, 5, 4)");

        db.execSQL("INSERT INTO LigneCommande (Idt, Quantite ,Code, Nom, Description, PrixUnitaire, Remise, IdtBon) VALUES (1, 4, 'RE15208', 'Comodo208', 'Boite à gant granuleux', 58, 0.05, 1),(2, 3, 'PE14542', 'Comodo542', 'Dock prise mobile', 18, 0.06, 1),(3, 4, 'FD13633', 'CommandeClim', 'Bloc commande climatisation', 85, 0, 2),(4, 2, 'PE14542', 'Comodo542', 'Dock prise mobile', 18, 0.02, 2),(5, 15, 'RE15208', 'Comodo208', 'Boite à gant granuleux', 58, 0, 2),(6, 5, 'RE15208', 'Comodo208', 'Boite à gant granuleux', 58, 0.05, 3),(7, 10, 'FD13633', 'CommandeClim', 'Bloc commande climatisation', 85, 0.1, 4),(8, 6, 'PE14542', 'Comodo542', 'Dock prise mobile', 18, 0.05, 4)");

        db.execSQL("INSERT INTO Produit	(IdtProduit ,Nom, Description, Categorie, Code ,Prix, IdtEntree) VALUES	(1, 'Comodo208', 'Boite à gant granuleux', 'Automobile', 'RE15208', 58, 1),(2, 'Comodo542', 'Dock prise mobile', 'Automobile', 'PE14542', 18, 2),(3, 'CommandeClim', 'Bloc commande climatisation', 'Automobile', 'FD13633', 85, 3),(10, 'Commande2', 'Bloc commande2', 'Automobile', 'KJ16233', 57, 4),(11, 'Commande3', 'Bloc commande3', 'Automobile', 'CD25478', 100, 5)");

        db.execSQL("INSERT INTO Stock (IdtEntree ,Quantite, DelaisMoy, Delais) VALUES (1, 10, 6, 0),(2, 5, 4, 0),(3, 2, 10, 0),(4, 1, 10, 2),(5, 4, 2, 0)");

        db.execSQL("INSERT INTO Objectif (IdtObjectif, Annee, Type , Libelle, Valeur, IdtCompte) VALUES	(1, '2015', 'CA', 'Chiffre d''affaire', 85, 2),(2, '2015','CD', 'Commandes', 150, 2),(3, '2015', 'CL', 'Nouveaux clients', 10, 2),(4, '2015','PR', 'Nouveaux prospects', 15, 2),(5, '2015', 'PE', 'Perte de client', 3, 2),(6, '2015','AN', 'Animations', 12, 2),(7, '2015', 'CA', 'Chiffre d''affaire', 90, 4),(8, '2015','CD', 'Commandes', 160, 4),(9, '2015', 'CL', 'Nouveaux clients', 10, 4),(10, '2015','PR', 'Nouveaux prospects', 15, 4),(11, '2015', 'PE', 'Perte de client', 3, 4),(12, '2015','AN', 'Animations', 10, 4)");

        db.execSQL("INSERT INTO Parametre (IdtParam ,Nom, Type, Libelle, Valeur, BitAjout ,BitModif, IdtCompte) VALUES	(1, 'Qauto', 'Booleen', 'Active mode auto', 1, 0, 0, 2),(2, 'Qetape', 'Chaine', 'Envoyer à l''étape', 'Commande validée', 0, 0, 2),(3, 'Qdelais', 'Heure', 'Envoyer après', 48, 0, 0, 2),(4, 'QModele', 'Chaine', 'Questionnaire', 'Version locale', 0, 0, 2),(5, 'Qauto', 'Booleen', 'Active mode auto', 1, 0, 0, 4),(6, 'Qetape', 'Chaine', 'Envoyer à l''étape', 'Terminé', 0, 0, 4),(7, 'Qdelais', 'Heure', 'Envoyer après', 24, 0, 0, 4),(8, 'QModele', 'Chaine', 'Questionnaire', 'Version locale', 0, 0, 4)");

        db.execSQL("INSERT INTO Commentaire	(IdtCommentaire, Texte, IdtSociete) VALUES	(1, 'Commentaire positif', 5),(2, 'Commentaire 2', 5),(3, 'Commentaire négatif', 6),(4, 'Commentaire sympathique', 6),(5, 'Commentaire très court', 7),(6, 'Commentaire 3', 7)");

        db.execSQL("INSERT INTO CorrespCouleur (IdtLigne, Nom, Couleur) VALUES (1, 'Bouvard Laurent', '#ff9e0e40' ),(2, 'Dupond Jean', '#ff00ff00'),(3,'','ffff0000'),(4, '', 'ffffff00'),(5,'','ff77b5fe'),(6,'','ffff00ff'),(7,'','ff87e990'),(8,'','ffc72c48'),(9,'','ffffd700'),(10,'','ff0f056b'),(11,'','ff9683ec'),(12,'','ff54f98d'),(13,'','ff6d071a'),(14,'','ff73c2fb'),(15,'','ff791cf8')");
    }
}