package fix;

import AST.node.ClassNode;
import AST.node.FolderNode;
import AST.obj.DebugPoint;
import AST.obj.MethodTest;
import AST.parser.ASTHelper;
import AST.parser.MyTestParser;
import AST.parser.ProjectParser;
import AST.stm.MethodInvocationStm;
import AST.stm.abstrct.AssertStatement;
import common.config.Configure;
import common.config.TestType;
import common.error.ObjectNotFound;
import fix.object.DebugData;
import jdb.JDBDebugger;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FixFolder {
    public static final Logger logger = LoggerFactory.getLogger(FixFolder.class);
    static final String pathToSouce = "/home/huyenhuyen/Desktop/HAPR/data_test/83453/";
    static final String MyTest_Name = "MyTest";
    static final String TestRunner_Name = "TestRunner";
    static final String Path_AST_Output = pathToSouce + File.separator +"AST.txt"; // path_to_source/AST.txt

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {

        //TODO: copy TestRunner to folder
        //Compile and run (to get testcase)
        RunningHelper.compileFolder(pathToSouce, pathToSouce);
        RunningHelper.runFolder(pathToSouce, pathToSouce, TestRunner_Name);

        String output = pathToSouce + File.separator + Configure.OUTPUT_TestRunner;
        File outputFile = new File(output);

        //PARSER MyTest
        List<ClassNode> classNodes = (new MyTestParser()).myTestParser
                (pathToSouce + File.separator + MyTest_Name + ".java",
                        TestType.ANNOTATION_TEST);

        //Parser folder
        FolderNode folderNode = ProjectParser.parse(pathToSouce);
        String json = JsonHelper.getInstance().getJson(folderNode);
        FileHelper.createFile(new File(Path_AST_Output),
                json);

        if (!outputFile.exists()) {
            logger.error(ObjectNotFound.MSG + Configure.OUTPUT_TestRunner);
        } else {
            //Get List testName Error & get list debug point
            List<DebugData> debugDatas = getListDebugData(classNodes, folderNode, output);
            debugger(debugDatas, pathToSouce, pathToSouce, MyTest_Name);
        }

    }


    public static void debugger(List<DebugData> debugDatas, String sourcePath, String classPath, String testName) throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        JDBDebugger jdbDebugger = new JDBDebugger(sourcePath
                , classPath, testName);
        for (DebugData debugData : debugDatas) {
            DebugPoint debugPoint = debugData.getDebugPoint();
            jdbDebugger.addDebugJDB(debugPoint.getClassname(), debugPoint.getLine());
        }
        jdbDebugger.runJDB();

        fix(debugDatas, jdbDebugger);
    }

    private static void fix(List<DebugData> debugDatas, JDBDebugger jdbDebugger) {

        for (DebugData debugData : debugDatas) {
            String var = jdbDebugger.printVarJDB("res");
            System.out.println("actual: " +var);
            String result = jdbDebugger.printVarJDB( debugData.getExpected().toString());
            String string =  debugData.getExpected().toString();
            System.out.println("expected: " + result);


        }

    }

    public static void getString(String string) {
        String[] elements = string.split("\"\\s*\\+\\s*\"");
        string = String.join("", elements);
        string.replace("\"", "");
        string.replace("\\n", "\n");
//        string.replace("\\n", "\n");
    }

    private static void fix(DebugData debugData) {
        Object expected = debugData.getExpected();
        String astString = FileHelper.readFile(new File(Path_AST_Output));
        FolderNode folderNode = JsonHelper.getInstance().getObject(astString, FolderNode.class);
        //buggy context
        ClassNode classNode = ParserHelper.findByClassName(folderNode, debugData.getDebugPoint().getClassname());

    }

    /**
     * @param classNodes      of MyTest
     * @param folderNode      of Project
     * @param pathToRunOutput of TestRunner (get test fails)
     * @return
     */
    public static List<DebugData> getListDebugData(List<ClassNode> classNodes, FolderNode folderNode, String pathToRunOutput) {
        List<DebugData> debugDataList = new ArrayList<>();
        List<String> tests = FileHelper.readDataAsList(pathToRunOutput);
        logger.info(tests.toString());
        for (ClassNode classNode : classNodes) {
            List<MethodTest> methodTests = classNode.getMethodTests();
            for (MethodTest methodTest : methodTests) {
                for (String testname : tests) {
//                        if (methodTest.getMethodName().equals("test1_readCustomerList")) {
//                            List<AssertStatement> asserts = methodTest.getAssertList();
//                            List<DebugPoint> debugPoints = JDIHelper.genDebugPoints(folderNode, asserts, classNode);
//
//                        }
                    if (testname.equals(methodTest.getMethodName())) {
                        debugDataList.addAll(JDBHelper.genDebugPoints(folderNode, methodTest, classNode));
                    }
                }
            }
        }
        return debugDataList;
    }
}
