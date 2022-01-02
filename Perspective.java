import java.awt.Color;

public class Perspective {

	
	static class Point {
		int xp;int yp; int zp;
	}
	//point d'observation
	static int Ox,Oy,Oz;
	//vecteur normal
	static double nx,ny;
	//distance au plan de projection vertical
	static int d;
	static int phiSoleil=45,tetaSoleil;
	//z-buffer
	static int Xwidth=Carte.Xwidth;
	static int Yheight=Carte.Yheight;
	//z-buffer
	//static int zbuff[][]=new int[Xwidth][Yheight];
	static int zbuffX[]=new int[Xwidth];
	
	static int zbuffcacheX[][]=new int[Xwidth][Yheight];
	static int zbuffcacheY[][]=new int[Xwidth][Yheight];
	static int zbuffcacheZ[][]=new int[Xwidth][Yheight];
	
	//on calcule pour chaque point de la carte le point intersection avec le plan de projection
	//on change de repere et on affiche selon zbuffers

	static Point nlles_coord(double x, double y, double z) {
		Point res=new Point();
		res.xp=(int) ((ny)*x-nx*y);
		res.yp=(int)z;
		res.zp=(int) (-nx*x-ny*y);
		return res;
	}
	
	static void initialiser_proj(int O_x,int O_y,int O_z, int dist, int deg, int degsoleil) {
		int degres=deg;
		double phi=(Math.PI/180.0)*(double)degres;
		nx=Math.cos(phi);
		ny=Math.sin(phi);
		Ox=O_x;
		Oy=O_y;
		Oz=O_z;
		d=dist;
		tetaSoleil=degsoleil;  
	}
	
	/*static Point vectoriel(double x1,double y1,double z1,double x2,double y2,double z2) {
		Point res=new Point();
		res.xp=(int) (y1*z2-y2*z1);
		res.yp=(int)(-x1*z2+x2*z1);
		res.zp=(int)(x1*y2-x2*y1);
		return res;
	}*/
	static double distance2(int x1,int y1,int z1,int x2,int y2,int z2) {
		return ((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2) + (z1-z2)*(z1-z2));
	}
	
	
	static double angle(double x1,double y1,double z1,double x2,double y2,double z2) {
		double ps=x1*x2+y1*y2+z1*z2;
		double n1=Math.sqrt(x1*x1+y1*y1+z1*z1);
		double n2=Math.sqrt(x2*x2+y2*y2+z2*z2);
		double cosangle=ps/(n1*n2);
		double angle=Math.acos(cosangle);
		return angle;
	}
	
	static int calcul_reflexion(int i,int j) {
		double s1=Math.sin(Math.PI*tetaSoleil/180.0)*Math.cos(Math.PI*phiSoleil/180.0);
		double s2=Math.sin(Math.PI*tetaSoleil/180.0)*Math.sin(Math.PI*phiSoleil/180.0);
		double s3=Math.cos(Math.PI*tetaSoleil/180.0);
		double u1=(double)(Carte.altCarte[i][j]-Carte.altCarte[i+1][j]);
		double u2=(double)(Carte.altCarte[i+1][j]-Carte.altCarte[i+1][j+1]);
		double u3=1.0;
		double p1=i-Ox, p2=j-Oy, p3=Carte.altCarte[i][j]-Oz;
		double angleus=angle(u1,u2,u3,s1,s2,s3);
		
		
		double val=2*(u1*s1+u2*s2+u3*s3)/Math.sqrt(u1*u1+u2*u2+u3*u3);
		double sym1=val*u1-s1;
		double sym2=val*u2-s2;
		double sym3=val*u3-s3;
		double anglepsym=angle(-p1,-p2,-p3,sym1,sym2,sym3);
		
		double coeff2=Math.cos(anglepsym)*Math.cos(anglepsym);
		double coeff1=Math.cos(angleus);
		if (angleus>(Math.PI/2.0)) coeff1=0;
		if (anglepsym>(Math.PI/2.0)) coeff2=0; 
		if ((p1*u1+p2*u2+p3*u3)>0) {coeff1=0;coeff2=0;}  

		double nbase1=100.0;
		double nbase2=145.0;
		int ngris= (int)(nbase1*coeff1+nbase2*coeff2);
		if (ngris>255) ngris=255;
		//int ngris= (int)(255*coeff1);
		Color coul=new Color(ngris,ngris,ngris);
		int rgb=coul.getRGB();	
		return rgb;
	}

	
	static boolean cache(int i, int j,int xproj, int yproj) {
		double dici2=distance2(Ox,Oy,Oz,i,j,Carte.altCarte[i][j]);
		double dvue2=distance2(Ox,Oy,Oz,zbuffcacheX[Xwidth/2+xproj][Yheight/2+yproj],
										zbuffcacheY[Xwidth/2+xproj][Yheight/2+yproj],
										zbuffcacheZ[Xwidth/2+xproj][Yheight/2+yproj]);
		return dvue2<dici2;
	}
	
