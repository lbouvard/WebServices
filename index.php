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
			WHERE BitModif = 0 AND BitSup = 0 AND TypeSociete != 'M' AND TypeSociete != 'F' 
			ORDER BY IdtSociete";
	$societes = $app->modelsManager->executeQuery($phql);
	
	$donnees = array();
	
	if( $societes != false ){
		
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
	}
	
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
	
	//Pour chaque client/prospect
	foreach( $societes as $societe ){
		
		//on verifie que le client n'existe pas déjà
		$phql = "SELECT NomSociete nom, Adresse1 adresse1, Adresse2 adresse2, CodePostal codePostal, Ville ville, Pays pays, 
					TypeSociete type, Commentaire commentaire, Auteur auteur FROM tabsociete 
				 WHERE NomSociete = :nom: AND BitModif = 0 AND BitSup = 0 AND TypeSociete != 'M' AND TypeSociete != 'F'";
		$client = $app->modelsManager->executeQuery($phql, array(
			'nom' => $societe->nom
		))->getFirst();
		
		if( $client != false ){
			$message = sprintf("Le client de nom %s existe déjà. Données : %s|%s|%s|%s|%s|%s|%s|%s", $client->nom,
									$client->adresse1, $client->adresse2, $client->codePostal, $client->ville, 
									$client->pays, $client->type, $client->commentaire, $client->auteur);
			error_log($message, 0);
			continue;
		}
		
		//insertion en base
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
			$etats[] = array('Etat' => 'OK', 'NewId' => $etat->getModel()->IdtSociete, 'OldId' => $societe->id );
		}
		else{
			$erreurs = array();
			 
			foreach( $stats->getMessages() as $message){
				 $erreurs[] = $message->getMessage();
			}
			
			$etats[] = array('Etat' => 'KO', 'Id' => -1, 'Erreur' => $erreurs);
			
			$erreur = true;
			
			//on sort de la boucle
			//break;
		}
	}
			
	//Réponse HTTP
	$reponse = new Phalcon\Http\Response();
	
	/*if( $erreur ){
		$reponse->setStatusCode(460, "Echec insertion.");
	}
	else{*/
		$reponse->setStatusCode(200, "Ajout réussi.");
	//}

	$reponse->setJsonContent($etats);
	
	return $reponse;
	
});

//Modification
$app->post('/api/societes/maj', function() use ($app) {
	
	$societes = $app->request->getJsonRawBody();

	foreach($societes as $societe){
		
		if( !$societe->ASupprimer ){
			//on sauvegarde les valeurs en cours
			$phql = "SELECT NomSociete nom, Adresse1 adresse1, Adresse2 adresse2, CodePostal codePostal, Ville ville, 
					Pays pays, TypeSociete type, Commentaire commentaire, Auteur auteur FROM tabsociete WHERE IdtSociete = :id:";
			$sauvegarde = $app->modelsManager->executeQuery($phql, array(
				'id' => $societe->id
			))->getFirst();
			
			error_log(var_export($sauvegarde, true), 0);
			
			$phql = "INSERT INTO tabsociete 
			(NomSociete, Adresse1, Adresse2, CodePostal, Ville, Pays, TypeSociete, Commentaire, Auteur, DateModif, BitModif, BitSup)
			VALUES
			(:nom:, :adresse1:, :adresse2:, :codePostal:, :ville:, :pays:, :type:, :commentaire:, :auteur:, NOW(), 1, 0)";
					
			$etat = $app->modelsManager->executeQuery($phql, array(
				'nom' => $sauvegarde->nom,
				'adresse1' => $sauvegarde->adresse1,
				'adresse2' => $sauvegarde->adresse2,
				'codePostal' => $sauvegarde->codePostal,
				'ville' => $sauvegarde->ville,
				'pays' => $sauvegarde->pays,
				'type' => $sauvegarde->type,
				'commentaire' => $sauvegarde->commentaire,
				'auteur' => $sauvegarde->auteur
			));		
		}
		
		//SUPPRESION
		if( $societe->ASupprimer ){
			$phql = "UPDATE tabsociete SET BitSup = 1, DateModif = NOW() 
						WHERE IdtSociete = :id:";	

			$etat = $app->modelsManager->executeQuery($phql, array(
			'id' => $societe->id
			));				
		}
		else{
			//MAJ
			$phql = "UPDATE tabsociete SET NomSociete = :nom:, Adresse1 = :adresse1:, Adresse2 = :adresse2:, CodePostal = :codePostal:, 
									Ville = :ville:, Pays = :pays:, TypeSociete = :type:, Commentaire = :commentaire:, Auteur = :auteur: 
					 WHERE IdtSociete = :id:";
				
			$etat = $app->modelsManager->executeQuery($phql, array(
				'nom' => $societe->nom,
				'adresse1' => $societe->adresse1,
				'adresse2' => $societe->adresse2,
				'codePostal' => $societe->codePostal,
				'ville' => $societe->ville,
				'pays' => $societe->pays,
				'type' => $societe->type,
				'commentaire' => $societe->commentaire,
				'auteur' => $societe->auteur,
				'id' => $societe->id
			));
		}
	}
});

