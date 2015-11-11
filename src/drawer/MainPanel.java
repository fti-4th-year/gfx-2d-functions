package drawer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if(handle.getContext().flags.cursor) {
					handle.getContext().cursor = true;
					handle.getContext().x = e.getPoint().x;
					handle.getContext().y = e.getPoint().y;
					handle.update();
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				if(handle.getContext().flags.cursor) {
					handle.getContext().cursor = false;
					handle.update();
				}
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
		if(c.flags.color)
			canvas.renderColorMap(func, c);
		if(c.flags.contour)
			canvas.renderContourLines(func, c);
		if(c.flags.cursor && c.cursor) {
			canvas.findCursorValue(func, c);
			canvas.renderSingleContourLine(func, c);
		}
		repaint();
	}
}
