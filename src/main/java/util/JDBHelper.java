package util;

import AST.node.ClassNode;
import AST.node.FolderNode;
import AST.node.MethodNode;
import AST.obj.DebugPoint;
import AST.obj.MethodTest;
import AST.stm.AssertEqualStm;
import AST.stm.MethodInvocationStm;
import AST.stm.abstrct.AssertStatement;
import AST.stm.abstrct.InitStatement;
import fix.object.DebugData;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class JDBHelper {
    public static final Logger logger = LoggerFactory.getLogger(JDBHelper.class);

//    public static List<DebugData> genDebugPoints(FolderNode folderNode, MethodTest methodTest, ClassNode classNode) {
//        List<AssertStatement> assertStatements = methodTest.getAssertList();
//        List<ClassNode> classNodes = folderNode.getClassNodes();
//        List<DebugData> debugDatas = new ArrayList<>();
//        for (AssertStatement assertStatement : assertStatements) {
//            //Case 1: AssertEqualStm
//            if (assertStatement instanceof AssertEqualStm) {
//                //Case 1.1 MethodInvocation
//                if (((AssertEqualStm) assertStatement).getActual() instanceof MethodInvocation) {
//                    MethodInvocation methodInvocation =
//                            (MethodInvocation) ((AssertEqualStm) assertStatement).getActual();
//
//                    List params = methodInvocation.arguments();
//
//                    MethodInvocationStm methodInvoStm = ParserHelper.getVarClassAndMethod(methodInvocation);
//                    InitStatement initStatement = classNode.findTypeVar(methodInvoStm.getVarClass(), methodTest.getMethodName(), params);
//                    if (initStatement != null) {
//                        if (methodInvoStm != null) {
//                            MethodNode methodNode = ParserHelper.findMethodInClass(classNodes, initStatement.getType().toString(), methodInvoStm.getMethodName(), params);
//                            if (initStatement != null) {
//                                DebugPoint debugPoint = new DebugPoint(initStatement.getType(), methodNode.getEndLine());
//                                String expected = convertString(((AssertEqualStm) assertStatement).getExpected().toString());
//                                DebugData debugData = new DebugData(debugPoint, expected, methodTest.getMethodName());
//                                debugData.setAssertStatement(assertStatement);
//                                debugDatas.add(debugData);
//                            }
//                            ParserHelper.genListByMethodNode(methodNode);
//                        }
//                    }
//                } else {
//                    //TODO: chưa xử lý
//                    logger.error("Chưa xử lý 1");
//                }
//            }
//        }
//        return debugDatas;
//    }



    public static void setUTF8() {
        System.setProperty("file.encoding", "UTF-8");

        Field charset = null;
        try {
            charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * Convert "Hello" + " world" -> "Hello world"
     *
     * @param string
     * @return
     */
    private static String convertString(String string) {
        String[] elements = string.split("\"\\s*\\+\\s*\"");
        string = String.join("", elements);
        string = string.replace("\"", "");
        string = string.replace("\\n", "\n");
        return string;
    }

}
