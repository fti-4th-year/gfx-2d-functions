package function;

public class Token {
	public int pos;
	public String string;
	public int type;
	
	public static final int 
		UNKNOWN = 0,
		NUMBER = 1,
		FUNCTION = 2,
		SEPARATOR = 3,
		OPERATOR = 4,
		LPAR = 5, RPAR = 6,
		VARIABLE = 7;
	
	private static boolean isOperator(char c) {
		return c == '^' || c == '*' || c == '/' || c == '+' || c == '-';
	}
	
	private static boolean isDigit(char c) {
		return Character.isDigit(c) || c == '.';
	}
	
	private static boolean isWord(char c) {
		return Character.isAlphabetic(c);
	}
	
	public Token(String s, int index) {
		pos = index;
		char c = s.charAt(index);
		string = Character.toString(c);
		if(isOperator(c)) {
			type = Token.OPERATOR;
		} else if(isWord(c)) {
			for(int i = index + 1; i < s.length() && isWord(c = s.charAt(i)); ++i) {
				string = string.concat(Character.toString(c));
			}
			if(c == '(')
				type = Token.FUNCTION;
			else
				type = Token.VARIABLE;
		} else if(isDigit(c)) {
			for(int i = index + 1; i < s.length() && isDigit(c = s.charAt(i)); ++i) {
				string = string.concat(Character.toString(c));
			}
			type = Token.NUMBER;
		} else if(c == '(') {
			type = Token.LPAR;
		} else if(c == ')') {
			type = Token.RPAR;
		} else if(c == ',') {
			type = Token.SEPARATOR;
		} else {
			type = Token.UNKNOWN;
		}
	}
}