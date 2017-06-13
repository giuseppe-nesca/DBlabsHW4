package it.polito;

import it.polito.db.DB;
import it.polito.finestre.FinestraPrincipale;

public class Main {

	public static void main(String[] args) {
		
		// Instanzio la classe che gestisce tutte le operazioni 
		// che coinvolgono la base di dati
		DB db=new DB();

		db.OpenConnection();
		
		new FinestraPrincipale(db);
	}

}
