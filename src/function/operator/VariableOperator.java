package function.operator;

import function.EvaluationStack;

public class VariableOperator implements Operator {
	public static class VarRef {
		public String name;
		public double value;
		public VarRef(String n) {
			name = n;
		}
	}
	
	private VarRef variable;
	
	public VariableOperator(VarRef var) {
		variable = var;
	}
	
	@Override
	public void evaluate(EvaluationStack stack) {
		stack.push(variable.value);
	}
	
	@Override
	public String toString() {
		return variable.name;
	}
}
