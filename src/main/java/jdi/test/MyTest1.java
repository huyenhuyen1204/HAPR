package jdi.test;

import junit.framework.TestCase;

public class MyTest1 extends TestCase {
    public void test1() {
        Rook rookWhite1 = new Rook(2, 3, "white");
        System.out.println("run test 1");
        assertEquals("sdsds", 2, rookWhite1.getCoordinatesX() );
        try {
            Runtime.getRuntime().exec(new String[]{"touch", "C:\\Users\\Dell\\IdeaProjects\\HAPR\\libs\\tmp\\hello.txt"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Rook rookWhite1 = new Rook(2, 3, "white");
        System.out.println("run test 1");
        assertEquals("sdsds", 2, rookWhite1.getCoordinatesX() );
        try {
            Runtime.getRuntime().exec(new String[]{"touch", "C:\\Users\\Dell\\IdeaProjects\\HAPR\\libs\\tmp\\hello.txt"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
