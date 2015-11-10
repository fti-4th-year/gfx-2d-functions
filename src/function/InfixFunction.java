package function;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Stack;

import function.operator.AdditionOperator;
import function.operator.DivisionOperator;
import function.operator.FunctionOperator;
import function.operator.MinusOperator;
import function.operator.MultiplicationOperator;
import function.operator.NumberOperator;
import function.operator.Operator;
import function.operator.PowerOperator;
import function.operator.SubtractionOperator;
import function.operator.VariableOperator;

public class InfixFunction implements Function {
	private String string;
	private ArrayList<Operator> operators;
	private VariableOperator.VarRef argX, argY;
	
	static int getPriority(String op) {
		switch (op) {
		case "^":
			return 1;
		case "#":
			return 2;
		case "*":
			return 3;
		case "/":
			return 3;
		case "+":
			return 4;
		case "-":
			return 4;
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
			switch (t.string) {
			case "sin":
				operators.add(new FunctionOperator() {@Override public double func(double x) {return Math.sin(x);}});
				break;
			case "cos":
				operators.add(new FunctionOperator() {@Override public double func(double x) {return Math.cos(x);}});
				break;
			case "tan":
				operators.add(new FunctionOperator() {@Override public double func(double x) {return Math.tan(x);}});
				break;
			case "asin":
				operators.add(new FunctionOperator() {@Override public double func(double x) {return Math.asin(x);}});
				break;
			case "acos":
				operators.add(new FunctionOperator() {@Override public double func(double x) {return Math.acos(x);}});
				break;
			case "atan":
				operators.add(new FunctionOperator() {@Override public double func(double x) {return Math.atan(x);}});
				break;
			case "exp":
				operators.add(new FunctionOperator() {@Override public double func(double x) {return Math.exp(x);}});
				break;
			case "sinh":
				operators.add(new FunctionOperator() {@Override public double func(double x) {return Math.sinh(x);}});
				break;
			case "cosh":
				operators.add(new FunctionOperator() {@Override public double func(double x) {return Math.cosh(x);}});
				break;
			case "tanh":
				operators.add(new FunctionOperator() {@Override public double func(double x) {return Math.tanh(x);}});
				break;
			case "log":
				operators.add(new FunctionOperator() {@Override public double func(double x) {return Math.log(x);}});
				break;
			default:
				throw new ParseException("unknown function '" + t.string + "'", t.pos);
			}
			break;
		case Token.OPERATOR:
			switch (t.string) {
			case "^":
				operators.add(new PowerOperator());
				break;
			case "#":
				operators.add(new MinusOperator());
				break;
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
	
	public String eliminateUnary(String expr) {
		String begin = new String();
		String end = new String(expr);
		int pos = -1;
		while((pos = end.indexOf('-')) >= 0) {
			begin += end.substring(0, pos);
			if(pos != 0) {
				char pc = end.charAt(pos - 1);
				if(
				  !Character.isDigit(pc) &&
				  !Character.isAlphabetic(pc) &&
				  pc != ')'
				  ) {
					begin += '#';
				} else {
					begin += '-';
				}
			} else {
				begin += '#';
			}
			end = end.substring(pos + 1);
		}
		begin += end;
		return begin;
	}
	
	public InfixFunction(String s) throws ParseException {
		operators = new ArrayList<Operator>();
		argX = new VariableOperator.VarRef("x");
		argY = new VariableOperator.VarRef("y");
		string = eliminateUnary(s.replaceAll("\\s",""));
		// System.out.println(string);
		parse();
		/*
		for(int i = 0; i < operators.size(); ++i) {
			System.out.print(operators.get(i).toString() + " ");
		}
		System.out.println();
		*/
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
