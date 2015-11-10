package drawer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import function.LinearYFunction;

public class SidePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	Canvas legend;

	public SidePanel(int w) {
		setPreferredSize(new Dimension(w, 100));
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				legend = new Canvas(32, getHeight() - 64);
				update();
				repaint();
			}
		});
	}
	
	public void update() {
		legend.setFunction(new LinearYFunction(0.0, 1.0));
		legend.renderColorMap();
		legend.renderContourLines();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.clearRect(0, 0, getWidth(), getHeight());
		if(legend != null) {
			g.drawImage(legend.getImage(), 160, 32, null);
		}
	}
}
