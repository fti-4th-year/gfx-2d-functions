package function;

public class TransformFunction implements Function {
	private Function func = null;
	public double scaleX = 1.0, scaleY = 1.0;
	public double offsetX = 0.0, offsetY = 0.0;
	
	public void setFunction(Function f) {
		func = f;
	}
	
	public Function getFunction() {
		return func;
	}
	
	@Override
	public double evaluate(double x, double y) {
		return func.evaluate(scaleX*x + offsetX, scaleY*y + offsetY);
	}
}
