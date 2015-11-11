package drawer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Context {
	public int num;
	public int[] colors;
	public int lineColor = 0x000000;
	
	public double min = 0.0;
	public double max = 0.0;
	
	public int sizeMinmax = 0x4;
	
	static public class Grid {
		public int numX = 12, numY = 8;
	};
	
	Grid grid;
	
	static public class Flags {
		public boolean contour  = true;
		public boolean cursor   = true;
		public boolean grid     = false;
		public boolean color    = true;
		public boolean gradient = false;
		public boolean dither   = false;
	};
	
	Flags flags;
	
	public int x = 100, y = 100;
	public boolean cursor = false;
	public double cval = 0.0;
	
	public Context(int n) throws Exception {
		
		grid = new Grid();
		flags = new Flags();
		
		URL url = getClass().getResource("config/config");
		if(url == null) {
			throw new Exception("cannot find config file");
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		
		String line;
		int stageNum = 0, colorNum = 0;
		while((line = br.readLine()) != null) {
			line = line.split("//")[0].trim().replaceAll("\\s+", " ");
			switch(stageNum) {
			case 0: {
				String[] nums = line.split(" ");
				grid.numX = Integer.parseInt(nums[0]);
				grid.numY = Integer.parseInt(nums[1]);
				break;
			}
			case 1: {
				num = Integer.parseInt(line);
				colors = new int[num];
				break;
			}
			default: {
				String[] nums = line.split(" ");
				int c = 0;
				for(int i = 0; i < 3; ++i) {
					c |= Integer.parseInt(nums[i]) << 8*i;
				}
				if(colorNum < num) {
					colors[colorNum] = c;
				} else if(colorNum == num) {
					lineColor = c;
				}
				break;
			}
			}
			if(stageNum < 2) {
				++stageNum;
			} else {
				++colorNum;
			}
		}
		
		br.close();
		
		/*
		num = n;
		colors = new int[num];
		for(int i = 0; i < num; ++i) {
			colors[i] = (int) (Math.random()*0xFFFFFF);
		}
		*/
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
