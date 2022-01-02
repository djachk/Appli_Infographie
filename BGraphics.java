import java.awt.Graphics;
import java.awt.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class BGraphics {
	
	
	Graphics bg;
	BufferedImage bi;
	int XMAX, YMAX;
	int valeurSpin=2;
	int valAltitude=100;
	int Ox,Oy,Oz,dist,deg,degsoleil,hauteurpcc,pentepcc;
	
	public BGraphics(BufferedImage bi) {
		 this.bi=bi;
		 this.bg=bi.getGraphics();
		 this.XMAX = bi.getWidth();
		 this.YMAX = bi.getHeight();
		 //System.out.println("dimensions bi"+ XMAX+" , "+YMAX);
	 }
	 void clearRect(int x, int y, int width, int height) {
		 bg.clearRect(x,y,width,height);
	 }
	 
	 void plot(int x, int y) {
		 bg.drawLine(x,y,x,y);	
	 }
	 

	 
	 void drawLine(int x1, int y1, int x2, int y2) {
		 bg.drawLine(x1, y1, x2, y2);
	 }
	 
	public void set_valeur_spin(int a) {
		valeurSpin=a;
	}
	
	public int get_valeur_spin() {
		return valeurSpin;
	}	

	public void set_valeur_Ox(int a) {
		Ox=a;
	}
	
	public int get_valeur_Ox() {
		return Ox;
	}	
	public void set_valeur_Oy(int a) {
		Oy=a;
	}
	
	public int get_valeur_Oy() {
		return Oy;
	}		
	public void set_valeur_Oz(int a) {
		Oz=a;
	}
	
	public int get_valeur_Oz() {
		return Oz;
	}		
	public void set_valeur_dist(int a) {
		dist=a;
	}
	
	public int get_valeur_dist() {
		return dist;
	}	
	public void set_valeur_deg(int a) {
		deg=a;
	}
	
	public int get_valeur_deg() {
		return deg;
	}	
	public void set_valeur_degsoleil(int a) {
		degsoleil=a;
	}
	
	public int get_valeur_degsoleil() {
		return degsoleil;
	}		
	public void set_valeur_hauteurpcc(int a) {
		hauteurpcc=a;
	}
	
	public int get_valeur_hauteurpcc() {
		return hauteurpcc;
	}		
	public void set_valeur_pentepcc(int a) {
		pentepcc=a;
	}
	
	public int get_valeur_pentepcc() {
		return pentepcc;
	}
	
	public int get_valeur_altitude() {
		return valAltitude;
	}	
	
	public void set_valeur_altitude(int a) {
		valAltitude=a;
		ProjetMain.baffichalt.setText(Integer.toString(a));
	}		
	
}
	
	

