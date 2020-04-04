package parser;

public class Token {
	private String name;
	private String value;
	private int precedence;
	private Type type;
	
	Token(String name, String value, Type type, int precedence) {
		this.name = name;
		this.value = value;
		this.precedence = precedence;
		this.type = type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public int getPrecedence() {
		return this.precedence;
	}
};
