import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ProjetMain {
	
	static JButton baffichalt= new JButton("");

	public static void createUI() throws IOException {
		
		int Xwidth=Carte.Xwidth;
		int Yheight=Carte.Yheight;

		class EcouteurBasique implements ActionListener {
			private Ardoise ardoise;
			public EcouteurBasique(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void actionPerformed(ActionEvent e) {
				ardoise.delierControleur();
				this.ardoise.effacer();
				ConstruireCarte.afficher_carte();
				//ConstruireCarte.afficher_carte_carree();
				this.ardoise.repaint();
			}
		}		

		class EcouteurNewCarte implements ActionListener {
			private Ardoise ardoise;
			public EcouteurNewCarte(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void actionPerformed(ActionEvent e) {
				ardoise.delierControleur();
				this.ardoise.effacer();
				initialiser_carte();
				int graine=Carte.bgraph.get_valeur_spin();
				CarteDiamond Cdiam=new CarteDiamond(0,50,0,50,250,graine); 
				CarteDiamond.calculerDiamond();
				//ConstruireCarte.ecrireFichier();
				CarteDiamond.etirer_carte();	
				ConstruireCarte.afficher_carte();
				this.ardoise.repaint();
			}
		}				
	
		class EcouteurNewCarteMilieu implements ActionListener {
			private Ardoise ardoise;
			public EcouteurNewCarteMilieu(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void actionPerformed(ActionEvent e) {
				ardoise.delierControleur();
				this.ardoise.effacer();
				initialiser_carte();
				int graine=Carte.bgraph.get_valeur_spin();
				CarteMilieu Cmilieu=new CarteMilieu(50,50,50,50,100,graine); 
				//CarteMilieu Cmilieu=new CarteMilieu(50,150,50,150,100,graine);
				CarteMilieu.calculerMilieu();
				//ConstruireCarte.ecrireFichier();
				CarteMilieu.etirer_carte();	
				ConstruireCarte.afficher_carte();
				this.ardoise.repaint();
			}
		}			
		
		class EcouteurCoupe implements ActionListener {
			private Ardoise ardoise;
			public EcouteurCoupe(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void actionPerformed(ActionEvent e) {
				ardoise.delierControleur();
				this.ardoise.effacer();
				ConstruireCarte.representer_coupe();
				this.ardoise.repaint();
			}
		}			

		class EcouteurProj implements ActionListener {
			private Ardoise ardoise;
			public EcouteurProj(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void actionPerformed(ActionEvent e) {
				ardoise.delierControleur();
				this.ardoise.effacer();
				int Ox=Carte.bgraph.get_valeur_Ox();
				int Oy=Carte.bgraph.get_valeur_Oy();
				int Oz=Carte.bgraph.get_valeur_Oz();
				int dist=-Carte.bgraph.get_valeur_dist();  //attention au moins!
				int degres=Carte.bgraph.get_valeur_deg();
				int degressoleil=Carte.bgraph.get_valeur_degsoleil();
				Perspective.initialiser_proj(Ox,Oy,Oz,dist,degres,degressoleil);
				Perspective.calculer_perspective();
				this.ardoise.repaint();
			}
		}				

		class EcouteurSoleil implements ActionListener {
			private Ardoise ardoise;
			public EcouteurSoleil(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void actionPerformed(ActionEvent e) {
				Carte.soleil=!Carte.soleil;
			}
		}						
		
		class EcouteurPerlin implements ActionListener {
			private Ardoise ardoise;
			public EcouteurPerlin(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void actionPerformed(ActionEvent e) {
				ardoise.delierControleur();
				this.ardoise.effacer();
				Perlin.calculer_perlin();
				Perlin.dessiner_perlin();
				this.ardoise.repaint();
			}
		}			
		
		
		class EcouteurLac implements ActionListener {
			private Ardoise ardoise;
			public EcouteurLac(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void actionPerformed(ActionEvent e) {
				ardoise.delierControleur();
				ardoise.lierControleur(new ControleurLac(ardoise,ardoise.gardoise));
			}
		}			

		class EcouteurDijkstra implements ActionListener {
			private Ardoise ardoise;
			public EcouteurDijkstra(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void actionPerformed(ActionEvent e) {
				ardoise.delierControleur();
				ardoise.lierControleur(new ControleurDijkstra(ardoise,ardoise.gardoise));
			}
		}				
		
		class EcouteurVoirLac implements ActionListener {
			private Ardoise ardoise;
			public EcouteurVoirLac(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void actionPerformed(ActionEvent e) {
				ardoise.delierControleur();
				ardoise.effacer();
				ConstruireCarte.voir_lacs();
				this.ardoise.repaint();
			}
		}			
		
		class EcouteurMontagne implements ActionListener {
			private Ardoise ardoise;
			public EcouteurMontagne(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void actionPerformed(ActionEvent e) {
				ardoise.delierControleur();
				ardoise.lierControleur(new ControleurMontagne(ardoise,ardoise.gardoise));
			}
		}		
		
		class EcouteurEffacer implements ActionListener {
			private Ardoise ardoise;
			public EcouteurEffacer(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void actionPerformed(ActionEvent e) {
				ardoise.effacer();
			}
		}		

		class EcouteurSpinner implements ChangeListener {
			private Ardoise ardoise;
			public EcouteurSpinner(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				int valeur = (int) s.getValue();
				ardoise.set_valeur_spin(valeur);
			}
		}
		class EcouteurSpinnerHauteurpcc implements ChangeListener {
			private Ardoise ardoise;
			public EcouteurSpinnerHauteurpcc(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				int valeur = (int) s.getValue();
				Carte.bgraph.set_valeur_hauteurpcc(valeur);
			}
		}
		class EcouteurSpinnerPentepcc implements ChangeListener {
			private Ardoise ardoise;
			public EcouteurSpinnerPentepcc(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				int valeur = (int) s.getValue();
				Carte.bgraph.set_valeur_pentepcc(valeur);
			}
		}
		
		
		class EcouteurSpinnerOx implements ChangeListener {
			private Ardoise ardoise;
			public EcouteurSpinnerOx(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				int valeur = (int) s.getValue();
				Carte.bgraph.set_valeur_Ox(valeur);
				this.ardoise.effacer();
				int Ox=Carte.bgraph.get_valeur_Ox();
				int Oy=Carte.bgraph.get_valeur_Oy();
				int Oz=Carte.bgraph.get_valeur_Oz();
				int dist=-Carte.bgraph.get_valeur_dist();  //attention au moins!
				int degres=Carte.bgraph.get_valeur_deg();
				int degressoleil=Carte.bgraph.get_valeur_degsoleil();
				Perspective.initialiser_proj(Ox,Oy,Oz,dist,degres,degressoleil);
				Perspective.calculer_perspective();
				this.ardoise.repaint();
			}
		}
		class EcouteurSpinnerOy implements ChangeListener {
			private Ardoise ardoise;
			public EcouteurSpinnerOy(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				int valeur = (int) s.getValue();
				Carte.bgraph.set_valeur_Oy(valeur);
				this.ardoise.effacer();
				int Ox=Carte.bgraph.get_valeur_Ox();
				int Oy=Carte.bgraph.get_valeur_Oy();
				int Oz=Carte.bgraph.get_valeur_Oz();
				int dist=-Carte.bgraph.get_valeur_dist();  //attention au moins!
				int degres=Carte.bgraph.get_valeur_deg();
				int degressoleil=Carte.bgraph.get_valeur_degsoleil();
				Perspective.initialiser_proj(Ox,Oy,Oz,dist,degres,degressoleil);
				Perspective.calculer_perspective();
				this.ardoise.repaint();
			}
		}		
		class EcouteurSpinnerOz implements ChangeListener {
			private Ardoise ardoise;
			public EcouteurSpinnerOz(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				int valeur = (int) s.getValue();
				Carte.bgraph.set_valeur_Oz(valeur);
				this.ardoise.effacer();
				int Ox=Carte.bgraph.get_valeur_Ox();
				int Oy=Carte.bgraph.get_valeur_Oy();
				int Oz=Carte.bgraph.get_valeur_Oz();
				int dist=-Carte.bgraph.get_valeur_dist();  //attention au moins!
				int degres=Carte.bgraph.get_valeur_deg();
				int degressoleil=Carte.bgraph.get_valeur_degsoleil();
				Perspective.initialiser_proj(Ox,Oy,Oz,dist,degres,degressoleil);
				Perspective.calculer_perspective();
				this.ardoise.repaint();
			}
		}		
		class EcouteurSpinnerdist implements ChangeListener {
			private Ardoise ardoise;
			public EcouteurSpinnerdist(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				int valeur = (int) s.getValue();
				Carte.bgraph.set_valeur_dist(valeur);
				this.ardoise.effacer();
				int Ox=Carte.bgraph.get_valeur_Ox();
				int Oy=Carte.bgraph.get_valeur_Oy();
				int Oz=Carte.bgraph.get_valeur_Oz();
				int dist=-Carte.bgraph.get_valeur_dist();  //attention au moins!
				int degres=Carte.bgraph.get_valeur_deg();
				int degressoleil=Carte.bgraph.get_valeur_degsoleil();
				Perspective.initialiser_proj(Ox,Oy,Oz,dist,degres,degressoleil);
				Perspective.calculer_perspective();
				this.ardoise.repaint();
			}
		}	
		class EcouteurSpinnerdeg implements ChangeListener {
			private Ardoise ardoise;
			public EcouteurSpinnerdeg(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				int valeur = (int) s.getValue();
				Carte.bgraph.set_valeur_deg(valeur);
				this.ardoise.effacer();
				int Ox=Carte.bgraph.get_valeur_Ox();
				int Oy=Carte.bgraph.get_valeur_Oy();
				int Oz=Carte.bgraph.get_valeur_Oz();
				int dist=-Carte.bgraph.get_valeur_dist();  //attention au moins!
				int degres=Carte.bgraph.get_valeur_deg();
				int degressoleil=Carte.bgraph.get_valeur_degsoleil();
				Perspective.initialiser_proj(Ox,Oy,Oz,dist,degres,degressoleil);
				Perspective.calculer_perspective();
				this.ardoise.repaint();
			}
		}		
		class EcouteurSpinnerdegsoleil implements ChangeListener {
			private Ardoise ardoise;
			public EcouteurSpinnerdegsoleil(Ardoise ardoise) { this.ardoise = ardoise; }
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				int valeur = (int) s.getValue();
				Carte.bgraph.set_valeur_degsoleil(valeur);
			}
		}
		
		
		
		
		JFrame frame = new JFrame("Carte");
		Container container = frame.getContentPane();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panneau=new JPanel();
		Ardoise ardoise=new Ardoise(Xwidth,Yheight);
		Carte.bgraph=ardoise.gardoise;
		
		panneau.add(ardoise);
		
		JPanel boite=new JPanel();
		
		JButton btBasique = new JButton("3D Basique");
		EcouteurBasique ebasic=new EcouteurBasique(ardoise);
		btBasique.addActionListener(ebasic);
		JButton btCoupe = new JButton("Coupe");
		EcouteurCoupe ecoupe=new EcouteurCoupe(ardoise);
		btCoupe.addActionListener(ecoupe);
		JButton btLac = new JButton("Creer Lacs");
		EcouteurLac elac=new EcouteurLac(ardoise);
		btLac.addActionListener(elac);
		JButton btVoirLac = new JButton("Voir Lacs");
		EcouteurVoirLac evoirlac=new EcouteurVoirLac(ardoise);
		btVoirLac.addActionListener(evoirlac);
		JButton btMontagne = new JButton("Creer Montagnes");
		EcouteurMontagne emont=new EcouteurMontagne(ardoise);
		btMontagne.addActionListener(emont);
		JButton btPerlin = new JButton("Perlin");
		EcouteurPerlin eperl=new EcouteurPerlin(ardoise);
		btPerlin.addActionListener(eperl);	
		JButton btProj = new JButton("Perspective");
		EcouteurProj eproj=new EcouteurProj(ardoise);
		btProj.addActionListener(eproj);
		
		JButton btEffacer = new JButton("Effacer");
		EcouteurEffacer eceff=new EcouteurEffacer(ardoise);
		btEffacer.addActionListener(eceff);	
		
		JButton btNewCarte = new JButton("New Carte Diamant");
		EcouteurNewCarte enew=new EcouteurNewCarte(ardoise);
		btNewCarte.addActionListener(enew);	

		JButton btNewCarteMilieu = new JButton("New Carte Milieu");
		EcouteurNewCarteMilieu enewmilieu=new EcouteurNewCarteMilieu(ardoise);
		btNewCarteMilieu.addActionListener(enewmilieu);		
		
		JButton btDijkstra = new JButton("Plus court chemin");
		EcouteurDijkstra edijks=new EcouteurDijkstra(ardoise);
		btDijkstra.addActionListener(edijks);
		JButton btSoleil = new JButton("Soleil On/off");
		EcouteurSoleil esol=new EcouteurSoleil(ardoise);
		btSoleil.addActionListener(esol);		
		
		JSpinner spinner = new JSpinner();
		SpinnerNumberModel model = new SpinnerNumberModel();
		//model.setMaximum(20);
		//model.setMinimum(0);
		model.setValue(2);		
		spinner.setModel(model);
		EcouteurSpinner eccspin=new EcouteurSpinner(ardoise);
		spinner.addChangeListener(eccspin);	
		
		JSpinner spinnerOx = new JSpinner();
		SpinnerNumberModel modelOx = new SpinnerNumberModel();
		//model.setMaximum(20);
		//model.setMinimum(-10000);
		modelOx.setValue(-500);	Carte.bgraph.set_valeur_Ox(-500);	
		spinnerOx.setModel(modelOx);
		EcouteurSpinnerOx eccspinOx=new EcouteurSpinnerOx(ardoise);
		spinnerOx.addChangeListener(eccspinOx);	
		
		JSpinner spinnerOy = new JSpinner();
		SpinnerNumberModel modelOy = new SpinnerNumberModel();
		//model.setMaximum(20);
		//model.setMinimum(-10000);
		modelOy.setValue(-500);	Carte.bgraph.set_valeur_Oy(-500);	
		spinnerOy.setModel(modelOy);
		EcouteurSpinnerOy eccspinOy=new EcouteurSpinnerOy(ardoise);
		spinnerOy.addChangeListener(eccspinOy);	

		JSpinner spinnerOz = new JSpinner();
		SpinnerNumberModel modelOz = new SpinnerNumberModel();
		//model.setMaximum(20);
		//model.setMinimum(-10000);
		modelOz.setValue(350);	Carte.bgraph.set_valeur_Oz(350);	
		spinnerOz.setModel(modelOz);
		EcouteurSpinnerOz eccspinOz=new EcouteurSpinnerOz(ardoise);
		spinnerOz.addChangeListener(eccspinOz);		
		
		JSpinner spinnerdist = new JSpinner();
		SpinnerNumberModel modeldist = new SpinnerNumberModel();
		//model.setMaximum(20);
		//model.setMinimum(-10000);
		modeldist.setValue(1100);	Carte.bgraph.set_valeur_dist(1100);	
		spinnerdist.setModel(modeldist);
		EcouteurSpinnerdist eccspindist=new EcouteurSpinnerdist(ardoise);
		spinnerdist.addChangeListener(eccspindist);			

		JSpinner spinnerdeg = new JSpinner();
		SpinnerNumberModel modeldeg = new SpinnerNumberModel();
		modeldeg.setMaximum(90);
		modeldeg.setMinimum(-90);
		modeldeg.setValue(45);	Carte.bgraph.set_valeur_deg(45);	
		spinnerdeg.setModel(modeldeg);
		EcouteurSpinnerdeg eccspindeg=new EcouteurSpinnerdeg(ardoise);
		spinnerdeg.addChangeListener(eccspindeg);			

		JSpinner spinnerdegsoleil = new JSpinner();
		SpinnerNumberModel modeldegsoleil = new SpinnerNumberModel();
		modeldegsoleil.setMaximum(90);
		modeldegsoleil.setMinimum(-90);
		modeldegsoleil.setValue(45);	Carte.bgraph.set_valeur_deg(45);	
		spinnerdegsoleil.setModel(modeldegsoleil);
		EcouteurSpinnerdegsoleil eccspindegsoleil=new EcouteurSpinnerdegsoleil(ardoise);
		spinnerdegsoleil.addChangeListener(eccspindegsoleil);		

		JSpinner spinnerhauteurpcc = new JSpinner();
		SpinnerNumberModel modelhauteurpcc = new SpinnerNumberModel();
		//modelhauteurpcc.setMaximum(90);
		//modelhauteurpcc.setMinimum(-90);
		modelhauteurpcc.setValue(10000);	Carte.bgraph.set_valeur_hauteurpcc(10000);	
		spinnerhauteurpcc.setModel(modelhauteurpcc);
		EcouteurSpinnerHauteurpcc eccspinhauteurpcc=new EcouteurSpinnerHauteurpcc(ardoise);
		spinnerhauteurpcc.addChangeListener(eccspinhauteurpcc);				

		JSpinner spinnerpentepcc = new JSpinner();
		SpinnerNumberModel modelpentepcc = new SpinnerNumberModel();
		//modelpentepcc.setMaximum(90);
		//modelpentepcc.setMinimum(-90);
		modelpentepcc.setValue(1000);	Carte.bgraph.set_valeur_pentepcc(1000);	
		spinnerpentepcc.setModel(modelpentepcc);
		EcouteurSpinnerPentepcc eccspinpentepcc=new EcouteurSpinnerPentepcc(ardoise);
		spinnerpentepcc.addChangeListener(eccspinpentepcc);				
		
		
		JButton bconsigne= new JButton("Consigne:");
		JButton baltitude= new JButton("Altitude:");
		JButton bOx= new JButton("Ox:");
		JButton bOy= new JButton("Oy:");
		JButton bOz= new JButton("Oz:");
		JButton bdist= new JButton("distance:");
		JButton bdeg= new JButton("degres:");
		JButton btbidon= new JButton("");
		
		boite.setLayout(new GridLayout(25,1));
		
		boite.add(btBasique);
		//boite.add(btCoupe);
		boite.add(btLac);
		boite.add(btVoirLac);
		boite.add(btMontagne);
		boite.add(btPerlin);
		//boite.add(btEffacer);
		boite.add(bconsigne);
		boite.add(spinner);	
		boite.add(baltitude);
		boite.add(baffichalt);
		
		boite.add(btProj);
		//boite.add(bOx);
		boite.add(spinnerOx);
		//boite.add(bOy);
		boite.add(spinnerOy);	
		//boite.add(bOz);
		boite.add(spinnerOz);	
		boite.add(bdist);
		boite.add(spinnerdist);		
		boite.add(bdeg);
		boite.add(spinnerdeg);			
		boite.add(btSoleil);
		boite.add(spinnerdegsoleil);
		boite.add(btDijkstra);
		boite.add(spinnerhauteurpcc);
		boite.add(spinnerpentepcc);
		//boite.add(btbidon);
		boite.add(btNewCarte);
		boite.add(btNewCarteMilieu);
		
		panneau.add(boite);
		container.add(panneau);
		
		frame.pack();
		frame.setVisible(true);	
	}
	
	public static void initialiser_carte() {
		Carte.pcc=null;
		for (int i=0;i<Carte.Xwidth;i++) {
			for (int j=0;j<Carte.Yheight;j++) {
				Carte.eauCarte[i][j]=false;
				Carte.couleurCarte[i][j]=new Color(0,0,0);
			}	
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
			
			try {
				createUI();  //lancement procedure principale pour afficher
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			initialiser_carte();
			CarteDiamond Cdiam=new CarteDiamond(0,50,0,50,250,43); //(0,50,0,50,250,43)
			CarteDiamond.calculerDiamond();
			//ConstruireCarte.ecrireFichier();
			CarteDiamond.etirer_carte();
			
			/*
			initialiser_carte();
			CarteMilieu Cmil=new CarteMilieu(0,50,0,50,140,40); //(0,50,0,50,250,43)
			CarteMilieu.calculerMilieu();
			//ConstruireCarte.ecrireFichier();
			CarteMilieu.etirer_carte();
			*/
	}
	
	

}
