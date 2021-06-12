package util;

import AST.node.ClassNode;
import AST.node.FolderNode;
import AST.node.MethodNode;
import AST.obj.DebugPoint;
import AST.stm.AssertEqualStm;
import AST.stm.MethodInvocationStm;
import AST.stm.abstrct.AssertStatement;
import AST.stm.abstrct.InitStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class JDBHelper {
    public static final Logger logger = LoggerFactory.getLogger(JDBHelper.class);
    public static List<DebugPoint> genDebugPoints(FolderNode folderNode, List<AssertStatement> assertStatements, ClassNode classNode) {
        List<ClassNode> classNodes = folderNode.getClassNodes();
        List<DebugPoint> debugPoints = new ArrayList<>();
        for (AssertStatement assertStatement : assertStatements) {
            //Case 1: AssertEqualStm
            if (assertStatement instanceof AssertEqualStm) {
                //Case 1.1 MethodInvocation
                if (((AssertEqualStm) assertStatement).getActual() instanceof MethodInvocation) {
                    MethodInvocation methodInvocation =
                            (MethodInvocation) ((AssertEqualStm) assertStatement).getActual();
                    System.out.println(((AssertEqualStm) assertStatement).getExpected());
                    List params = methodInvocation.arguments();
                    MethodInvocationStm methodInvoStm = getClassName(methodInvocation);
                    InitStatement initStatement = classNode.findTypeVar(methodInvoStm.getVarClass(), methodInvoStm.getMethodName(), params);
                    if (initStatement != null) {
                        if (methodInvoStm != null) {
                            MethodNode methodNode = ParserHelper.findMethodInClass(classNodes, initStatement.getType().toString(), methodInvoStm.getMethodName(), params);
                            if (initStatement != null) {
                                DebugPoint debugPoint = new DebugPoint(initStatement.getType(), methodNode.getEndLine());
                                if (!JavaLibrary.debugPointContainsName(debugPoints, debugPoint)) {
                                    debugPoints.add(debugPoint);
                                }
                            }
                        }
                    }
                } else {
                    //TODO: chưa xử lý
                    logger.error("Chưa xử lý 1");
                }
            }
        }
        return debugPoints;
    }

    public static MethodInvocationStm getClassName(MethodInvocation methodInvocation) {
        if (methodInvocation.getExpression() instanceof MethodInvocation) {
            return getClassName((MethodInvocation) methodInvocation.getExpression());
        } else {
            if (methodInvocation.getExpression() instanceof SimpleName) {
                String classname = ((SimpleName) methodInvocation.getExpression()).getIdentifier();
                String methodName = methodInvocation.getName().getIdentifier();
                return new MethodInvocationStm(classname, methodName);
            } else {
                return null;
            }
        }

    }

    public static void setUTF8()  {
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
}
