package function.operator;

import function.EvaluationStack;

public class VariableOperator implements Operator {
	public static class VarRef {
		public double value;
	}
	
	private VarRef variable;
	
	public VariableOperator(VarRef var) {
		variable = var;
	}
	
	@Override
	public void evaluate(EvaluationStack stack) {
		stack.push(variable.value);
	}

}