/************************************
**
** 	     CONTACTS
**
*************************************/
$app->get('/api/contacts', function() use ($app) {
	
	$phql = "SELECT con.IdtContact, con.NomContact, con.PrenomContact, con.IntitulePoste, 
			con.TelFixe, con.TelMobile, con.Fax, con.Email, con.Adresse, con.CodePostal, con.Ville, con.Pays, 
			con.Commentaire, con.Auteur, con.DateModif, con.BitModif, con.BitSup, con.Societe_id, con.Utilisateur_id
			FROM tabcontact con
			INNER JOIN tabsociete soc ON con.Societe_id = soc.IdtSociete
			WHERE con.BitModif = 0 AND con.BitSup = 0 AND soc.TypeSociete != 'F' 
			ORDER BY IdtContact";
			
			/* AND soc.TypeSociete != 'M' */
			
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
				'code_postal' => $contact->CodePostal,
				'ville' => $contact->Ville,
				'pays' => $contact->Pays,
				'commentaire' => $contact->Commentaire,
				'auteur' => $contact->Auteur,
				'id_societe' => $contact->Societe_id
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
		
				//on verifie que le client n'existe pas déjà
		$phql = "SELECT con.IdtContact, con.NomContact, con.PrenomContact, con.IntitulePoste, 
			con.TelFixe, con.TelMobile, con.Fax, con.Email, con.Adresse, con.CodePostal, con.Ville, con.Pays, 
			con.Commentaire, con.Auteur, con.DateModif, con.BitModif, con.BitSup, con.Societe_id, con.Utilisateur_id
			FROM tabcontact con
			INNER JOIN tabsociete soc ON con.Societe_id = soc.IdtSociete
			WHERE con.NomContact = :nom: AND con.PrenomContact = :prenom: AND con.BitModif = 0 AND con.BitSup = 0 AND soc.TypeSociete != 'F' AND soc.TypeSociete != 'M'";
			
		$existe = $app->modelsManager->executeQuery($phql, array(
			'nom' => $contact->nom,
			'prenom' => $contact->prenom
		))->getFirst();
		
		if( $existe != false ){
			$message = sprintf("Le contact (nom %s, prénom %s) existe déjà. Données : %s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s", $existe->NomContact, $existe->PrenomContact,
									$existe->IntitulePoste, $existe->Adresse, $existe->CodePostal, $existe->Ville, 
									$existe->Pays, $existe->Commentaire, $existe->Auteur, $existe->TelFixe, $existe->TelMobile, $existe->Fax, $existe->Email);
			error_log($message, 0);
			continue;
		}
		
		//on crée déjà un compte utilisateur
		//salt
		$salt = md5(uniqid(null, true));
		
		$phql = "INSERT INTO tabutilisateur 
				(Nom, MotDePasse, Email, Salt, DateModif, BitModif, BitSup, Actif)
				VALUES 
				(:nom:, :mdp:, :mail:, :salt:, NULL, 0, 0, 0)";
		$utilisateur = $app->modelsManager->executeQuery($phql, array(
			'nom' => $contact->nom.".".$contact->prenom,
			'mdp' => '-',
			'mail' => $contact->email,
			'salt' => $salt
		));
		
		if( $utilisateur->success() == true ){
			$user_id = $utilisateur->getModel()->IdtUtilisateur;
		}

		//on ajoute le contact
		$phql = "INSERT INTO tabcontact 
				(NomContact, PrenomContact, IntitulePoste, TelFixe, TelMobile, Fax, Email, Adresse, CodePostal, Ville, Pays, Commentaire, Auteur, DateModif, BitModif, BitSup, Societe_id, Utilisateur_id)
				VALUES
				(:nom:, :prenom:, :poste:, :fixe:, :mobile:, :fax:, :mail:, :adresse:, :codePostal:, :ville:, :pays:, :commentaire:, :auteur:, NULL, 0, 0, :societeid:, :userid:)";
				
		//mise en place des paramètres
		$etat = $app->modelsManager->executeQuery($phql, array(
			'nom' => $contact->nom,
			'prenom' => $contact->prenom,
			'poste' => $contact->poste,
			'fixe' => $contact->tel_fixe,
			'mobile' => $contact->tel_mobile,
			'fax' => $contact->fax,
			'mail' => $contact->email,
			'adresse' => $contact->adresse,
			'codePostal' => $contact->code_postal,
			'ville' => $contact->ville,
			'pays' => $contact->pays,
			'commentaire' => $contact->commentaire,
			'auteur' => $contact->auteur,
			'societeid' => $contact->id_societe,
			'userid' => $user_id
		));
		
		if( $etat->success() == true ){
			$etats[] = array('Etat' => 'OK', 'NewId' => $etat->getModel()->IdtContact, 'OldId' => $contact->id );
		}
		else{
			$erreurs = array();
				
			foreach( $etats->getMessages() as $message){
				 $erreurs[] = $message->getMessage();
			}
			
			$etats[] = array('Etat' => 'KO', 'Id' => -1, 'Erreur' => $erreurs);
			
			$erreur = true;
			
			//on sort de la boucle
			//break;
		}		
	}
	
	$reponse = new Phalcon\Http\Response();
	
	/*if( $erreur ){
		$reponse->setStatusCode(460, "Echec insertion.");
	}
	else{*/
		$reponse->setStatusCode(201, "Ajout réussi.");
	/*}*/
	
	$reponse->setJsonContent($etats);
	
	return $reponse;
});

