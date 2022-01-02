import java.awt.Graphics;
import java.awt.event.MouseAdapter;

public abstract class Controleur extends MouseAdapter{
	Ardoise ardoise;
	BGraphics g;
	
	public Controleur(Ardoise ardoise, BGraphics g) {
		this.ardoise=ardoise;
		this.g=g;
	}


}
