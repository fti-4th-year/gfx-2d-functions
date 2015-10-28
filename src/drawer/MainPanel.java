package drawer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import function.Function;

public class MainPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	Function function = null;
	
	int width;
	int height;
	private BufferedImage image;
	
	double zoom = 1.0;
	
	public MainPanel(int w, int h) {
		width = w;
		height = h;
		setPreferredSize(new Dimension(width, height));
		
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				e.getWheelRotation();
			}
		});
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.getGraphics().setColor(new Color(0xffffff));
		image.getGraphics().fillRect(0, 0, image.getWidth(), image.getHeight());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.clearRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, null);
	}
	
	public void setFunction(Function f) {
		function = f;
	}
	
	public Function getFunction() {
		return function;
	}
	
	private double sfx(double x) {
		return (x - width/2.0)/height;
	}
	private double sfy(double y) {
		return (y - height/2.0)/height;
	}
	private int fsx(double x) {
		return (int) (x*height + width/2.0);
	}
	private int fsy(double y) {
		return (int) (y*height + height/2.0);
	}
	
	int getIndex(double val, int num, double min, double max) {
		return Math.max(Math.min((int) Math.round(((val - min)/(max - min))*(num - 1)), num - 1), 0);
	}
	
	double getValue(double idx, int num, double min, double max) {
		return (idx/(num - 1))*(max - min) + min;
	}
	
	public void renderColorMap() {
		if(function == null)
			return;
		
		int[] levels = new int[9];
		levels[0] = 0x000000;
		levels[1] = 0x7f0000;
		levels[2] = 0x007f00;
		levels[3] = 0x00007f;
		levels[4] = 0x7f7f7f;
		levels[5] = 0xff0000;
		levels[6] = 0x00ff00;
		levels[7] = 0x0000ff;
		levels[8] = 0xffffff;
		
		double min = function.evaluate(0.0, 0.0);
		double max = min;
		
		int dx = 0x10, dy = 0x10;
		for(int iy = 0; iy < height; iy += dy) {
			for(int ix = 0; ix < width; ix += dx) {
				double x = sfx(ix), y = sfy(iy);
				double val = function.evaluate(x, y);
				if(val < min)
					min = val;
				if(val > max)
					max = val;
			}
		}
		
		for(int iy = 0; iy < height; ++iy) {
			for(int ix = 0; ix < width; ++ix) {
				double x = sfx(ix), y = sfy(iy);
				double val = function.evaluate(x, y);
				int index = getIndex(val, levels.length, min, max);
				image.setRGB(ix, iy, levels[index]);
			}
		}
	}
	
	private void drawLine(Graphics2D g2d, double x0, double y0, double x1, double y1) {
		g2d.drawLine((int) x0, (int) y0, (int) x1, (int) y1);
	}
	
	public void renderContourLines() {
		if(function == null)
			return;
		
		int num = 9;
		
		int sx = width/0x10, sy = height/0x10;
		double dx = (double) width/(sx - 1), dy = (double) height/(sy - 1);
		double[] grid = new double[sx*sy];
		
		double min = function.evaluate(0.0, 0.0);
		double max = min;
		
		for(int iy = 0; iy < sy; ++iy) {
			for(int ix = 0; ix < sx; ++ix) {
				double x = sfx(ix*dx), y = sfy(iy*dy);
				double val = function.evaluate(x, y);
				if(val < min)
					min = val;
				if(val > max)
					max = val;
				
				grid[iy*sx + ix] = val;
			}
		}
		
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(new Color(0x000000));
		
		int[] l = new int[4];
		double[] v = new double[4];
		double[] z = new double[4];
		boolean[] p = new boolean[4];
		
		double[] x = new double[4];
		double[] y = new double[4];
		
		for(int iy = 0; iy < sy - 1; ++iy) {
			for(int ix = 0; ix < sx - 1; ++ix) {
				for(int i = 0; i < 4; ++i) {
					v[i] = grid[(iy + i/2)*sx + (ix + Math.min(i%3, 1))];
					l[i] = getIndex(v[i], num, min, max);
				}
				int cnt = 0;
				for(int i = 0; i < 4; ++i) {
					if(p[i] = (l[i] != l[(i + 1)%4])) {
						double nrs = getValue(0.5*(l[i] + l[(i + 1)%4]), num, min, max);
						z[i] = (nrs - v[i])/(v[(i + 1)%4] - v[i]);
						++cnt;
					}
				}
				if(cnt == 2) {
					int c = 0;
					if(p[0]) {
						x[c] = (ix + z[0])*dx;
						y[c] = (iy + 0.0)*dy;
						++c;
					}
					if(p[1]) {
						x[c] = (ix + 1.0)*dx;
						y[c] = (iy + z[1])*dy;
						++c;
					}
					if(p[2]) {
						x[c] = (ix + 1.0 - z[2])*dx;
						y[c] = (iy + 1.0)*dy;
						++c;
					}
					if(p[3]) {
						x[c] = (ix + 0.0)*dx;
						y[c] = (iy + 1.0 - z[3])*dy;
						++c;
					}
					drawLine(g2d, x[0], y[0], x[1], y[1]);
				} else if(cnt == 4) {
					double xc = (ix + 0.5)*dx;
					double yc = (iy + 0.5)*dy;
					double vc = function.evaluate(sfx(xc), sfy(yc));
					int lc = getIndex(vc, num, min, max);
					if(l[0] == lc) {
						drawLine(g2d, (ix + z[0])*dx, (iy + 0.0)*dy, (ix + 1.0)*dx, (iy + z[1])*dy);
						drawLine(g2d, (ix + 1.0 - z[2])*dx, (iy + 1.0)*dy, (ix + 0.0)*dx, (iy + 1.0 - z[3])*dy);
					} else {
						drawLine(g2d, (ix + z[0])*dx, (iy + 0.0)*dy, (ix + 0.0)*dx, (iy + 1.0 - z[3])*dy);
						drawLine(g2d, (ix + 1.0)*dx, (iy + z[1])*dy, (ix + 1.0 - z[2])*dx, (iy + 1.0)*dy);
					}
				}
			}
		}
	}
}
