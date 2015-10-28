package function;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Stack;

import function.operator.AdditionOperator;
import function.operator.DivisionOperator;
import function.operator.MultiplicationOperator;
import function.operator.NumberOperator;
import function.operator.Operator;
import function.operator.SubtractionOperator;
import function.operator.VariableOperator;

public class InfixFunction implements Function {
	private String string;
	private ArrayList<Operator> operators;
	private VariableOperator.VarRef argX, argY;
	
	int getPriority(String op) {
		switch (op) {
		case "^":
			return 1;
		case "*":
			return 2;
		case "/":
			return 2;
		case "+":
			return 3;
		case "-":
			return 3;
		default:
			return -1;
		}
	}
	
	private void addOperator(Token t) throws ParseException {
		switch(t.type) {
		case Token.NUMBER:
			try {
				operators.add(new NumberOperator(Double.parseDouble(t.string)));
			} catch(NumberFormatException e) {
				throw new ParseException("invalid number format '" + t.string + "'", t.pos);
			}
			break;
		case Token.FUNCTION:
			// TODO: create function operator
			throw new ParseException("unknown function '" + t.string + "'", t.pos);
		case Token.OPERATOR:
			switch (t.string) {
			case "^":
				// TODO: create power operator
				throw new ParseException("power operator not implemented yet", t.pos);
			case "*":
				operators.add(new MultiplicationOperator());
				break;
			case "/":
				operators.add(new DivisionOperator());
				break;
			case "+":
				operators.add(new AdditionOperator());
				break;
			case "-":
				operators.add(new SubtractionOperator());
				break;
			default:
				throw new ParseException("unknown operator '" + t.string + "'", t.pos);
			}
			break;
		case Token.VARIABLE:
			if(t.string.compareTo("x") == 0)
				operators.add(new VariableOperator(argX));
			else if(t.string.compareTo("y") == 0)
				operators.add(new VariableOperator(argY));
			else 
				throw new ParseException("variable must be 'x' or 'y'", t.pos);
			break;
		default:
			throw new ParseException("unknown token type '" + t.type + "' for '" + t.string + "'", t.pos);
		}
		
		try {
			evaluate(0, 0);
		} catch (IndexOutOfBoundsException e) {
			throw new ParseException("not enough args for operator '" + t.string + "'", t.pos);
		}
	}
	
	/**
	 * Dijkstra's shunting-yard algorithm implementation
	 */
	private void parse() throws ParseException {
		int index = 0;
		Stack<Token> opStack = new Stack<Token>();
		while(index < string.length()) {
			Token t = new Token(string, index);
			// System.out.println(t.string + " : " + t.type);
			if(t.type == Token.UNKNOWN) {
				throw new ParseException("unknown token", t.pos);
			}
			
			if(t.type == Token.NUMBER) {
				addOperator(t);
			} else if(t.type == Token.VARIABLE) {
				addOperator(t);
			} else if(t.type == Token.FUNCTION) {
				opStack.add(t);
			} else if(t.type == Token.SEPARATOR) {
				Token st;
				while(opStack.size() > 0 && (st = opStack.lastElement()).type != Token.LPAR) {
					addOperator(st);
					opStack.pop();
				}
				if(opStack.size() <= 0)
					throw new ParseException("missing ',' or '('", t.pos);
			} else if(t.type == Token.OPERATOR) {
				Token st;
				while(
				  opStack.size() > 0 &&
				  (st = opStack.lastElement()).type == Token.OPERATOR && 
				  getPriority(st.string) < getPriority(t.string)
				) {
					addOperator(st);
					opStack.pop();
				}
				opStack.add(t);
			} else if(t.type == Token.LPAR) {
				opStack.add(t);
			} else if(t.type == Token.RPAR) {
				Token st;
				while(opStack.size() > 0 && (st = opStack.lastElement()).type != Token.LPAR) {
					addOperator(st);
					opStack.pop();
				}
				if(opStack.size() <= 0)
					throw new ParseException("missing '('", t.pos);
				opStack.pop();
				
				if(
				  opStack.size() > 0 && 
				  (st = opStack.lastElement()).type == Token.FUNCTION
				) {
					addOperator(st);
					opStack.pop();
				}
			}
			index += t.string.length();
		}
		while(opStack.size() > 0) {
			Token t = opStack.pop();
			if(t.type == Token.LPAR)
				throw new ParseException("missing ')'", t.pos);
			addOperator(t);
		}
		if(operators.size() <= 0)
			throw new ParseException("expression is empty", 0);
	}
	
	public InfixFunction(String s) throws ParseException {
		operators = new ArrayList<Operator>();
		argX = new VariableOperator.VarRef();
		argY = new VariableOperator.VarRef();
		string = s.replaceAll("\\s","");
		parse();
	}
	
	@Override
	public double evaluate(double x, double y) {
		EvaluationStack stack = new EvaluationStack(operators.size());
		argX.value = x;
		argY.value = y;
		for(int i = 0; i < operators.size(); ++i) {
			operators.get(i).evaluate(stack);
		}
		return stack.pop();
	}

}
