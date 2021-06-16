import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.InvocationTargetException;

import org.junit.ComparisonFailure;



public class TestRunner {
    public static final String KEY = "===OASIS_LOG===";
    public static final String KEY_ENTER = "===OASIS_ENTER===";
    public static final String KEY_OPEN = "=OASIS_OPEN=";
    public static final String KEY_CLOSE = "=OASIS_CLOSE=";

    //use TestRunner to readfail Test => debug position fail ->
    static int run(String testClassName) throws ClassNotFoundException, IOException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        setUTF8();
        Class classname = Class.forName(testClassName);
        Method[] methods = classname.getMethods();

        Result result = JUnitCore.runClasses(classname);
        System.out.println(result.getFailureCount());
        System.out.println(result.getRunCount());
        List<String> fails = new ArrayList<>();
        String s = "";
        for (Failure failure : result.getFailures()) {
            s+=failure.getDescription().getMethodName() + "\n";
            System.out.println(failure.getDescription().getMethodName());
        }
        for (Failure failure : result.getFailures()) {
            fails.add(failure.getDescription().getMethodName());
            Throwable throwable = failure.getException();
            if ( throwable instanceof ComparisonFailure)  {
//                s += failure.getDescription().getMethodName()+ "(" + classname.getName() + "): expected:" + KEY_OPEN +
//                        ((ComparisonFailure) throwable).getExpected() + KEY_CLOSE +" but was:" + KEY_OPEN +
//                        ((ComparisonFailure) throwable).getActual() + KEY_CLOSE + KEY_ENTER;
            } else if (throwable instanceof InvocationTargetException) {
//                s += failure.getDescription().getMethodName()+ "(" + classname.getName() + "): " +
//                        ((InvocationTargetException) throwable).getCause() + KEY_ENTER;
            }
            else if (throwable instanceof AssertionError) {
                String err = ((AssertionError) throwable).getMessage();
                if (err != null) {
                    String newErr = err.replaceAll("expected:<", "expected:" + KEY_OPEN).replaceAll("> but was:<", KEY_CLOSE + " but was:" + KEY_OPEN);
//                    s += failure.getDescription().getMethodName() + "(" + classname.getName() + "): " + replaceLast(newErr, ">", KEY_CLOSE) + KEY_ENTER;
                    System.out.println(s);
                } else {
//                    s += failure.getDescription().getMethodName()+ "(" + classname.getName() + "): " + throwable.toString() + KEY_ENTER;
                    System.out.println(s);
                }
            } else {
                System.out.println(s);
//                s+=failure.getDescription().getMethodName()+ "(" + classname.getName() + "): " + throwable.toString() + KEY_ENTER;
            }
        }

        createFile(new File("./OUTPUT_RUN.txt"), s);
        Thread.sleep(1000);
        return 0;
    }
    public static void createFile(File file, String content) {
        FileWriter writer = null;
        BufferedWriter bw = null;

        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) file.createNewFile();
            writer = new FileWriter(file);
            bw = new BufferedWriter(writer);
            bw.write(content);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(bw);
            close(writer);
        }
    }

    public static void setUTF8() throws NoSuchFieldException, IllegalAccessException {
        System.setProperty("file.encoding", "UTF-8");

        Field charset = Charset.class.getDeclaredField("defaultCharset");
        charset.setAccessible(true);
        charset.set(null, null);
    }

    public static void close(BufferedWriter bw) {
        try {
            if (bw != null) {
                bw.close();
                bw = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void close(FileWriter writer) {
        try {
            if (writer != null) {
                writer.close();
                writer = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException,  NoSuchFieldException, IllegalAccessException {
        TestRunner.run("MyTest");
    }

}