package function.operator;

import function.EvaluationStack;

public class DivisionOperator implements Operator {
	@Override
	public void evaluate(EvaluationStack stack) {
		double a = stack.pop();
		double b = stack.pop();
		if(Math.abs(a) < 1e-8) {
			stack.push(b*1e8);
		} else {
			stack.push(b/a);
		}
	}
	@Override
	public String toString() {
		return "/";
	}
}
