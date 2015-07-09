<?php

use Phalcon\Mvc\Model,
	Phalcon\Mvc\Model\Message,
	Phalcon\Mvc\Model\Validator\InclusionIn,
	Phalcon\Mvc\Model\Validator\Uniqueness;
	
class tabsociete extends Model
{
	public function validation(){
		/*
		//type client ou prospect
		$this->validate( new InclusionIn(
			array(
				"field" => "Type", 
				"domain" => array("C", "P")
			)
		));
		
		//unicité du nom de la société
		$this->validate( new Uniqueness(
			array(
				"field" => "Nom",
				"message" => "La société est déjà présente."
			)
		));*/
		
		//controle code postal
		
		//controle fixe
		
		//controle mobile
		
		//controle fax
		
		//A ton un message d'erreur
		if( $this->validationHasFailed() == true ){
			return false;
		}
		
		return true;
	}
}



?>