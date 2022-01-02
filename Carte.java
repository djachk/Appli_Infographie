import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Carte {
	
	static int Xwidth=1100,Yheight=700;
	static int altCarte[][]=new int[Xwidth][Yheight];  //altitude en chaque point
	static int projCarte[][]=new int[Xwidth][Yheight];  //carte projetee en perspective
	static boolean eauCarte[][]=new boolean[Xwidth][Yheight]; 
	static Color couleurCarte[][]=new Color[Xwidth][Yheight];
	
	static BGraphics bgraph;
	static  ArrayList<Integer> pcc;
	static Dijkstra dj = new Dijkstra();
	static boolean soleil=false;
}
