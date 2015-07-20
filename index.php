<?php

//Register autoloader
$loader = new \Phalcon\Loader();

$loader->registerDirs(array(
	__DIR__. '/models/'
))->register();

$base = new \Phalcon\DI\FactoryDefault();

//accès à la base
$base->set('db', function(){
	return new \Phalcon\Db\Adapter\Pdo\Mysql(
		array(
			"host" => "localhost",
			"username" => "UserWeb",
			"password" => "Uz28*Cesi",
			"dbname" => "svrprod",
			'charset' => 'utf8'
		)
	);
});
 
//on charge le micro framework
$app = new \Phalcon\Mvc\Micro($base);

/***********/
/*** API ***/
/***********/

/************************************
**
**	UTILISATEURS
**
*************************************/
//Recupération depuis le serveur
$app->get('/api/utilisateurs', function() use ($app) {
	
	$phql = "SELECT user.IdtUtilisateur, user.Nom, user.MotDePasse, user.Email, user.Salt 
			FROM tabsociete soc
			INNER JOIN tabcontact con ON soc.IdtSociete = con.Societe_id
			INNER JOIN tabutilisateur user ON con.Utilisateur_id = user.IdtUtilisateur
			WHERE soc.TypeSociete = 'M' AND user.Actif = 1 AND user.BitModif = 0 AND user.BitSup = 0";
	$utilisateurs = $app->modelsManager->executeQuery($phql);
	
	$donnees = array();
	
	foreach( $utilisateurs as $user){
		$donnees[] = array(
				'id' => $user->IdtUtilisateur, 
				'nom' => $user->Nom, 
				'motDePasse' => $user->MotDePasse,
				'mail' => $user->Email,
				'salt' => $user->Salt
				);
	}
	
	echo json_encode($donnees, JSON_UNESCAPED_UNICODE);
});

/************************************
**
**	PRODUITS
**
*************************************/
//Recupération depuis le serveur
$app->get('/api/produits', function() use ($app) {
	
	$phql = "SELECT prod.IdtProduit, prod.NomProduit, prod.DescriptionProduit, prod.CategorieProduit, prod.CodeProduit, prod.PrixProduit 
			FROM tabproduits prod
			INNER JOIN tabsociete soc ON prod.Producteur_id = soc.IdtSociete
			WHERE prod.BitModif = 0 AND prod.BitSup = 0 AND soc.TypeSociete = 'M'";	
	$produits = $app->modelsManager->executeQuery($phql);

	$donnees = array();
	
	foreach( $produits as $article){
		$donnees[] = array(
				'id' => $article->IdtProduit, 
				'nom' => $article->NomProduit, 
				'description' => $article->DescriptionProduit,
				'categorie' => $article->CategorieProduit,
				'code' => $article->CodeProduit,
				'prix' => $article->PrixProduit
				);
	}
	
	echo json_encode($donnees, JSON_UNESCAPED_UNICODE);
});

/************************************
**
** 	CLIENTS / PROSPECTS
**
*************************************/
//Recupération depuis le serveur
$app->get('/api/societes', function() use ($app) {
	
	$phql = "SELECT IdtSociete, NomSociete, Adresse1, Adresse2, CodePostal, Ville, Pays, TypeSociete, Commentaire, Auteur 
			FROM tabsociete 
			WHERE BitModif = 0 AND BitSup = 0 AND TypeSociete != 'M' AND TypeSociete != 'F'";
	$societes = $app->modelsManager->executeQuery($phql);
	
	$donnees = array();
	
	foreach( $societes as $societe){
		$donnees[] = array(
				'id' => $societe->IdtSociete, 
				'nom' => $societe->NomSociete, 
				'adresse1' => $societe->Adresse1,
				'adresse2' => $societe->Adresse2,
				'codePostal' => $societe->CodePostal,
				'ville' => $societe->Ville,
				'pays' => $societe->Pays,
				'type' => $societe->TypeSociete,
				'commentaire' => $societe->Commentaire,
				'auteur' => $societe->Auteur
				);
	}
	
	echo json_encode($donnees, JSON_UNESCAPED_UNICODE);
	
	/*switch (json_last_error()) {
        case JSON_ERROR_NONE:
            echo ' - Aucune erreur';
        break;
        case JSON_ERROR_DEPTH:
            echo ' - Profondeur maximale atteinte';
        break;
        case JSON_ERROR_STATE_MISMATCH:
            echo ' - Inadéquation des modes ou underflow';
        break;
        case JSON_ERROR_CTRL_CHAR:
            echo ' - Erreur lors du contrôle des caractères';
        break;
        case JSON_ERROR_SYNTAX:
            echo ' - Erreur de syntaxe ; JSON malformé';
        break;
        case JSON_ERROR_UTF8:
            echo ' - Caractères UTF-8 malformés, probablement une erreur d\'encodage';
        break;
        default:
            echo ' - Erreur inconnue';
        break;
    }*/
});

