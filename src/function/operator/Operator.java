package function.operator;

import function.EvaluationStack;

public interface Operator {
	public void evaluate(EvaluationStack stack);
	public String toString();
}