	static void calculer_perspective() {
		for (int i=1; i<Xwidth-1; i++) {
				zbuffX[i]=-Integer.MAX_VALUE;
		}
		for (int i=1; i<Xwidth-1; i++) {
			for (int j=1;j<Yheight-1;j++) {
				zbuffcacheZ[i][j]=-Integer.MAX_VALUE;
			}
		}		
		for (int i=1; i<Xwidth-1; i++) {
			for (int j=1;j<Yheight-1;j++) {
				double t= -((double)d)/(double)(nx*(i-Ox)+ny*(j-Oy));
				double xd=t*(i-Ox);
				double yd=t*(j-Oy);
				double zd=t*(Carte.altCarte[i][j]-Oz);
				Point proj=nlles_coord(xd,yd,zd);
				int xproj=proj.xp;
				int yproj=proj.yp;
				//double ps=(i-Ox)*nx+(j-Oy)*ny; 
				//boolean apres=(ps>Math.abs(d)); //pour n afficher que les points au dela de l ecran
				if ((xproj>-Xwidth/2)&&(xproj<Xwidth/2)&&(yproj>-Yheight/2)&&(yproj<Yheight/2)) {
					Color coul=Carte.couleurCarte[i][j];
					int rgb=coul.getRGB();
					if (!Carte.soleil) {  //pas de soleil
						int altici=Carte.altCarte[i][j];
						int altvue=zbuffcacheZ[Xwidth/2+xproj][Yheight/2+yproj];
						if ((altvue==-Integer.MAX_VALUE) || (!cache(i,j,xproj,yproj))) {
							zbuffcacheX[Xwidth/2+xproj][Yheight/2+yproj]=i;
							zbuffcacheY[Xwidth/2+xproj][Yheight/2+yproj]=j;
							zbuffcacheZ[Xwidth/2+xproj][Yheight/2+yproj]=altici;
							Carte.bgraph.bi.setRGB(Xwidth/2+xproj,Yheight/2- yproj,rgb);
						} 
						
						
						if (((i%7==0)&&(int)zd>zbuffX[Xwidth/2+xproj])&&(coul!=Color.RED)) {
							zbuffX[Xwidth/2+xproj]=(int)zd;						
							Carte.bgraph.plot(Xwidth/2+xproj,Yheight/2- yproj);
						}					
					} else {  // avec soleil
						int altici=Carte.altCarte[i][j];
						int altvue=zbuffcacheZ[Xwidth/2+xproj][Yheight/2+yproj];
						if ((altvue==-Integer.MAX_VALUE) || (!cache(i,j,xproj,yproj))) {
							zbuffcacheX[Xwidth/2+xproj][Yheight/2+yproj]=i;
							zbuffcacheY[Xwidth/2+xproj][Yheight/2+yproj]=j;
							zbuffcacheZ[Xwidth/2+xproj][Yheight/2+yproj]=altici;
							rgb=calcul_reflexion(i,j);
							Carte.bgraph.bi.setRGB(Xwidth/2+xproj,Yheight/2- yproj,rgb);							
						} 
					}
					
					
				}
	
		}
	
	}
	}
}