$app->post('/api/contacts/maj', function() use ($app) {
	
	$contacts = $app->request->getJsonRawBody();

	foreach($contacts as $contact){
		
		if( !$contact->ASupprimer ){
			//on sauvegarde les valeurs en cours
			$phql = "SELECT NomContact, PrenomContact, IntitulePoste, 
						TelFixe, TelMobile, Fax, Email, Adresse, CodePostal, Ville, Pays, 
						Commentaire, Auteur, Societe_id, Utilisateur_id
					FROM tabcontact WHERE IdtContact = :id:";
					
			$sauvegarde = $app->modelsManager->executeQuery($phql, array(
				'id' => $contact->id
			))->getFirst();
			
			$phql = "INSERT INTO tabcontact 
			(NomContact, PrenomContact, IntitulePoste, TelFixe, TelMobile, Fax, Email, Adresse, CodePostal, Ville, Pays, Commentaire, Auteur, DateModif, BitModif, BitSup, Societe_id, Utilisateur_id)
			VALUES
			(:nom:, :prenom:, :intitule:, :fixe:, :mobile:, :fax:, :mail:, :adresse:, :code_postal:, :ville:, :pays:, :commentaire:, :auteur:, NOW(), 1, 0, :societe_id:, :utilisateur_id:)";
			
			$etat = $app->modelsManager->executeQuery($phql, array(
				'nom' => $sauvegarde->NomContact,
				'prenom' => $sauvegarde->PrenomContact,
				'intitule' => $sauvegarde->IntitulePoste,
				'fixe' => $sauvegarde->TelFixe,
				'mobile' => $sauvegarde->TelMobile,
				'fax' => $sauvegarde->Fax,
				'mail' => $sauvegarde->Email,
				'adresse' => $sauvegarde->Adresse,
				'code_postal' => $sauvegarde->CodePostal,
				'ville' => $sauvegarde->Ville,
				'pays' => $sauvegarde->Pays,
				'commentaire' => $sauvegarde->Commentaire,
				'auteur' => $sauvegarde->Auteur,
				'societe_id'=> $sauvegarde->Societe_id,
				'utilisateur_id' => $sauvegarde->Utilisateur_id
			));

			if( $etat->success() != true ){
				foreach( $etat->getMessages() as $message){
					 error_log($message->getMessage(), 0);
				}	
			}			
		}
		
		//SUPPRESION
		if( $contact->ASupprimer ){
			$phql = "UPDATE tabcontact SET BitSup = 1, DateModif = NOW() WHERE IdtContact = :id:";	

			$etat = $app->modelsManager->executeQuery($phql, array(
				'id' => $contact->id
			));
			
			if( $etat->success() != true ){
				foreach( $etat->getMessages() as $message){
					 error_log($message->getMessage(), 0);
				}	
			}			

			//on désactive le compte utilisateur lié
			$phql = "SELECT Utilisateur_id FROM tabcontact WHERE IdtContact = :id:";
			
			$id_user = $app->modelsManager->executeQuery($phql, array(
				'id' => $contact->id
			))->getFirst();
			
			if( $id_user != false ){
			
				$phql = "UPDATE tabutilisateur SET Actif = 0 WHERE IdtUtilisateur = :id:";
				$etat = $app->modelsManager->executeQuery($phql, array(
					'id' => $id_user->Utilisateur_id
				));
			
				if( $etat->success() != true ){
					foreach( $etat->getMessages() as $message){
						 error_log($message->getMessage(), 0);
					}	
				}
			}
			else{
				foreach( $etat->getMessages() as $message){
					error_log($message->getMessage(), 0);
				}
			}
			
		}
		else{
			//MAJ
			$phql = "UPDATE tabcontact SET NomContact = :nom:, PrenomContact = :prenom:, IntitulePoste = :intitule:, TelFixe = :fixe:, TelMobile = :mobile:, 
							Fax = :fax:, Email = :mail:, Adresse = :adresse:, CodePostal = :codePostal:, Ville = :ville:, Pays = :pays:, Commentaire = :commentaire:, 
							Auteur = :auteur: 
							WHERE IdtContact = :id:";
			
			$etat = $app->modelsManager->executeQuery($phql, array(
				'nom' => $contact->nom,
				'prenom' => $contact->prenom,
				'intitule' => $contact->poste,
				'fixe' => $contact->tel_fixe,
				'mobile' => $contact->tel_mobile,
				'fax' => $contact->fax,
				'mail' => $contact->email,
				'adresse' => $contact->adresse,
				'codePostal' => $contact->code_postal,
				'ville' => $contact->ville,
				'pays' => $contact->pays,
				'commentaire' => $contact->commentaire,
				'auteur' => $contact->auteur,
				'id' => $contact->id
			));
			
			if( $etat->success() != true ){
				foreach( $etat->getMessages() as $message){
					 error_log($message->getMessage(), 0);
				}	
			}
		}
	}	
});

