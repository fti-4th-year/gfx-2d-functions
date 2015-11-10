package function.operator;

import function.EvaluationStack;

public abstract class FunctionOperator implements Operator {
	public abstract double func(double x);
	@Override
	public void evaluate(EvaluationStack stack) {
		stack.push(func(stack.pop()));
	}
}
