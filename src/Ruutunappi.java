import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;


/**
 * Luokka, jonka olioita Pelipaneeli luo. 
 * Tuntee omat koordinaattinsa ja sisältää metodeja nappien muuttamiseen.
 * Luokassa esiintyvät setText-metodit jotka sijoittavat välilyöntejä 
 * nappeihin estävät nappien rumia ja epätarkoituksenmukaisia koonmuutoksia.
 *  
 * @author 290836
 *
 */
@SuppressWarnings("serial")
public class Ruutunappi extends JButton {

	private int x;
	private int y;

	public Ruutunappi(int x, int y) {
		super();

		this.x = x;
		this.y = y;
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setBackground(new Color(190,190,190));
		//this.setText("    "+'\n'+"    ");
		
		this.setPreferredSize(new Dimension(25,25));
		this.setMinimumSize(new Dimension(25,25));
	}

	public int annaX() {
		return this.x;
	}
	public int annaY() {
		return this.y;
	} 

	public void nappiPohjaan() {
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		this.varitaAvattuRuutu();
	}
	/**
	 * Värittää vihjenumerot niiden suuruuden perusteella.
	 * @param vihjenumero
	 */
	private void asetaVari(int vihjenumero) {
		Color vari;
		switch(vihjenumero) {
		case 1 : vari = new Color(0,0,255); //blue
		break;
		case 2 : vari = new Color(0,128,0); //green
		break;
		case 3 : vari = new Color(255,0,0); //red
		break;
		case 4 : vari = new Color(0,128,128); //teal
		break;
		case 5 : vari = new Color(128,128,0); //olive
		break;
		case 6 : vari = new Color(128,0,0); //maroon
		break;
		case 7 : vari = new Color(128,0,128); //purple
		break;
		case 8 : vari = new Color(0,0,128); //navy
		break;
		default : vari = Color.BLACK; //duh
		}
		this.setForeground(vari);
	}

	public void naytaMiina() {
		this.setIcon(new ImageIcon("pommi.gif"));

	}

	public void naytaVihje(int vihjenumero) {
		this.setText(""+vihjenumero/*+" "+'\n'+"   "*/);
		this.asetaVari(vihjenumero);
	}
	/**
	 * Antaa napille vaaleanharmaan värin.
	 */
	public void varitaAvattuRuutu() {
		this.setBackground(new Color(225, 225, 225));
	}
	/**
	 * Antaa napille kirkkaanpunaisen värin.
	 */
	public void varitaOsumaRuutu() {
		this.setBackground(new Color(255, 0, 0));
	}
	/**
	 * Sijoittaa nappiin lipun tai ottaa sen pois.
	 * @param lippu
	 */
	public void naytaLippu(boolean lippu) {
		if(lippu) {
			//this.setText("");
			this.setIcon(new ImageIcon("lippu.gif"));
		}
		else {
			this.setIcon(null);
			//this.setText("    "+'\n'+"    ");
		}
	}

	public void naytaHuti() {
		this.setIcon(new ImageIcon("huti.gif"));
		//this.setText("");
	}
}

