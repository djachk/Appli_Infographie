import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class ControleurDijkstra extends Controleur {


	private int x, y,prevX,prevY;
	private int nbpoints=0,ind=0;
	private int xpoints[]=new int[2];
	private int ypoints[]=new int[2];
	
	public ControleurDijkstra(Ardoise ardoise, BGraphics g) {
		super(ardoise, g);
	}

	public void mouseMoved(MouseEvent e) {
		//System.out.println(e.getX()+" "+e.getY());
		x = e.getX();
		y = e.getY();
		ConstruireCarte.montrer_altitude(x, Carte.Yheight-y);
		//this.ardoise.repaint();
	}	
	
	public void mousePressed(MouseEvent e) {
		if (nbpoints>1) return;
		x = e.getX();
		y = e.getY();
		xpoints[ind]=x;
		ypoints[ind]=y;
		ind++;
		nbpoints++;
		if (nbpoints==2) {
			System.out.println("pcc entre x="+xpoints[0]+" y="+ypoints[0]+" et x="+xpoints[1]+" y="+ypoints[1]);
			int h=Carte.bgraph.get_valeur_hauteurpcc();
			double p=((double)Carte.bgraph.get_valeur_pentepcc())/100.0;
			Carte.dj.setAretes(h,p);
			//Carte.dj.setAretesHauteur(200);
			//Carte.dj.setAretesPente(10.0);
			Carte.pcc=Carte.dj.searchShortestPath(xpoints[0],Carte.Yheight-ypoints[0],xpoints[1],Carte.Yheight-ypoints[1]);
		}
	}
	
	
	
	
}
