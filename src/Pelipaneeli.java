import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

/**
 * Luokka, joka piirtää Ruutunapit ja hoitaa niiden kuuntelun.
 * @author 290836
 *
 */
@SuppressWarnings("serial")
public class Pelipaneeli extends JPanel implements MouseListener{

	private Peliruudukko peliruudukko;
	private Ruutunappi[][] nappipaneeli;

	public Pelipaneeli(Peliruudukko peliruudukko) {
		this.peliruudukko = peliruudukko;
		peliruudukko.asetaPelipaneeli(this);

		int leveys = peliruudukko.annaLeveys();
		int korkeus = peliruudukko.annaKorkeus();

		if (leveys < 0 || korkeus < 0) {
			return;
		}

		nappipaneeli = new Ruutunappi[leveys][korkeus];

		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);

		GridBagConstraints c = new GridBagConstraints(
				0,0,			
				1,1,			
				0,0,			
				GridBagConstraints.CENTER,	
				GridBagConstraints.NONE,	
				new Insets(0,0,0,0),
				0,10);		
		/**
		 * piirtää napit
		 */
		int kor = 0;
		while (kor < korkeus) {

			int lev = 0;
			while (lev < leveys) {

				Ruutunappi uusinappi = new Ruutunappi(lev, kor);
				uusinappi.addMouseListener(this);
				c.gridx = lev; 
				c.gridy = kor;		
				this.add(uusinappi, c);
				this.nappipaneeli[lev][kor] = uusinappi;
				uusinappi = null;
				lev++;
			}
			kor++;
		}
	}
	/**
	 * antaa napin.
	 * @param x
	 * @param y
	 * @return
	 */
	public Ruutunappi annaPaneelista(int x, int y) {
		return this.nappipaneeli[x][y];
	}
	/**
	 * Tarkastaa onKuollut()-kutsulla onko peli vielä käynnissä, sitten 
	 * kutsuu hiiren painikkeen perusteella oikeaa metodia Peliruudukosta. 
	 * Tarkastuttaa voittoehdot joka napinpainalluksen jälkeen.
	 */
	
	public void mouseClicked(MouseEvent e) {
		if (!this.peliruudukko.onKuollut()) {
			Ruutunappi nappi = (Ruutunappi)e.getSource();

			if (e.getButton() == 1) {
				this.peliruudukko.vasen(nappi);
			}
			else if (e.getButton() == 3) {
				this.peliruudukko.oikea(nappi);
			}
			this.peliruudukko.tarkastaVoitto();
		}
	}
	
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
