import java.util.Arrays;
import java.util.Random;

/**
 * 
 * Tein tehtävänannon mukaan rajapinnan toteuttavan luokan, mutta totesin
 * rajapinnan olevan pelin logiikan kanssa yhteensopimaton ja tarpeettoman monimutkainen. 
 * Testivaiheen jälkeen karsin turhia osia pois.
 * 
 * Poikkeuksia tehtävänannosta:
 * 
 * -avaa(int x, int y) 
 * 			- metodi poistettu. samaa toiminnallisuutta 
 * 			  hoitavat metodit vasen() ja painaNappia(int x, int y).
 * 
 * -asetaLippu(int x, int y,boolean lippu) 
 * 			- parametri lippu poistettu, koska oli täysin turha. 
 * 			  Metodi vaihtaa lipun arvon aina toiseksi. 
 * 
 * - OLI_JO_AUKI, OLI_LIPUTETTU ja OLI_MIINA
 * 			- vakiot poistettu turhina.
 * 
 * Muutoin kuten dokumentaatiossa.
 * 	
 * Pelin voittaa kun jokainen ruutu on joko avattu tai miinoitettu.
 *
 * 
 * @author 290836
 *
 */


public class Peliruudukko {

	//koko x-suunnassa
	private int leveys;
	//koko y-suunnassa
	private int korkeus;
	//miinojen määrä
	private int miinoja;

	private static String loukkaus[] = {"Et vaan osaa!","Kuolit!","Ähähää!","Amatööri!","Ei se noin mene!","Tunari!","Huoh."};
	
	//boolean-taulukot, jotka tietävät ruudun tilan paikassa [x][y]
	private boolean[][] miinat;
	private boolean[][] avatut;
	private boolean[][] liputetut;

	//jos kuollut == true, paneelin napit eivät reagoi enää
	private boolean kuollut;

	Random kone = new Random();
	
	private Pelipaneeli pelipaneeli;
	private Lippulaskuri lippulaskuri;
	private Miinapeli miinapeli;

	public Peliruudukko(int leveys, int korkeus, int miinoja) {
		this.leveys = leveys;
		this.korkeus = korkeus;
		this.miinoja = miinoja;
		this.kuollut = false;

		this.teeRuudut();
		this.miinoita(miinoja);
	}

	public void asetaLippulaskuri(Lippulaskuri lippulaskuri) {
		this.lippulaskuri = lippulaskuri; 
	}

	public void asetaMiinapeli(Miinapeli miinapeli) {
		this.miinapeli = miinapeli;
	}

	public boolean onKuollut() {
		return this.kuollut;
	}

	public int annaMiinat() {
		return this.miinoja;
	}

	public Pelipaneeli annaPelipaneeli() {
		return this.pelipaneeli;
	}

	public void asetaPelipaneeli(Pelipaneeli paneeli) {
		this.pelipaneeli = paneeli;
	}

	public int annaLeveys() {
		return this.leveys;
	}

	public int annaKorkeus() {
		return this.korkeus;
	}

	/**
	 * Alustaa taulukot ja täyttää ne false-arvoilla.
	 */
	private void teeRuudut() {
		this.miinat = new boolean[this.leveys][this.korkeus];
		this.avatut = new boolean[this.leveys][this.korkeus];
		this.liputetut = new boolean[this.leveys][this.korkeus];

		for(boolean[] sarake : avatut) {
			Arrays.fill(sarake, false);
		}
		for(boolean[] sarake : liputetut) {
			Arrays.fill(sarake, false);
		}
		for(boolean[] sarake : miinat) {
			Arrays.fill(sarake, false);
		}
	}
	/**
	 * kylvää miinat taulukkoon
	 * @param miinoja	montako miinaa laitetaan
	 */
	private void miinoita(int miinoja) {
		
		int luku = 0;
		while(luku < miinoja) {

			int x = kone.nextInt(annaLeveys());
			int y = kone.nextInt(annaKorkeus());	

			if (!onMiina(x,y)) {
				miinat[x][y] = true;
				luku++;
			}
		}
	}

	public boolean onAuki(int x, int y)  {
		return avatut[x][y];
	}

	public boolean onLiputettu(int x, int y)  {
		return liputetut[x][y];		
	}

	public boolean onMiina(int x, int y)  {
		return miinat[x][y];
	}

