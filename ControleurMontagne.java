import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class ControleurMontagne extends Controleur{

	private int x, y;
	
	public ControleurMontagne(Ardoise ardoise, BGraphics g) {
		super(ardoise, g);
		
	}
	
	public void mousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		//System.out.println("x= "+x); 
		//System.out.println("y= "+y); 
		//System.out.println("altsommet= "+Carte.bg.altitudeSommet);
		ConstruireCarte.creer_sommet(x,Carte.Yheight-y,Carte.bgraph.valeurSpin);
		this.ardoise.effacer();
		ConstruireCarte.afficher_carte();
		this.ardoise.repaint();
	}
}
