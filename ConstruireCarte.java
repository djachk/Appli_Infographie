import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//import BGraphics.Point;

//import BGraphics.Point;

public class ConstruireCarte {
	static CarteDiamond C;
	//static CarteMilieu Cmil;
	static int Xwidth=Carte.Xwidth,Yheight=Carte.Yheight;
	static BGraphics bgraph=Carte.bgraph;

	public static int alt_max() {
		int max=-Integer.MAX_VALUE;
		for (int i=0;i<Xwidth;i++) {
			for (int j=0;j<Yheight;j++) {
				if(Carte.altCarte[i][j]>max)
				{
					max=Carte.altCarte[i][j];
				}
		}
		}
		return max;
	}
	
	
	public static void afficher_carte() {
		int zbuf[]=new int[Xwidth];
		int altMax=alt_max();
		for (int i=0;i<Xwidth;i++) {
			zbuf[i]=Yheight;
		}
		int prevX=0,prevY=0;
		for (int j=0;j<Carte.Xwidth-1;j++) {   //C.dimX
			if (j>0 && j<Carte.Yheight-1 &&j%5==0) {  //C.dimY
				for (int i=0;i<Carte.Xwidth;i++) {  //dimX 
					if ((i>0))  {
						int altold=Yheight-prevY/2 - Carte.altCarte[prevX][prevY];
						int altnew=Yheight -j/2 - Carte.altCarte[i][j];						
						if (altnew<zbuf[i]) {
							bgraph.drawLine(prevX, altold,i, altnew);
							zbuf[i]=altnew;
						}
					}
					prevX=i;
				}
			}
			prevY=j;
		}
	}
	
	
	public static void afficher_carte_carree() {
		int zbuf[]=new int[Xwidth];
		for (int i=0;i<Xwidth;i++) {
			zbuf[i]=Yheight;
		}
		int prevX=0,prevY=0;
		for (int j=0;j<C.dim-1;j++) {
			if (j>0 && j<C.dim-1 &&j%5==0) {
				for (int i=0;i<C.dim;i++) {
					if ((i>0))  {
						int altold=Yheight-prevY/2 - C.alt[prevX][prevY];
						int altnew=Yheight -j/2 - C.alt[i][j];
						if (altnew<zbuf[i]) {
							bgraph.drawLine(prevX, altold,i, altnew);
							zbuf[i]=altnew;
						}
					}
					prevX=i;
				}
			}
			prevY=j;
		}
	}
	
	static public void ecrireFichier() throws FileNotFoundException, UnsupportedEncodingException {
	PrintWriter writer = new PrintWriter("carte.txt", "UTF-8");
	writer.println(Xwidth);
	for (int i=0;i<Xwidth;i++) {
		for (int j=0;j<Yheight;j++) {
			writer.println(i);
			writer.println(j);
			writer.println(Carte.altCarte[i][j]);
		}
	}
	writer.close();
}

	static public void creer_sommet(int xSommet, int ySommet, int deltaAltSommet) {
		if (Carte.eauCarte[xSommet][ySommet]) return;
		double r=100.0;
		for (int i=0;i<Xwidth;i++) {
			for (int j=0;j<Yheight;j++) {
				double dist2=(double) ( (i-xSommet)*(i-xSommet) + (j-ySommet)*(j-ySommet));
				double dist=Math.sqrt(dist2);
				if ((dist2 < r*r)&&(!Carte.eauCarte[i][j])) {
					//double temp=coeffHomot* (  (double) Carte.altCarte[i][j]  )   ;
					double temp=(double) deltaAltSommet;
					//double ratio2=(double)dist2 / (double) (r*r);
					double ratio=(double)dist / (double) (r);
					//double ratio2=Math.pow(ratio, 2.0);
					Carte.altCarte[i][j]+=(int) (temp*  Math.pow((1.0-ratio),1.0)    )  ;
				}
				
			}
		}		
		
	}// fin creer sommet
	
	
	public static void representer_coupe() {
		int limite=Carte.bgraph.valeurSpin;
		for (int i=0;i<Xwidth;i++) {
			for (int j=0;j<Yheight;j++) {
				if (Carte.altCarte[i][j]>=limite) {
					Carte.bgraph.plot(i, Yheight-j);
				}
			}
		}
	}

