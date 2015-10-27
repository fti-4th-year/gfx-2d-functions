package drawer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MainPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	int width;
	int height;
	private BufferedImage image;
	
	public MainPanel(int w, int h) {
		width = w;
		height = h;
		setPreferredSize(new Dimension(width, height));
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.getGraphics().setColor(new Color(0xffffff));
		image.getGraphics().fillRect(0, 0, image.getWidth(), image.getHeight());
	}
}