/************************************
**
** 	     BONS
**
*************************************/
//les bons
$app->get('/api/bons', function() use ($app) {

	$phql = "SELECT IdtCommande, DateCommande, EtatCommande, Commentaire, DateChg, Auteur, Contact_id, Societe_id
			FROM tabcommande 
			WHERE BitSup = 0";
	$bons = $app->modelsManager->executeQuery($phql);
	
	$donnees = array();
	
	foreach( $bons as $bon){
		$donnees[] = array(
			'id' => $bon->IdtCommande, 
			'date' => $bon->DateCommande, 
			'etat' => $bon->EtatCommande,
			'commentaire' => $bon->Commentaire,
			'datechg' => $bon->DateChg,
			'auteur' => $bon->Auteur,
			'contact_id' => $bon->Contact_id,
			'societe_id' => $bon->Societe_id
		);
	}
	
	echo json_encode($donnees, JSON_UNESCAPED_UNICODE);
	
});
//les articles d'un bon
$app->get('/api/articles', function() use ($app) {
	
	$phql = "SELECT Idt, CodeProduit, NomProduit, DescriptionProduit, Quantite, PrixProduit, PrixTotal, commande_id 
			FROM tabcommandeproduits";
	$articles = $app->modelsManager->executeQuery($phql);
	
	$donnees = array();
	
	foreach( $articles as $article){
		$donnees[] = array(
			'id' => $article->Idt, 
			'code' => $article->CodeProduit, 
			'nom' => $article->NomProduit,
			'description' => $article->DescriptionProduit,
			'quantite' => $article->Quantite,
			'prix' => $article->PrixProduit,
			'prix_total' => $article->PrixTotal,
			'commande_id' => $article->commande_id
		);
	}
	
	echo json_encode($donnees, JSON_UNESCAPED_UNICODE);
});

$app->post('/api/bons/ajt', function() use ($app) {
	
});

$app->post('/api/bons/maj', function() use ($app) {
	
});

$app->post('/api/articles/ajt', function() use ($app) {
	
});

$app->get('/api/articles/maj', function() use ($app) {
	
});