	/**
	 * poikkeus tehtävänannosta: metodi vaihtaa lipun arvon aina toiseksi. 
	 * Mielestäni ei ole syytä tehdä vaikeasti kun voi tehdä helposti.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean asetaLippu(int x, int y) {
		if(onAuki(x,y)) {
			return false;
		}
		//jos lippu on jo paikoillaan
		boolean onkoJoLippu = liputetut[x][y];

		//se poistetaan taulukosta
		liputetut[x][y] = !onkoJoLippu;

		//poistetaan grafiikasta
		pelipaneeli.annaPaneelista(x, y).naytaLippu(!onkoJoLippu);

		//ja lisataan lippuvarastoon
		lippulaskuri.lisaa(!onkoJoLippu);

		//joka paivitetaan
		lippulaskuri.paivita();

		//toimii myös päinvastoin samalla logiikalla.
		return true;
	}

	/**
	 * Metodi, joka määrittää mitä tapahtuu 
	 * kun klikkaa ruutua vasemmalla napilla.
	 * @param nappi		Ruutunappi jota on painettu
	 */
	public void vasen(Ruutunappi nappi) {

		//napin koordinaatit
		int x = nappi.annaX();
		int y = nappi.annaY();

		//liputettu tai avonainen ei tee mitään
		if(this.onLiputettu(x, y) || (this.onAuki(x, y))) {
			return;
		}

		//jos tuli miina
		if (onMiina(x,y)) {

			//näytetään miina
			nappi.naytaMiina();
			painaNappia(x,y);

			//maalataan punaiseksi
			nappi.varitaOsumaRuutu();

			//lopetetaan peli
			tappio(x,y);
			return;
		}
		//jos on vihjenumero, laitetaan se ruutuun näkyville
		else if (annaVihjenumero(x,y) > 0) {
			nappi.naytaVihje(annaVihjenumero(x,y));
		}
		//jos vihje on 0, paljastetaan aluetta
		else if (annaVihjenumero(x,y) == 0) {
			paljastaAlue(x, y);
		}
		painaNappia(x,y);
	}
	/**
	 * Metodi, joka määrittää mitä tapahtuu 
	 * kun klikkaa ruutua oikealla napilla.
	 * @param nappi 	Ruutunappi jota on painettu
	 */
	public void oikea(Ruutunappi nappi) {
		int x = nappi.annaX();
		int y = nappi.annaY();

		if(onAuki(x, y)) {
			return;
		}
		else asetaLippu(x, y);
	}
	/**
	 * Paljastaa naapuriruutuja niin pitkään kun oma vihje on 0. 
	 * Toimii kutsumalla itseään laillisten naapurien koordinaateilla.
	 * @param x
	 * @param y
	 */
	private void paljastaAlue(int x, int y) {
		//jos auki tai lipullinen -> return
		if (onAuki(x,y) || onLiputettu(x,y)) {
			return;
		}

		if (annaVihjenumero(x,y) == 0) {

			// tässä ei viitata vasen() koska rekursio lähtee käsistä.
			painaNappia(x,y);

			// nullpointer-tarkastukset && rekursiot.

			if(!onkoOikeallaReunalla(x)) {
				paljastaAlue(x+1, y);
			}
			if (!onkoVasemmallaReunalla(x)) {
				paljastaAlue(x-1, y);
			}
			if (!onkoYlaReunalla(y)) {
				paljastaAlue(x, y-1);
			}
			if(!onkoAlaReunalla(y)) {
				paljastaAlue(x, y+1);
			}
			if (!onkoVasemmallaReunalla(x) && !onkoAlaReunalla(y)) {
				paljastaAlue(x-1, y+1);
			}
			if (!onkoVasemmallaReunalla(x) && !onkoYlaReunalla(y)) {
				paljastaAlue(x-1, y-1);
			}
			if (!onkoOikeallaReunalla(x) && !onkoAlaReunalla(y)) {
				paljastaAlue(x+1, y+1);
			}
			if (!onkoOikeallaReunalla(x) && !onkoYlaReunalla(y)) {
				paljastaAlue(x+1, y-1);
			}
		}

		//paljastetaan myös ruudut joissa vihje, mutta ei jatketa paljastamista niistä eteenpäin.
		else if(annaVihjenumero(x,y) > 0 && !onMiina(x,y)) {

			//viitataan vasen() jotta vihje tulee näkyviin oikein.
			vasen(pelipaneeli.annaPaneelista(x, y));
		}

	}

