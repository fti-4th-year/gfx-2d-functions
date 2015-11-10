package drawer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import function.Function;

public class MainPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	Function function = null;
	
	private Canvas canvas = null;
	
	double zoom = 1.0;
	
	public MainPanel(int w, int h) {
		setPreferredSize(new Dimension(w, h));
		
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				e.getWheelRotation();
			}
		});
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				canvas = new Canvas(getWidth(), getHeight());
				update();
				repaint();
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.clearRect(0, 0, getWidth(), getHeight());
		if(canvas != null) {
			g.drawImage(canvas.getImage(), 0, 0, null);
		}
	}
	
	public void setFunction(Function f) {
		function = f;
	}
	
	public void update() {
		canvas.setFunction(function);
		canvas.renderColorMap();
		canvas.renderContourLines();
	}
}
