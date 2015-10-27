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

public class InfixFunction implements Function {
	private String string;
	private ArrayList<Operator> operators;
	
	private class Token {
		public int pos;
		public String string;
		public int type;
		
		static final int 
			UNKNOWN = 0,
			NUMBER = 1,
			FUNCTION = 2,
			SEPARATOR = 3,
			OPERATOR = 4,
			LPAR = 5, RPAR = 6,
			VARIABLE = 7;
	}
	
	int getPriority(String op) {
		switch (op) {
		case "^":
			return 2;
		case "*":
			return 5;
		case "/":
			return 5;
		case "+":
			return 6;
		case "-":
			return 6;
		default:
			return -1;
		}
	}
	
	boolean isOperator(char c) {
		return c == '^' || c == '*' || c == '/' || c == '+' || c == '-';
	}
	
	boolean isDigit(char c) {
		return Character.isDigit(c) || c == '.';
	}
	
	boolean isWord(char c) {
		return Character.isAlphabetic(c);
	}
	
	private Token readToken(String s, int index) {
		Token t = new Token();
		t.pos = index;
		char c = s.charAt(index);
		t.string = Character.toString(c);
		if(isOperator(c)) {
			t.type = Token.OPERATOR;
		} else if(isWord(c)) {
			for(int i = index + 1; i < s.length() && isWord(c = s.charAt(i)); ++i) {
				t.string = t.string.concat(Character.toString(c));
			}
			if(c == '(')
				t.type = Token.FUNCTION;
			else
				t.type = Token.VARIABLE;
		} else if(isDigit(c)) {
			for(int i = index + 1; i < s.length() && isDigit(c = s.charAt(i)); ++i) {
				t.string = t.string.concat(Character.toString(c));
			}
			t.type = Token.NUMBER;
		} else if(c == '(') {
			t.type = Token.LPAR;
		} else if(c == ')') {
			t.type = Token.RPAR;
		} else if(c == ',') {
			t.type = Token.SEPARATOR;
		} else {
			t.type = Token.UNKNOWN;
		}
		return t;
	}
	
	private void addOperator(Token t) throws ParseException {
		switch(t.type) {
		case Token.NUMBER:
			operators.add(new NumberOperator(Double.parseDouble(t.string)));
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
			// TODO: create variable operator
			throw new ParseException("variable operator not implemented yet", t.pos);
		default:
			throw new ParseException("unknown token type " + t.type + " '" + t.string + "'", t.pos);
		}
		
		try {
			evaluate(0, 0);
		} catch (IndexOutOfBoundsException e) {
			throw new ParseException("not enough args for operator", t.pos);
		}
	}
	
	/**
	 * Dijkstra's shunting-yard algorithm implementation
	 */
	private void parse() throws ParseException {
		int index = 0;
		Stack<Token> opStack = new Stack<Token>();
		while(index < string.length()) {
			Token t = readToken(string, index);
			System.out.println(t.string + " : " + t.type);
			if(t.type == Token.UNKNOWN) {
				throw new ParseException("unknown token", t.pos);
			}
			
			if(t.type == Token.NUMBER) {
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
				  getPriority(st.string) > getPriority(t.string)
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
	}
	
	public InfixFunction(String s) throws ParseException {
		string = s.replaceAll("\\s","");
		operators = new ArrayList<Operator>();
		parse();
	}
	
	@Override
	public double evaluate(double x, double y) {
		EvaluationStack stack = new EvaluationStack(operators.size());
		for(int i = 0; i < operators.size(); ++i) {
			operators.get(i).evaluate(stack);
		}
		return stack.pop();
	}

}
