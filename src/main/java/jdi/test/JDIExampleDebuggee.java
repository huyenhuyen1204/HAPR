package jdi.test;

public class JDIExampleDebuggee {
    private String a = "AAAAA";
    public String b = "BBBBB";

    public static void main(String[] args) {
        String jpda = "Java Platform Debugger Architecture";
//        System.out.println("Hi Everyone, Welcome to " + jpda); //add a break point here
        Piece1 piece = new Piece1(1000, 2000);
        String jdi = "Java Debug Interface"; //add a break point here and also stepping in here
        String text = "Today, we'll dive into " + jdi;
        JDIExampleDebuggee jdiExampleDebuggee = new JDIExampleDebuggee();
        jdiExampleDebuggee.print();
        System.out.println("END");
//        System.out.println(text);
    }

    public  void print() {
        a = "BBBBBBBBB";
        b = "AAAAAAA";
        String content = "Hello2";
        Piece1 piece = new Piece1(1000, 2000);
        System.out.println(content);
    }
    
}