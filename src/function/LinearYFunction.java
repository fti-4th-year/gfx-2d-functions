package function;

public class LinearYFunction implements Function {
	private double min, max;
	
	public LinearYFunction(double a, double b) {
		min = a;
		max = b;
	}

	@Override
	public double evaluate(double x, double y) {
		return 0.5*y*(max - min) + 0.5*(max + min);
	}
}
