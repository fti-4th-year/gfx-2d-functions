package function.operator;

import function.EvaluationStack;

public class MinusOperator implements Operator {
	@Override
	public void evaluate(EvaluationStack stack) {
		stack.push(-stack.pop());
	}
	@Override
	public String toString() {
		return "#";
	}
}
