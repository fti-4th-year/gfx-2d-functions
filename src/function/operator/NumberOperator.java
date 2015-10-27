package function.operator;

import function.EvaluationStack;

public class NumberOperator implements Operator {
	
	private double number;
	
	public NumberOperator(double num) {
		number = num;
	}
	
	@Override
	public void evaluate(EvaluationStack stack) {
		stack.push(number);
	}
}
