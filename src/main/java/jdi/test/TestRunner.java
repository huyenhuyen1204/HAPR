package jdi.test;

import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public static final String KEY = "===OASIS_LOG===";
    public static final String KEY_ENTER = "===OASIS_ENTER===";
    public static final String KEY_OPEN = "=OASIS_OPEN=";
    public static final String KEY_CLOSE = "=OASIS_CLOSE=";


    static int run(String testClassName) throws ClassNotFoundException, InterruptedException {
        int a = 0;
        int b = 1000;
        String str = "HUyennn";
        Class classname = Class.forName(testClassName);
        Method[] methods = classname.getMethods();
        Result result = JUnitCore.runClasses(classname);
        Thread.sleep(2000);
        System.out.println(KEY);
        System.out.println(result.getFailureCount());
        System.out.println(result.getRunCount());
        List<String> fails = new ArrayList<>();

        for (Failure failure : result.getFailures()) {
            fails.add(failure.getDescription().getMethodName());
            Throwable throwable = failure.getException();
            if ( throwable instanceof ComparisonFailure)  {
                System.out.println(failure.getDescription().getMethodName()+ "(" + classname.getName() + "): expected:" + KEY_OPEN +
                        ((ComparisonFailure) throwable).getExpected() + KEY_CLOSE +" but was:" + KEY_OPEN +
                        ((ComparisonFailure) throwable).getActual() + KEY_CLOSE + KEY_ENTER);
            } else if (throwable instanceof InvocationTargetException) {
                System.out.println(failure.getDescription().getMethodName()+ "(" + classname.getName() + "): " +
                        ((InvocationTargetException) throwable).getCause() + KEY_ENTER);
            }
            else if (throwable instanceof AssertionError) {
                String err = ((AssertionError) throwable).getMessage();
                if (err != null) {
                    String newErr = err.replaceAll("expected:<", "expected:" + KEY_OPEN).replaceAll("> but was:<", KEY_CLOSE + " but was:" + KEY_OPEN);
                    System.out.println(failure.getDescription().getMethodName() + "(" + classname.getName() + "): " + replaceLast(newErr, ">", KEY_CLOSE) + KEY_ENTER);
                } else {
                    System.out.println(failure.getDescription().getMethodName()+ "(" + classname.getName() + "): " + throwable.toString() + KEY_ENTER);
                }
            } else {
                System.out.println(failure.getDescription().getMethodName()+ "(" + classname.getName() + "): " + throwable.toString() + KEY_ENTER);
            }
        }

        for (Method m : methods) {
            Test test = m.getAnnotation( Test.class);
            if (test != null) {
                if (!fails.contains(m.getName())) {
                    System.out.println(m.getName() + "(" + classname.getName() + "): pass" + KEY_ENTER);
                }
            }
        }
        return 0;
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }

    public static void main(String[] args) throws ClassNotFoundException, InterruptedException {

        int a = 0;
        int b = 1000;
        Class classname = Class.forName("jdi.test.MyTest1");
//        TestRunner.run("jdi.test.MyTest1");
        JUnitCore.runClasses(classname);
        String end = "ENDDD";
        String end2 = "F.ENDDD";
        System.out.println(end);
        System.out.println(end2);
        a += b;
//        System.out.println(a);
    }
}

