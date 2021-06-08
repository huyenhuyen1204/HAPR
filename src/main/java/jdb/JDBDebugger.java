package jdb;

import common.config.Configure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class JDBDebugger {
    public static final Logger logger = LoggerFactory.getLogger(JDBDebugger.class);
    Process process;
    PrintWriter printWriter;
    BufferedReader stdInput;
    public static final String END_RUN = "Breakpoint hit: \"thread=main\",";

    public JDBDebugger(String pathToSource, String pathToOutClass, String classname) throws IOException {
        compileFolder(pathToSource, pathToOutClass);
        initDebugJDB(pathToSource, pathToOutClass, classname);
        stdInput = new BufferedReader(new
                InputStreamReader(this.process.getInputStream()));
    }

    public void compileFolder(String pathToSource, String pathToOutClass) throws IOException {
        String cmd = "javac -J-Dfile.encoding=UTF-8 -g -d " + pathToOutClass +
                " -cp " + Configure.APR_JAR_LIB + "\\tools.jar;" + Configure.APR_JAR_LIB + "\\hamcrest-core-1.3.jar;" +
                Configure.APR_JAR_LIB + "\\junit-4.12.jar;" + Configure.APR_JAR_LIB + "\\oasis-junit-1.0.jar  *.java TestRunner.java";
        logger.info(cmd);
        Runtime.getRuntime().exec(cmd, null, new File(pathToSource));
    }

    public void initDebugJDB(String pathTOSource, String pathToClass, String classname) throws IOException {
        String cmd = "jdb -classpath  " + pathToClass + ";" +  Configure.APR_JAR_LIB + "\\oasis.jar;"
                + Configure.APR_JAR_LIB + "\\junit-4.12.jar junit.textui.TestRunner " + classname;
        logger.info(cmd);
        this.process = Runtime.getRuntime().exec(cmd, null, new File(pathTOSource));
        this.printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), false);
    }

    public void addDebugJDB(String classname, int line) {
        this.printWriter.println("stop at " + classname + ":" + line);
        this.printWriter.flush(); //tra ra stream
    }

    public String runJDB() throws IOException {
        this.printWriter.println("run");
        this.printWriter.flush(); //tra ra stream
        return printLog(END_RUN);
    }

    public String printVarJDB(String var) throws IOException {
        this.printWriter.println("print " + var);
        this.printWriter.flush(); //tra ra stream
        String endvar = String.format("%s = ", var);
        return printLog(endvar);
    }

    public void contJDB() throws IOException {
        this.printWriter.println("cont");
        this.printWriter.flush(); //tra ra stream
        String end = printLog(END_RUN);
    }
    public void stepJDB() throws IOException {
        this.printWriter.println("step");
        this.printWriter.flush(); //tra ra stream
        String end = printLog(END_RUN);
    }

    public void destroyProcessJDB() {
        this.printWriter.println("quit");
        this.printWriter.close();
        this.process.destroy();
    }

    public String printLog(String endString) throws IOException {
        String result = "";
        String s = null;
        long  startTime = System.nanoTime();
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
            result += s + "\n";
            if (s.contains(endString)) {
                break;
            }
            long  endTime = System.nanoTime();
            if(endTime-startTime > 1000000000) {
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
////        CASE 1: test in this project
//        JDBDebugger jdbDebugger = new JDBDebugger("C:\\Users\\Dell\\Desktop\\DebuRepair\\src\\main\\java"
//                ,"C:\\Users\\Dell\\Desktop\\DebuRepair\\target\\classes", "MyTest1");
//        jdbDebugger.addDebugJDB("MyTest1", 11);
//        String logrun = jdbDebugger.runJDB();
//        if (!logrun.equals("")) {
//            String var  = jdbDebugger.printVarJDB("debuggee");
//            System.out.println("===VARRR==: " + var);
//            jdbDebugger.stepJDB();
//            String var1 = jdbDebugger.printVarJDB("a");
//            System.out.println("===VARRR=="+  var1);
//        } else {
//            System.out.println("ERROR");
//        }


        //CASE 2: test in student Project
        JDBDebugger jdbDebugger = new JDBDebugger("C:\\Users\\Dell\\Desktop\\APR_Test\\data_test\\83778"
                ,"C:\\Users\\Dell\\Desktop\\APR_Test\\data_test\\83778", "MyTest");
        jdbDebugger.addDebugJDB("MyTest", 78);
//        jdbDebugger.addDebugJDB("MyTest", 21);
        jdbDebugger.runJDB();

        String var  = jdbDebugger.printVarJDB("customerList");
        System.out.println("===VARRR==: " + var);
        for (int i = 0 ; i < 10; i++) {
            jdbDebugger.stepJDB();
        }
//        jdbDebugger.contJDB();
//        String var1 = jdbDebugger.printVarJDB("Assert.assertEquals(237225996L, customerList.get(3).getIdNumber())");
//        System.out.println("===VARRR=="+  var1);

    }
}