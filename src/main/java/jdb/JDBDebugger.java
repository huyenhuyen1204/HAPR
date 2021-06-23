package jdb;

import common.Configure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fix.DebugPointSetter;
import util.JavaLibrary;
import util.OSHelper;
import util.RunningHelper;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JDBDebugger {
    public static final Logger logger = LoggerFactory.getLogger(JDBDebugger.class);
    Process process;
    PrintWriter printWriter;
    BufferedReader stdInput;
    public static final String END_RUN = "Breakpoint hit: \"thread=main\",";
    public final String Separate_Char = "/huyenhuyen1204/";

    public JDBDebugger(String pathToSource, String pathToOutClass, String classname) throws IOException {
        RunningHelper.compileFolder(pathToSource, pathToOutClass);
        initDebugJDB(pathToSource, pathToOutClass, classname);
//        this.process.getInputStream().
        stdInput = new BufferedReader(new
                InputStreamReader(this.process.getInputStream(), StandardCharsets.UTF_8));
    }


    public void initDebugJDB(String pathTOSource, String pathToClass, String classname) throws IOException {
        JavaLibrary.setUTF8();
        String cmd = "jdb -classpath  " + pathToClass + OSHelper.delimiter()
                + Configure.APR_JAR_LIB + File.separator + "oasis.jar" + OSHelper.delimiter()
                + Configure.APR_JAR_LIB + File.separator + "junit-4.12.jar junit.textui.TestRunner " + classname;
        logger.info(cmd);
        this.process = Runtime.getRuntime().exec(cmd, null, new File(pathTOSource));
        this.printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream(),
                StandardCharsets.UTF_8)), false);

//       print new java.lang.String("Lịch sử giao dịch của tài khoản 7562459315:\n".getBytes("UTF-16"), "UTF-8")
    }

    public void addDebugJDB(String classname, int line) {
        this.printWriter.println("stop at " + classname + ":" + line);
        this.printWriter.flush(); //tra ra stream
    }

    public String runJDB() {
        try {
            this.printWriter.println("run");
            this.printWriter.flush(); //tra ra stream
            return printLog(END_RUN);
        } catch (IOException e) {
            logger.error("Run JDB error");
        }
        return null;
    }


    public String printVarJDB(String var) {

        try {
            this.printWriter.println("print " + var);
            this.printWriter.flush();
            Thread.sleep(1000);
            this.printWriter.println("info");
            this.printWriter.flush();
            String s = printAllLog();
            return parseLog(var, s);
        } catch (InterruptedException | IOException e) {
           logger.error("Print var ERROR: " + var);
        }
        return null;
    }

    private String parseLog(String var, String log) {
        String newLog = log.replace(var + " = ", Separate_Char);
        String[] strings = newLog.split(Separate_Char);
        if (strings.length > 1) {
            return strings[1];
        }
        return null;
    }

    public void contJDB() throws IOException {
        this.printWriter.println("cont");
        this.printWriter.flush(); //tra ra stream
        String end = printLog(END_RUN);
    }

    public void stepJDB() {
        this.printWriter.println("step");
        this.printWriter.flush(); //tra ra stream
//        String end = printLog(END_RUN);
    }

    public void destroyProcessJDB() {
        this.printWriter.println("quit");
        this.printWriter.close();
        this.process.destroy();
    }

    public String printLog(String endString) throws IOException {
        String result = "";
        String s = null;
        long startTime = System.nanoTime();
        while ((s = stdInput.readLine()) != null) {
            System.out.println("Log: " + s);
            result += s + "\n";
            if (s.contains(endString)) {
                break;
            }
            long endTime = System.nanoTime();
            if (endTime - startTime > 1000000000) {
                break;
            }
        }
        return result;
    }

    public String printAllLog() throws IOException {
        String result = "";
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println("Log: " + s);
            String breakline = "main[1] Unrecognized command: 'info'.  Try help...";
            if (s.trim().equals(breakline)) {
                break;
            }
            result += s + "\n";
        }
//        FileHelper.writeInputStreamToFile(this.process.getInputStream(), "C:\\Users\\Dell\\Desktop\\APR_test\\data_test\\83778\\OUT.txt");
        return result;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
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
                , "C:\\Users\\Dell\\Desktop\\APR_Test\\data_test\\83778", "MyTest");
        jdbDebugger.addDebugJDB("MyTest", 78);
//        jdbDebugger.addDebugJDB("MyTest", 21);
        jdbDebugger.runJDB();

        String var = jdbDebugger.printVarJDB("customerList");
        System.out.println("===VARRR==: " + var);
//        for (int i = 0; i < 10; i++) {
//            jdbDebugger.stepJDB();
//        }
//        jdbDebugger.contJDB();
//        String var1 = jdbDebugger.printVarJDB("Assert.assertEquals(237225996L, customerList.get(3).getIdNumber())");
//        System.out.println("===VARRR=="+  var1);

    }
}