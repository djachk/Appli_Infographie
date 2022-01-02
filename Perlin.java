import java.awt.Color;
import java.util.Iterator;
import java.util.Random;

public class Perlin {
	static int Xwidth=Carte.Xwidth, Yheight=Carte.Yheight;
	static int pasX=20, pasY=10;
	static int nbrePasX, nbrePasY;
	static double gradientX[][]=new double[Xwidth][Yheight];
	static double gradientY[][]=new double[Xwidth][Yheight];
	static Random r;
	
	static void initialiser_perlin(int pX,int pY) {
		pasX=pX;
		pasY=pY;
	}
	
	static void generer_gradients() {
		for (int i=0; i<Xwidth; i++) {
			for (int j=0;j<Yheight;j++) {
				if ((i%pasX==0)&&(j%pasY==0)) {
					double teta=r.nextDouble()*2*Math.PI;
					gradientX[i][j]=Math.cos(teta);
					gradientY[i][j]=Math.sin(teta);
					//donner un niveau de gris ici et initialsier couleur
				}
			}
		}
	}
	
	
	static double interpoler(double a0, double a1, double w) {
		return (1.0-w)*a0 + w*a1;
	}
	
	static double produit_scalaire(int gx, int gy, int x, int y) {
		double dx=(double) (x-gx);
		double dy=(double) (y-gy);
		return dx*gradientX[gx][gy] + dy*gradientY[gx][gy];
	}
	
	static double bruit_perlin (int x, int y) {
		int x0=(x/pasX)*pasX;
		int x1=x0+pasX; if (x1>=Xwidth) return 0.0;
		int y0=(y/pasY)*pasY; 
		int y1=y0+pasY;	if (y1>=Yheight) return 0.0;
		
		double sx=(double)(x-x0)/(double)pasX;
		double sy=(double)(y-y0)/(double) pasY;
		
		double n0,n1,ix0,ix1,value;
		n0=produit_scalaire(x0,y0,x,y);
		n1=produit_scalaire(x1,y0,x,y);
		ix0=interpoler(n0,n1,sx);

		n0=produit_scalaire(x0,y1,x,y);
		n1=produit_scalaire(x1,y1,x,y);
		ix1=interpoler(n0,n1,sx);	
		
		value=interpoler(ix0,ix1,sy);
		return value;
	}

	static void dessiner_perlin() {
		for (int i=1; i<Xwidth-1; i++) {
			for (int j=1;j<Yheight-1;j++) {
				Color couleur=Carte.couleurCarte[i][j];
				int rgb = couleur.getRGB();
				Carte.bgraph.bi.setRGB(i,Yheight-j, rgb);
			}
		}
	}
	
	
	static void calculer_perlin() {
		double dpasX=(double)pasX;
		double dpasY=(double)pasY;
		long seed=(long) Carte.bgraph.get_valeur_spin();
		r=new Random(seed);
		int ngris;
		initialiser_perlin(15,10);  //15,10
		generer_gradients();
		for (int i=1; i<Xwidth-1; i++) {
			for (int j=1;j<Yheight-1;j++) {
				double noise=bruit_perlin(i,j)/Math.sqrt(dpasX*dpasX+dpasY*dpasY);
				ngris=(int) ((double)128*noise) + 127;
				Color couleur=new Color(ngris,ngris,ngris);
				//int rgb = couleur.getRGB();
				//if (i%pasX==0) System.out.println("i= "+i+" j= "+j+" perlin= "+ngris);
				//Carte.bgraph.bi.setRGB(i,Yheight-j, rgb);
				if (Carte.eauCarte[i][j]) { 
					Carte.couleurCarte[i][j]=new Color(128,128,200);
				} //else if (Carte.altCarte[i][j]<100) {
				  //	Carte.couleurCarte[i][j]=new Color(102,255,153);
				//}  				
				else {
					Carte.couleurCarte[i][j]=new Color(ngris,ngris,ngris);
				}
				//if (Carte.pcc!=null) {
				//	for (int p=0;p<Carte.pcc.size();p++) {
				//		int xpcc=Carte.dj.getX(Carte.pcc.get(p));
				//		int ypcc=Carte.dj.getY(Carte.pcc.get(p));
				//		Carte.couleurCarte[xpcc][ypcc]=new Color(255,0,0);					
				//	}
				//}
		}
	  }/////
	  if (Carte.pcc!=null) {
		for (int p=0;p<Carte.pcc.size();p++) {
			int xpcc=Carte.dj.getX(Carte.pcc.get(p));
			int ypcc=Carte.dj.getY(Carte.pcc.get(p));
			//Carte.couleurCarte[xpcc][ypcc]=new Color(255,0,0);	
			Carte.couleurCarte[xpcc][ypcc]=Color.RED;
		}
	  }
		
	}
}
