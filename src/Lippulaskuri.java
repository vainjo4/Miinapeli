import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * Luokka, joka pitää kirjaa ja näyttää tiedon kentälle sijoitetuista lipuista. 
 * Kertoo implisiittisesti myös miinojen määrän kentällä.
 * Perii JPanelin.
 * Miinapeli luo instanssin tästä ja sijoittaa ikkunan ylälaitaan. 
 * @author 290836
 *
 */
@SuppressWarnings("serial")
public class Lippulaskuri extends JPanel {

	private int lippujaJaljella;
	private int miinojaAlussa;

	private JLabel laskuri;

	public Lippulaskuri(Peliruudukko peliruudukko) {

		this.miinojaAlussa = peliruudukko.annaMiinat();
		this.lippujaJaljella = this.miinojaAlussa;		
		this.teeLaskuri();
	}

	public int annaLippujaJaljella() {
		return this.lippujaJaljella;
	}
	/**
	 * Päivittää laskurin lukeman.
	 */
	public void paivita() {
		this.laskuri.setText("Lippuja jäljellä "+annaLippujaJaljella()+ "/" + miinojaAlussa);
	}

	private void teeLaskuri() {

		JLabel laskuri = new JLabel();
		this.laskuri = laskuri;
		this.paivita();
		this.add(laskuri);
	}
	/**
	 * jos lippu on lisätty kentälle, se vähennetään varastosta 
	 * eli laskurin arvo pienenee ja päinvastoin.
	 * @param lippuLisatty
	 */
	public void lisaa(boolean lippuLisatty) {
		if(lippuLisatty) {
			lippujaJaljella--;
		}
		else lippujaJaljella++;
	}
}