	public static void voir_lacs() {
		for (int i=0;i<Xwidth;i++) {
			for (int j=0;j<Yheight;j++) {
				if (Carte.eauCarte[i][j]) {
					Carte.bgraph.plot(i, Yheight-j);
				}
			}
		}
	}	
	
	
	
	
	public static boolean xyvalides(int x,int y) {
		return ((x>0) && (x<Carte.Xwidth) && (y>0) && (y<Carte.Yheight)); 
	}
	
	public static void montrer_altitude(int x,int y) {
		if (!xyvalides(x,y)) return;
		int alt=Carte.altCarte[x][y];
		Carte.bgraph.set_valeur_altitude(alt);
	}
	
	
// remplissage lac
	
	// ****debut Smith***********************************
	
	public static class Point {
		int x,y;
		//boolean allume=false;
		Point(int x0, int y0) {
			x=x0;
			y=y0;
		}
	}

	static ArrayList<Point> pile=new ArrayList<Point>();
	
	static boolean [][]allume=new boolean [bgraph.XMAX][bgraph.YMAX];

	static public void allumer(int x,int y) {
		bgraph.plot(x,y);
		if (xyvalides(x,y)) {
			Carte.eauCarte[x][Carte.Yheight-y]=true;
			Carte.altCarte[x][Carte.Yheight-y]=bgraph.get_valeur_spin();
		}
	}
	
	static public boolean estAllume(int x, int y) {
		return (bgraph.bi.getRGB(x, y) & 0xfff) != 0;
	}
	
	static public void empiler(Point P) {
		//System.out.println("j'empile ");
		pile.add(P);
	}
	static public Point depiler() {
		//System.out.println("je depile ");
		int s=pile.size();
		//System.out.println("avant get");
		Point p=pile.get(s-1);
		//System.out.println("apres get ");
		pile.remove(s-1);
		return p;
	}
	
	static public boolean interieur(int x,int y) {
		//System.out.println("interieur");
		return x>= 0 && x<bgraph.XMAX && y >= 0 && y<bgraph.YMAX && !estAllume(x,y); 
	}
	
	/*public boolean interieur(int x,int y) {
		//System.out.println("interieur");
		return x<XMAX && y<YMAX && !allume[x][y]; 
	}*/

	
	static public boolean interieurgauche(int x,int y) {
		return interieur(x,y) && !interieur(x-1,y);
	}
	
	static public boolean interieurdroit(int x,int y) {
		return interieur(x,y) && !interieur(x+1,y);
	}

	static public void Ajouts(int xg, int xd, int y) {
		int xdd=xd;
		while(interieur(xdd,y)) {
			xdd++;
		}
		while(xg<=xdd) {
			while(xg<=xdd && !interieur(xdd,y)) {
				xdd--;
			}
			if (xg<=xdd) {
				Point p=new Point(xdd,y);
				empiler(p);
				while(xg<=xdd && interieur(xdd,y)) {
					xdd--;
				}
			}	
		}
	}
	
	static public void remplir_smith(int x0, int y0) {
		Point pc;
		pile.clear();
		if (!interieur(x0,y0)) return;
		int xd=x0; int y;  int xg;                                 //Point p=new Point(xd,y0);
		while(!interieurdroit(xd,y0)) {
			xd++;
		}
		Point p=new Point(xd,y0);
		//System.out.println("(b)" + xd+ " " + y0);
		empiler(p);
		
		while (!pile.isEmpty()) {
			pc=depiler();
			xd=pc.x; y=pc.y; xg=xd;
			if (estAllume(xd, y))
				continue;
			while(!interieurgauche(xg,y)) {
				xg--;
			}
			for(int i=xg;i<=xd;i++) {
				allumer(i,y);
			}
			if (y>0) Ajouts(xg,xd,y-1);
			if (y<bgraph.YMAX) Ajouts(xg,xd,y+1);
		
		//plot(x0,y0);
		}	
	
	
	}

	//fin Smith
	
	
	

}//fin class ConstruireCarte
