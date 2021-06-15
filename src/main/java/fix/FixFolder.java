package fix;

import AST.node.ClassNode;
import AST.node.FolderNode;
import AST.obj.DebugPoint;
import AST.obj.MethodTest;
import AST.parser.MyTestParser;
import AST.parser.ProjectParser;
import AST.stm.MethodInvocationStm;
import AST.stm.abstrct.AssertStatement;
import common.config.Configure;
import common.config.TestType;
import common.error.ObjectNotFound;
import jdb.JDBDebugger;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileHelper;
import util.JDBHelper;
import util.RunningHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FixFolder {
    public static final Logger logger = LoggerFactory.getLogger(FixFolder.class);

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        String pathToSouce = "/home/huyenhuyen/Desktop/HAPR/data_test/83778/";
        //TODO: copy TestRunner to folder
        //Compile and run (to get testcase)
        RunningHelper.compileFolder(pathToSouce, pathToSouce);
        RunningHelper.runFolder(pathToSouce, pathToSouce, "TestRunner");

        String output = pathToSouce + File.separator + Configure.OUTPUT_TestRunner;
        File outputFile = new File(output);

        //PARSER MyTest
        List<ClassNode> classNodes = (new MyTestParser()).myTestParser
                (pathToSouce + File.separator + "MyTest.java",
                TestType.ANNOTATION_TEST);

        //Parser folder
        FolderNode folderNode =  ProjectParser.parse(pathToSouce);

        if (!outputFile.exists()) {
            logger.error(ObjectNotFound.MSG + Configure.OUTPUT_TestRunner);
        } else {
            //Get List testName Error & get list debug point
            List<DebugPoint> debugPoints = getListDebug(classNodes, folderNode, output);
            debugger(debugPoints, pathToSouce, pathToSouce, "MyTest");

        }

    }


    public static MethodInvocationStm parserMethodInvocation(MethodInvocation methodInvocation) {
       Expression expression = methodInvocation.getExpression();
       return null;
//       return new MethodInvocationStm(expression.toString(), methodInvocation.me)
    }
    public static void debugger (List<DebugPoint> debugPoints, String sourcePath, String classPath, String testName) throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        JDBDebugger jdbDebugger = new JDBDebugger(sourcePath
                ,classPath, testName);
        for (DebugPoint debugPoint : debugPoints) {
            jdbDebugger.addDebugJDB(debugPoint.getClassname(), debugPoint.getLine());
        }
        jdbDebugger.runJDB();

        String var  = jdbDebugger.printVarJDB("res");
        System.out.println("===VARRR==: " + var);
    }

    /**
     *
     * @param classNodes of MyTest
     * @param folderNode of Project
     * @param pathToRunOutput of TestRunner (get test fails)
     * @return
     */
    public  static List<DebugPoint> getListDebug(List<ClassNode> classNodes, FolderNode folderNode, String pathToRunOutput) {
        List<DebugPoint> debugPoints = new ArrayList<>();
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
                        List<AssertStatement> asserts = methodTest.getAssertList();
                        debugPoints = JDBHelper.genDebugPoints(folderNode, asserts, classNode);
                        System.out.println(debugPoints.toString());
                    }
                }
            }
        }
        return debugPoints;
    }
}
