package parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexical {
	private enum Tokens {
//		WHITESPACE("[ \t]+") {
//			public String action() {
//				return "";
//			}
//		},
		REAL("-?([0-9]+)?(\\\\.[0-9]+)?([eE][+-]?[0-9]+)?", Type.OPERAND, -1),
		PI("PI|pi", Type.OPERAND, -1),
		VAR("X|x", Type.OPERAND, -1),
		PLUS("\\+", Type.OPERATOR, 0),
		MINUS("-", Type.OPERATOR, 0),
		TIMES("\\*", Type.OPERATOR, 1),
		DIVIDE("/", Type.OPERATOR, 1),
		POW("\\^", Type.OPERATOR, 2),
		MOD("%", Type.OPERATOR, 2),
		L_PAREN("\\(", Type.L_PAREN, -1),
		R_PAREN("\\)", Type.R_PAREN, -1),
		FUNCTION("sin|cos|tan|exp|ln", Type.FUNCTION, -1),
		COMMA(",", Type.DISCARD, -1),
		EOL("\n", Type.DISCARD, -1),
		ERROR(".", Type.DISCARD, -1);
		
		private final Pattern regex;
		private final Type type;
		private final int precedence;
		
		Tokens(String regex, Type type, int precedence) {
			this.regex = Pattern.compile("^(" + regex + ")");
			this.type = type;
			this.precedence = precedence;
		}
		
		public Pattern getRegex() {
			return this.regex;
		}
		
		public Type getType() {
			return this.type;
		}
		
		public int getPrecedence() {
			return this.precedence;
		}
	};
	
	public static ArrayList<Token> analyse(String input) {
		ArrayList<Token> tokens = new ArrayList<Token>();
		input = input.replaceAll("\\s+", ""); // Remove whitespaces
		
		while (input.length() > 0) {
			for(Tokens t : Tokens.values()) {
				Matcher m = t.getRegex().matcher(input);
				
				if (m.find() && m.end() > 0) {
					String matched = input.substring(0, m.end());
					input = input.substring(m.end(), input.length());
					
					if (t.toString().equals("ERROR")) {
						System.out.println("Invalid symbol -> " + matched);
						return null;
					}
					
					tokens.add(new Token(t.toString(), matched,
							t.getType(), t.getPrecedence()));
					break;
				}
			}
		}
		
		return tokens;
	}

}
