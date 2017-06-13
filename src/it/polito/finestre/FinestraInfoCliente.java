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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FinestraInfoCliente extends JFrame{

	private static final long serialVersionUID = -4660356044131765132L;
	
	private final Dimension dim = getToolkit().getScreenSize();
	
	private JButton info;
	private JTextField cod_cliente;
	private JTextArea dati_cliente,corsi_cliente;

	public FinestraInfoCliente(final DB db){
		
		setLayout(new BorderLayout());
		setTitle("Info Cliente");
		setSize(350,350);
		//setto la posizione in cui verrá mostrata
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
		c.gridx = 0;
		c.gridy = 0;		
		center_panel.add(new JLabel("Codice Cliente"),c);
		
		c.gridx = 0;
		c.gridy = 1;
		//aggiungo un padding per spaziare gli elementi fra di loro
		c.insets = new Insets(5,5,0,5);
		cod_cliente=new JTextField(20);
		center_panel.add(cod_cliente,c);
		c.gridx = 1;
		c.gridy = 1;
		info=new JButton("Info");
		center_panel.add(info,c);
		
		c.gridx = 0;
		c.gridy = 2;
		center_panel.add(new JLabel("Dati Clienti"),c);
		c.gridx = 0;
		c.gridy = 3;
		dati_cliente=new JTextArea(5, 20);
		dati_cliente.setEditable(false);
		dati_cliente.setBackground(Color.lightGray);
		dati_cliente.setForeground(Color.blue);
		center_panel.add(new JScrollPane(dati_cliente),c);
		
		c.gridx = 0;
		c.gridy = 4;
		center_panel.add(new JLabel("Iscritto ai seguenti corsi"),c);
		c.gridx = 0;
		c.gridy = 5;
		corsi_cliente=new JTextArea(5, 20);
		corsi_cliente.setEditable(false);
		corsi_cliente.setBackground(Color.lightGray);
		corsi_cliente.setForeground(Color.blue);
		center_panel.add(new JScrollPane(corsi_cliente),c);
		
		add(center_panel,BorderLayout.CENTER);
		
		/*
		 * Listen anonimo sul Bottone Info.
		 * Alla pressione ottiene il codice cliente e inserire nelle due JtextArea
		 * i dati del cliente i corsi a cui é iscritto.
		 * 
		 */
		info.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				try{
					long codice_cliente=Long.parseLong(cod_cliente.getText());
					
					
					dati_cliente.setText(db.ottieniDatiCliente(codice_cliente));
					corsi_cliente.setText("");
					
					for(String s:db.ottieniCorsiCliente(codice_cliente))
						corsi_cliente.append(s+"\n");
				}catch(Exception er){}	
			}
		});
		
		setVisible(true);	
	}	

}
