package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Testing {
	public static void main(String[] args) {
		String input = "sin(10)";
//		String func = "sin(max(2, x) / x * PI + e^x) + 2*x/cos(tan(x^2)) * 10e3 * -2";
		String func = "((sin(2) * 3) / 3 * PI) + exp(2*x/cos(tan(x^2))) * 10e3 * -2 / ln(2^3^4)^2";
//		String func = "((sin(2) * 3) / 3 * PI) + 2*x/cos(tan(x^2))";
//		String func = "2 + 3 * 4";
//		func = "2^3^4^5";
		

		double res = Syntactical.eval(func, 3);
		
		System.out.println("Result: " + res);
	}
}
