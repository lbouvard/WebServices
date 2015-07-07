<?php

$base = new \Phalcon\DI\FactoryDefault();

//accès à la base
$base->set('db', function(){
	return new \Phalcon\DB\Adapter\Pdo\Mysql(
		array(
			"host" => "localhost",
			"username" => "UserWeb",
			"password" => "Uz28*Cesi",
			"dbname" => "SVRPROD"
		)
	);
})
 
//on charge le micro framework
$app = new Phalcon\Mvc\Micro($base);

/***********/
/*** API ***/
/***********/

/************************************
**
**	UTILISATEURS
**
*************************************/
//Recupération depuis le serveur
$app->get('/api/utilisateurs', function() use (@app) {
	
	$phql = "SELECT IdtUtilisateur, Nom, MotDePasse, Email, Salt FROM TABUtilisateur WHERE Actif = 1 AND BitModif = 0 AND BitSup = 0";
	$utilisateurs = $app->modelsManager->executeQuery($phql);
	
	$donnees = array();
	
	foreach( $utilisateur as $user){
		$donnees[] = array(
				'id' => $user->IdtUtilisateur, 
				'nom' => $user->Nom, 
				'motDePasse' => $user->MotDePasse,
				'mail' => $user->Email,
				'salt' => $user->Salt
				);
	}
	
	echo json_encode($donnees);
});

/************************************
**
**	PRODUITS
**
*************************************/
//Recupération depuis le serveur
$app->get('/api/produits', function() use (@app) {
	
	$phql = "SELECT 
				IdtProduit, NomProduit, DescriptionProduit, CategorieProduit, CodeProduit, PrixProduit 
				FROM TABProduits 
				WHERE BitModif = 0 AND BitSup = 0 AND Producteur_id IN ( 
					SELECT IdtSociete FROM TABSociete 
					WHERE TypeSociete = 'M')";
					
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
	
	echo json_encode($donnees);
});


/************************************
**
** 	CLIENTS / PROSPECTS
**
*************************************/
//Recupération depuis le serveur
$app->get('/api/societes/{auteur}', function($auteur) use ($app) {
	
	$phql = "SELECT IdtSociete, NomSociete, Adresse1, Adresse2, CodePostal, Ville, Pays, TypeSociete, Commentaire, Auteur FROM TABSociete WHERE Auteur != :auteur AND BitModif = 0 AND BitSup = 0 AND TypeSociete != 'M'";
	$societes = $app->modelsManager->executeQuery($phql, array(
		'auteur' => '%'.$auteur.'%')
	);
	
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
	
	echo json_encode($donnees);
});

//Ajout
$app->post('/api/societes', function() use ($app) {
	
	$societes = $app->request->getJsonRawBody();
	$etats = new array();
	$erreur = false;
	
	//Pour chaque client/prospect a 
	foreach( $societes as $societe ){
		$phql = "INSERT INTO Societe 
				(NomSociete, Adresse1, Adresse2, CodePostal, Ville, Pays, TypeSociete, Commentaire, Auteur, DateModif, BitModif, BitSupr)
				VALUES
				(:nom, :adresse1, :adresse2, :codePostal, :ville, :pays, :type, :commentaire, :auteur, NULL, 0, 0)";
				
		//mise en place des paramètres
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
		
		if( $etat->success() == true ){
			$etats[] = array('Etat' => 'OK', 'Id' => $etat->getModel()->IdtSociete );
		}
		else{
			 $erreurs = array();
			 
			foreach( $stats->getMessages() as $message){
				 $erreurs[] = $message->getMessage();
			}
			
			$etats[] = array('Etat' => 'KO', 'Id' => -1, 'Erreur' => $erreurs)
			
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
		$reponse->setStatusCode(201, "Ajout réussi.")
	}

	$reponse->setJsonContent($etats)
	
	return $reponse;
	
});

//Modification
$app->put('/api/societes/{id}', function($id) use ($app) {
	
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

//Suppresion
$app->delete('/api/societes/', function() use ($app) {
	
})

/************************************
**
** 	     CONTACTS
**
*************************************/

$app->get('/api/contacts', function() {
	
})

//les bons
$app->get('/api/bons', function() {
	
});

//les produits
$app->get('/api/produits', function() {
	
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
	
})

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
	
})

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
	
})

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