	/**
	 * Tallettaa avaamistiedon taulukkoon. 
	 * Koordinaateissa oleva piste painetaan graafisesti "pohjaan".
	 * @param x
	 * @param y
	 */
	private void painaNappia(int x, int y) {
		//jos on jo auki niin ei tee mitään
		if(!onAuki(x,y)) {			

			pelipaneeli.annaPaneelista(x, y).nappiPohjaan();
			//tallennetaan tieto avaamisesta
			avatut[x][y] = true;
		}
	}
	/**
	 * Laskee miinojen määrän ruudun naapureissa. Toimii tarkastamalla kaikki lailliset naapurit.
	 * @param x
	 * @param y
	 * @return miinojen määrä naapureissa
	 */
	public int annaVihjenumero(int x, int y) {
		int laskuri = 0;
		boolean alareunalla = onkoAlaReunalla(y);
		boolean ylareunalla = onkoYlaReunalla(y);
		boolean vasemmallareunalla = onkoVasemmallaReunalla(x);
		boolean oikeallareunalla = onkoOikeallaReunalla(x);

		//laillisuustarkastukset tehtävänannosta huolimatta

		if(!oikeallareunalla) {

			if (!alareunalla) { 
				if (miinat[x+1][y+1]) {
					laskuri++;
				}
			}
			if(miinat[x+1][y]){
				laskuri++;
			}
			if(!ylareunalla) { 
				if(miinat[x+1][y-1]){
					laskuri++;
				}
			}
		}
		if(!ylareunalla) { 
			if(miinat[x][y-1]){
				laskuri++;
			}
		}
		if(!vasemmallareunalla) {

			if(!ylareunalla) { 
				if (miinat[x-1][y-1]){
					laskuri++;
				}
			}
			if(miinat[x-1][y]){
				laskuri++;
			}
			if(!alareunalla){ 
				if(miinat[x-1][y+1]){

					laskuri++;
				}
			}
		}
		if(!alareunalla) {
			if(miinat[x][y+1]){

				laskuri++;
			}
		}	
		return laskuri;
	}
	/**
	 * Miinaan osuttaessa kutsutaan tätä. Metodi käy kaikki 
	 * ruudut läpi ja painaa kaikkia miinallisia nappeja 
	 * paitsi sitä, josta tappio tuli.
	 * @param pommix
	 * @param pommiy
	 */
	private void tappio(int pommix, int pommiy) {	
		int x = 0; 

		while (x < this.leveys) {
			int y = 0;

			while (y < this.korkeus) {	

				//ei koske räjähtäneeseen ruutuun
				if(pommix != x || pommiy != y) {

					if (miinat[x][y]) {
						this.painaNappia(x,y);

						//oikein liputettuihin tulee pommi.gif
						if (liputetut[x][y]) {
							pelipaneeli.annaPaneelista(x,y).naytaMiina();
						}
						//liputtomiin huti.gif
						else if (!liputetut[x][y]) {
							pelipaneeli.annaPaneelista(x,y).naytaHuti();
						}
					}
				}
				y++;
			}
			x++;
		}
		this.miinapeli.asetaAlapalkinTeksti(loukkaus[kone.nextInt(loukkaus.length)]+ " Aloita alusta.");
		this.kuollut = true;
	}

	/**
	 * Pelin voittaa kun jokainen ruutu on joko avattu tai miinoitettu.
	 * Tässä tarkastetaan tämä ehto.
	 */
	public void tarkastaVoitto() {
				
			int i = 0;
			while(i < this.leveys) {

				int j = 0; 
				while(j < this.korkeus) {
					
					//jos on ruutu joka ei ole avattu ja siinä ei ole miinaa
					//-> ei voittoa
					if (!avatut[i][j] && !miinat[i][j]) {
						return;
					
				}
					j++;
				}
				i++;
			}
			//muuten voittaa jos mikään ei estä.
			voitto();
		
	}
	/**
	 * tarkastaVoitto() kutsuu tätä jos mikään ei estä voittoa.
	 * booleania "kuollut" käytetään myös voitossa.
	 * liputtaa jokaisen ruudun joka on liputon ja avaamaton.
	 */
	private void voitto() {
		int i = 0;
		while(i < this.leveys) {

			int j = 0; 
			while(j < this.korkeus) {
				
				
				if (!avatut[i][j] && !liputetut[i][j]) {
					this.asetaLippu(i,j);
				
			}
				j++;
			}
			i++;
		}
		this.miinapeli.asetaAlapalkinTeksti("Voitit pelin!");		
		this.kuollut = true;
	}


	private boolean onkoAlaReunalla(int y) {
		if (y == this.korkeus-1) {
			return true;
		}
		else return false;
	}
	private boolean onkoYlaReunalla(int y) {
		if (y == 0) {
			return true;
		}
		else return false;	
	}
	private boolean onkoOikeallaReunalla(int x) {
		if (x == this.leveys-1) {
			return true;
		}
		else return false;
	}
	private boolean onkoVasemmallaReunalla(int x) {
		if (x == 0) {
			return true;
		}
		else return false;
	}
}
