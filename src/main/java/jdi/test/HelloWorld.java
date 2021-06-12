package jdi.test;

/**
 * Debug this program with JDI & read all local variables.
 * 
 * @author ravik
 *
 */
public class HelloWorld {
 
	public static void main(String[] args) {
		String helloWorld = "";
 
		String welcome = "Số CMND: 832443592. Họ tên: Hoàng Văn Phượng.";
 
		String greeting = helloWorld + welcome;
 
		System.out.println("Hi Everyone, " + greeting);// Put a break point at this line.
 
	}
 
}