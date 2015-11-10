package drawer;

public class Context {
	public int num;
	public int[] colors;
	
	public double min = 0.0;
	public double max = 0.0;
	
	public int sizeMinmax = 0x4;
	public int sizeContour = 0x4;
	
	public boolean flagContour = true;
	public boolean flagColor = true;
	public boolean flagGrad = false;
	public boolean flagDither = false;
	
	public Context(int n) {
		num = n;
		colors = new int[num];
		for(int i = 0; i < num; ++i) {
			colors[i] = (int) (Math.random()*0xFFFFFF);
		}
	}
	
	int getIndex(double val) {
		return (int) Math.round(getFloatIndex(val));
	}
	
	double getFloatIndex(double val) {
		return Math.max(Math.min(((val - min)/(max - min))*(num - 1), num - 1), 0);
	}
	
	double getValue(double idx) {
		return (idx/(num - 1))*(max - min) + min;
	}
	
	void setNum(int n) {
		int[] newColors = new int[n];
		for(int i = 0; i < Math.min(n, num); ++i) {
			newColors[i] = colors[i];
		}
		for(int i = num; i < n; ++i) {
			newColors[i] = (int) (Math.random()*0xFFFFFF);
		}
		colors = newColors;
		num = n;
	}
}
