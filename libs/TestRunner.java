import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        String s = "";
        for (Failure failure : result.getFailures()) {
            s+=failure.getDescription().getMethodName() + "\n";
            System.out.println(failure.getDescription().getMethodName());
        }
        createFile(new File("./OUTPUT_RUN.txt"), s);
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

    public static void main(String[] args) throws ClassNotFoundException {
        TestRunner.run("MyTest");
    }

}