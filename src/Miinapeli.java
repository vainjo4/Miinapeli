import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * Tämä luokka ajamalla peli käynnistyy. 
 * Luo ja sijoittaa pääikkunan ja komponentit.
 * @author 290836
 *
 */
@SuppressWarnings("serial")
public class Miinapeli extends JFrame {

	private Peliruudukko peliruudukko;
	//alapalkin JLabel, jota muokataan muualta.
	private JLabel alakyltti;

	/**
	 * @param peliruudukko Peliruudukko, joka huolehtii pelilogiikasta
	 */
	public Miinapeli(Peliruudukko peliruudukko) {
		super("Miinapeli");
		this.asetaPeliruudukko(peliruudukko);
		this.peliruudukko.asetaMiinapeli(this);
		this.teeIkkuna();
		this.pack();
	}

	public void asetaAlapalkinTeksti(String viesti) {
		this.alakyltti.setText(viesti);
	}

	public void asetaPeliruudukko(Peliruudukko peliruudukko) {
		this.peliruudukko = peliruudukko;
	}

	/**
	 * Sisäluokka Kuuntelija. Kuuntelee Peli-valikkoa.
	 */
	public class Kuuntelija implements ActionListener {
		private String mikanappi;
		private JLabel kyltti;

		/**
		 * Konstruktori ottaa Stringin, jolla se tunnistaa MenuItemin.
		 * @param mikanappi	erottaa MenuItemit toisistaan
		 * @param kyltti	viittaus muutettavaan JLabeliin
		 */
		public Kuuntelija(String mikanappi, JLabel kyltti) {
			this.mikanappi = mikanappi;
			this.kyltti = kyltti;
		}
		/**
		 * Tekee asioita MenuItemin Stringin perusteella.
		 */

		public void actionPerformed(ActionEvent e) {
			if(this.mikanappi.equals("aloita")) {
				pyydaParametrit();
				this.kyltti.setText("	");
			}
			else if(this.mikanappi.equals("lopeta")){
				System.exit(0);
			}
		}

	}
	/**
	 * Luo swingillä ikkunan ja komponentit pelille.
	 */	
	private void teeIkkuna() {


		Pelipaneeli pelipaneeli = new Pelipaneeli(this.peliruudukko);
		Lippulaskuri lippulaskuri = new Lippulaskuri(this.peliruudukko);
		this.peliruudukko.asetaLippulaskuri(lippulaskuri);	

		//ikkunalle annetaan koko joka approksimoi ruudukon kokoa. Lopullinen koko kuitenkin
		//saattaa määräytyä ruutujen viemän tilan mukaan. MinimumSize estää rumat ratkaisut.

		int kokox = this.peliruudukko.annaLeveys() * 30;
		int kokoy = 100 + this.peliruudukko.annaKorkeus() * 30;
		setMinimumSize(new Dimension (kokox,kokoy));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);

		JMenuBar palkki = new JMenuBar();					
		JPanel alapaneeli = new JPanel();		
		JMenu pelimenu = new JMenu("Peli");	
		JLabel alakyltti = new JLabel(" ");

		this.alakyltti = alakyltti;
		alapaneeli.add(alakyltti);

		JMenuItem aloitaitem = new JMenuItem("aloita alusta");		
		JSeparator separaattori = new JSeparator();		
		JMenuItem lopetaitem = new JMenuItem("lopeta");

		aloitaitem.addActionListener(new Kuuntelija("aloita", alakyltti));
		lopetaitem.addActionListener(new Kuuntelija("lopeta", null));

		pelimenu.add(aloitaitem);
		pelimenu.add(separaattori);
		pelimenu.add(lopetaitem);

		palkki.add(pelimenu);

		this.add(lippulaskuri, BorderLayout.NORTH);
		this.add(pelipaneeli, BorderLayout.CENTER);
		this.add(alapaneeli, BorderLayout.SOUTH);
		this.setJMenuBar(palkki);

		this.setVisible(true);
	}
	/**
	 * Luo uuden parametri-ikkunan.
	 */
	private void pyydaParametrit() {
		new ParametriIkkuna(this);
	}
	/**
	 * Tekee uuden pelin ja selvittelee ParametriIkkunan antamia lukuja. 
	 * Antaa MessageDialogeja, jos luvut hasardeja.
	 * @param leveys
	 * @param pituus
	 * @param miinoja
	 * @param ikkuna
	 */
	public void aloitaAlusta(String leveys, String pituus, 
			String miinoja, ParametriIkkuna ikkuna) {
		int l = 10;
		int p = 10;
		int m = 15;

		//sulkee vanhan miinapelin
		this.dispose();

		try {
			l = Integer.parseInt(leveys);
			p = Integer.parseInt(pituus);
			m = Integer.parseInt(miinoja);			
		}
		catch(NumberFormatException ex) {
			//poikkeuksen sattuessa ohjelma tekee itsestään uuden,
			//toimivan pelin vanhoilla parametreilla.
		}

		//rajoittaa miinamäärän 75% ruuduista.
		if (m > (3*l*p)/4) {
			Object nootti1 = "Miinoja saa olla korkeintaan 75% ruutujen määrästä.";
			JOptionPane.showMessageDialog(ikkuna, nootti1);
			pyydaParametrit();
			return;
		}
		//ei salli mittoja yli 22.
		if (l > 22 || p > 22) {
			Object nootti2 = "Maksimikoko on 22*22.";
			JOptionPane.showMessageDialog(ikkuna, nootti2);
			pyydaParametrit();
			return;
		}
		//valittaa myös yli 15 mutta sallii.
		if (l > 15 || p > 15) {
			Object nootti3 = "Suuret luvut voivat aiheuttaa ongelmia.";
			JOptionPane.showMessageDialog(ikkuna, nootti3);
		}

		//luo uuden pelin.
		luoMiinapeli(l, p, m);
	}

	public static void luoMiinapeli(int leveys, int pituus, int miinoja) {
		new Miinapeli(new Peliruudukko(leveys, pituus, miinoja));
	}

	public static void main(String[] args) {
		luoMiinapeli(10, 10, 15);
	}
}
