package jdb;

import common.config.Configure;

import java.io.*;

public class JDBDebugger {
    Process process;
    PrintWriter printWriter;
    BufferedReader stdInput;
    BufferedReader stdError;

    public JDBDebugger(String pathToOutClass) throws IOException {
        compileFolder(pathToOutClass);
        initDebugJDB(pathToOutClass);
        stdInput = new BufferedReader(new
                InputStreamReader(this.process.getInputStream()));
        stdError = new BufferedReader(new
                InputStreamReader(this.process.getErrorStream()));
    }

    public void compileFolder(String classpath) throws IOException {
        String cmd = "javac -J-Dfile.encoding=UTF-8 -g -d "+classpath+" -cp "+ Configure.APR_JAR_LIB+"\\tools.jar;"+Configure.APR_JAR_LIB+"\\hamcrest-core-1.3.jar;"+Configure.APR_JAR_LIB+"\\junit-4.12.jar;"+Configure.APR_JAR_LIB+"\\oasis-junit-1.0.jar  *.java";
        Runtime.getRuntime().exec(cmd, null, new File(classpath));
    }
    public void initDebugJDB (String pathToClass) throws IOException {
        String cmd = "jdb -classpath  "+pathToClass+";"+Configure.APR_JAR_LIB+"\\junit-4.12.jar junit.textui.TestRunner MyTest1";
        this.process = Runtime.getRuntime().exec(cmd, null, new File(pathToClass));
        this.printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), false);
    }
    public void addDebugJDB(String classname, int line) throws InterruptedException, IOException {
        this.printWriter.println("stop at "+ classname+ ":" + line);
        this.printWriter.flush(); //tra ra stream
//        printLog();
    }
    public void runJDB() throws InterruptedException, IOException {
        this.printWriter.println("run");
        this.printWriter.flush(); //tra ra stream
        Thread.sleep(2000);
        printLog();
    }
    public void printVarJDB() throws InterruptedException {
        this.printWriter.println("print rookWhite1");
        this.printWriter.flush(); //tra ra stream
        Thread.sleep(1000);
        this.printWriter.println("cont");
        this.printWriter.flush();
    }
    public void stepJDB() {
        this.printWriter.println("cont");
        this.printWriter.flush(); //tra ra stream
    }
    public void destroyProcessJDB() {
        this.printWriter.println("quit");
        this.printWriter.close();
        this.process.destroy();
    }

    public void printLog() throws IOException {
// Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
//        s = stdInput.readLine();
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
    }


    public void JDBProcess() throws IOException {
        String cmd = "jdb -classpath  C:\\Users\\Dell\\IdeaProjects\\HAPR\\target\\classes;"+Configure.APR_JAR_LIB+"\\junit-4.12.jar junit.textui.TestRunner MyTest1";
//        String[] commands = {cmd, "stop at MyTest1:9", "run"};
        Process process = Runtime.getRuntime().exec(cmd);
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), true);
        out.println("stop at MyTest1:9");
        out.println("run");
        out.println("print rookWhite1");
        out.flush();
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(process.getErrorStream()));

// Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

// Read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }
        process.destroy();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        JDBDebugger jdbDebugger = new JDBDebugger("C:\\Users\\Dell\\IdeaProjects\\HAPR\\src\\main\\java");
        jdbDebugger.addDebugJDB("MyTest1", 8);
        jdbDebugger.runJDB();
        jdbDebugger.printVarJDB();


        jdbDebugger.destroyProcessJDB();



//        BufferedReader stdInput2 = new BufferedReader(new
//                InputStreamReader(jdbDebugger.process.getInputStream()));
//
//        BufferedReader stdError2 = new BufferedReader(new
//                InputStreamReader(jdbDebugger.process.getErrorStream()));
//
//// Read the output from the command
//        System.out.println("Here is the standard output of the command:\n");
//        String s2 = null;
//        while ((s2 = stdInput2.readLine()) != null) {
//            System.out.println(s2);
//        }
//
//// Read any errors from the attempted command
//        System.out.println("Here is the standard error of the command (if any):\n");
//        while ((s2 = stdError2.readLine()) != null) {
//            System.out.println(s2);
//        }
    }
}
