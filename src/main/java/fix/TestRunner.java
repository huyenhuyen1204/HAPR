package fix;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    //use TestRunner to readfail Test => debug position fail ->
    static int run(String testClassName) throws ClassNotFoundException {
        Class classname = Class.forName(testClassName);
        Method[] methods = classname.getMethods();

        Result result = JUnitCore.runClasses(classname);
        System.out.println(result.getFailureCount());
        System.out.println(result.getRunCount());
        List<String> fails = new ArrayList<>();

        for (Failure failure : result.getFailures()) {
            fails.add(failure.getDescription().getMethodName());
            System.out.println(failure.getDescription().getMethodName());
        }
        return 0;
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        TestRunner.run("MyTest");
    }

}
