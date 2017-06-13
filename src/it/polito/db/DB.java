package it.polito.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import oracle.*;

public class DB {
	
	/* conn serve per "memorizzare" la connessione verso la base di dati */
	private Connection conn;
	
	/*
	 * Costruttore
	 */
	public DB(){
		/* Registrazione driver */
		/* Si ricorda che la classe che implementa il Driver JDBC per oracle è 
		 * oracle.jdbc.driver.OracleDriver */
		/* Scrivere il pezzo di codice necessario per instanziare/registrare 
		 * il driver Oracle che sarà utilizzato in seguito */
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception e){
			System.err.println("Errore durante il collegamento alla base dati : "+e);
		}
		/* Fine registrazione driver */
	}
		
	/*
	 * Metodo che apre la connessione con la base di dati.
	 * Ritorna true se la connessione viene aperta con successo, false altrimenti.
	 */
	public boolean OpenConnection(){
		try{
			/* Aprire la connessione e salvarla nella variabile conn 
			 * conn= ..... */
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe","usr","usr");
			return true;
			
		} catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
		
	/*
	 * Questo metodo riceve in ingresso il codice di un cliente e 
	 * ritorna i dati di tale cliente sottoforma di stringa.
	 * Praticamente la stringa restituita e' una concatenazione dei nomi 
	 * degli attributi e dei suoi valori.
	 * Se ad esempio il cliente selezionato si chiama Paolo Garza, ha come indirizzo 
	 * "Via Inesistente 24, Torino"
	 * e come numero di cellulare 0110907022 allora il metodo deve restituire
	 * la stringa "Nome: Paolo\nCognome: Garza\nIndirizzo: Via Inesistente 24, Torino\nCell: 0110907022"
	 */
	public String ottieniDatiCliente(long codice_cliente) {
		
		String dati=null;
		boolean clienteEsiste=true;
		Statement statement;
		ResultSet resultSet;

		try {

			/* Eseguire l'interrogazione che reperisce dalla base di dati 
			 * le informazioni relative al cliente con codice pari a quello 
			 * presente nel parametro codice_cliente di questpo metodo */
			
			/* Se il cliente esiste restituire la stringa con i suoi dati.
			 * Altrimenti una stringa contenente l'informazione "Cliente inestistente" */
			
			String query = "SELECT Nome, Cognome, Indirizzo, Cell "
					+ "FROM cliente "
					+ "WHERE CodiceCliente = " + codice_cliente;
			statement = conn.createStatement();
			resultSet = statement.executeQuery(query);
			clienteEsiste = resultSet.next();
			
			if (clienteEsiste==true){
				
				/* Modificare il codice in modo da restituire i dati del cliente 
				 * ottenuti tramite l'esecuzione dell'interrogazione.
				 * Usare lo stesso formato presente in questo esempio "statico" 
				 * per la formattazione della stringa di ritorno.
				 * Gestire con attenzione le persone che non hanno un numero di 
				 * cellulare (il campo e' di tipo NULL)
				
				dati=new String("Nome: Paolo\n"+
							    "Cognome: Garza\n"+
							    "Indirizzo: Via Inesistente 24, Torino\n"+
							    "Cell: 0110907022");*/
				dati = new String(
								"Nome: " + resultSet.getString("Nome") + 
							    "\nCognome: " + resultSet.getString("Cognome") +
							    "\nIndirizzo: " + resultSet.getString("Indirizzo") +
 							    "\nCell: " + resultSet.getString("Cell")
						);
			}
			else{
				/* Se il cliente non esiste restituisco un messaggio d'errore */
				dati=new String("Cliente inesistente");
			}			
			resultSet.close();
			statement.close();
			//conn.close(); //da chiudere a fine programma non ora!!!!!			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return dati;
		
	}	
	
	/*
	 * Questo metodo ritorna una lista di stringhe. Ogni stringa contiene 
	 * il nome di un corso. La lista di stringhe restituite corrisponde 
	 * all'elenco di corsi cui e' iscritto il cliente con codice pari 
	 * a quello presente nel parametro codice_cliente di questo metodo
	 */
	public List<String> ottieniCorsiCliente(long codice_cliente) {
		List<String> list=new LinkedList<String>();
		Statement statement;
		ResultSet resultSet;

		try {
			
			/* Inserire il pezzo di codice che accede alla base di 
			 * dati e recupera i nomi del corsi cui e' iscritto 
			 * il cliente con codice pari a quello presente 
			 * nel parametro codice_cliente di questo metodo.
			 * Inserire i nomi dei corsi nella lista di stringhe restituita 
			 * da questo metodo (ogni stringa della lista e' il nome di un corso) 
			 * */
			
			/* Esempio "statico" che restituisce sempre gli stessi nomi di corsi. 
			list.add("Corso inventato");
			list.add("Corso di fantasia");
			list.add("Corso facile");*/
			
			String query = new String("SELECT NomeC "
					+ "FROM ISCRITTO_CORSO IC , CORSO "
					+ "WHERE IC.CODICECORSO = CORSO.CODCORSO AND IC.CODICECLIENTE = " + codice_cliente);
			statement = conn.createStatement();
			resultSet = statement.executeQuery(query);
			
			while(resultSet.next()){
				list.add(resultSet.getString("NomeC"));
			}
			
			resultSet.close();
			statement.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		return list;
	}
	
	
	
	/*
	 * Il metodo ritorna una lista di stringhe con dentro il codice dei corsi
	 * per cui c'e' ancora almeno un posto libero e i rispettivi nomi. Ogni stringa 
	 * della lista restituita corrisponde ad un singolo corso e deve 
	 * essere formattata usando il formato codice_corso - nome_corso
	 */
	public List<String> ottieniCodiciCorsi() {
		List<String> list=new LinkedList<String>();

		try {
			/* Scrivere il pezzo di codice che reperisce le informazioni (codice e nome)
			 * per i corsi che hanno ancora posti disponibili (un corso ha ancora posti 
			 * disponibili se l'attributo corso. postidisponibili e' maggiore o uguale 
			 * a 1). 
			 * Inserire nella lista una stringa per ogni corso restituito dall'interrogazione. 
			 * */
			String query = new String (
						"SELECT CodCorso, NomeC FROM CORSO WHERE PostiDisponibili > 0"
					);
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			while (rs.next()){
				list.add(rs.getString("CodCorso") + " - " + rs.getString("NomeC"));
			}
			
			/* Esempio statico che restituisce sempre gli stessi corsi (fare attenzione 
			 * al formato usato, ossia "codice_corso - nome_corso")

			list.add("101"+" - "+"Corso di fantasia");
			list.add("21"+" - "+"Corso inventato");
			*/
			
			rs.close();
			statement.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return list;
	}
	
	
	
	/*
	 * Questo metodo riceve in ingresso (parametri di input) il codice di un corso
	 * e il codice di un cliente e iscrive il cliente al corso (ossia 
	 * aggiunge una nuova iscrizione nella base di dati). L'iscrizione 
	 * consiste nell'inserimento della tupla appropriata nella tabella iscritto_corso.
 	 * 
 	 * Una volta aggiunta l'iscrizione nella tabella iscritto_corso, il metodo 
 	 * viene aggiornare (decrementandolo di uno) il valore dei posti disponibili
 	 * per il corso indicato in codCorso (praticamente di deve eseguire un'istruzione 
 	 * SQL di aggiornamento che vada a decrementare di uno il valore del 
 	 * campo corso.postidisponibili per il corso per cui si e' appena effettuata 
 	 * l'iscrizione).
	 * 
	 * Il metodo ritorna true se l'iscrizione e' avvenuta correttamente, false altrimenti.
	 */
	public boolean aggiungiIscrizione(long codCorso,long codCliente){
		try {
			    /* Inserire il codice necessario per 
			     * 1 - inserire la nuova tupla nella tabella iscritto_corso, ossia quella relativa
			     *     all'iscrizione del cliente con codice codCliente al corso con codice codCorso  
			     * 2 - aggiornare il numero di posti disponibili (attributo corso.postidisponibili)
			     *     per il corso con codice pari a codCorso.
			     *     
			     * Fare attenzione che le due operazioni devono andare entrambe a buon fine, 
			     * oppure essere annullate entrambe se una delle due fallisce (vedere 
			     * i lucidi sulla gestione delle le transazioni in JDBC)
			     * */ 

				return true;
		} catch (Exception e) {
				e.printStackTrace();
				return false;
		}
		
	}
	
	
	/*
	 * Questo metodo chiude la connessione con il DB
	 */
	public void CloseConnection(){
		try {
			
			/* Scrivere il pezzo di codice che chiude la connessione con la base di dati */
		} catch (Exception e) {
			System.err.println("Errore nel chiudere la connessione con il DB!");
		}
	}
}
