package fix;

import AST.node.ClassNode;
import AST.node.FolderNode;
import AST.node.MethodNode;
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
import util.JDIHelper;
import util.RunningHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FixFolder {
    public static final Logger logger = LoggerFactory.getLogger(FixFolder.class);

    public static void main(String[] args) throws IOException {
        String pathToSouce = "C:\\Users\\Dell\\Desktop\\APR_test\\data_test\\83778";
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
            //Get List testName Error
            List<String> tests = FileHelper.readDataAsList(output);
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
                            List<DebugPoint> debugPoints = JDIHelper.genDebugPoints(folderNode, asserts, classNode);
                            System.out.println(debugPoints.toString());
                        }
                    }
                }
            }
        }

    }


    public static MethodInvocationStm parserMethodInvocation(MethodInvocation methodInvocation) {
       Expression expression = methodInvocation.getExpression();
       return null;
//       return new MethodInvocationStm(expression.toString(), methodInvocation.me)
    }
    public static void debugger (String sourcePath, String classPath, String testName) throws IOException {
        JDBDebugger jdbDebugger = new JDBDebugger(sourcePath
                ,classPath, testName);
        jdbDebugger.addDebugJDB("MyTest", 78);
//        jdbDebugger.addDebugJDB("MyTest", 21);
        jdbDebugger.runJDB();

        String var  = jdbDebugger.printVarJDB("customerList");
        System.out.println("===VARRR==: " + var);
    }
}