/************************************
**
** 	     EVENEMENTS
**
*************************************/
$app->get('/api/evenements/{id:[0-9]+}', function($id) use ($app) {
	
	$phql = "SELECT IdtEvent, DateDeb, DateFin, Recurrent, Frequence, Titre, Emplacement, Commentaire, Disponibilite, EstPrive, IdtCompte 
			FROM tabevenement 
			WHERE BitSup = 0 AND BitModif = 0 AND IdtCompte = :id:";
			
	$events = $app->modelsManager->executeQuery($phql, array(
		'id' => $id
	));
	
	$donnees = array();
	
	foreach( $events as $event){
		$donnees[] = array(
			'id' => $event->IdtEvent, 
			'ddeb' => $event->DateDeb, 
			'dfin' => $event->DateFin,
			'recurrent' => $event->Recurrent,
			'frequence' => $event->Frequence,
			'titre' => $event->Titre,
			'emplacement' => $event->Emplacement,
			'commentaire' => $event->Commentaire,
			'disponibilite' => $event->Disponibilite,
			'est_prive' => $event->EstPrive,
			'compte_id' => $event->IdtCompte
		);
	}
	
	echo json_encode($donnees, JSON_UNESCAPED_UNICODE);
});

/************************************
**
** 	     PARAMETRES
**
*************************************/
$app->get('/api/parametres/{id: [0-9]+}', function($id) use ($app) {
	
	$phql = "SELECT IdtParam, Nom, Type, Libelle, Valeur 
			FROM tabparametre 
			WHERE IdtCompte = :id:";
	$params = $app->modelsManager->executeQuery($phql, array(
		'id' => $id
	));
	
	$donnees = array();
	
	foreach( $params as $param){
		$donnees[] = array(
			'id' => $param->IdtParam, 
			'nom' => $param->Nom, 
			'type' => $param->Type,
			'libelle' => $param->Libelle,
			'valeur' => $param->Valeur
		);
	}
	
	echo json_encode($donnees, JSON_UNESCAPED_UNICODE);
});

/************************************
**
** 	     OBJECTIFS
**
*************************************/
$app->get('/api/objectifs/{id: [0-9]+}', function($id) use ($app) {
	
	$phql = "SELECT IdtObjectif, Annee, Type, Libelle, Valeur 
			FROM tabobjectif 
			WHERE IdtCompte = :id:";
	$objectifs = $app->modelsManager->executeQuery($phql, array(
		'id' => $id
	));
	
	$donnees = array();
	
	foreach( $objectifs as $objectif){
		$donnees[] = array(
			'id' => $objectif->IdtObjectif, 
			'annee' => $objectif->Annee, 
			'type' => $objectif->Type,
			'libelle' => $objectif->Libelle,
			'valeur' => $objectif->Valeur
		);
	}
	
	echo json_encode($donnees, JSON_UNESCAPED_UNICODE);
});

/************************************
**
** 	     SATISFACTIONS
**
*************************************/
$app->get('/api/satisfactions/{id: [0-9]+}', function($id) use ($app) {
	
	$phql = "SELECT IdtSatisfaction, Nom, DateEnvoi, DateRecu 
			FROM tabsatisfaction 
			WHERE IdtSociete = :id:";
	$satisfactions = $app->modelsManager->executeQuery($phql, array(
		'id' => $id
	));
	
	$donnees = array();
	
	foreach( $satisfactions as $satisfaction){
		$donnees[] = array(
			'id' => $satisfaction->IdtSatisfaction, 
			'nom' => $satisfaction->Nom, 
			'denvoi' => $satisfaction->DateEnvoi,
			'drecu' => $satisfaction->DateRecu
		);
	}
	
	echo json_encode($donnees, JSON_UNESCAPED_UNICODE);
	
});

/************************************
**
** 	     REPONSES
**
*************************************/
$app->get('/api/reponses', function() use ($app) {
	
	$phql = "SELECT Idt, Question, Reponse, Categorie, Type, IdtSatisfaction 
			FROM tabreponse";
	$reponses = $app->modelsManager->executeQuery($phql);
	
	$donnees = array();
	
	foreach( $reponses as $reponse){
		$donnees[] = array(
			'id' => $reponse->Idt, 
			'question' => $reponse->Question, 
			'reponse' => $reponse->Reponse,
			'categorie' => $reponse->Categorie,
			'type' => $reponse->Type
		);
	}
	
	echo json_encode($donnees, JSON_UNESCAPED_UNICODE);
	
});

//Gestionnaire
$app->handle();

?>