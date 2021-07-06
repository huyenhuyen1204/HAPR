package core.jdb;

import common.Configure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JavaLibraryHelper;
import util.OSHelper;
import util.RunningHelper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JDBDebugger {
    public static final Logger logger = LoggerFactory.getLogger(JDBDebugger.class);
    Process process;
    PrintWriter printWriter;
    BufferedReader stdInput;
    public static final String END_RUN = "\"thread=main\", ";

    public final String Separate_Char = "/huyenhuyen1204/";

    public JDBDebugger(String pathToSource, String pathToOutClass, String classname) throws IOException {
        RunningHelper.compileFolder(pathToSource, pathToOutClass);
        initDebugJDB(pathToSource, pathToOutClass, classname);
        stdInput = new BufferedReader(new
                InputStreamReader(this.process.getInputStream(), StandardCharsets.UTF_8));
    }


    public void initDebugJDB(String pathTOSource, String pathToClass, String classname) throws IOException {
        JavaLibraryHelper.setUTF8();
        String cmd = "jdb -classpath  " + pathToClass + OSHelper.delimiter()
                + Configure.APR_JAR_LIB + File.separator + "oasis-junit-1.0.jar" + OSHelper.delimiter()
                + Configure.APR_JAR_LIB + File.separator + "junit-4.12.jar junit.textui.TestRunner " + classname;
        logger.info(cmd);
        this.process = Runtime.getRuntime().exec(cmd, null, new File(pathTOSource));
        this.printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream(),
                StandardCharsets.UTF_8)), false);

    }

    public void addDebugJDB(String classname, int line) {
        try {
            this.printWriter.println("stop at " + classname + ":" + line);
            System.out.println("stop at " + classname + ":" + line);
//            Thread.sleep(100);
            this.printWriter.flush(); //tra ra stream
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearDebugJDB(String classname, int line) {
        try {
            this.printWriter.println("clear " + classname + ":" + line);
            System.out.println("clear " + classname + ":" + line);
//            Thread.sleep(100);
            this.printWriter.flush(); //tra ra stream
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            Thread.sleep(200);
            this.printWriter.println("info");
            this.printWriter.flush();
            String s = printAllLog();
            return JavaLibraryHelper.removeFirstAndLastChars(parseLog(var, s));
        } catch (IOException | InterruptedException e) {
           logger.error("Print var ERROR: " + var);
        }
        return null;
    }

    private String parseLog(String var, String log) {
        String newLog = log.replace("main[1]  " +var + " = ", Separate_Char);
        String[] strings = newLog.split(Separate_Char);
        if (strings.length > 1) {
            return strings[1];
        }
        return null;
    }

    public void listJDB() throws IOException {
        this.printWriter.println("list");
        this.printWriter.flush(); //tra ra stream
        String end = printLog(END_RUN);
    }

    public String contJDB() {
        try {
            this.printWriter.println("cont");
            this.printWriter.flush(); //tra ra stream
            Thread.sleep(200);
            String end = printLog(END_RUN);
            return end;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
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
                result = s.split(endString)[1];
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
            System.out.println("LogALL: " + s);
            String breakline = "main[1] Unrecognized command: 'info'.  Try help...";
            if (s.trim().equals(breakline)) {
                break;
            }
            result += "\n" +JavaLibraryHelper.removeFirstAndLastChars(s);
        }
//        FileHelper.writeInputStreamToFile(this.process.getInputStream(), "C:\\Users\\Dell\\Desktop\\APR_test\\data_test\\83778\\OUT.txt");
        return result;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
//        String s = "Log: Breakpoint hit: ông hợp lệ: $-4000.0\n" +
//                "Log: \"thread=main\", Account.getTransactionHistory(), line=56 bci=8\n" +
//                "Log: 56            res.append(\"Lịch sử giao dịch của tài khoản \");";
////        s = s.replace(RegeX_END_RUN, "<====>" );
//        System.out.println(s);
//        Pattern p = Pattern.compile("Breakpoint hit:(.|\\s)*\"thread=main\", ");
//        Matcher matcher = p.matcher(s);
//        if (matcher.find()) {
//            System.out.println(matcher.group());
//        }
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