//Ajout
$app->post('/api/societes/ajt', function() use ($app) {
	
	$societes = $app->request->getJsonRawBody();
	$etats = array();
	$erreur = false;
	
	var_dump($societes);
	exit(0);
	
	//Pour chaque client/prospect
	foreach( $societes as $societe ){
		$phql = "INSERT INTO tabsociete 
				(NomSociete, Adresse1, Adresse2, CodePostal, Ville, Pays, TypeSociete, Commentaire, Auteur, DateModif, BitModif, BitSup)
				VALUES
				(:nom:, :adresse1:, :adresse2:, :codePostal:, :ville:, :pays:, :type:, :commentaire:, :auteur:, NULL, 0, 0)";
				
		//mise en place des paramètres
		$etat = $app->modelsManager->executeQuery($phql, array(
			'nom' => $societe->nom,
			'adresse1' => $societe->adresse1,
			'adresse2' => $societe->adresse2,
			'codePostal' => $societe->codePostal,
			'ville' => $societe->ville,
			'pays' => $societe->pays,
			'type' => $societe->type,
			'commentaire' => $societe->commentaire,
			'auteur' => $societe->auteur
		));
		
		if( $etat->success() == true ){
			$etats[] = array('Etat' => 'OK', 'Id' => $etat->getModel()->IdtSociete );
		}
		else{
			$erreurs = array();
			 
			foreach( $stats->getMessages() as $message){
				 $erreurs[] = $message->getMessage();
			}
			
			$etats[] = array('Etat' => 'KO', 'Id' => -1, 'Erreur' => $erreurs);
			
			$erreur = true;
			
			//on sort de la boucle
			break;
		}
	}
			
	//Réponse HTTP
	$reponse = new Phalcon\Http\Response();
	
	if( $erreur ){
		$reponse->setStatusCode(460, "Echec insertion.");
	}
	else{
		$reponse->setStatusCode(201, "Ajout réussi.");
	}

	$reponse->setJsonContent($etats);
	
	return $reponse;
	
});

//Modification
$app->post('/api/societes/maj', function() use ($app) {
	
	$societes = $app->request->getJsonRawBody();

	foreach($societes as $societe){
		
		//on sauvegarde les valeurs en cours
		$phql = "SELECT NomSociete, Adresse1, Adresse2, CodePostal, Ville, Pays, TypeSociete, Commentaire, Auteur FROM Societe WHERE IdtSociete = ".$id;
		$sauvegarde = $app->modelsManager->executeQuery($phsql);
		
		$phql = "INSERT INTO Societe 
		(NomSociete, Adresse1, Adresse2, CodePostal, Ville, Pays, TypeSociete, Commentaire, Auteur, DateModif, BitModif, BitSupr)
		VALUES
		(:nom, :adresse1, :adresse2, :codePostal, :ville, :pays, :type, :commentaire, :auteur, NOW(), 1, 0)";
				
		$etat = $app->modelsManager->executeQuery($phql, array(
			'nom' => $sauvegarde->NomSociete,
			'adresse1' => $sauvegarde->Adresse1,
			'adresse2' => $sauvegarde->Adresse2,
			'codePostal' => $sauvegarde->CodePostal,
			'ville' => $sauvegarde->Ville,
			'pays' => $sauvegarde->Pays,
			'type' => $sauvegarde->TypeSociete,
			'commentaire' => $sauvegarde->Commentaire,
			'auteur' => $sauvegarde->Auteur
		));		
		
		
		//Mise à jour
		$phql = "UPDATE Societe SET Nom = :nom, Adresse1 = :adresse1, Adresse2 = :adresse2, CodePostal = :codePostal, 
									Ville = :ville, Pays = :pays, TypeSociete = : type, Commentaire = commentaire";
									
		$etat = $app->modelsManager->executeQuery($phql, array(
			'nom' => $societe->NomSociete,
			'adresse1' => $societe->Adresse1,
			'adresse2' => $societe->Adresse2,
			'codePostal' => $societe->CodePostal,
			'ville' => $societe->Ville,
			'pays' => $societe->Pays,
			'type' => $societe->TypeSociete,
			'commentaire' => $societe->Commentaire,
			'auteur' => $societe->Auteur
		));
	}
	
});

/************************************
**
** 	     CONTACTS
**
*************************************/

