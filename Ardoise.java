
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class Ardoise extends JPanel {
	private BufferedImage image;
	public BGraphics gardoise;
	int largeur,hauteur;
	Controleur controleur;
	
	public Ardoise(int largeur, int hauteur) {
		this.largeur=largeur;
		this.hauteur=hauteur;
		Dimension d = new Dimension(largeur,hauteur);
		this.setPreferredSize(d);
		image=new BufferedImage(largeur,hauteur,BufferedImage.TYPE_INT_RGB);
		gardoise=new BGraphics(image);
	}

	public void paintComponent(Graphics g) {
		//System.out.println("paint Component");
	
		g.drawImage(image, 0, 0, null);
	}

	public void effacer() {
		gardoise.clearRect(0,0,largeur,hauteur);
		repaint();
	}
	
	public void lierControleur(Controleur c) {
		this.addMouseListener(c);
		this.addMouseMotionListener(c);
		this.controleur=c;
	}
	
	public void delierControleur() {
		if (this.controleur !=null) {
			this.removeMouseListener(this.controleur);
			this.removeMouseMotionListener(this.controleur);
		}
	}	
	
	public void set_valeur_spin(int a) {
		gardoise.set_valeur_spin(a);
	}

	public void set_valeur_altitude(int a) {
		gardoise.set_valeur_altitude(a);
	}
	
	
	
}