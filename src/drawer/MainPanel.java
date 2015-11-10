package drawer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import function.Function;
import function.TransformFunction;

public class MainPanel extends JPanel implements Updateable {
	
	private static final long serialVersionUID = 1L;
	
	private Handle handle = null;
	private Canvas canvas = null;
	private TransformFunction func;
	
	public MainPanel(Handle hnd, int w, int h) {
		handle = hnd;
		
		setPreferredSize(new Dimension(w, h));
		
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int wr = e.getWheelRotation();
				double m = Math.pow(1.2, wr);
				func.scaleX *= m;
				func.scaleY *= m;
				handle.update();
			}
		});
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				canvas = new Canvas(getWidth(), getHeight());
				update();
			}
		});
		
		func = new TransformFunction();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.clearRect(0, 0, getWidth(), getHeight());
		if(canvas != null) {
			g.drawImage(canvas.getImage(), 0, 0, null);
		}
	}
	
	@Override
	public void update() {
		Function f = handle.getFunction();
		func.setFunction(f);
		Context c = handle.getContext();
		canvas.findMinMax(func, c);
		canvas.clear();
		if(c.flagColor)
			canvas.renderColorMap(func, c);
		if(c.flagContour)
			canvas.renderContourLines(func, c);
		repaint();
	}
}
