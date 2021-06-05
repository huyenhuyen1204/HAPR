package jdb;

import common.config.Configure;

import java.io.*;

public class JDBDebugger {
    Process process;
    PrintWriter printWriter;
    BufferedReader stdInput;
    public static final String END_RUN = "Breakpoint hit: \"thread=main\",";

    public JDBDebugger(String pathToSource, String pathToOutClass) throws IOException {
        compileFolder(pathToSource, pathToOutClass);
        initDebugJDB(pathToOutClass);
        stdInput = new BufferedReader(new
                InputStreamReader(this.process.getInputStream()));
    }

    public void compileFolder(String pathToSource, String pathToOutClass) throws IOException {
        String cmd = "javac -J-Dfile.encoding=UTF-8 -g -d " + pathToOutClass + " -cp " + Configure.APR_JAR_LIB + "\\tools.jar;" + Configure.APR_JAR_LIB + "\\hamcrest-core-1.3.jar;" + Configure.APR_JAR_LIB + "\\junit-4.12.jar;" + Configure.APR_JAR_LIB + "\\oasis-junit-1.0.jar  *.java";
        Runtime.getRuntime().exec(cmd, null, new File(pathToSource));
    }

    public void initDebugJDB(String pathToClass) throws IOException {
        String cmd = "jdb -classpath  " + pathToClass + ";" + Configure.APR_JAR_LIB + "\\junit-4.12.jar junit.textui.TestRunner MyTest1";
        this.process = Runtime.getRuntime().exec(cmd, null, new File(pathToClass));
        this.printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), false);
    }

    public void addDebugJDB(String classname, int line) {
        this.printWriter.println("stop at " + classname + ":" + line);
        this.printWriter.flush(); //tra ra stream
    }

    public void runJDB() throws IOException {
        this.printWriter.println("run");
        this.printWriter.flush(); //tra ra stream
        System.out.println(printLog(END_RUN));
    }

    public String printVarJDB(String var) throws IOException {
        this.printWriter.println("print " + var);
        this.printWriter.flush(); //tra ra stream
        String endvar = String.format("%s = ", var);
        return printLog(endvar);
    }

    public void stepJDB() throws IOException {
        this.printWriter.println("cont");
        this.printWriter.flush(); //tra ra stream
        String end = printLog(END_RUN);
        System.out.println("CONT " +end);
    }

    public void destroyProcessJDB() {
        this.printWriter.println("quit");
        this.printWriter.close();
        this.process.destroy();
    }

    public String printLog(String endString) throws IOException {
        String result = "";
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            result += s + "\n";
            if (s.contains(endString)) {
                break;
            }
        }
        return result;
    }

    public String printAllLog() throws IOException {
        String result = "";
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
            result += s + "\n";
        }
        return result;
    }


    public static void main(String[] args) throws IOException {
        JDBDebugger jdbDebugger = new JDBDebugger("C:\\Users\\Dell\\Desktop\\DebuRepair\\src\\main\\java"
                ,"C:\\Users\\Dell\\Desktop\\DebuRepair\\target\\classes");
        jdbDebugger.addDebugJDB("MyTest1", 10);
        jdbDebugger.addDebugJDB("MyTest1", 21);
        jdbDebugger.runJDB();

        String var  = jdbDebugger.printVarJDB("debuggee");
        System.out.println("===VARRR==: " + var);
        jdbDebugger.stepJDB();
        String var1 = jdbDebugger.printVarJDB("a");
        System.out.println("===VARRR=="+  var1);


    }
}