package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Syntactical {
	public static List<Token> parse(List<Token> tokens) { // Shunting Yard algorithm
		List<Token> output = new ArrayList<Token>();
		Stack<Token> operators = new Stack<Token>();
		
		for (Token t : tokens) {
			if (t.getType() == Type.OPERAND) {
				output.add(t);
			}
			if (t.getType() == Type.FUNCTION) {
				operators.push(t);
			}
			if (t.getType() == Type.OPERATOR) {
					while((!operators.empty()
							&& (operators.peek().getType() == Type.FUNCTION
							|| (operators.peek().getType() == Type.OPERATOR
								&& operators.peek().getPrecedence() > t.getPrecedence())
							|| (operators.peek().getType() == Type.OPERATOR
								&& operators.peek().getPrecedence() == t.getPrecedence()
								&& !t.getName().equals("POW")) // In this case, pow is the only right associative token
							&& (operators.peek().getType() != Type.L_PAREN)))) {
						output.add(operators.pop());
					}
				operators.push(t);
			}
			if (t.getType() == Type.L_PAREN) {
				operators.push(t);
			}
			if (t.getType() == Type.R_PAREN) {
				while (!operators.empty()
						&& operators.peek().getType() != Type.L_PAREN) {
					output.add(operators.pop());
				}
				if (!operators.empty()
						&& operators.peek().getType() == Type.L_PAREN) {
					operators.pop();
				}
			}
		}
		
		while(!operators.empty()) {
			output.add(operators.pop());
		}
		
		return output;
	}
	
	public static double eval(List<Token> tokens, double x) {
		try {
			List<Token> rpn = Syntactical.parse(tokens);
			
			Stack<Token> evalStack = new Stack<Token>();
			double[] val  = {0, 0};
			
			for (int i = 0; i < rpn.size(); i++) {
				Token current = rpn.get(i);
				if (current.getType() == Type.OPERAND) {
					if (current.getName().equals("VAR")) {
						evalStack.push(new Token(current.getName(),
								""+x, current.getType(), current.getPrecedence()));
					} else if (current.getName().equals("PI")) {
						evalStack.push(new Token(current.getName(),
								""+Math.PI, current.getType(), current.getPrecedence()));
					} else {
						evalStack.push(current);
					}
				} else if (current.getType() == Type.OPERATOR) {
					switch(current.getName()) {
					case "PLUS":
						val[0] = Double.parseDouble(evalStack.pop().getValue());
						val[1] = Double.parseDouble(evalStack.pop().getValue());
						
//						System.out.println(val[1] + " + " + val[0]);
						
						evalStack.push(new Token(current.getName(),
								""+(val[1] + val[0]), current.getType(),
								current.getPrecedence()));
						
//						System.out.println("Result: " + evalStack.peek().getValue() + "\n");
						break;
					case "MINUS": 
						val[0] = Double.parseDouble(evalStack.pop().getValue());
						val[1] = Double.parseDouble(evalStack.pop().getValue());
						
//						System.out.println(val[1] + " - " + val[0]);
						
						evalStack.push(new Token(current.getName(),
								""+(val[1] - val[0]), current.getType(),
								current.getPrecedence()));
						
//						System.out.println("Result: " + evalStack.peek().getValue() + "\n");
						
						break;
					case "TIMES":
						val[0] = Double.parseDouble(evalStack.pop().getValue());
						val[1] = Double.parseDouble(evalStack.pop().getValue());
						
//						System.out.println(val[1] + " * " + val[0]);
						
						evalStack.push(new Token(current.getName(),
								""+(val[1] * val[0]), current.getType(),
								current.getPrecedence()));
						
//						System.out.println("Result: " + evalStack.peek().getValue() + "\n");
						break;
					case "DIVIDE":
						val[0] = Double.parseDouble(evalStack.pop().getValue());
						val[1] = Double.parseDouble(evalStack.pop().getValue());
						
//						System.out.println(val[1] + " / " + val[0]);
						
						evalStack.push(new Token(current.getName(),
								""+(val[1] / val[0]), current.getType(),
								current.getPrecedence()));
						
//						System.out.println("Result: " + evalStack.peek().getValue() + "\n");
						break;
					case "POW":
						val[0] = Double.parseDouble(evalStack.pop().getValue());
						val[1] = Double.parseDouble(evalStack.pop().getValue());
						
//						System.out.println(val[1] + " ^ " + val[0]);
						
						evalStack.push(new Token(current.getName(),
								""+Math.pow(val[1], val[0]), current.getType(),
								current.getPrecedence()));
						
//						System.out.println("Result: " + evalStack.peek().getValue() + "\n");
						break;
					case "MOD":
						val[0] = Double.parseDouble(evalStack.pop().getValue());
						val[1] = Double.parseDouble(evalStack.pop().getValue());
						evalStack.push(new Token(current.getName(),
								""+(val[1] % val[0]), current.getType(),
								current.getPrecedence()));
						break;
					}
				} else if (current.getType() == Type.FUNCTION) {
//					System.out.println(current.getValue());
					switch(current.getValue()) {
					case "sin":
						val[0] = Double.parseDouble(evalStack.pop().getValue());
						
//						System.out.println("sin(" + val[0] + ")");
						
						evalStack.push(new Token(current.getName(),
								""+Math.sin(val[0]), current.getType(),
								current.getPrecedence()));
						
//						System.out.println("Result: " + evalStack.peek().getValue() + "\n");
						break;
					case "cos": 
						val[0] = Double.parseDouble(evalStack.pop().getValue());
						
//						System.out.println("cos(" + val[0] + ")");
						
						evalStack.push(new Token(current.getName(),
								""+Math.cos(val[0]), current.getType(),
								current.getPrecedence()));
						
//						System.out.println("Result: " + evalStack.peek().getValue() + "\n");
						
						break;
					case "tan":
						val[0] = Double.parseDouble(evalStack.pop().getValue());
						
//						System.out.println("tan(" + val[0] + ")");
						
						evalStack.push(new Token(current.getName(),
								""+Math.tan(val[0]), current.getType(),
								current.getPrecedence()));
						
//						System.out.println("Result: " + evalStack.peek().getValue() + "\n");
						
						break;
					case "exp":
						val[0] = Double.parseDouble(evalStack.pop().getValue());
						evalStack.push(new Token(current.getName(),
								""+Math.exp(val[0]), current.getType(),
								current.getPrecedence()));
						break;
					case "ln":
						val[0] = Double.parseDouble(evalStack.pop().getValue());
						evalStack.push(new Token(current.getName(),
								""+Math.log(val[0]), current.getType(),
								current.getPrecedence()));
						break;
					}
				}
			}
			
			return Double.parseDouble(evalStack.pop().getValue());
		} catch(NullPointerException e) {
			System.out.println("Lexical error.");
		}
		
		return 0;
	}
}
