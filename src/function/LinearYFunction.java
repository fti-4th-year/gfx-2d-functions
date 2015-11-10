package function;

public class LinearYFunction implements Function {
	public double min = -0.5, max = 0.5;
	
	@Override
	public double evaluate(double x, double y) {
		return y*(max - min) + 0.5*(max + min);
	}
}
