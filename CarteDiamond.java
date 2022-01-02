import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class CarteDiamond {

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
	
	public CarteDiamond( int coin0, int coin1, int coin2, int coin3, int alti,long seed) {
		alt[0][0]=coin0; alt[dim][0]=coin1; alt[dim][dim]=coin2; alt[0][dim]=coin3;
		this.seed=seed;
		this.alti=alti;
	}
	
	static public void calculerDiamond() {
		deltac=dim; //delta courant
		altc=alti;
		Random r = new Random(seed);
		int boucle=0;
		int compteurPoints=4;
		while (deltac>1) {
		boucle++;
		//System.out.println("boucle="+boucle+"   **********************");
		//diamond step
		for (int i=0;i<(dim/deltac);i++) {
			for (int j=0;j<(dim/deltac);j++) {
				alea= r.nextInt(altc+1);
				int x=i*deltac + deltac/2;
				int y=j*deltac + deltac/2;
				avg=(alt[x-deltac/2][y-deltac/2]+alt[x+deltac/2][y-deltac/2]+alt[x+deltac/2][y+deltac/2]+alt[x-deltac/2][y+deltac/2])/4;
				alt[x][y]=avg + alea;
				compteurPoints++;
			}
		}
		//square step
		//en haut et en bas
		for (int i=0;i<(dim/deltac);i++) {
			alea= r.nextInt(altc+1);
			int x=deltac/2 + i*deltac;
			int y=0;
			avg=(alt[x-deltac/2][y]+alt[x+deltac/2][y]+alt[x][y+deltac/2])/3;  // 3 points seulement
			alt[x][y]= avg + alea;
			compteurPoints++;
			
			for (int j=1;j<(dim/deltac);j++) {
				y=j*deltac;
				alea= r.nextInt(altc+1);
				avg=(alt[x][y-deltac/2]+alt[x][y+deltac/2]+alt[x-deltac/2][y]+alt[x+deltac/2][y])/4;
				alt[x][y]= avg + alea;
				compteurPoints++;
			}
			
			y=dim;
			alea= r.nextInt(altc+1);
			avg=(alt[x-deltac/2][y]+alt[x+deltac/2][y]+alt[x][y-deltac/2])/3;
			alt[x][y]= avg + alea;
			compteurPoints++;
		}
		//a gauche et a droite
		for (int j=0;j<(dim/deltac);j++) {
			//Random r = new Random();
			alea= r.nextInt(altc+1);
			int x=0;
			int y=deltac/2 + j*deltac;
			avg=(alt[x][y-deltac/2]+alt[x][y+deltac/2]+alt[x+deltac/2][y])/3;
			alt[x][y]= avg + alea;
			compteurPoints++;
			
			for (int i=1;i<(dim/deltac);i++) {
				x=i*deltac;
				alea= r.nextInt(altc+1);
				//System.out.println("alea="+alea);
				avg=(alt[x][y-deltac/2]+alt[x][y+deltac/2]+alt[x-deltac/2][y]+alt[x+deltac/2][y])/4;				//avg=(alt[x-deltac/2][y-deltac/2]+alt[x+deltac/2][y-deltac/2]+alt[x+deltac/2][y+deltac/2]+alt[x-deltac/2][y+deltac/2])/4;
				alt[x][y]= avg + alea;
				compteurPoints++;
				//System.out.println("x="+x+" y="+y+" alt="+alt[x][y]);
			} 
			
			x=dim;
			alea= r.nextInt(altc+1);
			//System.out.println("alea="+alea);
			avg=(alt[x][y-deltac/2]+alt[x-deltac/2][y]+alt[x][y+deltac/2])/3;
			alt[x][y]= avg + alea;
			compteurPoints++;
			//System.out.println("x="+x+" y="+y+" alt="+alt[x][y]);
		}
		//dans le carre => c'est fait!!

		deltac/=2;
		altc/=2;

		
	}
		System.out.println("compteur points=" + compteurPoints);
	}
	static public void afficher() {
		int compt=0;
		for (int j=0;j<=dim;j++) {
			for (int i=0;i<=dim;i++) {
				compt++;
				//System.out.println("compt="+compt+""+" j="+j+" i="+i+" "+alt[i][j]);
			}
		}	
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
