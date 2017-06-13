package it.polito.finestre;

import it.polito.db.DB;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class FinestraPrincipale extends JFrame{

	private static final long serialVersionUID = 1868837321666586496L;
	
	private final Dimension dim = getToolkit().getScreenSize();
	
	private JButton info_cliente, iscrizione_corsi;
	
	public FinestraPrincipale(final DB db){
		
		setLayout(new BorderLayout());
		setTitle("esercitazioneJDBC");
		setSize(250,150);
		//setto la posizione in cui verrá mostrata
		setLocation(dim.width / 2 - getWidth() / 2,
				dim.height / 2 - getHeight() / 2);
		//disabilito la possibilitá di resize
		setResizable(false);
		//setto l'uscita automatica dal programma quando l'icona X viene premuta
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		/*
		 * Creo un pannello che verrá inserito a north del frame
		 * e che conterrá i miei elementi.
		 * 
		 * Utilizzo il GridBagLayout per comoditá nel posizionare i vari elementi.
		 * 
		 */
		JPanel north_panel=new JPanel();
		north_panel.setLayout(new GridBagLayout());
		north_panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		north_panel.add(new JLabel("Gestione clienti e iscrizione corsi"),c);
		add(north_panel,BorderLayout.NORTH);
		
		
		/*
		 * Creo un pannello che verrá inserito centralmente al frame
		 * e che conterrá i miei elementi.
		 * 
		 * Utilizzo il GridBagLayout per comoditá nel posizionare i vari elementi.
		 * 
		 */
		JPanel center_panel=new JPanel();
		center_panel.setLayout(new GridBagLayout());
		center_panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		c.gridx = 0;
		c.gridy = 0;
		info_cliente=new JButton("Info Cliente");
		center_panel.add(info_cliente,c);
		
		c.gridx = 0;
		c.gridy = 1;
		//aggiungo un padding per spaziare gli elementi fra di loro
		c.insets = new Insets(10,0,0,0);
		iscrizione_corsi=new JButton("Iscrizione Corsi");
		center_panel.add(iscrizione_corsi,c);
		add(center_panel,BorderLayout.CENTER);
		
		/*
		 * Listen anonimo sul Bottone Info Cliente.
		 * Apre una nuova finestra relativa alle Info del Cliente
		 * 
		 */
		info_cliente.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				new FinestraInfoCliente(db);
			}
		});
		
		/*
		 * Listen anonimo sul Bottone Iscrizione Corsi.
		 * Apre una nuova finestra relativa alla Iscrizione ai Corsi
		 * 
		 */
		iscrizione_corsi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				new FinestraIscrizioneCorsi(db);
			}
		});
		
		/*
		 * Listen anonimo sul finestra principale.
		 * Serve per intercettare quando l'utente preme l'icona X,
		 * quella di chiusura della finestra. In questo modo possiamo
		 * essere sicuri che la connessione al Db venga chiusa giusto
		 * prima che venga fatto il dispose() del nostro programma.
		 * 
		 */
		this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                db.CloseConnection();
            }
        });

		setVisible(true);	
	}

}
