package function;

public class EvaluationStack {
	private double[] array;
	private int ptr;
	
	public EvaluationStack(int maxSize) {
		array = new double[maxSize];
		ptr = 0;
	}
	
	public void push(double elem) {
		array[ptr] = elem;
		++ptr;
	}
	
	public double pop() {
		--ptr;
		return array[ptr];
	}
	
	public int size() {
		return ptr;
	}
}
