package drawer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

import function.LinearYFunction;

public class Legend extends JPanel implements Updateable {
	private static final long serialVersionUID = 1L;
	
	Canvas canvas;
	int level = -1;
	int num = 2;
	
	Handle handle;
	LinearYFunction linFunc;
	
	public Legend(Handle hnd, int w) {
		handle = hnd;
		
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(w, 100));
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				canvas = new Canvas(getWidth() - 2, getHeight());
				update();
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if(e.getPoint().x > getWidth()/2) {
					level = (int) Math.round(((double) e.getPoint().y/getHeight())*(num - 1));
				} else {
					level = -1;
				}
				repaint();
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				level = -1;
				repaint();
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(level >= 0) {
					handle.selectColor(num - level - 1);
					handle.update();
				}
			}
		});
		
		linFunc = new LinearYFunction();
	}
	
	@Override
	public void update() {
		Context c = handle.getContext();
		linFunc.min = c.min;
		linFunc.max = c.max;
		num = c.num;
		canvas.clear();
		if(c.flags.color)
			canvas.renderColorMap(linFunc, c);
			canvas.clear(0, 0, canvas.getWidth()/2, canvas.getHeight());
		if(c.flags.contour)
			canvas.renderContourLines(linFunc, c);
		repaint();
	}
	
	@Override
	public void redraw() {
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Context c = handle.getContext();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		if(canvas != null) {
			g.drawImage(canvas.getImage(), 2, 0, null);
			if(c.flags.cursor && c.cursor)
				canvas.renderSingleContourLine(linFunc, c, g);
			g.setColor(Color.BLACK);
			for(int i = 0; i < num - 1; ++i) {
				double val = (i + 0.5)/(num - 1)*(linFunc.max - linFunc.min) + linFunc.min;
				String vs = Double.toString(val);
				vs = vs.substring(0, Math.min(val < 0 ? 7 : 6, vs.length()));
				g.drawString(vs, 0, (int) (getHeight()*(num - i - 1.5)/(num - 1)) - 2);
			}
			if(c.flags.cursor && c.cursor) {
				double val = c.cval;
				String vs = Double.toString(val);
				vs = vs.substring(0, Math.min(val < 0 ? 7 : 6, vs.length()));
				g.setColor(Color.YELLOW);
				g.fillRect(0, (int) (getHeight()*(1.0 - (val - c.min)/(c.max - c.min))) - 13, 48, 12);
				g.setColor(Color.BLACK);
				g.drawString(vs, 0, (int) (getHeight()*(1.0 - (val - c.min)/(c.max - c.min))) - 2);
			}
		}
		if(level >= 0) {
			((Graphics2D) g).setStroke(new BasicStroke(4));
			g.setColor(new Color(0x000000));
			g.drawRect(2 + getWidth()/2, (int) (getHeight()*(level - 0.5)/(num - 1)), getWidth()/2 - 4, getHeight()/(num - 1));
		}
	}
}
