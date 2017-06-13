package it.polito.finestre;


import it.polito.db.DB;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FinestraIscrizioneCorsi extends JFrame{

	private static final long serialVersionUID = -1387136103507145906L;
	
	private final Dimension dim = getToolkit().getScreenSize();
	
	private JButton iscrivi;
	private JComboBox cod_corso;
	private JTextField cod_cliente;
	private JTextField msg;
	
	public FinestraIscrizioneCorsi(final DB db){
		
		setLayout(new BorderLayout());
		setTitle("Iscrizione Corsi");
		setSize(350,200);
		//setto la posizione in cui verra' mostrata la finestra
		setLocation(dim.width / 2 - getWidth() / 2,
				dim.height / 2 - getHeight() / 2);
		//disabilito la possibilitá di resize
		setResizable(false);
		//setto il dispose automatico della finestra se il l'icona X viene premuta
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
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
		
		GridBagConstraints c = new GridBagConstraints();
		//aggiungo un padding per spaziare gli elementi fra di loro
		c.insets = new Insets(5,5,5,5);
		c.gridx = 0;
		c.gridy = 0;		
		center_panel.add(new JLabel("Codice Corso"),c);
		c.gridx = 2;
		c.gridy = 0;		
		center_panel.add(new JLabel("Codice Cliente"),c);
		
		c.gridx = 0;
		c.gridy = 1;
		cod_corso=new JComboBox(db.ottieniCodiciCorsi().toArray(new String[0]));
		center_panel.add(cod_corso,c);
		c.gridx = 2;
		c.gridy = 1;
		cod_cliente=new JTextField(10);
		center_panel.add(cod_cliente,c);
		
		c.gridx = 0;
		c.gridy = 2;
		iscrivi=new JButton("Iscrivi");
		center_panel.add(iscrivi,c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		msg=new JTextField(20);
		msg.setBackground(Color.lightGray);
		msg.setForeground(Color.blue);
	
		msg.setEditable(false);
		center_panel.add(msg,c);
		
		add(center_panel,BorderLayout.CENTER);
		
		/*
		 * Listen anonimo sul Bottone Iscrivi.
		 * Alla pressione ottiene il codice cliente e il codice corso e aggiunge
		 * l'iscrizione relativa nel DB.
		 * Se viene eseguita inserisce nel JTextField un messaggio di successo,
		 * altrimenti di insuccesso.
		 * 
		 * Inoltre aggiorna gli elementi presenti nella tendina del codice cliente
		 * e del codice corso in modo tale da rispettare eventuali modifiche che
		 * vengono effettuate nel DB dalla funzione di aggiungiIscrizione o da
		 * fattori esterni.
		 * 
		 */
		iscrivi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				try{
					String [] s1=cod_corso.getSelectedItem().toString().replaceAll("\\s","").split("-");
					boolean ok=db.aggiungiIscrizione(Long.parseLong(""+s1[0]),Long.parseLong(""+cod_cliente.getText()));
					if(ok==true)
						msg.setText("Iscrizione avvenuta con successo!");
					else 
						msg.setText("Errore nel procedimento di Iscrizione!");
					
					cod_corso.removeAllItems();
					for(String s:db.ottieniCodiciCorsi())
						cod_corso.addItem(s);
				}catch(Exception er){
					if(er instanceof NumberFormatException)
						msg.setText("Il codice deve essere numerico!");	
				}
				
			}
		});
		
		setVisible(true);
	}

}
