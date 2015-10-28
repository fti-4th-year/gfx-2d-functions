package function.operator;

import function.EvaluationStack;

public class DivisionOperator implements Operator {
	@Override
	public void evaluate(EvaluationStack stack) {
		double a = stack.pop();
		double b = stack.pop();
		stack.push(b/a);
	}
}
