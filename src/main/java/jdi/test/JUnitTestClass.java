package jdi.test;

public class JUnitTestClass {

    private int att1 = 0;

    public int method1() {
        this.att1 += 1;
        return this.att1;
    }
}