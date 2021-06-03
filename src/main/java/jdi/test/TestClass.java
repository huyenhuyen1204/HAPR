package jdi.test;

public class TestClass
{
    public long factorial(int x) 
    {
        long response = 1;

        for (int i=1; i<=x; i++) {
            response *= i;
        }

        return response;
    }
}