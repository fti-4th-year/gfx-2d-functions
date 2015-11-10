package drawer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import function.Function;

public class Canvas {
	
	private int width;
	private int height;
	private BufferedImage image;
	
	public Canvas(int w, int h) {
		width = w;
		height = h;
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.getGraphics().setColor(new Color(0xffffff));
		image.getGraphics().fillRect(0, 0, image.getWidth(), image.getHeight());
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	private double sfx(double x) {
		return (x - width/2.0)/height;
	}
	private double sfy(double y) {
		return (height/2.0 - y)/height;
	}
	@SuppressWarnings("unused")
	private int fsx(double x) {
		return (int) (x*height + width/2.0);
	}
	@SuppressWarnings("unused")
	private int fsy(double y) {
		return (int) (- y*height - height/2.0);
	}
	
	private int mixColors(int c0, int c1, double p) {
		int rc = 0x000000;
		for(int i = 0; i < 3; ++i) {
			rc |= ((int) ((1.0 - p)*((c0 >> i*8) & 0xff) + p*((c1 >> i*8) & 0xff))) << 8*i;
		}
		return rc;
	}
	
	void findMinMax(Function func, Context ctx) {
		ctx.min = func.evaluate(0.0, 0.0);
		ctx.max = ctx.min;
		
		int dx = ctx.sizeMinmax, dy = ctx.sizeMinmax;
		for(int iy = dy/2; iy < height; iy += dy) {
			for(int ix = dx/2; ix < width; ix += dx) {
				double x = sfx(ix), y = sfy(iy);
				double val = func.evaluate(x, y);
				if(val < ctx.min)
					ctx.min = val;
				if(val > ctx.max)
					ctx.max = val;
			}
		}
	}
	
	private static int roundColorComp(int c) {
		return c > 0x7f ? 0xff : 0x00;
	}
	
	private static int roundColor(int color) {
		for(int i = 0; i < 3; ++i) {
			int c = (color >> 8*i) & 0xff;
			int rc = roundColorComp(c);
			color &= ~(0xff << 8*i);
			color |= (rc << 8*i);
		}
		return color;
	}
	
	private static int addColorComp(int a, int b, int i) {
		int c = Math.max(Math.min(((a >> 8*i) & 0xff) + b, 0xFF), 0x00);
		a &= ~(0xff << 8*i);
		a |= (c << 8*i);
		return a;
	}
	
	public void renderColorMap(Function func, Context ctx) {
		for(int iy = 0; iy < height; ++iy) {
			for(int ix = 0; ix < width; ++ix) {
				double x = sfx(ix), y = sfy(iy);
				double val = func.evaluate(x, y);
				if(val == val) {
					if(ctx.flagGrad) {
						double i = ctx.getFloatIndex(val);
						int ci = (int) Math.ceil(i);
						int fi = (int) Math.floor(i);
						double p = i - fi;
						image.setRGB(ix, iy, mixColors(ctx.colors[fi], ctx.colors[ci], p));
					} else {
						int index = ctx.getIndex(val);
						image.setRGB(ix, iy, ctx.colors[index]);
					}
				} else {
					image.setRGB(ix, iy, 0xFF0000 | (((ix/8) + (iy/8))%2)*0x00FFFF);
				}
			}
		}
		if(ctx.flagDither) {
			int[] next = new int[4];
			for(int iy = 1; iy < height - 1; ++iy) {
				for(int ix = 1; ix < width - 1; ++ix) {
					
					next[0] = image.getRGB(ix + 1, iy);
					next[1] = image.getRGB(ix - 1, iy + 1);
					next[2] = image.getRGB(ix,     iy + 1);
					next[3] = image.getRGB(ix + 1, iy + 1);
					
					int color = image.getRGB(ix, iy);
					for(int i = 0; i < 3; ++i) {
						int c = (color >> i*8) & 0xff;
						int rc = roundColor(c);
						color &= ~(0xff << 8*i);
						color |= (rc << 8*i);
						
						int qe = c - rc;
						next[0] = addColorComp(next[0], (qe*7)/16, i);
						next[1] = addColorComp(next[1], (qe*3)/16, i);
						next[2] = addColorComp(next[2], (qe*5)/16, i);
						next[3] = addColorComp(next[3], (qe*1)/16, i);
					}
					image.setRGB(ix, iy, color);
					
					image.setRGB(ix + 1, iy,     next[0]);
					image.setRGB(ix - 1, iy + 1, next[1]);
					image.setRGB(ix,     iy + 1, next[2]);
					image.setRGB(ix + 1, iy + 1, next[3]);
				}
			}
			for(int iy = 0; iy < height; ++iy) {
				image.setRGB(0, iy, roundColor(image.getRGB(0, iy)));
				image.setRGB(width - 1, iy, roundColor(image.getRGB(width - 1, iy)));
			}
			for(int ix = 0; ix < width; ++ix) {
				image.setRGB(ix, 0, roundColor(image.getRGB(ix, 0)));
				image.setRGB(ix, height - 1, roundColor(image.getRGB(ix, height - 1)));
			}
		}
	}
	
	private void drawLine(Graphics2D g2d, double x0, double y0, double x1, double y1) {
		g2d.drawLine((int) x0, (int) y0, (int) x1, (int) y1);
	}
	
	public void renderContourLines(Function func, Context ctx) {
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		if(ctx.flagColor) {
			g2d.setStroke(new BasicStroke(1));
			g2d.setColor(Color.BLACK);
		} else {
			g2d.setStroke(new BasicStroke(3));
		}
		
		int sx = width/ctx.sizeContour, sy = height/ctx.sizeContour;
		double dx = (double) width/(sx - 1), dy = (double) height/(sy - 1);
		double[] grid = new double[sx*sy];
		
		for(int iy = 0; iy < sy; ++iy) {
			for(int ix = 0; ix < sx; ++ix) {
				double x = sfx(ix*dx), y = sfy(iy*dy);
				double val = func.evaluate(x, y);
				grid[iy*sx + ix] = val;
				
				// drawLine(g2d, ix*dx, iy*dy, (ix + 1)*dx, iy*dy);
				// drawLine(g2d, ix*dx, iy*dy, ix*dx, (iy + 1)*dy);
			}
		}
		
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
					l[i] = ctx.getIndex(v[i]);
				}
				int cnt = 0;
				for(int i = 0; i < 4; ++i) {
					if(p[i] = (l[i] != l[(i + 1)%4])) {
						double nrs = ctx.getValue(0.5*(l[i] + l[(i + 1)%4]));
						z[i] = (nrs - v[i])/(v[(i + 1)%4] - v[i]);
						++cnt;
					}
				}
				if(!ctx.flagColor && cnt > 1) {
					int mi = Math.min(Math.min(l[0], l[1]), Math.min(l[2], l[3]));
					g2d.setColor(new Color(ctx.colors[mi]));
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
					double vc = func.evaluate(sfx(xc), sfy(yc));
					int lc = ctx.getIndex(vc);
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
	
	public void clear() {
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
	}
	
	public void clear(int x, int y, int w, int h) {
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(x, y, w, h);
	}
}
