import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class ControleurLac extends Controleur{

	private int x, y;
	
	public ControleurLac(Ardoise ardoise, BGraphics g) {
		super(ardoise, g);
		this.ardoise.effacer();
		ConstruireCarte.representer_coupe();
		this.ardoise.repaint();
		
	}

	public void mouseMoved(MouseEvent e) {
		//System.out.println(e.getX()+" "+e.getY());
		x = e.getX();
		y = e.getY();
		ConstruireCarte.montrer_altitude(x, Carte.Yheight-y);
		//this.ardoise.repaint();
	}
	
	
	
	public void mousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		ConstruireCarte.remplir_smith(x,y);   
		this.ardoise.repaint();
	}
}

