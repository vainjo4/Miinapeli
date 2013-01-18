import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Luokka joka muodostaa ikkunan kolmella tekstikentällä. 
 * Pyytää parametrit joita käytetään uuden pelin aloittamiseen.
 * Oletusarvot ovat x = 10, y = 10, miinoja = 15.
 * @author 290836
 *
 */
@SuppressWarnings("serial")
public class ParametriIkkuna extends JFrame implements ActionListener {

	private TextField leveyskentta;
	private TextField korkeuskentta;
	private TextField miinakentta;
	private Miinapeli miinapeli;
	/**
	 * Ikkuna jossa kolme tekstikenttää ja ok-nappi.
	 * @param miinapeli
	 */
	public ParametriIkkuna(Miinapeli miinapeli) {
		super("Uusi peli");
		this.miinapeli = miinapeli;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(200, 200);
		this.setLocationRelativeTo(null);

		JPanel paneeli = new JPanel();
		paneeli.setLayout(new BoxLayout(paneeli, BoxLayout.Y_AXIS));

		JLabel leveyskyltti = new JLabel("uusi leveys:");
		JLabel korkeuskyltti = new JLabel("uusi korkeus:");
		JLabel miinakyltti = new JLabel("miinojen määrä:");

		this.leveyskentta = new TextField("10", 20);
		this.korkeuskentta = new TextField("10", 20);
		this.miinakentta = new TextField("15", 20);

		JButton ok = new JButton("OK");
		ok.addActionListener(this);

		paneeli.add(leveyskyltti);
		paneeli.add(this.leveyskentta);

		paneeli.add(korkeuskyltti);
		paneeli.add(this.korkeuskentta);

		paneeli.add(miinakyltti);
		paneeli.add(this.miinakentta);

		paneeli.add(ok);

		this.add(paneeli);

		this.setVisible(true);
	}
	/**
	 * Ok:ta painettaessa kutsutaan tätä. 
	 * Sulkee ikkunan ja kutsuu okPainettu()
	 */
	public void actionPerformed(ActionEvent e) {

		this.dispose();
		okPainettu();

	}
	/**
	 * Kutsuu miinapeliä ja antaa syötetyt 
	 * tiedot sille aloitusparametreiksi.
	 */
	private void okPainettu() {
		miinapeli.aloitaAlusta(
				this.leveyskentta.getText(),
				this.korkeuskentta.getText(),
				this.miinakentta.getText(), 
				this);	
	}
}
