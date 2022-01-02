import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class CarteMilieu {

	static int alti=100;
	static int dim=512; //512 c est bien...
	static int alt[][]=new int[dim+1][dim+1];
	static int deltac, altc;
	static int alea,avg;
	static long seed; 
	
	//int dimX=1000, dimY=700;  //pour carte etiree 1200
	static int dimX=Carte.Xwidth, dimY=Carte.Yheight;  //pour carte etiree 1200
	//int altEtire[][]=new int[dimX][dimY]; 
	//static int altEtire[][]=Carte.altCarte;
	
	static double coeffX=1.0,coeffY=1.0;
	
	public CarteMilieu( int coin0, int coin1, int coin2, int coin3, int alti,long seed) {
		alt[0][0]=coin0; alt[dim][0]=coin1; alt[dim][dim]=coin2; alt[0][dim]=coin3;
		this.seed=seed;
		this.alti=alti;
	}
	
	static public void calculerMilieu() {
		deltac=dim; //delta courant
		altc=alti;
		Random r = new Random(seed);
		int boucle=0;
		int compteurPoints=4;
		while (deltac>1) {	
			boucle++;
			for (int i=0;i<=(dim/deltac);i++) {
				for (int j=0;j<(dim/deltac);j++) {
					//alea= r.nextInt(altc+1);
					alea=(int) (r.nextGaussian()*altc);
					int xcible=i*deltac;
					int ycible=deltac/2+j*deltac;
					int xc1=i*deltac;
					int yc1=j*deltac;
					int xc2=i*deltac;
					int yc2=(j+1)*deltac;
					
					avg=(alt[xc1][yc1] + alt[xc2][yc2])/2;
					alt[xcible][ycible]=avg + alea;
					compteurPoints++;
				}
			}
			for (int j=0;j<=(2*dim/deltac);j++) {
				for (int i=0;i<(dim/deltac);i++) {
					//alea= r.nextInt(altc+1);
					alea=(int) (r.nextGaussian()*altc);
					int xcible=deltac/2+ i*(deltac);
					int ycible=j*(deltac/2);
					int xc1=i*deltac;
					int yc1=j*(deltac/2);
					int xc2=(i+1)*deltac;
					int yc2=j*(deltac/2);
					
					avg=(alt[xc1][yc1] + alt[xc2][yc2])/2;
					alt[xcible][ycible]=avg + alea;
					compteurPoints++;
				}
			}
			deltac/=2;
			altc/=2;
		}
		System.out.println("compteur="+ compteurPoints);
	}
	
	
	static public void etirer_carte() {
		coeffX= ((double) dimX)/((double) dim);
		coeffY= ((double) dimY)/((double) dim);
		for(int i=0; i<dimX;i++) {
			for (int j=0;j<dimY;j++) {
				double xf=((double)i)/coeffX;
				double yf=((double)j)/coeffY;
				int x=(int) (xf);
				int y=(int) (yf);
				Carte.altCarte[i][j]=(int) ((xf-x)*(double)(alt[x][y]) + (x+1-xf)*(double)(alt[x+1][y]));

				Color coul=new Color(0,0,0); //init couleur
				Carte.couleurCarte[i][j]=coul;
				
			}
		}
		
	}
}
