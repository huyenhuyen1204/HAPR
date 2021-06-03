package jdi.test;

import junit.framework.TestCase;

public class TestJUnit extends TestCase {

    public void test1() {
        try {
            JUnitTestClass mtc = new JUnitTestClass();
            assertTrue(mtc.method1() == 1);
            assertTrue(mtc.method1() == 2);
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }

    public static void main(String[] args) {
        int a = 1000;
        int b = 2000;
    }
}