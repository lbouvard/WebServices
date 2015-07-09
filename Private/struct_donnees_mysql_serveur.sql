CREATE TABLE IF NOT EXISTS `tabsociete` (
  `IdtSociete` int(11) NOT NULL AUTO_INCREMENT,
  `NomSociete` varchar(50) COLLATE utf8_bin NOT NULL,
  `Adresse1` varchar(50) COLLATE utf8_bin NOT NULL,
  `Adresse2` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `CodePostal` varchar(10) COLLATE utf8_bin NOT NULL,
  `Ville` varchar(50) COLLATE utf8_bin NOT NULL,
  `Pays` varchar(50) COLLATE utf8_bin NOT NULL,
  `TypeSociete` varchar(25) COLLATE utf8_bin NOT NULL,
  `Commentaire` text COLLATE utf8_bin,
  `DateModif` datetime DEFAULT NULL,
  `BitModif` tinyint(1) NOT NULL,
  `BitSup` tinyint(1) NOT NULL,
  PRIMARY KEY (`IdtSociete`),
  UNIQUE KEY (`NomSociete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

INSERT INTO tabsociete 
	(NomSociete, Adresse1, Adresse2, CodePostal, Ville, Pays, TypeSociete, Commentaire, DateModif, BitModif, BitSup) 
VALUES    
	('PlastProd', '1 rue du comodo', '', '54600', 'Villers-Lès-Nancy', 'France', 'M', 'Société mère', null, 0, 0),
	('Valeo', 'ZI dormant', '', '69000', 'Lyon', 'France', 'F', '', null, 0, 0),
	('Ideal', '150 avenue du général Leclerc', '', '75001', 'Paris', 'France', 'F', '', null, 0, 0),
	('PlasticLux', '12 strassen Gerd', '', 'L-1250', 'Luxembourg', 'Luxembourg', 'F', '', null, 0, 0),
	('Societe1', 'ZI Fessel', '', '69000', 'Lyon', 'France', 'C', '', null, 0, 0),
	('Societe2', '16 rue du clos', '', '93600', 'Bondy', 'France', 'C', '', null, 0, 0),
	('Boite3', '20 avenue du général de Gaulle', '', '78000', 'Versailles', 'France', 'C', '', null, 0, 0)
	
	
CREATE TABLE IF NOT EXISTS `tabutilisateur` (
  `IdtUtilisateur` int(11) NOT NULL AUTO_INCREMENT,
  `Nom` varchar(25) COLLATE utf8_bin NOT NULL,
  `MotDePasse` varchar(100) COLLATE utf8_bin NOT NULL,
  `Email` varchar(60) COLLATE utf8_bin NOT NULL,
  `Salt` varchar(60) COLLATE utf8_bin NOT NULL,
  `DateModif` datetime DEFAULT NULL,
  `BitModif` tinyint(1) NOT NULL,
  `BitSup` tinyint(1) NOT NULL,
  `Actif` tinyint(1) NOT NULL,
  PRIMARY KEY (`IdtUtilisateur`),
  UNIQUE KEY `Nom` (`Nom`,`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;	

INSERT INTO tabutilisateur
	(Nom, MotDePasse, Email, Salt, DateModif, BitModif, BitSup, Actif) 
VALUES 
	('bouvard.laurent', 'ZQGi8N+qt7Rt0o1Z/4hFodTwaXrj8BIYtj5zCbXMtXg2j5CKpoaoveoKPQodBS1oTs3XC+0bjwGLfj9mHjiX6Q==', 'laurent.bouvard@plastprod.fr', '5703c8599affced67f20c76ff6ec0116', null, 0, 0, 1),
	('dupond.jean', 'xs2y6GqgDMuy1G+jJxelOTeouwIeVwdad1/vUJi3U87fDNfpNiiNkFoLcGmt/pYHIVvjgs0Xb48Fys2zFjaAxQ==', 'jean.dupond@plasprod.fr', '5703c8599affgku67f20c76ff6ec0116', null, 0, 0, 1),
	('super.admin', 'whqoSMIHm68/KSh1L4mvV/aWen4c4VlIQ9RBPYzCAFkDBwtJBgZcraI9at0uzqXyjda7n5LiJn5Nybd9NlP8Iw==', 'admin@plastprod.fr', '0575f5b5602389cf17daf9bbbc5b4e7a', null, 0, 0, 1)

	
CREATE TABLE IF NOT EXISTS `tabproduits` (
  `IdtProduit` int(11) NOT NULL AUTO_INCREMENT,
  `NomProduit` varchar(50) COLLATE utf8_bin NOT NULL,
  `DescriptionProduit` text COLLATE utf8_bin,
  `CategorieProduit` varchar(25) COLLATE utf8_bin NOT NULL,
  `CodeProduit` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `PrixProduit` float NOT NULL,
  `DateModif` datetime DEFAULT NULL,
  `BitModif` tinyint(1) NOT NULL,
  `BitSup` tinyint(1) NOT NULL,
  `Producteur_id` int(11) NOT NULL,
  PRIMARY KEY (`IdtProduit`),
  UNIQUE KEY `NomProduit` (`NomProduit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

INSERT INTO tabproduits
	(NomProduit, DescriptionProduit, CategorieProduit, CodeProduit, PrixProduit, DateModif, BitModif, BitSup, Producteur_id)
VALUES
	('Comodo208', 'Boite à gant granuleux', 'Automobile', 'RE15208', 58, null, 0, 0, 1),
	('Comodo542', 'Dock prise mobile', 'Automobile', 'PE14542', 18, null, 0, 0, 1),
	('CommandeClim', 'Bloc commande climatisation', 'Automobile', 'FD13633', 85, null, 0, 0, 1),
	('Plast1254', 'Attache universel', 'General', '125475621', 10, null, 0, 0, 2),
	('Bouton diam25', 'Bouton poussoir soft', 'Connectique', 'B5412S-25', 3, null, 0, 0, 2),
	('Bande PVC', 'Bande PVC noir mat', 'Matiere', '546454SS', 15, null, 0, 0, 5)
	
	
CREATE TABLE IF NOT EXISTS `tabcontact` (
  `IdtContact` int(11) NOT NULL AUTO_INCREMENT,
  `NomContact` varchar(100) COLLATE utf8_bin NOT NULL,
  `PrenomContact` varchar(100) COLLATE utf8_bin NOT NULL,
  `IntitulePoste` varchar(100) COLLATE utf8_bin NOT NULL,
  `TelFixe` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `TelMobile` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `Fax` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `Email` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `Adresse` varchar(100) COLLATE utf8_bin NOT NULL,
  `CodePostal` varchar(10) COLLATE utf8_bin NOT NULL,
  `Ville` varchar(50) COLLATE utf8_bin NOT NULL,
  `Pays` varchar(50) COLLATE utf8_bin NOT NULL,
  `Commentaire` text COLLATE utf8_bin,
  `DateModif` datetime DEFAULT NULL,
  `BitModif` tinyint(1) NOT NULL,
  `BitSup` tinyint(1) NOT NULL,
  `Societe_id` int(11) NOT NULL,
  `Utilisateur_id` int(11) NOT NULL,
  PRIMARY KEY (`IdtContact`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

INSERT INTO tabcontact
	(NomContact, PrenomContact, IntitulePoste, TelFixe, TelMobile, Fax, Email, Adresse, CodePostal, Ville, Pays, Commentaire, DateModif, BitModif, BitSup, Societe_id, Utilisateur_id)
VALUES
	('Antoine','Auguste', 'Commercial', '+33421689514', '+33634659871', null, 'antoine.auguste@valeo.com', 'ZI dormant', '69000', 'Lyon', 'France', '', null, 0, 0, 2, 1),
	('Bouvard','Laurent', 'Tech IT', '+33383256594', '+33645986147', null, 'laurent.bouvard@plastprod.fr', '1 rue du comodo', '54600', 'Villers-Lès-Nancy', 'France', '', null, 0, 0, 1, 2),
	('Convenant','Claude', 'Commercial', '+33145328564', '+33610447251', null, 'convenant.claude@boite3.fr', '20 avenue du général de Gaulle', '78000', 'Versailles', 'France', '', null, 0, 0, 7, 3),
	('Dupond','Jean', 'Commercial', '+33383256598', '+33612356898', null, 'jean.dupond@plastprod.fr', '1 rue du comodo', '54600', 'Villers-Lès-Nancy', 'France', 'Commentaire', null, 0, 0, 1, 4),
	('Kruger','Gerald', 'Commercial', '+352275465', '', null, 'kruger.gerald@plasticlux.lu', '12 strassen Gerd', 'L-1250', 'Luxembourg', 'Luxembourg', '', null, 0, 0, 4, 5), 
	('Lemoine','Alain', 'Commercial', '+33133443002', '+33610447251', null, 'alain.lemoine@societe2.fr', '16 rue du clos', '93600', 'Bondy', 'France', '', null, 0, 0, 6, 6),
	('Morandi','Pierre', 'Directeur technique', '+33445238590', '+33645464724', null, 'pierre.morandi@societe1.fr', 'ZI Fessel', '69000', 'Lyon', 'France', '', null, 0, 0, 5, 7),
	('Muller','Yvan', 'Commercial', '+33160468521', '+33633456598', null, 'muller.yvan@ideal.fr', '150 avenue du général Leclerc', '75001', 'Paris', 'France', '', null, 0, 0, 3, 8)

	
CREATE TABLE IF NOT EXISTS `tabcommande` (
  `IdtCommande` int(11) NOT NULL AUTO_INCREMENT,
  `DateCommande` datetime DEFAULT NULL,
  `EtatCommande` varchar(50) COLLATE utf8_bin NOT NULL,
  `Commentaire` text COLLATE utf8_bin,
  `DateChg` datetime DEFAULT NULL,
  `BitChg` tinyint(1) NOT NULL,
  `BitSup` tinyint(1) NOT NULL,
  `Contact_id` int(11) NOT NULL,
  `Societe_id` int(11) NOT NULL,
  PRIMARY KEY (`IdtCommande`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

INSERT INTO tabcommande
	(DateCommande, EtatCommande, Commentaire, DateChg, BitChg, BitSup, Contact_id, Societe_id)
VALUES
	('2015-03-17 18:59:05', '', 'Commentaire', null, 0, 0, 1, 5),
    ('2015-03-17 19:38:52', '', null, null, 0, 0, 3, 7)
	
	
CREATE TABLE IF NOT EXISTS `tabcommandeproduits` (
  `Idt` int(11) NOT NULL AUTO_INCREMENT,
  `CodeProduit` varchar(50) COLLATE utf8_bin NOT NULL,
  `NomProduit` varchar(50) COLLATE utf8_bin NOT NULL,
  `DescriptionProduit` text COLLATE utf8_bin,
  `Quantite` int(11) NOT NULL,
  `PrixProduit` float NOT NULL,
  `PrixTotal` float NOT NULL,
  `commande_id` int(11) NOT NULL,
  PRIMARY KEY (`Idt`),
  UNIQUE KEY `CodeProduit` (`CodeProduit`,`NomProduit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

INSERT INTO tabcommandeproduits
	(CodeProduit, NomProduit, DescriptionProduit, Quantite, PrixProduit, PrixTotal, commande_id)
VALUES
	('RE15208', 'Comodo208', 'Boite à gant granuleux', 4, 58, 232, 1),
	('PE14542', 'Comodo542', 'Dock prise mobile', 3, 18, 54, 1),
	('FD13633', 'CommandeClim',  'Bloc commande climatisation', 4, 85, 340, 2),
	('PE14542', 'Comodo542', 'Dock prise mobile', 2, 18, 36, 2),
	('RE15208', 'Comodo208', 'Boite à gant granuleux', 15, 58, 870, 2)