$app->get('/api/contacts', function() use ($app) {
	
	$phql = " SELECT con.IdtContact, con.NomContact, con.PrenomContact, con.IntitulePoste, 
			con.TelFixe, con.TelMobile, con.Fax, con.Email, con.Adresse, con.CodePostal, con.Ville, con.Pays, 
			con.Commentaire, con.Auteur, con.DateModif, con.BitModif, con.BitSup, con.Societe_id, con.Utilisateur_id
			FROM tabcontact con
			INNER JOIN tabsociete soc ON con.Societe_id = soc.IdtSociete
			WHERE con.BitModif = 0 AND con.BitSup = 0 AND soc.TypeSociete != 'F' AND soc.TypeSociete != 'M'";
			
	$contacts = $app->modelsManager->executeQuery($phql);
	
	$donnees = array();
	
	foreach( $contacts as $contact){
		$donnees[] = array(
				'id' => $contact->IdtContact, 
				'nom' => $contact->NomContact,
				'prenom' => $contact->PrenomContact,
				'poste' => $contact->IntitulePoste,
				'tel_fixe' => $contact->TelFixe,
				'tel_mobile' => $contact->TelMobile,
				'fax' => $contact->Fax,
				'email' => $contact->Email,
				'adresse' => $contact->Adresse,
				'codePostal' => $contact->CodePostal,
				'ville' => $contact->Ville,
				'pays' => $contact->Pays,
				'commentaire' => $contact->Commentaire,
				'auteur' => $contact->Auteur,
				'idt_societe' => $contact->Societe_id
				);
	}
	
	echo json_encode($donnees, JSON_UNESCAPED_UNICODE);
});

$app->post('/api/contacts/ajt', function() use ($app) {
	
	$contacts = $app->request->getJsonRawBody();
	$etats = array();
	$erreur = false;
	
	//Pour chaque contact
	foreach( $contacts as $contact){
		
		var_dump($contact);
		
		$phql = "INSERT INTO tabcontact 
				(NomContact, PrenomContact, IntitulePoste, TelFixe, TelMobile, Fax, Email, Adresse, CodePostal, Ville, Pays, Commentaire, Auteur, DateModif, BitModif, BitSup, Societe_id, Utilisateur_id)
				VALUES
				(:nom, :prenom, :poste, :fixe, :mobile, :fax, :mail, :adresse, :codePostal, :ville, :pays, :commentaire, :auteur, NULL, 0, 0, :societe, -1)";
				
		//mise en place des paramètres
		$etat = $app->modelsManager->executeQuery($phql, array(
			'nom' => $contact->nom,
			'prenom' => $contact->prenom,
			'poste' => $contact->poste,
			'fixe' => $contact->fixe,
			'mobile' => $contact->mobile,
			'fax' => $contact->fax,
			'mail' => $contact->mail,
			'adresse' => $contact->adresse,
			'codePostal' => $contact->codePostal,
			'ville' => $contact->ville,
			'pays' => $contact->pays,
			'commentaire' => $contact->commentaire,
			'auteur' => $contact->auteur,
			'societe' => $contact->societe,
		));
		/*
		if( $etat->success() == true ){
			$etats[] = array('Etat' => 'OK', 'Id' => $etat->getModel()->IdtContact);
		}
		else{
			$erreurs = array();
				
			foreach( $stats->getMessages() as $message){
				 $erreurs[] = $message->getMessage();
			}
			
			$etats[] = array('Etat' => 'KO', 'Id' => -1, 'Erreur' => $erreurs);
			
			$erreur = true;
			
			//on sort de la boucle
			break;
		}		*/
	}
	
	$reponse = new Phalcon\Http\Response();
	
	if( $erreur ){
		$reponse->setStatusCode(460, "Echec insertion.");
	}
	else{
		$reponse->setStatusCode(201, "Ajout réussi.");
	}
	
	$reponse->setJsonContent($etats);
	
	return $reponse;
});


//les bons
$app->get('/api/bons', function() {
	
});

//Evenements
$app->get('/api/evenements', function() {
	
});

//Paramètres
$app->get('/api/parametres', function() {
	
});

//Objectifs
$app->get('/api/objectifs', function() {
	
});

//Données de satisfactions
$app->get('/api/satisfactions', function() {
	
});

/* AJOUT depuis l'application Android */

//les sociétés
$app->post('/api/societes', function() {
	
});

//les contacts
$app->post('/api/contacts', function() {
	
});

//les bons
$app->post('/api/bons', function() {
	
});

//Evenements
$app->post('/api/evenements', function() {
	
});

//Paramètres
$app->post('/api/parametres', function() {
	
});

/* MODIFICATION depuis l'application Android */

//les sociétés
$app->put('/api/societes', function() {
	
});

//les contacts
$app->put('/api/contacts', function() {
	
});

//les bons
$app->put('/api/bons', function() {
	
});

//Paramètres
$app->put('/api/parametres', function() {
	
});

/* SUPPRESION depuis l'application Android */

//les sociétés
$app->post('/api/societes', function() {
	
});

//les contacts
$app->post('/api/contacts', function() {
	
});

//les bons
$app->post('/api/bons', function() {
	
});

//Evenements
$app->post('/api/evenements', function() {
	
});

//Paramètres
$app->post('/api/parametres', function() {
	
});

$app->handle();

?>