package drawer;

import function.Function;

public interface Handle {
	public Context getContext();
	public void setContext(Context ctx);
	
	public Function getFunction();
	public void setFunction(Function func);
	public void setFunction(String expr);
	
	public void setColor(int n, int color);
	public void selectColor(int n);
	
	public void setLevelCount(int n);
	
	public void showAbout();
	
	public void update();
	public void redraw();
}
